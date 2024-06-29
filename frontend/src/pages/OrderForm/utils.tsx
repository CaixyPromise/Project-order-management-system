import {OrderFormServer} from "@/pages/OrderForm/typings";
import {queryCategory, queryLangType} from "@/pages/OrderForm/server";

const fetchLangType = async () =>
{
    return (await queryLangType()).map(item => ({
        ...item,
        value: String(item.value)
    })) as OrderFormServer.LangTypeResponse;
}

const fetchCategory = async () =>
{
    return (await queryCategory()).map(item => ({
        ...item,
        value: String(item.value)
    })) as OrderFormServer.CategoryResponse
}


export {
    fetchLangType,
    fetchCategory
}
