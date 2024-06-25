import React, {useEffect, useState} from 'react';
import {Badge, Calendar, Card, Col, List, message, Row, Spin} from 'antd';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {queryTask} from "@/pages/OrderCalendar/server";
import dayjs, {Dayjs} from "dayjs";
import EventList from './components/EventList';
import EventDetails from "@/pages/OrderCalendar/components/EventDetails";

const App = () =>
{
    const [ year, setYear ] = useState(new Date().getFullYear());
    const [ month, setMonth ] = useState(new Date().getMonth() + 1);
    const [ events, setEvents ] = useState<API.EventVOOrderInfoVO_>({});
    const [ queryHandler, isLoading ] = useAsyncHandler<API.EventVOOrderInfoVO_[]>()
    const [ selectedDateEvents, setSelectedDateEvents ] = useState<API.EventVOOrderInfoVO_[]>([]);
    const [ modalVisible, setModalVisible ] = useState<boolean>(false);

    const onSelect = (value: Dayjs) =>
    {
        const dayEvents = events[value.format('YYYY-MM-DD')] || [];
        setSelectedDateEvents(dayEvents);
        setModalVisible(true);
    };


    const dateCellRender = (value: Dayjs) =>
    {
        // @ts-ignore
        const dayEvents = events[value.format('YYYY-MM-DD')] || [];
        return (
            <List>
                {/*// @ts-ignore*/}
                {dayEvents.map((item, index) => (
                    <List.Item key={index}>
                        <Badge status={item.level} text={item.content.orderTitle}/>
                    </List.Item>
                ))}
            </List>
        );
    };

    useEffect(() =>
    {
        const fetchEvents = async () =>
        {
            // @ts-ignore
            const result = await queryHandler(() => queryTask(year, month), [], () => message.error("获取待办失败"));
            if (result)
            {
                const eventsMap: API.EventVOOrderInfoVO_ = result.reduce((acc: API.EventVOOrderInfoVO_, event) =>
                {
                    const eventDate: string = dayjs(event.date).format('YYYY-MM-DD');
                    // @ts-ignore
                    if (!acc[eventDate])
                    {
                        // @ts-ignore
                        acc[eventDate] = [];
                    }
                    // @ts-ignore
                    acc[eventDate].push(event);
                    return acc;
                }, {} as API.EventVOOrderInfoVO_);
                setEvents(eventsMap);
            }
        };
        fetchEvents();
    }, [ year, month ]);


    const onPanelChange = (value: dayjs.Dayjs) =>
    {
        setYear(value.year());
        setMonth(value.month() + 1);
    };


    return (
        <Row gutter={16} style={{ display: 'flex', height: '87vh', overflow: 'hidden' }}>
            <Col span={18} style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
                <div style={{ flexGrow: 1, overflowY: 'auto' }}>
                    <Calendar cellRender={dateCellRender} onPanelChange={onPanelChange} onSelect={onSelect}/>
                </div>
            </Col>
            <Col span={6} style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
                <Card title="待办订单" style={{ flexGrow: 1, overflowY: 'auto' }}>
                    <Spin spinning={isLoading}>
                        <EventList data={events}/>
                    </Spin>
                </Card>
            </Col>
            <EventDetails dataSource={selectedDateEvents} open={modalVisible} setOpen={setModalVisible}/>
        </Row>
    );
};

export default App;
