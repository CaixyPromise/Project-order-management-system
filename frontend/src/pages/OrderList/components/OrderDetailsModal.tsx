import React, {useEffect, useMemo, useState} from "react";
import {Descriptions, message, Modal, Tag} from "antd";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {queryOrderVO} from "@/pages/OrderList/server";
import dayjs from "dayjs";
import {ContactTypeEnum} from "@/enums/ContactTypeEnum";
import {OrderSourceEnum} from "@/enums/OrderSourceEnum";
import {PaymentMethodEnum} from "@/enums/PaymentMethodEnum";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";
import Paragraph from "antd/es/typography/Paragraph";
import OrderFileTable from "@/pages/OrderList/components/OrderFileTable";

interface InfoModalProps
{
    currentRow: API.OrderInfoVO
    open: boolean
    setOpen: React.Dispatch<React.SetStateAction<boolean>>
}


const InfoModal: React.FC<InfoModalProps> = ({ currentRow, open, setOpen }) =>
{
    const [ queryHandler, isLoading ] = useAsyncHandler<API.OrderInfoVO>();
    const [ data, setData ] = useState<API.OrderInfoVO>({});


    const fetchData = async () =>
    {
        const response = await queryHandler(queryOrderVO, [ currentRow.id ], () => message.error('查询订单详情失败'));
        if (response)
        {
            setData(response as API.OrderInfoVO);
        }
    }

    useEffect(() =>
    {
        if (open && currentRow.id)
        {
            fetchData()
        }
    }, [ open, currentRow.id ]);

    const dayTextColor: { sort: number[], colorDict: {[key: number]: string}} = useMemo(() =>
    {
        const colorDict = {
            3: "red",
            5: "orange",
            10: "#1890ff",
        };
        const sort = Object.keys(colorDict).map(Number).sort((a, b) => a - b);

        return {
            sort,
            colorDict
        }
    }, [])


    const getTagsList = (info: string | undefined) =>
    {
        if (info && info.trim().length > 0 && info !== '[]')
        {
            try
            {
                const tags = JSON.parse(info);
                return tags.length > 0 ? tags?.map((item: string, key: number) =>
                {
                    return <Tag key={key}>{item}</Tag>
                }) : '无';
            }
            catch (error)
            {
                return <span style={{ color: "red" }}>标签解析错误</span>;
            }
        }
        else
        {
            return <span style={{ color: "green" }}>无</span>;
        }
    }


    const getMarkdownContent = (content: string | undefined) =>
    {
        if (content && content.length > 0)
        {
            return <div dangerouslySetInnerHTML={{ __html: content }}></div>;
        }
        else
        {
            return <span>无</span>
        }
    }

    const getDayText = (timeText: string | undefined) =>
    {
        if (timeText)
        {
            return dayjs(timeText).format("YYYY-MM-DD HH:mm:ss");
        }
        else
        {
            return "暂无";
        }
    }

    // 获取颜色函数
    const getDayColor = (diffDay: number) =>
    {
        // 获取最接近的天数差异颜色
        for (const day of dayTextColor.sort)
        {
            if (diffDay <= day)
            {
                return dayTextColor.colorDict[day];
            }
        }
        return "green";
    };

    const getDangerDayText = (timeText: string | undefined) =>
    {
        if (timeText)
        {
            const time = dayjs(timeText);
            const now = dayjs();
            const diffDay = now.diff(time, 'day');
            // 如果今天距离目标时间还差3天，则返回红色
            // 如果今天距离目标时间还差7天，则返回黄色
            // 如果今天距离目标时间还差10天，则返回蓝色
            return <span style={{ color: getDayColor(diffDay) }}>{getDayText(timeText)}，距离{diffDay}天</span>;
        }
        else
        {
            return "暂无";
        }
    }

    return <>
        <Modal
            open={open}
            footer={null}
            onCancel={() => setOpen(false)}
            loading={data && isLoading} width={1500}
            destroyOnClose
        >
            <Descriptions title="订单详情" bordered column={2}>
                <Descriptions.Item label="ID">{data.id}</Descriptions.Item>

                <Descriptions.Item label="订单平台Id">{data.orderId}</Descriptions.Item>
                <Descriptions.Item label="订单状态">{OrderStatusEnum.getEnumByValue(
                    data.orderStatus)?.getText()}</Descriptions.Item>
                <Descriptions.Item label="创建时间">{getDayText(data.createTime)}</Descriptions.Item>
                <Descriptions.Item label="更新时间">{getDayText(data.updateTime)}</Descriptions.Item>
                <Descriptions.Item label="订单名称">{data.orderTitle}</Descriptions.Item>
                <Descriptions.Item label="创建用户名称">{data.creatorName}</Descriptions.Item>

                <Descriptions.Item label="订单金额">{data.amount}</Descriptions.Item>
                <Descriptions.Item label="已支付金额">{data.amountPaid}</Descriptions.Item>

                <Descriptions.Item label="联系方式类型">{ContactTypeEnum.getEnumByValue(
                    data?.customerContactType)?.getText()}</Descriptions.Item>
                <Descriptions.Item label="客户联系信息">{
                    <Paragraph copyable>
                        {data.customerContact}
                    </Paragraph>
                }</Descriptions.Item>

                {data.customerEmail && (
                    <Descriptions.Item label="顾客邮箱">{data.customerEmail ?
                        <Paragraph copyable>{data.customerEmail}</Paragraph> :
                        "暂无"}</Descriptions.Item>
                )}
                <Descriptions.Item label="是否对外分配">{data.isAssigned ? '是' : '否'}</Descriptions.Item>
                <Descriptions.Item label="订单分配人微信ID">{data.orderAssignToWxId ?
                    <Paragraph copyable>{data.orderAssignToWxId}</Paragraph> :
                    "无"}</Descriptions.Item>
                <Descriptions.Item label="是否支付">{data.isPaid ? '是' : '否'}</Descriptions.Item>
                <Descriptions.Item label="支付方式">{PaymentMethodEnum.getByCode(
                    data.paymentMethod)?.getText()}</Descriptions.Item>

                <Descriptions.Item label="订单来源">{OrderSourceEnum.getByCode(
                    data.orderSource)?.getText()}</Descriptions.Item>

                {data.orderCommissionRate !== undefined && (
                    <Descriptions.Item label="订单佣金比例">{data.orderCommissionRate}%</Descriptions.Item>
                )}
                <Descriptions.Item label="订单分类">{data.categoryName}</Descriptions.Item>


                <Descriptions.Item label="订单编程语言">{data.langName}</Descriptions.Item>

                <Descriptions.Item label="订单标签">{getTagsList(data.orderTags)}</Descriptions.Item>

                <Descriptions.Item label="交付截止日期">{getDangerDayText(data.orderDeadline)}</Descriptions.Item>

                <Descriptions.Item label="订单完成时间">{getDayText(data.orderCompletionTime)}</Descriptions.Item>


                <Descriptions.Item label="订单开始日期">{getDayText(data.orderStartDate)}</Descriptions.Item>


                <Descriptions.Item label="订单结束日期">{getDayText(data.orderEndDate)}</Descriptions.Item>

                <Descriptions.Item label="是否有效">{data.isValid === 1 ? '是' :
                    <span style={{ color: "red" }}>否</span>}</Descriptions.Item>
                <Descriptions.Item label="订单描述" span={2}>{getMarkdownContent(
                    data.orderDesc)}</Descriptions.Item>
                <Descriptions.Item label="订单备注" span={2}>{getMarkdownContent(
                    data.orderRemark)}</Descriptions.Item>
            </Descriptions>
        </Modal>
    </>
}

export default InfoModal;
