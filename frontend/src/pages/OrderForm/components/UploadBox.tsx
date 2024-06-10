import React, {useState} from "react";
import {UploadFile, UploadProps} from "antd";
import Dragger from "antd/lib/upload/Dragger";
import {InboxOutlined} from "@ant-design/icons";

interface UploadBoxProps
{
    onFileUidChange: (uids: string[]) => void;
}

const UploadBox: React.FC<UploadBoxProps> = ({ onFileUidChange }) =>
{
    const [ fileList, setFileList ] = useState<UploadFile[]>([]);

    const handleChange: UploadProps["onChange"] = (info) =>
    {
        const updatedFileList = [ ...info.fileList ];
        setFileList(updatedFileList);

        // 提取所有文件的 uid
        const uids = updatedFileList.map(file => file.uid);
        // 通过回调将 uid 传递给父组件
        onFileUidChange(uids);
    };

    const uploadProps: UploadProps = {
        name: 'file',
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
            <p className="ant-upload-hint">
                支持单个或批量上传
            </p>
        </Dragger>
    );
};

export default UploadBox;
