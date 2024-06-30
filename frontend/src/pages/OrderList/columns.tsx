import type {ProColumns} from "@ant-design/pro-components";
import {Space, Typography} from "antd";
import OrderActionButton from "@/pages/OrderList/components/OrderActionButton";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";
import {history} from "@@/exports";
import {OrderSourceEnum} from "@/enums/OrderSourceEnum";
import BooleanTag from "@/components/BooleanTag";
import {fetchCategory, fetchLangType} from "@/pages/OrderForm/utils";
import {ColumnsParams} from "@/typings";

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
        title: "客户联系方式",
        dataIndex: "customerContact",
        valueType: "text",
        hideInTable: true,
    },
    {
        title: "客户邮箱",
        dataIndex: "customerEmail",
        valueType: "text",
        hideInTable: true,
    },
    {
        title: "订单分配人微信Id",
        dataIndex: "orderAssignToWxId",
        valueType: "text",
        hideInTable: true,
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
        valueType: "digit",
        hideInForm: true,
    },
    {
        title: "已支付金额",
        dataIndex: "amountPaid",
        valueType: "text",
        hideInSearch: true,
    },
    {
        title: "订单编程语言",
        dataIndex: "langName",
        valueType: "select",
        request: async () =>
        {
            return fetchLangType()
        },
        editable: false,
    },
    {
        title: "订单分类名称",
        dataIndex: "orderCategoryName",
        valueType: "select",
        request: async () =>
        {
            return fetchCategory()
        },
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
        editable: false,
        hideInSearch: true
    },
    {
        title: "订单状态",
        dataIndex: "orderStatus",
        valueType: "select",
        valueEnum: OrderStatusEnum.getAllStatus,
        fieldProps: {
            options: OrderStatusEnum.getAllOptions()
        }
    },
    {
        title: "是否外包",
        dataIndex: "isAssigned",
        valueType: "select",
        valueEnum: {
            1: {
                text: "是",
                status: "success"
            },
            0: {
                text: "否",
                status: "process"
            }
        },
        render: (_, record) =>
        {
            return <BooleanTag text={record.isAssigned}/>
        }
    },
    {
        title: "是否支付",
        dataIndex: "isPaid",
        valueType: "select",
        render: (_, record) =>
        {
            return <BooleanTag text={record.isPaid}/>
        },
        valueEnum: {
            1: {
                text: "是",
                status: "success"
            },
            0: {
                text: "否",
                status: "process"
            }
        },
    },
    {
        title: "订单来源",
        dataIndex: "orderSource",
        valueType: "select",
        editable: false,
        fieldProps: {
            options: OrderSourceEnum.getAllOptions()
        }
    },

    {
        title: "交付截止日期",
        dataIndex: "orderDeadline",
        valueType: "date",
        editable: false,
        hideInSearch: true,

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
                <OrderActionButton record={record}/>

                <Typography.Link
                    onClick={() =>
                    {
                        setCurrentRow?.(record);
                        setDetailsModalVisible(true);
                    }}
                >
                    查看详情
                </Typography.Link>

                <Typography.Link onClick={() =>
                {
                    history.push(`/addOrder/${record.id}`)
                }}>
                    编辑
                </Typography.Link>

                <Typography.Link type="danger" onClick={() => handleDelete?.(record)}>
                    删除
                </Typography.Link>
            </Space>
        ),
    },
]);
