import {PlusOutlined} from '@ant-design/icons';
import type {ActionType} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, message, Spin} from 'antd';
import React, {useEffect, useMemo, useRef, useState} from 'react';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {getOrderListColumn} from "@/pages/OrderList/columns";
import {history} from "@umijs/max";
import OrderDetailsModal from "@/pages/OrderList/components/OrderDetailsModal";
import {useParams} from "@@/exports";
import {handleDelete} from "@/pages/OrderList/server";
import {listOrderInfoVoByPageUsingPost1, searchOrderInfoVoByPageUsingPost1} from "@/services/backend/orderController";
import OrderFileTable from "@/pages/OrderList/components/OrderFileTable";

/**
 * 订单管理页面
 *
 * @constructor
 */
const OrderAdminPage: React.FC = () =>
{
    const { id } = useParams();
    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<API.OrderInfoPageVO>({});
    const [ queryHandler, pageLoading ] = useAsyncHandler<{
        code?: number;
        data?: API.EsPageOrderInfoPageVO_;
        message?: string;
    }>();
    const [ searchAfter, setSearchAfter ] = useState<Record<string, any>[]>([]);
    // 定义 editableKeys 为 React.Key[] 类型
    const [ detailsModalVisible, setDetailsModalVisible ] = useState<boolean>(false);
    const [ isSearch, setIsSearch ] = useState<boolean>(false);

    // 修改列配置，特别是 editable 相关的部分
    const column = useMemo(() => getOrderListColumn({
        setCurrentRow,
        setDetailsModalVisible,
        handleDelete,
    }), []);

    useEffect(() =>
    {
        if (id !== undefined)
        {
            setCurrentRow({
                id: id
            } as unknown as API.OrderInfoPageVO)
            setDetailsModalVisible(true)
        }
    }, [ id ])

    return (
        <>
            <Spin spinning={pageLoading}>
                <ProTable<API.OrderInfoPageVO>
                    headerTitle={'查询表格'}
                    actionRef={actionRef}
                    rowKey="id"
                    expandable={{
                        expandedRowRender: record => <OrderFileTable
                            dataSource={record.orderAttachmentList}
                            orderId={record.id}
                        />
                    }}
                    search={{
                        labelWidth: 250,
                    }}
                    toolBarRender={() => [
                        <Button
                            type="primary"
                            key="primary"
                            onClick={() =>
                            {
                                history.push("/addOrder");
                            }}
                        >
                            <PlusOutlined/> 新建
                        </Button>,
                    ]}
                    onSubmit={() => {
                        setIsSearch(true)
                    }}
                    request={async (params, sort, filter) =>
                    {
                        const sortField = Object.keys(sort)?.[0];
                        const sortOrder = sort?.[sortField] ?? undefined;
                        console.log(filter)
                        const response = await queryHandler(async () =>
                        {
                            const payload = {
                                ...params,
                                sortField,
                                sortOrder,
                                ...filter,
                            } as API.UserQueryRequest
                            if (!isSearch)
                            {
                                return await listOrderInfoVoByPageUsingPost1({
                                    ...payload,
                                    searchAfter
                                });
                            }
                            return await searchOrderInfoVoByPageUsingPost1(payload);
                        }, [], error => {
                            message.error(error.message)
                        })
                        // @ts-ignore
                        const { data, code } = response;
                        setSearchAfter(() =>
                        {
                            setIsSearch(false)
                            if (data?.searchAfter)
                            {
                                return data.searchAfter
                            }
                        });
                        return {
                            success: code === 0,
                            data: data?.records || [],
                            total: Number(data?.total) || 0,
                        };
                    }}
                    columns={column}
                />
            </Spin>
            {/*// @ts-ignore*/}
            <OrderDetailsModal currentRow={currentRow} open={detailsModalVisible} setOpen={setDetailsModalVisible}/>
        </>
    );
};
export default OrderAdminPage;
