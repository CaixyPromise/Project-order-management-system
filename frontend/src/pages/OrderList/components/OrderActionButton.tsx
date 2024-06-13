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
        // 添加修改订单状态的逻辑
        console.log("修改订单状态");
    };

    const handleEditOrderInfo = () =>
    {
        setPopoverVisible(false);
        // 添加修改订单信息的逻辑
        console.log("修改订单信息");
    };

    const handleAddOrderFile = () => {
        setPopoverVisible(false);
        // 添加上传文件的逻辑
        console.log("上传文件");
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
            <Link>修改</Link>
        </Popover>
    );
};

export default OrderActions;
