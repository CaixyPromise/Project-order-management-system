import type {ProColumns} from "@ant-design/pro-components";
import {Space, Typography} from "antd";
import React from "react";
import {ColumnsParams} from "@/typings";


export const getLangeTableColumn = ({
    setCurrentRow, setUpdateModalVisible, handleDelete
}: ColumnsParams<API.LanguageTypeVO>): ProColumns<API.LanguageTypeVO>[] => ([
    {
        title: 'id',
        dataIndex: 'id',
        valueType: 'text',
        hideInForm: true,

    },
    {
        title: "语言名称",
        dataIndex: "languageName",
        valueType: "text",
    },
    {
        title: "创建人名称",
        dataIndex: "creatorName",
        valueType: "text",
        hideInForm: true
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
