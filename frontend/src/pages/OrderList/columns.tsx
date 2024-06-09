import type {ProColumns} from "@ant-design/pro-components";
import {Space, Typography} from "antd";
import React from "react";
import {ColumnsParams} from "@/typings";


export const getOrderListColumn = ({
    setCurrentRow, setUpdateModalVisible, handleDelete
}: ColumnsParams<API.OrderInfoVO>): ProColumns<API.OrderInfoVO>[] => ([
    {
        title: 'id',
        dataIndex: 'id',
        valueType: 'text',
        hideInForm: true,
    },
    {
        title: "平台订单id",
        dataIndex: "orderId",
        valueType: "text",
    },
    {
        title: "订单描述名称",
        dataIndex: "orderTitle",
        valueType: "text",
        hideInForm: true
    },
    {
        title: "创建人名称",
        dataIndex: "creatorName",
        valueType: "text",
        hideInForm: true
    },
    {
        title: "总金额",
        dataIndex: "amount",
        valueType: "text",
    },
    {
        title: "已支付金额",
        dataIndex: "amountPaid",
        valueType: "text",
    },
    {
        title: "订单编程语言",
        dataIndex: "langName",
        valueType: "text",
    },
    {
        title: "订单分类名称",
        dataIndex: "orderCategoryName",
        valueType: "text",
    },


    {
        title: "订单状态",
        dataIndex: "orderStatus",
        valueType: "text",
    },
    {
        title: "是否外包",
        dataIndex: "isAssigned",
        valueType: "text",
        render: (_, record) =>
        {
            if (record.isAssigned === true)
            {
                return <span color="green">是</span>
            }
            else
            {
                return <span color="red">否</span>
            }
        }
    },
    {
        title: "是否支付",
        dataIndex: "isPaid",
        valueType: "text",
        render: (_, record) =>
        {
            if (record.isPaid === true)
            {
                return <span color="green">是</span>
            }
            else
            {
                return <span color="red">否</span>
            }
        }
    },
    {
        title: "订单来源",
        dataIndex: "orderSource",
        valueType: "text",
    },

    {
        title: "交付截止日期",
        dataIndex: "orderDeadline",
        valueType: "date",
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
