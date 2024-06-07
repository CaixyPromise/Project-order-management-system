import React from "react";
import {message} from "antd";
import {modifyPasswordUsingPost1} from "@/services/backend/userController";
import {ModalForm, ProFormText} from "@ant-design/pro-components";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {PASSWORD_REGEX} from "@/constants/regex";

interface ModifyPasswordProps
{
    open: boolean;
    setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}


const ModifyPassword: React.FC<ModifyPasswordProps> = (props) =>
{
    const {open, setOpen} = props;
    const [submittedHandler, isLoading] = useAsyncHandler<boolean>()
    const onSummit = async (values: any) =>
    {
        const onError = (error: string) =>
        {
            message.error(`修改失败: ${error}`);
        }

        const response = await submittedHandler(async () =>
        {
            const { code, data } = await modifyPasswordUsingPost1(values);
            if (code !== 0)
            {
                return false;
            }
            return data as boolean;
        }, onError);

        if (response)
        {
            setOpen(false);
            message.success('修改成功，请重新登录');
            window.location.href = "/index"
            return true;
        }
    }

    return <>
        <ModalForm
            open={open}
            onFinish={onSummit}
            onOpenChange={setOpen}
        >
            <ProFormText.Password
                name="oldPassword"
                label="旧密码"
                placeholder={"请输入旧密码"}
                fieldProps={{
                    maxLength: 20,
                }}
                rules={[
                    { required: true, message: '请输入旧密码' },
                    { min: 8, message: '密码长度不能少于8位' },
                    { max: 20, message: '密码长度不能超过20位' },
                    PASSWORD_REGEX,
                ]}
            />
            <ProFormText.Password
                name="newPassword"
                label="新密码"
                fieldProps={{
                    maxLength: 20,
                }}
                placeholder={"请输入新密码"}

                rules={[
                    { required: true, message: '请输入新密码' },
                    { min: 8, message: '密码长度不能少于8位' },
                    { max: 20, message: '密码长度不能超过20位' },
                    PASSWORD_REGEX,
                ]}
            />
            <ProFormText.Password
                name="confirmPassword"
                label="确认密码"
                fieldProps={{
                    maxLength: 20,
                }}
                placeholder={"请再次输入新密码"}
                dependencies={[ 'newPassword' ]}
                rules={[
                    { required: true, message: '请确认密码' },
                    { min: 8, message: '密码长度不能少于8位' },
                    { max: 20, message: '密码长度不能超过20位' },
                    PASSWORD_REGEX,
                    ({ getFieldValue }) => ({
                        validator(_, value)
                        {
                            if (!value || getFieldValue('newPassword') === value)
                            {
                                return Promise.resolve();
                            }
                            return Promise.reject(new Error('两次输入的密码不匹配'));
                        },
                    }),
                ]}
            />
        </ModalForm>
    </>
}

export default ModifyPassword;
