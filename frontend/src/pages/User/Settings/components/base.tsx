import {ProForm, ProFormText,} from '@ant-design/pro-components';
import {Button, Image, message, Spin, Upload} from 'antd';
import React, {useEffect, useMemo} from 'react';
import useStyles from './index.style';
import {updateMyUserUsingPost1} from "@/services/backend/userController";
import {ProFormSelect} from "@ant-design/pro-form/lib";
import {UploadOutlined, UserOutlined} from "@ant-design/icons";
import {useUserData} from "@/pages/User/Settings/context";
import {RcFile} from "antd/lib/upload";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {uploadFileUsingPost1} from "@/services/backend/fileController";
import {UploadType} from "@/constants/UploadType";
import {STATIC_URL} from "@/constants";

const BaseView: React.FC = () =>
{
    const { styles } = useStyles();
    const { userData, setUserData } = useUserData()
    const [ form ] = ProForm.useForm();
    const [ uploadFileHandler, uploading ] = useAsyncHandler<string>()

    const userAvatar = useMemo(() => {
        return userData.userAvatar ? `${STATIC_URL}${userData.userAvatar}` : null
    }, [userData])

    const uploadFile = async (file: RcFile): Promise<string> =>
    {
        const response = await uploadFileHandler(async () =>
        {
            const { data, code } = await uploadFileUsingPost1({ biz: UploadType.USER_AVATAR }, {}, file)
            if (code !== 0)
            {
                return Promise.reject();
            }
            message.success("上传成功");
            return data as string
        }, [],error =>
        {
            message.error(error.message);
        })
        if (response)
        {
            setUserData(prev => ({ ...prev, userAvatar: response }))
            return response;
        }
        return "";
    }

    const AvatarView = () => (
        <>
            <div className={styles.avatar_title}>头像</div>
            <div className={styles.avatar}>
                {userAvatar ? <Image src={userAvatar} alt="avatar" /> : <UserOutlined />}
            </div>
            <Upload showUploadList={false} accept={'.png,.jpg,.jpeg'}
                    action={uploadFile}
                    disabled={uploading}
                    progress={{

                    }}
            >
                <div className={styles.button_view}>
                    <Spin spinning={uploading}>
                        <Button>
                            <UploadOutlined/>
                            更换头像
                        </Button>
                    </Spin>
                </div>
            </Upload>
        </>
    );

    const handleFinish = async (values: React.SetStateAction<API.AboutMeVO>) =>
    {
        try
        {
            const { code } = await updateMyUserUsingPost1(values as API.AboutMeVO)
            if (code === 0)
            {
                // setUserData(values)
                form.setFieldsValue(values);
                message.success('更新基本信息成功');
            }
            else
            {
                message.error('更新基本信息失败')
            }
        }
        catch (e: any)
        {
            message.success('更新基本信息成功');
        }
    };

    useEffect(() =>
    {
        if (userData)
        {
            form.setFieldsValue(userData);
        }
    }, [ userData, form ]);


    return (
        <div className={styles.baseView}>
            <div className={styles.left}>
                <ProForm
                    form={form}
                    layout="vertical"
                    onFinish={handleFinish}
                    submitter={{
                        searchConfig: {
                            submitText: '更新基本信息',
                        },
                        render: (_, dom) => dom[1],
                    }}
                    initialValues={{
                        ...userData
                    }}
                >
                    <ProFormText
                        name="userAccount"
                        label="用户账号"
                        disabled={true}
                        initialValue={userData.userAccount}
                    />
                    <ProFormSelect
                        width="md"
                        name="userGender"
                        label="用户性别"
                        fieldProps={{
                            options: [ {
                                label: "未知",
                                value: 0
                            },
                                {
                                    label: "男",
                                    value: 1
                                },
                                {
                                    label: "女",
                                    value: 2
                                }, ],

                        }}
                        initialValue={userData.userGender}
                        valueEnum={{
                            0: "未知",
                            1: "男",
                            2: "女"
                        }}
                        rules={[
                            {
                                required: true,
                                message: '请输入您的性别!',
                            },
                        ]}
                    />
                    <ProFormText
                        width="md"
                        name="userName"
                        label="昵称"
                        initialValue={userData.userName}
                        rules={[
                            {
                                required: true,
                                message: '请输入您的昵称!',
                            },
                        ]}
                    />
                </ProForm>
            </div>
            <div className={styles.right}>
                <AvatarView />
            </div>
        </div>
    );
};
export default BaseView;
