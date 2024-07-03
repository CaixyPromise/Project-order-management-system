import React from "react";
import {ProColumns, ProTable} from "@ant-design/pro-components";
import dayjs from "dayjs";
import {message, Space, Typography} from "antd";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {getOrderFileDownloadUrlByIdUsingGet1} from "@/services/backend/orderController";
import {downloadFileByIdUsingGet1} from "@/services/backend/fileController";
import {UploadType} from "@/constants/UploadType";
import {calculateSHA256} from "@/utils/CryptoUtils";

interface OrderFileListProps
{
    dataSource: API.OrderFilePageVO[]
    orderId: number
}


const OrderFileList: React.FC<OrderFileListProps> = ({ dataSource, orderId }) =>
{
    const [ downloadHandler ] = useAsyncHandler();
    const fetchUrl = async (record: API.OrderFilePageVO): Promise<API.DownloadFileVO> =>
    {
        const { data } = await getOrderFileDownloadUrlByIdUsingGet1({
            id: record.id,
            orderId: orderId
        } as API.getOrderFileDownloadUrlByIdUsingGET1Params)
        console.log("data: ", data)
        return data as API.DownloadFileVO
    }
    // 定义文件大小格式化函数
    const formatFileSize = (size: number): string =>
    {
        if (size < 1024)
        {
            return size + ' B';
        }
        else if (size < 1048576)
        {
            return (size / 1024).toFixed(2) + ' KB';
        }
        else if (size < 1073741824)
        {
            return (size / 1048576).toFixed(2) + ' MB';
        }
        else if (size < 1099511627776)
        {
            return (size / 1073741824).toFixed(2) + ' GB';
        }
        else
        {
            return (size / 1099511627776).toFixed(2) + ' TB';
        }
    };

    const downloadFile = async (downloadFileVO: API.DownloadFileVO) =>
    {
        if (!downloadFileVO)
        {
            message.error("文件不存在");
            return;
        }
        const response = await downloadFileByIdUsingGet1({
            bizName: UploadType.ORDER_ATTACHMENT,
            id: downloadFileVO.id
        }, {
            responseType: "blob",
            getResponse: true,
        });

        if (!response)
        {
            message.error("文件下载失败");
            return;
        }
        const contentDisposition = response.headers["content-type"]
        let fileName = 'downloadedFile';
        if (contentDisposition)
        {
            const matches = contentDisposition.match(/filename=(.*);/);
            console.log("matches: ", matches)
            if (matches && matches[1])
            {
                fileName = decodeURIComponent(matches[1]);
            }
        }


        const blob = response.data;
        const file = new File([blob], fileName, { type: blob.type });

        try {
            const hash = await calculateSHA256(file);
            if (hash !== downloadFileVO.sha256)
            {
                message.error("文件校验失败");
                return;
            }

            const downloadUrl = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = downloadUrl;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(downloadUrl);
        } catch (error) {
            console.error('计算SHA-256失败：', error);
            message.error("计算SHA-256失败");
        }
    }

    const columns: ProColumns<API.OrderFilePageVO>[] = [
        {
            title: "文件名",
            dataIndex: "fileName",
            render: (_, record: API.OrderFilePageVO) =>
            {
                return <p>{record.fileName}</p>
            }
        },
        {
            title: "文件大小",
            dataIndex: "fileSize",
            hideInSearch: true,
            render: (_, record: API.OrderFilePageVO) =>
            {
                if (record.fileSize !== undefined)
                {
                    return <p>{formatFileSize(record.fileSize)}</p>
                }
            }
        },
        {
            title: "上传时间",
            dataIndex: "createTime",
            hideInSearch: true,
            render: (_, record: API.OrderFilePageVO) =>
            {
                return <p>{dayjs(record?.createTime).format("YYYY-MM-DD HH:mm:ss")}</p>
            }
        },
        {
            title: "更新时间",
            dataIndex: "updateTime",
            hideInSearch: true,
            render: (_, record: API.OrderFilePageVO) =>
            {
                return <p>{dayjs(record?.updateTime).format("YYYY-MM-DD HH:mm:ss")}</p>
            }
        },
        {
            title: "操作",
            hideInSearch: true,
            render: (_, record: API.OrderFilePageVO) =>
            {
                return <Space size="middle">
                    <Typography.Link onClick={() =>
                    {
                        downloadHandler(async () =>
                        {
                            const downloadFileVO = await fetchUrl(record)
                            await downloadFile(downloadFileVO);
                            return null;
                        }, [], (error) => message.error(`下载失败： ${error}`))
                    }}>
                        下载
                    </Typography.Link>
                    <Typography.Link>
                        删除
                    </Typography.Link>
                </Space>
            }
        }
    ]

    return <ProTable
        columns={columns}
        dataSource={dataSource}
        search={false}
        pagination={false}
    />
}

export default OrderFileList;
