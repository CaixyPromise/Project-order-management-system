import {OptionArray} from "@/typings";

declare namespace OrderFormServer
{
    type CategoryResponse = OptionArray<string>
    type LangTypeResponse = OptionArray<string>
}


declare namespace OrderFormType
{
    type FileInfo = {
        fileUid: string;
        fileName: string;
        fileSha256: string;
    }
}
