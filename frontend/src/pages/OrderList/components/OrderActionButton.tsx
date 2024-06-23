import React, {useState} from 'react';
import {Button, DatePicker, message, Modal, Radio, Typography} from 'antd';
import moment from 'moment';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";
import {postUpdate} from "@/pages/OrderList/server";

const OrderActions: React.FC<{ record: API.OrderInfoPageVO }> = ({ record }) =>
{
    const [ modalVisible, setModalVisible ] = useState(false);
    const [ selectedStatus, setSelectedStatus ] = useState<OrderStatusEnum | null>(null);
    const [ completionDate, setCompletionDate ] = useState<moment.Moment | null>(moment());
    const [ updateStatusHandler, loading ] = useAsyncHandler<boolean>();

    const handleUpdateStatus = async () =>
    {
        if (!selectedStatus)
        {
            message.error('请选择一个状态');
            return;
        }
        if (selectedStatus === OrderStatusEnum.FINISHED && !completionDate)
        {
            message.error('请填写完成时间');
            return;
        }
        await updateStatusHandler(postUpdate, {
            id: record.id,
            orderStatus: selectedStatus.getCode(),
            completionDate: completionDate?.toISOString()
        });
        setModalVisible(false);
        setSelectedStatus(null);
        setCompletionDate(null);
    }

    return (
        <>
            <Typography.Link onClick={() => setModalVisible(true)}>状态流转</Typography.Link>
            <Modal
                title="更新订单状态"
                open={modalVisible}
                onOk={handleUpdateStatus}
                onCancel={() => setModalVisible(false)}
                confirmLoading={loading}
                okButtonProps={{ disabled: selectedStatus === OrderStatusEnum.FINISHED && !completionDate }}
            >
                <Radio.Group
                    onChange={e => setSelectedStatus(OrderStatusEnum.getEnumByValue(e.target.value))}
                    value={selectedStatus?.getCode()}
                >
                    {OrderStatusEnum.getAllValues().map((status) => (
                        <Radio key={status.getCode()} value={status.getCode()}>{status.getText()}</Radio>
                    ))}
                </Radio.Group>
                {selectedStatus === OrderStatusEnum.FINISHED && (
                    <DatePicker
                        style={{ marginTop: 16 }}
                        value={completionDate}
                        onChange={setCompletionDate}
                        showTime
                    />
                )}
            </Modal>
        </>
    );
};

export default OrderActions;
