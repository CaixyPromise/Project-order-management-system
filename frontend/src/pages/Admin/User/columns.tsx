import type {ProColumns} from "@ant-design/pro-components";
import {Image, Space, Typography} from "antd";
import React from "react";
import {ColumnsParams} from "@/typings";
import {STATIC_URL} from "@/constants";
import UserSexLined from "@/components/UserSexLined";

export const getUserTableColumn = ({
    setCurrentRow, setUpdateModalVisible, handleDelete
}: ColumnsParams<API.User>): ProColumns<API.User>[] => ([
    {
        title: 'id',
        dataIndex: 'id',
        valueType: 'text',
        hideInForm: true,

    },
    {
        title: '账号',
        dataIndex: 'userAccount',
        valueType: 'text',
    },
    {
        title: '用户名',
        dataIndex: 'userName',
        valueType: 'text',
    },
    {
        title: "用户手机号",
        dataIndex: "userPhone",
        valueType: "text",
    },
    {
        title: "用户邮箱",
        dataIndex: "userEmail",
        valueType: "text",
    },
    {
        title: "用户性别",
        dataIndex: "userGender",
        valueType: "select",
        valueEnum: {
            0: {
                text: "未知",
            },
            1: {
                text: "男",
            },
            2: {
                text: "女",
            }
        },
        render: (_, record) => {
            return <UserSexLined sex={record.userGender as number} />
        }
    },
    {
        title: '头像',
        dataIndex: 'userAvatar',
        valueType: 'image',
        fieldProps: {
            width: 64,
        },
        hideInSearch: true,
        render: (_, record) => {
            return <Image
                style={{
                    width: '121px',
                    height: '121px',
                    borderRadius: "8px"
                }}
                src={`${STATIC_URL}${record.userAvatar}`}
                alt={"avatar"} key={record.id}/>
        }
    },
    {
        title: '简介',
        dataIndex: 'userProfile',
        valueType: 'textarea',
    },
    {
        title: '权限',
        dataIndex: 'userRole',
        valueEnum: {
            user: {
                text: '用户',
            },
            admin: {
                text: '管理员',
            },
        },
    },
    {
        title: '创建时间',
        sorter: true,
        dataIndex: 'createTime',
        valueType: 'dateTime',
        hideInSearch: true,
        hideInForm: true,
    },
    {
        title: '更新时间',
        sorter: true,
        dataIndex: 'updateTime',
        valueType: 'dateTime',
        hideInSearch: true,
        hideInForm: true,
    },
    {
        title: '操作',
        dataIndex: 'option',
        valueType: 'option',
        render: (_, record) => (
            <Space size="middle">
                <Typography.Link
                    onClick={() =>
                    {
                        setCurrentRow(record);
                        setUpdateModalVisible(true);
                    }}
                >
                    修改
                </Typography.Link>
                <Typography.Link type="danger" onClick={() => handleDelete(record)}>
                    删除
                </Typography.Link>
            </Space>
        ),
    },
]);
