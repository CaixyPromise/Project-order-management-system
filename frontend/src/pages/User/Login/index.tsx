import Footer from '@/components/Footer';

import {LockOutlined, MailOutlined, MobileOutlined, PartitionOutlined, SafetyOutlined, UserOutlined} from '@ant-design/icons';
import {LoginForm, ProFormText} from '@ant-design/pro-components';
import {useEmotionCss} from '@ant-design/use-emotion-css';
import {Helmet, history, useModel} from '@umijs/max';
import {Form, message, Tabs} from 'antd';
import React, {useMemo, useRef, useState} from 'react';
import Settings from '../../../../config/defaultSettings';
import {userLoginUsingPost1, userRegisterUsingPost1} from "@/services/backend/userController";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {ACCOUNT_REGEX, EMAIL_REGEX, NAME_REGEX, PASSWORD_REGEX, PHONE_REGEX} from "@/constants/regex";
import AuthForm from "@/pages/User/Login/components/AuthForm";
import RegisterForm from "@/pages/User/Login/components/RegisterForm";
import {FormInstance} from "antd/lib";

const Login: React.FC = () =>
{
    enum ActionType
    {
        AccountLogin = "account",
        Register = "register",
    }
    const formRef = useRef<FormInstance>();
    const [ type, setType ] = useState<ActionType>(ActionType.AccountLogin);
    const { initialState, setInitialState } = useModel('@@initialState');
    const [ loginHandler, isPending,  ] = useAsyncHandler<API.LoginUserVO>()
    const [ registerHandler, isPendingRegister ] = useAsyncHandler<boolean>()
    const styles = useMemo(() => {
        const baseStyle = {
            transition: 'opacity 0.5s ease, max-height 0.5s ease',
            maxHeight: '0px',
            overflow: 'hidden',
            opacity: 0
        };

        const activeStyle = {
            maxHeight: '400px',
            opacity: 1
        };

        return {
            formField: type ? { ...baseStyle } : { ...baseStyle, ...activeStyle },
            formFieldActive: { ...baseStyle, ...activeStyle }
        };
    }, [type]);
    const containerClassName = useEmotionCss(() =>
    {
        return {
            display: 'flex',
            flexDirection: 'column',
            height: '100vh',
            overflow: 'auto',
            backgroundImage:
                "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
            backgroundSize: '100% 100%',
        };
    });

    const handleLogin = async (values: API.UserLoginRequest) =>
    {
        const onError = (error: Error) =>
        {
            const defaultLoginFailureMessage = `登录失败，${error.message}`;
            message.error(defaultLoginFailureMessage);
        };

        const res = await loginHandler(async () =>
        {
            // 登录
            const response = await userLoginUsingPost1({
                ...values,
            });
            if (!response.data)
            {
                return Promise.reject(new Error('登录请求失败'));
            }

            return response.data;
        }, [], onError);

        if (res)
        {
            const defaultLoginSuccessMessage = '登录成功！';
            message.success(defaultLoginSuccessMessage);
            // 保存已登录用户信息
            setInitialState((prevState: any) => ({
                ...prevState,
                currentUser: res,
            }));
            const urlParams = new URL(window.location.href).searchParams;
            const redirectUrl = urlParams.get('redirect') || '/';
            history.push(redirectUrl);
        }
    };

    const handleRegister = async (values: API.UserRegisterRequest) =>
    {
        const onError = (error: Error) => {
            const defaultLoginFailureMessage = `注册失败，${error.message}`;
            message.error(defaultLoginFailureMessage);
        };

        const res = await registerHandler(async () =>
        {
            // 注册
            const response = await userRegisterUsingPost1({
                ...values,
            });
            if (!response.data)
            {
                return Promise.reject(new Error('注册请求失败'));
            }

            return response.data;
        }, [],onError);

        if (res)
        {
            const defaultLoginSuccessMessage = '注册成功！';
            message.success(defaultLoginSuccessMessage);
            setType(ActionType.AccountLogin);
            return { success: true, userAccount: values.userAccount}
        }
        else{
            return { success: false, userAccount: null}
        }
    }

    const getTabList = (): {
        key: string;
        label: string;
        dom?: React.ReactNode;
    }[] => ([
        {
            key: ActionType.AccountLogin,
            label: '账户密码登录',
            dom: <AuthForm />,
        },
        {
            key: ActionType.Register,
            label: '用户注册',
            dom: <RegisterForm />
        },
    ])

    const onSubmit = async () =>
    {
        const values = formRef.current?.getFieldsValue();

        if (type === ActionType.Register)
        {
            formRef?.current?.validateFields?.(['userAccount', 'userPassword']);
            const {success, userAccount} = await handleRegister({
                ...values
            })
            if (success)
            {
                formRef.current?.resetFields();
                formRef.current?.setFieldsValue({
                    "userAccount-Login": userAccount,
                })
            }
        }
        else
        {
            formRef.current?.validateFields?.(['userAccount-Login', 'userPassword-Login']).then(async values =>
            {
                await handleLogin({
                    userAccount: values['userAccount-Login'],
                    userPassword: values['userPassword-Login']
                }).finally(() => {
                    formRef.current?.resetFields();
                });
            });
        }
    }

    return (
        <div className={containerClassName}>
            <Helmet>
                <title>
                    {'登录'}- {Settings.title}
                </title>
            </Helmet>
            <div
                style={{
                    flex: '1',
                    padding: '32px 0',
                }}
            >
                <LoginForm
                    contentStyle={{
                        minWidth: 280,
                        maxWidth: '75vw',
                    }}
                    formRef={formRef}
                    logo={<img alt="logo" style={{ height: '100%' }} src="/logo.svg"/>}
                    title="前端模板"
                    subTitle={'快速开发属于自己的前端项目'}
                    initialValues={{
                        autoLogin: true,
                    }}

                    submitter={{
                        searchConfig: {
                            submitText: type === ActionType.Register ? '注册' : '登录'
                        },
                        submitButtonProps: {
                            onClick: onSubmit
                        }
                    }}
                >
                    <Tabs
                        activeKey={type}
                        onChange={(activeKey) => {
                            console.log('activeKey', activeKey)
                            setType(activeKey as ActionType)
                        }}
                        centered
                        items={getTabList()}
                    />
                    {
                        getTabList().map((item, index) => {
                            return <>
                                <div key={index}
                                     style={type === item.key ? styles.formFieldActive : styles.formField}>
                                    {item.dom}
                                </div>
                            </>
                        })
                    }

                    <div
                        style={{
                            marginBottom: 24,
                            textAlign: 'right',
                        }}
                    >
                        {
                            type === ActionType.AccountLogin
                                ? <a onClick={() => setType(ActionType.Register)}>新用户注册</a>
                                : <a onClick={() => setType(ActionType.AccountLogin)}>使用已有账户登录</a>
                        }
                    </div>
                </LoginForm>
            </div>
            <Footer/>
        </div>
    );
};
export default Login;
