import {getCategoryOptionListUsingGet1} from "@/services/backend/orderCategoryController";
import {addOrderInfoUsingPost1, updateOrderInfoUsingPost1} from "@/services/backend/orderController";
import {RcFile} from "antd/lib/upload";
import {UploadType} from "@/constants/UploadType";
import {getLanguageTypeOptionListUsingGet1} from "@/services/backend/languageTypeController";
import {uploadFileUsingPost1} from "@/services/backend/fileController";
import {OrderFormServer} from "@/pages/OrderForm/typings";


const fetchCategory = async () =>
{
    const { data, code } = await getCategoryOptionListUsingGet1();
    if (code === 0 && data)
    {
        // 确保value以字符串形式处理
        return data.map(item => ({
            ...item,
            value: item?.value.toString()
        })) as OrderFormServer.CategoryResponse;
    }
    return [] as OrderFormServer.CategoryResponse;
}

const fetchLangType = async () =>
{
    const { data, code } = await getLanguageTypeOptionListUsingGet1();
    if (code === 0 && data)
    {
        // 确保value以字符串形式处理
        return data.map(item => ({
            ...item,
            value: String(item.value)
        })) as OrderFormServer.LangTypeResponse;
    }
    return [] as OrderFormServer.LangTypeResponse;
}


const postOrderInfo = async (orderInfo: API.OrderInfoAddRequest | API.OrderInfoUpdateRequest, isUpdate: boolean) =>
{
    let data, code;
    if (isUpdate)
    {
        data = await updateOrderInfoUsingPost1(orderInfo)
        code = data?.code
    }
    else
    {
        data = await addOrderInfoUsingPost1(orderInfo)
        code = data?.code
    }
    return code === 0 ? data as API.OrderInfoUploadResponse : {}
}

const uploadAttachment = async (file: RcFile, token: string): Promise<boolean> =>
{
    const { code } = await uploadFileUsingPost1({
        biz: UploadType.ORDER_ATTACHMENT,
        token
    }, {}, file)
    return code !== undefined && code === 0;
}


export {
    fetchCategory,
    fetchLangType,
    postOrderInfo,
    uploadAttachment
}
