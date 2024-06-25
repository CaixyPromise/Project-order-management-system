import React from "react";
import {Badge, Card, Empty} from "antd";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";
import dayjs from "dayjs";

interface EventListProps
{
    data: API.EventVOOrderInfoVO_
}

const CardTitle: React.FC<{ item: API.EventVOOrderInfoVO_ }> = ({ item }) =>
{
    return (
        <div>
            <a href={`/orderList/${item.id}`}>
                {/*// @ts-ignore*/}
                时间： <Badge status={item.level} text={dayjs(item.content?.orderDeadline).format(
                "YYYY-MM-DD")}/> 名称：{item.content?.orderTitle}
            </a>
        </div>
    );
};

const EventList: React.FC<EventListProps> = ({ data }) =>
{
    const allEvents = Object.values(data).flat();  // Flatten all events into a single array for rendering
    console.log(allEvents)
    if (allEvents.length === 0)
    {
        return <Empty description="暂无待办"/>;
    }

    return (
        <>
            {allEvents.map((value, index) => (
                <Card
                    key={index}
                    type="inner"
                    // @ts-ignore
                    title={<CardTitle item={value}/>}
                    style={{ marginBottom: "8px" }}
                >
                    {/*// @ts-ignore*/}
                    <p>标题：{value.content?.orderTitle}</p>
                    {/*// @ts-ignore*/}
                    <p>对外分配：{value.content?.isAssigned === 0 ? "否" : "是"}</p>
                    {/*// @ts-ignore*/}
                    <p>订单状态：{OrderStatusEnum.getEnumByValue(value.content?.orderStatus)?.getText()}</p>
                    <p>订单描述信息：</p>
                    {/*// @ts-ignore*/}
                    <div dangerouslySetInnerHTML={{ __html: value.content?.orderDesc || "无" }}></div>
                    <p>订单备注</p>
                    {/*// @ts-ignore*/}
                    <div dangerouslySetInnerHTML={{ __html: value.content?.orderRemark || "无" }}></div>
                </Card>
            ))}
        </>
    );
}

export default EventList;
