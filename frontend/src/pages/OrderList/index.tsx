import CreateModal from '@/pages/Admin/User/components/CreateModal';
import UpdateModal from '@/pages/Admin/User/components/UpdateModal';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType} from '@ant-design/pro-components';
import {PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, message} from 'antd';
import React, {useEffect, useMemo, useRef, useState} from 'react';
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {getOrderListColumn} from "@/pages/OrderList/columns";
import {listOrderInfoVoByPageUsingPost1} from "@/services/backend/orderController";
import {history} from "@umijs/max";
import OrderDetailsModal from "@/pages/OrderList/components/OrderDetailsModal";
import {deleteOrder} from "@/pages/OrderList/server";
import {useParams} from "@@/exports";

/**
 * 用户管理页面
 *
 * @constructor
 */
const UserAdminPage: React.FC = () =>
{
    const {id} = useParams();
    // 是否显示新建窗口
    const [ createModalVisible, setCreateModalVisible ] = useState<boolean>(false);
    // 是否显示更新窗口
    const [ updateModalVisible, setUpdateModalVisible ] = useState<boolean>(false);
    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<API.OrderInfoPageVO>({});
    const [ queryHandler] = useAsyncHandler<{
        code?: number;
        data?: API.PageOrderInfoPageVO_;
        message?: string;
    }>();
    // 定义 editableKeys 为 React.Key[] 类型
    const [ detailsModalVisible, setDetailsModalVisible ] = useState<boolean>(false);
    /**
     * 删除节点
     *
     * @param row
     */
    const handleDelete = async (row: API.OrderInfoPageVO) =>
    {
        const hide = message.loading('正在删除');
        if (!row) return true;
        try
        {
            // @ts-ignore
            await deleteOrder(row?.id as string);
            hide();
            message.success('删除成功');
            actionRef?.current?.reload();
            return true;
        }
        catch (error: any)
        {
            hide();
            message.error('删除失败，' + error.message);
            return false;
        }
    };


// 修改列配置，特别是 editable 相关的部分
    const column = useMemo(() => getOrderListColumn({
        setCurrentRow,
        setDetailsModalVisible,
        handleDelete,
        setUpdateModalVisible
    }), []); // 添加 editableKeys 作为依赖项，确保在其变化时重新计算

    useEffect(() => {
        if (id !== undefined)
        {
            setCurrentRow({
                id: id
            } as unknown as API.OrderInfoPageVO)
            setDetailsModalVisible(true)
        }
    }, [id])

    return (
        <PageContainer ghost={true}>
            <ProTable<API.OrderInfoPageVO>
                headerTitle={'查询表格'}
                actionRef={actionRef}
                rowKey="id"
                search={{
                    labelWidth: 120,
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
                request={async (params, sort, filter) =>
                {
                    const sortField = Object.keys(sort)?.[0];
                    const sortOrder = sort?.[sortField] ?? undefined;

                    const response = await queryHandler(async () =>
                    {
                        return await listOrderInfoVoByPageUsingPost1({
                            ...params,
                            sortField,
                            sortOrder,
                            ...filter,
                        } as API.UserQueryRequest);
                    },[],  error => {message.error(error.message)})
                    // @ts-ignore
                    const { data, code } = response;
                    return {
                        success: code === 0,
                        data: data?.records || [],
                        total: Number(data?.total) || 0,
                    };
                }}
                columns={column}
            />
            <CreateModal
                visible={createModalVisible}
                columns={column}
                onSubmit={() =>
                {
                    setCreateModalVisible(false);
                    actionRef.current?.reload();
                }}
                onCancel={() =>
                {
                    setCreateModalVisible(false);
                }}
            />
            <UpdateModal
                visible={updateModalVisible}
                columns={column}
                oldData={currentRow}
                onSubmit={() =>
                {
                    setUpdateModalVisible(false);
                    setCurrentRow({  });
                    actionRef.current?.reload();
                }}
                onCancel={() =>
                {
                    setUpdateModalVisible(false);
                }}
            />
            <OrderDetailsModal currentRow={currentRow} open={detailsModalVisible} setOpen={setDetailsModalVisible} />
        </PageContainer>
    );
};
export default UserAdminPage;
