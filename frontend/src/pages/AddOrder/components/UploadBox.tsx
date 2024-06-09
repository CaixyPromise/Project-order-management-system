import React from "react";
import {message, UploadProps} from "antd";
import Dragger from "antd/lib/upload/Dragger";
import {InboxOutlined} from "@ant-design/icons";


const UploadBox: React.FC = () =>
{
    const uploadProps: UploadProps = {
        name: 'file',
        multiple: true,
        fileList: [],

    };
    return <>
        <Dragger {...uploadProps}>
            <p className="ant-upload-drag-icon">
                <InboxOutlined/>
            </p>
            <p className="ant-upload-text">上传订单附件</p>
            <p className="ant-upload-hint">
                支持单个或批量上传
            </p>
        </Dragger>
    </>
}

export default UploadBox;
