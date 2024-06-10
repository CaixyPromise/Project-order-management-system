import {getOrderCategoryVosUsingGet1} from "@/services/backend/orderCategoryController";
import {getLangTypeVoSUsingGet1} from "@/services/backend/languageTypeController";
import {addOrderInfoUsingPost1} from "@/services/backend/orderController";

const fetchCategory = async () =>
{
    const { data, code } = await getOrderCategoryVosUsingGet1()
    if (code === 0)
    {
        return data as OrderFormServer.CategoryResponse
    }
    return {} as OrderFormServer.CategoryResponse
}

const fetchLangType = async () =>
{
    const { data, code } = await getLangTypeVoSUsingGet1()
    if (code === 0)
    {
        return data as OrderFormServer.LangTypeResponse
    }
    return {} as OrderFormServer.LangTypeResponse
}


const postOrderInfo = async (orderInfo: API.OrderInfoAddRequest) =>
{
    const {data, code} = await addOrderInfoUsingPost1(orderInfo)
    if (code === 0)
    {
        return data as unknown as string
    }
    return ''
}


export {
    fetchCategory,
    fetchLangType,
    postOrderInfo,
}
