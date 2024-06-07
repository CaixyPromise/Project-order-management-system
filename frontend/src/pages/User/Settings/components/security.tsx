import {List} from 'antd';
import React, {useState} from 'react';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {useUserData} from "@/pages/User/Settings/context";
import ModifyPassword from "@/pages/User/Settings/components/components/ModifyPassword";
import {UNIMPLEMENTED} from "@/utils/unImplemented";
import useStyles from "./index.style";

type Unpacked<T> = T extends (infer U)[] ? U : T;

const passwordStrength = {
    strong: <span className="strong">强</span>,
    medium: <span className="medium">中</span>,
    weak: <span className="weak">弱 Weak</span>,
};

const SecurityView: React.FC = () =>
{
    const [ open, setOpen ] = useState<boolean>(false);
    const { userData } = useUserData()
    const getData = () => [
        {
            title: '账户密码',
            description: (
                <>
                    当前密码强度：
                    {passwordStrength.strong}
                </>
            ),
            actions: [ <a key="Modify" onClick={() => setOpen(true)}>修改</a> ],
        },
        {
            title: '绑定手机',
            // 使用正则对手机号脱密
            description: userData.userPhone ? <span>已绑定手机：{userData.userPhone}</span> : <span>暂未绑定手机号</span>,
            actions: [ <a key="phone" onClick={() => UNIMPLEMENTED("手机号操作")}>{userData.userPhone ? "修改" : "绑定"}</a> ],
        },
        {
            title: "绑定邮箱",
            description: userData.userEmail ? <span>已绑定邮箱：{userData.userEmail}</span> : <span>暂未绑定邮箱</span>,
            actions: [ <a key="email" onClick={() => UNIMPLEMENTED("邮箱操作")}>{userData.userEmail ? "修改" : "绑定"}</a> ],
        }
    ];

    const data = getData();
    return (
        <>

                <List<Unpacked<typeof data>>
                    itemLayout="horizontal"
                    dataSource={data}
                    renderItem={(item) => (
                        <List.Item actions={item.actions}>
                            <List.Item.Meta title={item.title} description={item.description}/>
                        </List.Item>
                    )}
                />
                <ModifyPassword open={open} setOpen={setOpen} />
        </>
    );
};

export default SecurityView;
