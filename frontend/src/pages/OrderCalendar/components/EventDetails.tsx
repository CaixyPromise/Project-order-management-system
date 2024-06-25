import React from "react";
import {Modal, Table} from "antd";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";
import dayjs from "dayjs";

interface EventDetailsProps
{
    dataSource: API.EventVOOrderInfoVO_[]
    open: boolean
    setOpen: React.Dispatch<React.SetStateAction<boolean>>
}

const columns = [
    {
        title: '时间',
        dataIndex: ['content', 'orderStartDate'], // Assuming 'orderStartDate' is the intended date field for display
        key: 'orderStartDate',
        render: text => text ? dayjs(text).format('YYYY-MM-DD') : '未知'
    },
    {
        title: '名称',
        dataIndex: ['content', 'orderTitle'],
        key: 'orderTitle'
    },
    {
        title: '对外分配',
        dataIndex: ['content', 'isAssigned'],
        key: 'isAssigned',
        render: isAssigned => isAssigned === 0 ? "否" : "是"
    },
    {
        title: '订单状态',
        dataIndex: ['content', 'orderStatus'],
        key: 'orderStatus',
        render: status => OrderStatusEnum.getEnumByValue(status)?.getText() || '未知'
    },
    {
        title: '订单描述信息',
        dataIndex: ['content', 'orderDesc'],
        key: 'orderDesc',
        render: (text: string) => <div dangerouslySetInnerHTML={{__html : text}}></div>
    },
    {
        title: '订单备注',
        dataIndex: ['content', 'orderRemark'],
        key: 'orderRemark',
        render: (text: string) => <div dangerouslySetInnerHTML={{__html : text}}></div>
    },
    {
        title: '操作',
        key: 'action',
        render: (_, record) => (
            <a href={`/orderList/${record.content.id}`}>查看详情</a>
        )
    }
];

const EventDetails: React.FC<EventDetailsProps> = ({ dataSource, open, setOpen }) =>
{
    const close = () => {
        setOpen(false)
    }
    console.log(dataSource)
    return (
        <Modal
            open={open}
            title={"当日详情"}
            onCancel={close}
            footer={null}
            width={800}
        >
            <Table columns={columns} dataSource={dataSource} pagination={false} />
        </Modal>
    )
}

export default EventDetails;
