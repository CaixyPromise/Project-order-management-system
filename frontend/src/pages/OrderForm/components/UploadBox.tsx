import React, {forwardRef, useImperativeHandle, useState} from "react";
import {message, UploadFile, UploadProps} from "antd";
import Dragger from "antd/lib/upload/Dragger";
import {InboxOutlined} from "@ant-design/icons";
import crypto from "crypto-js";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {RcFile} from "antd/lib/upload";
import {history} from "@umijs/max";

interface UploadBoxProps
{
    onFileUidChange: (files: OrderFormType.FileInfo[]) => void;
}

export interface UploadBoxHandle
{
    uploadFiles: (tokenMap: Record<string, API.UploadFileInfoDTO>, uploadActionFunction: (file: RcFile, token: string) => Promise<boolean>) => Promise<boolean>;
}

const UploadBox: React.ForwardRefRenderFunction<UploadBoxHandle, UploadBoxProps> = ({ onFileUidChange }, ref) =>
{
    const [ fileList, setFileList ] = useState<UploadFile[]>([]);
    const [ uploadHandler, isRunning ] = useAsyncHandler<boolean>();

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
        uploadFiles: async (tokenMap: Record<string, API.UploadFileInfoDTO>, uploadActionFunction: (file: RcFile, token: string) => Promise<boolean>): Promise<boolean> =>
        {
            if (isRunning)
            {
                message.warning("文件上传正在进行中，请稍候...");
                return false;
            }

            let allSuccessful = true;

            for (const file of fileList)
            {
                const fileInfoDTO = tokenMap[file.uid];

                if (fileInfoDTO)
                {
                    const closeMsgBox = message.loading(`上传中：${file.name}`, 0);
                    try
                    {
                        const result = await uploadHandler(
                            uploadActionFunction,
                            [ file.originFileObj as RcFile, fileInfoDTO ],
                            (error) => message.error(`上传失败: ${file.name}，错误信息: ${error}`)
                        );
                        if (!result)
                        {
                            allSuccessful = false;
                        }
                    }
                    finally
                    {
                        closeMsgBox();
                    }
                }
                else
                {
                    message.error(`上传失败：文件 ${file.name} 未找到对应的 token`);
                    allSuccessful = false;
                }
            }

            if (allSuccessful)
            {
                message.success("所有文件上传成功");
                setTimeout(() => {
                    message.success("正在跳转至订单列表页面...", 1000)
                    history.push("/orderList")
                }, 1000)
            }

            return allSuccessful;
        },
    }), [ fileList, isRunning, uploadHandler ]);

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
};

export default forwardRef<UploadBoxHandle, UploadBoxProps>(UploadBox);
