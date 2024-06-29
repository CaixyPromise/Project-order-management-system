import {
    deleteOrderInfoUsingPost1,
    getOrderInfoVoByIdUsingGet1,
    updateOrderInfoUsingPost1
} from "@/services/backend/orderController";
import {message} from "antd";

export const postUpdateOrder = async (values: API.OrderInfoUpdateRequest) =>
{
    const { data } = await updateOrderInfoUsingPost1(values);
    return data;
}

export const queryOrderVO = async (id: string) =>
{
    // @ts-ignore
    const { code, data } = await getOrderInfoVoByIdUsingGet1({ id })
    if (code === 0)
    {
        return data as API.OrderInfoVO
    }
    return {} as API.OrderInfoVO
}

export const deleteOrder = async (id: string) =>
{
    const { code } = await deleteOrderInfoUsingPost1({ id });
    return code === 0
}


export const postUpdate = async (values: API.OrderInfoUpdateRequest) =>
{
    const { data } = await updateOrderInfoUsingPost1(values);
    return data as boolean;
}


/**
 * 删除节点
 *
 * @param row
 */
export const handleDelete = async (row: API.OrderInfoPageVO, actionRef) =>
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
