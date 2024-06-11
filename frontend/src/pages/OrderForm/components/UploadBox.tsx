import React, {forwardRef, useImperativeHandle, useState} from "react";
import {message, UploadFile, UploadProps} from "antd";
import Dragger from "antd/lib/upload/Dragger";
import {InboxOutlined} from "@ant-design/icons";
import crypto from "crypto-js";


interface UploadBoxProps
{
    onFileUidChange: (files: OrderFormType.FileInfo[]) => void;
}

const UploadBox: React.FC<UploadBoxProps> = forwardRef(({ onFileUidChange }: UploadBoxProps, ref) =>
{
    const [ fileList, setFileList ] = useState<UploadFile[]>([]);

    const calculateSHA256 = (file: File): Promise<string> =>
    {
        return new Promise((resolve, reject) =>
        {
            const reader = new FileReader();
            reader.onload = () =>
            {
                const hash = crypto.SHA256(crypto.enc.Latin1.parse(reader.result as string)).toString();
                resolve(hash);
            };
            reader.onerror = reject;
            reader.readAsBinaryString(file);
        });
    };

    const handleChange: UploadProps["onChange"] = async (info) =>
    {
        const updatedFileList = [ ...info.fileList ];
        setFileList(updatedFileList);

        const fileWithHashes = await Promise.all(
            updatedFileList.map(async (file) =>
            {
                const sha256 = await calculateSHA256(file.originFileObj as File);
                return {
                    fileUid: file.uid,
                    fileName: file.name,
                    fileSha256: sha256,
                };
            })
        );

        // 通过回调将 fileUid, fileName 和 sha256 传递给父组件
        onFileUidChange(fileWithHashes);
    };


    useImperativeHandle(ref, () => ({
        uploadFiles: async (tokenMap) =>
        {
            for (const file of fileList)
            {
                const token = tokenMap[file.uid];

                if (token)
                {
                    const closeMsgBox = message.loading(`上传中：${file.name}`, 0)

                    closeMsgBox()
                }
                else {
                    message.error(`上传失败：文件 ${file.name} 未找到对应的 token`);
                }
            }
        },
    }));


    const uploadProps: UploadProps = {
        name: "file",
        multiple: true,
        fileList,
        onChange: handleChange,
    };


    return (
        <Dragger {...uploadProps}>
            <p className="ant-upload-drag-icon">
                <InboxOutlined/>
            </p>
            <p className="ant-upload-text">上传订单附件</p>
            <p className="ant-upload-hint">支持单个或批量上传</p>
        </Dragger>
    );
});

export default UploadBox;
