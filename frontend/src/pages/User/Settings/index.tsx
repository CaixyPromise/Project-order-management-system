import {GridContent} from '@ant-design/pro-components';
import {Menu, message, Spin} from 'antd';
import React, {useEffect, useLayoutEffect, useRef, useState} from 'react';
import BaseView from './components/base';
import SecurityView from './components/security';
import {UserContext} from './context';
import useStyles from './style.style';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {getMeUsingGet1} from "@/services/backend/userController";
import {useParams} from "@@/exports";

type SettingsStateKeys = 'base' | 'security';
type SettingsState = {
    mode: 'inline' | 'horizontal';
    selectKey: SettingsStateKeys;
};
const Settings: React.FC = () =>
{
    const {operation} = useParams<{operation: SettingsStateKeys | undefined}>()
    const [ userData, setUserData ] = useState<API.AboutMeVO>({});
    const [ queryUserInfoHandler, isPending ] = useAsyncHandler<API.AboutMeVO>();

    const { styles } = useStyles();
    const menuMap: Record<string, React.ReactNode> = {
        base: '基本设置',
        security: '安全设置'
    };
    const [ initConfig, setInitConfig ] = useState<SettingsState>({
        mode: 'inline',
        selectKey: operation ? operation : "base",
    });
    const dom = useRef<HTMLDivElement>();
    useEffect(() =>
    {
        if (operation)
        {
            setInitConfig(prevState =>  ({
                ...prevState,
                selectKey: operation,
            }))
        }
    }, [operation]);

    const resize = () =>
    {
        requestAnimationFrame(() =>
        {
            if (!dom.current)
            {
                return;
            }
            let mode: 'inline' | 'horizontal' = 'inline';
            const { offsetWidth } = dom.current;
            if (dom.current.offsetWidth < 641 && offsetWidth > 400)
            {
                mode = 'horizontal';
            }
            if (window.innerWidth < 768 && offsetWidth > 400)
            {
                mode = 'horizontal';
            }
            setInitConfig({
                ...initConfig,
                mode: mode as SettingsState['mode'],
            });
        });
    };
    useLayoutEffect(() =>
    {
        if (dom.current)
        {
            window.addEventListener('resize', resize);
            resize();
        }
        return () =>
        {
            window.removeEventListener('resize', resize);
        };
    }, [ dom.current ]);
    const getMenu = () =>
    {
        return Object.keys(menuMap).map((item) => ({ key: item, label: menuMap[item] }));
    };

    const renderChildren = () =>
    {
        const { selectKey } = initConfig;
        switch (selectKey)
        {
            case 'security':
                return <SecurityView/>;
            // 默认返回base
            case 'base':
            default:
                return <BaseView/>;
        }
    };

    const fetchUserData = async () =>
    {
        const onError = (error: string) =>
        {
            message.error("获取信息失败")
        }
        const response = await queryUserInfoHandler(async () =>
        {
            const { data, code } = await getMeUsingGet1()
            if (code !== 0)
            {
                return Promise.reject('获取信息失败')
            }
            return data as API.AboutMeVO;
        },[], onError)

        if (response)
        {
            setUserData(response)
        }
    }

    useEffect(() =>
    {
        fetchUserData();
    }, []);
    return (
        <GridContent>
            <div
                className={styles.main}
                ref={(ref) =>
                {
                    if (ref)
                    {
                        dom.current = ref;
                    }
                }}
            >
                <div className={styles.leftMenu}>
                    <Menu
                        mode={initConfig.mode}
                        selectedKeys={[ initConfig.selectKey ]}
                        onClick={({ key }) =>
                        {
                            setInitConfig({
                                ...initConfig,
                                selectKey: key as SettingsStateKeys,
                            });
                        }}
                        items={getMenu()}
                    />
                </div>
                <div className={styles.right}>
                    <Spin spinning={isPending} tip={"加载中"}>
                        <div className={styles.title}>{menuMap[initConfig.selectKey]}</div>
                        <UserContext.Provider value={{ userData, setUserData }}>
                            {renderChildren()}
                        </UserContext.Provider>
                    </Spin>
                </div>
            </div>
        </GridContent>
    );
};
export default Settings;
