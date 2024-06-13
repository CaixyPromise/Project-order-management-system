import CreateModal from '@/pages/Admin/User/components/CreateModal';
import UpdateModal from '@/pages/Admin/User/components/UpdateModal';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType} from '@ant-design/pro-components';
import {PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, message} from 'antd';
import React, {useMemo, useRef, useState} from 'react';
import {deleteUserUsingPost1} from "@/services/backend/userController";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {getOrderListColumn} from "@/pages/OrderList/columns";
import {listOrderInfoVoByPageUsingPost1} from "@/services/backend/orderController";
import {history} from "@umijs/max";

/**
 * 用户管理页面
 *
 * @constructor
 */
const UserAdminPage: React.FC = () =>
{
    // 是否显示新建窗口
    const [ createModalVisible, setCreateModalVisible ] = useState<boolean>(false);
    // 是否显示更新窗口
    const [ updateModalVisible, setUpdateModalVisible ] = useState<boolean>(false);
    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<API.User>({});
    const [ queryHandler, isLoading] = useAsyncHandler<{
        code?: number;
        data?: API.PageUser_;
        message?: string;
    }>()
    /**
     * 删除节点
     *
     * @param row
     */
    const handleDelete = async (row: API.User) =>
    {
        const hide = message.loading('正在删除');
        if (!row) return true;
        try
        {
            await deleteUserUsingPost1({
                id: row.id as any,
            });
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
    const column = useMemo(() => getOrderListColumn({ setCurrentRow , setUpdateModalVisible, handleDelete }), []);




    /**
     * 表格列配置
     */

    return (
        <PageContainer>
            <ProTable<API.User>
                headerTitle={'查询表格'}
                actionRef={actionRef}
                rowKey="key"
                search={{
                    labelWidth: 120,
                }}
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() =>
                        {
                            history.push("/addOrder/");
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
        </PageContainer>
    );
};
export default UserAdminPage;
