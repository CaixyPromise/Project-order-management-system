import React from "react";
import {ProFormText} from "@ant-design/pro-components";
import {
    LockOutlined,
    MailOutlined,
    MobileOutlined,
    PartitionOutlined,
    SafetyOutlined,
    UserOutlined
} from "@ant-design/icons";
import {ACCOUNT_REGEX, EMAIL_REGEX, NAME_REGEX, PASSWORD_REGEX, PHONE_REGEX} from "@/constants/regex";

interface RegisterFormProps {

}


const RegisterForm: React.FC<RegisterFormProps> = () =>
{
    return <>
        <ProFormText
            name="userPhone"
            fieldProps={{
                size: 'large',
                prefix: <MobileOutlined/>,
            }}
            placeholder={'请输入用户手机号'}
            rules={[
                {
                    required: true,
                    message: '手机号是必填项！',
                },
                PHONE_REGEX
            ]}
        />
        <ProFormText
            name="userEmail"
            fieldProps={{
                size: 'large',
                prefix: <MailOutlined />,
            }}
            placeholder={'请输入用户邮箱'}
            rules={[
                {
                    required: true,
                    message: '邮箱是必填项！',
                },
                EMAIL_REGEX
            ]}
        />

        <ProFormText
            name="userAccount"
            fieldProps={{
                size: 'large',
                prefix: <PartitionOutlined/>,
            }}
            placeholder={'请输入用户登录账号'}
            rules={[
                {
                    required: true,
                    message: '用户登录账号是必填项！',
                },
                {
                    max: 20,
                    message: "用户登录账号长度不能大于20位",
                },
                ACCOUNT_REGEX
            ]}
        />

        <ProFormText
            name="userName"
            fieldProps={{
                size: 'large',
                prefix: <UserOutlined/>,
            }}
            placeholder={'请输入用户姓名'}
            rules={[
                {
                    required: true,
                    message: '姓名是必填项！',
                },
                {
                    max: 30,
                    message: "姓名长度不能大于30位",
                },
                NAME_REGEX
            ]}
        />

        <ProFormText.Password
            name="userPassword"
            fieldProps={{
                size: 'large',
                prefix: <LockOutlined/>,
            }}
            placeholder={'请输入密码'}
            rules={[
                {
                    required: true,
                    message: '密码是必填项！',
                },
                PASSWORD_REGEX,
                {
                    min: 8,
                    message: '用户密码长度不能小于8位',
                },
            ]}
        />
        <ProFormText.Password
            name="checkPassword"
            fieldProps={{
                size: 'large',
                prefix: <SafetyOutlined/>,
            }}
            placeholder={'再次输入密码以确认'}
            dependencies={[ 'userPassword' ]}
            rules={[
                {
                    required: true,
                    message: '请再次确认密码！',
                },
                PASSWORD_REGEX,
                ({ getFieldValue }) => ({
                    validator(_, value)
                    {
                        if (!value || getFieldValue('userPassword') === value)
                        {
                            return Promise.resolve();
                        }
                        return Promise.reject(new Error('两次输入的密码不一致！'));
                    },
                }),
            ]}
        />
    </>
}

export default RegisterForm;
