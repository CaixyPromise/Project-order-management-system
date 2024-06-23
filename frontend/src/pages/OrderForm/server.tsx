import {getOrderCategoryVosUsingGet1} from "@/services/backend/orderCategoryController";
import {addOrderInfoUsingPost1} from "@/services/backend/orderController";
import {RcFile} from "antd/lib/upload";
import {UploadType} from "@/constants/UploadType";
import {getLangTypeVoSUsingGet1} from "@/services/backend/languageTypeController";
import {uploadFileUsingPost1} from "@/services/backend/fileController";

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
    const { data, code } = await addOrderInfoUsingPost1(orderInfo)
    if (code === 0)
    {
        return data as unknown as API.OrderInfoAddResponse
    }
    return {}
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
