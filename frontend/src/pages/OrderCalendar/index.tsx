import React, {useEffect, useState} from 'react';
import {Badge, Calendar, Card, Col, List, message, Row, Select} from 'antd';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {queryTask} from "@/pages/OrderCalendar/server";
import dayjs from "dayjs";

const mockData = {
    "2024-06-01": [
        { "type": "error", "content": "System outage", "level": "High" },
        { "type": "warning", "content": "Scheduled maintenance", "level": "Medium" }
    ],
    "2024-06-15": [
        { "type": "success", "content": "New feature release", "level": "Low" }
    ],
    "2024-06-18": [
        { "type": "error", "content": "Service disruption", "level": "High" },
        { "type": "warning", "content": "Performance issues", "level": "Medium" }
    ]
};

const App = () =>
{
    const [ year, setYear ] = useState(new Date().getFullYear());
    const [ month, setMonth ] = useState(new Date().getMonth() + 1);
    const [ events, setEvents ] = useState<API.EventVOOrderInfoVO_[]>([]);
    const [queryHandler, isLoading] = useAsyncHandler<API.EventVOOrderInfoVO_[]>()


    const dateCellRender = (value: any) =>
    {
        const dayEvents = events.filter(event => event.date.startsWith(value.format('YYYY-MM-DD')));
        return (
            <List>
                {dayEvents.map((item, index) => (
                    <List.Item key={index}>
                        {/*// @ts-ignore*/}
                        <Badge status={item.level} text={item.content?.orderTitle}/>
                    </List.Item>
                ))}
            </List>
        );
    };

    useEffect(() =>
    {
        const fetchEvents = async () =>
        {
            const result = await queryHandler(queryTask, [ year, month ], () => message.error("获取待办失败"))
            setEvents(result);
        };

        fetchEvents();
    }, [ year, month ]);

    const handleYearChange = (newYear: number) => {
        setYear(newYear);
    };

    const handleMonthChange = (newMonth: number) => {
        setMonth(newMonth);
    };

    const CardTitle: React.FC<{ item: API.EventVOOrderInfoVO_ }> = ({ item }) => {
        return (
            <div>
               时间： <Badge status={item.level} text={dayjs(item.content?.orderDeadline).format("YYYY-MM-DD")} /> 名称：{item.content?.orderTitle}
            </div>
        );
    };



    return (
        <Row gutter={16}>
            <Col span={18}>
                <Select defaultValue={year} style={{ width: 120 }} onChange={handleYearChange}>
                    {Array.from({ length: 10 }, (_, i) => (
                        <Option value={year - i} key={i}>{year - i}</Option>
                    ))}
                </Select>
                <Select defaultValue={month} style={{ width: 120 }} onChange={handleMonthChange}>
                    {Array.from({ length: 12 }, (_, i) => (
                        <Option value={i + 1} key={i}>{i + 1}</Option>
                    ))}
                </Select>
                <Calendar cellRender={dateCellRender} />
            </Col>
            <Col span={6}>
                <Card title="Events Details">
                    {events.map(event => (
                        <Card key={event.id} type="inner" title={<CardTitle item={event} />}>
                            <p>标题：{event.content?.orderTitle}</p>
                        </Card>
                    ))}
                </Card>
            </Col>
        </Row>
    );
};

export default App;
