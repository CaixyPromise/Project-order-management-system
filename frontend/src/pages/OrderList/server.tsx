import {
    deleteOrderInfoUsingPost1,
    getOrderInfoVoByIdUsingGet1,
    updateOrderInfoUsingPost1
} from "@/services/backend/orderController";

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
