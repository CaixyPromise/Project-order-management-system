import React, {useState} from 'react';
import {Button, Popover, Space, Typography} from 'antd';

const { Link } = Typography;

const OrderActions: React.FC = () =>
{
    const [ popoverVisible, setPopoverVisible ] = useState(false);

    const handleVisibleChange = (visible: boolean) =>
    {
        setPopoverVisible(visible);
    };

    const handleEditOrderStatus = () =>
    {
        setPopoverVisible(false);
        // todo: 添加修改订单状态的逻辑
    };

    const handleEditOrderInfo = () =>
    {
        setPopoverVisible(false);
        // todo: 添加修改订单信息的逻辑
    };

    const handleAddOrderFile = () => {
        setPopoverVisible(false);
        // todo: 添加上传文件的逻辑
    }


    const popoverContent = (
        <Space direction="horizontal">
            <Button type="primary" size="small" onClick={handleEditOrderStatus}>
                修改订单状态
            </Button>
            <Button type="default" size="small" onClick={handleEditOrderInfo}>
                修改订单信息
            </Button>
            <Button type="default" size="small" onClick={handleAddOrderFile}>
                新增订单文件
            </Button>
        </Space>
    );

    return (
        <Popover
            content={popoverContent}
            title="选择操作"
            trigger="click"
            open={popoverVisible}
            onOpenChange={handleVisibleChange}
        >
            <Link>状态流转</Link>
        </Popover>
    );
};

export default OrderActions;
