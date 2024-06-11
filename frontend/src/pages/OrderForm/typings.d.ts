declare namespace OrderFormServer
{
    type CategoryResponse = { [key: string]: API.OrderCategoryVO }
    type LangTypeResponse = { [key: string]: API.LanguageTypeVO }
}


declare namespace OrderFormType
{
    type FileInfo = {
        fileUid: string;
        fileName: string;
        fileSha256: string;
    }
}
