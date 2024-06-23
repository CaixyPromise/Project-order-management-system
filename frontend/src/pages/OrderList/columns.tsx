import type {ProColumns} from "@ant-design/pro-components";
import {Space, Tag, Typography} from "antd";
import React from "react";
import {ColumnsParams, OptionArray} from "@/typings";
import OrderActionButton from "@/pages/OrderList/components/OrderActionButton";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";

const BooleanTag = ({ text }: { text: boolean | undefined }) =>
{
    if (text)
    {
        return <Tag color="green">是</Tag>
    }
    else
    {
        return <Tag color="red">否</Tag>
    }
}


export const getOrderListColumn = ({
    setCurrentRow, setDetailsModalVisible, handleDelete
}: ColumnsParams<API.OrderInfoPageVO> & {
    setDetailsModalVisible: (visible: boolean) => void
}): ProColumns<API.OrderInfoPageVO>[] => ([
    {
        title: 'id',
        dataIndex: 'id',
        valueType: 'text',
        hideInForm: true,
        editable: false

    },
    {
        title: "平台订单id",
        dataIndex: "orderId",
        valueType: "text",
        width: 100,
        editable: false
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
        hideInForm: true,
        editable: false

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
        editable: false,
    },
    {
        title: "订单分类名称",
        dataIndex: "orderCategoryName",
        valueType: "text",
        editable: false
    },
    {
        title: "是否包含附件",
        dataIndex: "hasOrderAttachment",
        valueType: "text",
        render: (_, record) =>
        {
            return <BooleanTag text={record.hasOrderAttachment}/>
        },
        editable: false
    },
    {
        title: "订单状态",
        dataIndex: "orderStatus",
        valueType: "select",
        fieldProps: {
            options: OrderStatusEnum.getAllOptions()
        }
    },
    {
        title: "是否外包",
        dataIndex: "isAssigned",
        valueType: "text",
        render: (_, record) =>
        {
            return <BooleanTag text={record.isAssigned}/>
        }
    },
    {
        title: "是否支付",
        dataIndex: "isPaid",
        valueType: "text",
        render: (_, record) =>
        {
            return <BooleanTag text={record.isPaid}/>
        }
    },
    {
        title: "订单来源",
        dataIndex: "orderSource",
        valueType: "text",
        editable: false
    },

    {
        title: "交付截止日期",
        dataIndex: "orderDeadline",
        valueType: "date",
        editable: false
    },
    {
        title: '创建时间',
        sorter: true,
        dataIndex: 'createTime',
        valueType: 'dateTime',
        hideInSearch: true,
        hideInForm: true,
        editable: false
    },
    {
        title: '更新时间',
        sorter: true,
        dataIndex: 'updateTime',
        valueType: 'dateTime',
        hideInSearch: true,
        hideInForm: true,
        editable: false
    },
    {
        title: '操作',
        dataIndex: 'option',
        valueType: 'option',
        width: 200,
        render: (_, record) => (
            <Space size="middle">
                <OrderActionButton/>

                <Typography.Link
                    onClick={() =>
                    {
                        setCurrentRow(record);
                        setDetailsModalVisible(true);
                    }}
                >
                    查看详情
                </Typography.Link>

                <Typography.Link onClick={() =>
                {

                }}>
                    编辑
                </Typography.Link>

                <Typography.Link type="danger" onClick={() => handleDelete(record)}>
                    删除
                </Typography.Link>
            </Space>
        ),
    },
]);
