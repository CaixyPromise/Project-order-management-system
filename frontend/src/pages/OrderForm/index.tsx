import React, {useEffect, useRef, useState} from "react";
import {
    PageContainer,
    ProForm,
    ProFormDatePicker,
    ProFormDependency,
    ProFormDigit,
    ProFormText
} from "@ant-design/pro-components";
import {useParams} from "@@/exports";
import {useForm} from "antd/lib/form/Form";
import {Card, message, Space} from "antd";
import {ProFormSelect} from "@ant-design/pro-form/lib";
import BraftEditor, {EditorState} from 'braft-editor';
import 'braft-editor/dist/index.css';
import UploadBox, {UploadBoxHandle} from "@/pages/OrderForm/components/UploadBox";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import EditableTags from "@/pages/OrderForm/components/EditableTags";
import useStyles from "@/pages/OrderForm/style.style";
import {OptionArray} from "@/typings";
import {history} from "@umijs/max";
import {
    customerContactTypeOptions,
    isAssignedOption,
    isPaidOptions,
    orderSourceOptions,
    paymentMethodOptions,
    statusOptions
} from "@/constants/OrderOptionConstants";
import {queryOrderVO} from "@/pages/OrderList/server";
import JsonUtils from "@/utils/JsonUtils";
import {OrderFormServer, OrderFormType} from "@/pages/OrderForm/typings";
import {fetchCategory, fetchLangType} from "@/pages/OrderForm/utils";
import {postOrderInfo, uploadAttachment} from "@/pages/OrderForm/server";


const Index: React.FC = () =>
{
    const { styles } = useStyles();
    // @ts-ignore
    const { id }: { id: string | undefined } = useParams<{ id?: string }>();
    const isUpdate: boolean = id !== undefined;
    const [ formRef ] = useForm();
    const fileUploadRef = useRef<UploadBoxHandle>(null);

    const [ categoryHandle, isCategoryLoading ] = useAsyncHandler<OrderFormServer.CategoryResponse>();
    const [ langTypeHandle, isLangTypeLoading ] = useAsyncHandler<OrderFormServer.LangTypeResponse>();
    const [ categoryOption, setCategoryOption ] = useState<OptionArray<string>>([]);
    const [ langTypeOption, setLangTypeOption ] = useState<OptionArray<string>>([]);
    const [ orderTag, setOrderTag ] = useState<string[]>([ "1", "2" ]);
    const [ fileInfo, setFileInfo ] = useState<OrderFormType.FileInfo[]>([]);
    const [ submitHandler ] = useAsyncHandler<API.OrderInfoUploadResponse>()
    const [ fetchOrderInfoHandler, fetchOrderLoading ] = useAsyncHandler<API.OrderInfoVO>()

    const fetchOption = async () =>
    {
        const categoryOption = await categoryHandle(fetchCategory, [], () => message.error("获取分类信息失败"))
        const langTypeOption = await langTypeHandle(fetchLangType, [], () => message.error("获取语言类型失败"))
        if (categoryOption && langTypeOption)
        {
            console.log(categoryOption, langTypeOption)
            setCategoryOption(categoryOption);
            setLangTypeOption(langTypeOption);
        }
        else
        {
            message.error("获取分类或语言类型信息失败")
        }
    }

    const fetchOrderInfo = async () =>
    {
        const response = await fetchOrderInfoHandler(queryOrderVO, [ id ], () => message.error("获取订单信息失败，返回订单列表"))
        if (response)
        {
            console.log("response is: ", response)
            formRef.setFieldsValue({
                ...response,
                orderDesc: BraftEditor.createEditorState(response.orderDesc),
                orderRemark: BraftEditor.createEditorState(response.orderRemark),
                orderTag: JsonUtils.fromJson(response?.orderTags)
            })
        }
        else {
            history.push("/orderList")
        }
    }


    useEffect(() =>
    {
        fetchOption();
        if (isUpdate)
        {
            fetchOrderInfo()
        }
    }, [ id ]);

    const submitOrder = async (values: any) =>
    {
        console.log(values)
        // 将 BraftEditor 的内容转换为 HTML 字符串
        const orderDescHtml = values.orderDesc ? values.orderDesc?.toHTML() : '';
        const orderRemarkHtml = values.orderRemark ? values.orderRemark?.toHTML() : '';
        // 构造提交的数据
        const dataToSubmit: API.OrderInfoAddRequest | API.OrderInfoUpdateRequest = {
            ...values,
            orderTag,
            attachmentList: fileInfo, // 包含文件 UID
            orderDesc: orderDescHtml, // 使用转换后的 HTML 字符串
            orderRemark: orderRemarkHtml, // 使用转换后的 HTML 字符串
            id: id
        };

        const response = await submitHandler(postOrderInfo,
            [ dataToSubmit, isUpdate ],
            (error) => message.error(`提交异常: ${error}`)
        )
        // 如果提交成功，且需要上传附件
        if (!response?.isFinish && response?.tokenMap)
        {
            await fileUploadRef?.current?.uploadFiles(response.tokenMap, uploadAttachment);
        }
        else if (response !== null)
        {
            message.success("提交成功")
            history.push("/orderList");
        }
    }

    const handleFileUidChange = (uids: OrderFormType.FileInfo[]) =>
    {
        // 将 uid 存储到 state 中
        setFileInfo(uids);
    };

    const initialValue = {
        "orderTitle": "123",
        "orderSource": 1,
        "orderId": "123",
        "orderCategoryId": "1798994019288621057",
        "orderLangId": "1798993735380377602",
        "amount": 2,
        "amountPaid": 3,
        "isAssigned": 1,
        "isPaid": 1,
        "orderStatus": 5,
        "customerContactType": 3,
        "customerContact": "2",
        "customerEmail": "1944630344@qq.com",
        "orderStartDate": "2024-06-09",
        "orderDeadline": "2024-06-28",
        "orderCompletionTime": "2024-06-09",
        "orderCommissionRate": 60,
        "orderAssignToWxId": "4",
        "orderRemark": "<p>123</p>",
        "orderDesc": "<p>123</p>",
        "paymentMethod": 1
    }


    return <>
        <PageContainer title={(id === undefined ? "添加" : "更新") + "订单信息"}
                       loading={isCategoryLoading && isLangTypeLoading && fetchOrderLoading}>
            <ProForm
                form={formRef}
                style={{
                    gap: "10px"
                }}
                onFinish={submitOrder}
                initialValues={{
                    ...initialValue,
                    "orderRemark": BraftEditor.createEditorState(initialValue.orderRemark),
                    "orderDesc": BraftEditor.createEditorState(initialValue.orderDesc),
                }}
            >
                <Card bordered={false} className={styles.card}>
                    <ProFormText name="orderTitle" label="订单名称描述"
                                 placeholder="请输入订单名称描述"
                                 tooltip="可以在这里写描述这个订单的名称-例如：视觉检测开发"
                                 rules={[ { required: true } ]}
                    />
                    <Space.Compact title={"订单信息"}>
                        <ProFormSelect
                            name="orderSource"
                            label="顾客来源"
                            initialValue={1}
                            options={orderSourceOptions}
                        />
                        <ProFormText width={"lg"} name="orderId" label="订单号" placeholder="请输入订单号"
                                     rules={[ { required: true } ]}/>

                    </Space.Compact>

                    <ProForm.Item
                        label="订单标签"
                        name="orderTags"
                    >
                        <EditableTags setTags={setOrderTag} tags={orderTag}/>
                    </ProForm.Item>

                    <ProForm.Group title="基础信息">
                        <ProFormSelect width="md"
                                       name="orderCategoryId"
                                       label="订单分类"
                                       options={categoryOption}
                                       rules={[ { required: true } ]}
                        />
                        <ProFormSelect width="md"
                                       name="orderLangId"
                                       label="订单语言类型"
                                       options={langTypeOption}
                                       rules={[ { required: true } ]}

                        />
                        <ProFormDigit width="md" name="amount" label="订单金额" placeholder="请输入订单金额" min={0}
                                      rules={[ { required: true } ]}/>
                        <ProFormDigit width="md" name="amountPaid" label="已支付金额" placeholder="请输入已支付金额"
                                      min={0} rules={[ { required: true } ]}/>
                    </ProForm.Group>

                    <ProForm.Group title="订单状态与管理">
                        <ProFormSelect
                            width="md"
                            name="isAssigned"
                            label="是否对外分配"
                            options={isAssignedOption}
                            rules={[ { required: true } ]}
                        />
                        <ProFormSelect
                            width="md"
                            name="isPaid"
                            label="已支付全款"
                            initialValue={0}
                            options={isPaidOptions}
                            rules={[ { required: true } ]}
                        />

                        <ProFormSelect
                            width="md"
                            name="orderStatus"
                            label="订单状态"
                            required
                            initialValue={1}
                            options={statusOptions}
                            rules={[ { required: true } ]}
                        />

                        <ProFormSelect
                            width="md"
                            name="paymentMethod"
                            label="支付方式"
                            required
                            initialValue={1}
                            options={paymentMethodOptions}
                        />
                    </ProForm.Group>

                    <ProForm.Group title="客户信息">
                        <ProFormSelect
                            width="md"
                            name="customerContactType"
                            label="顾客联系方式类型"
                            options={customerContactTypeOptions}
                            rules={[ { required: true } ]}
                        />
                        <ProFormText width="md" name="customerContact" label="顾客联系方式"
                                     placeholder="请输入顾客联系方式" rules={[ { required: true } ]}/>
                        <ProFormText width="md" name="customerEmail" label="顾客邮箱" placeholder="请输入顾客邮箱"/>
                    </ProForm.Group>

                    <ProFormDependency name={[ 'isAssigned' ]}>
                        {({ isAssigned }) =>
                        {
                            // 当选择“是”（value为1）时，显示额外的表单项
                            if (isAssigned === 1)
                            {
                                return (
                                    <>
                                        <ProForm.Group title="订单分配信息">
                                            <ProFormSelect
                                                width="md"
                                                name="orderCommissionRate"
                                                label="订单佣金比例"
                                                options={Array.from({ length: 9 }, (_, i) => ({
                                                    value: (i + 1) * 10,
                                                    label: `${(i + 1) * 10}%`
                                                }))}
                                                placeholder="请选择佣金比例"
                                                rules={[ { required: true } ]}
                                            />
                                            <ProFormText
                                                width="md"
                                                name="orderAssignToWxId"
                                                label="订单分配人微信Id"
                                                placeholder="请输入订单分配人微信Id"
                                                rules={[ { required: true } ]}
                                            />
                                        </ProForm.Group>
                                    </>
                                );
                            }
                            return null;
                        }}
                    </ProFormDependency>


                    <ProForm.Group title="订单时间">
                        <ProFormDatePicker width="md" name="orderStartDate" label="订单开始日期"
                                           rules={[ { required: true } ]}/>
                        <ProFormDatePicker width="md" name="orderDeadline" label="交付截止日期"
                                           rules={[ { required: true } ]}/>
                        <ProFormDependency name={[ "orderStatus" ]}>
                            {({ orderStatus }) =>
                            {
                                return orderStatus === 5 &&
                                    <ProFormDatePicker width="md" name="orderCompletionTime" label="订单完成时间"/>
                            }}
                        </ProFormDependency>
                    </ProForm.Group>

                    <ProForm.Item label="订单附件">
                        <UploadBox onFileUidChange={handleFileUidChange} ref={fileUploadRef}/>
                    </ProForm.Item>

                    <ProForm.Item
                        required
                        name="orderDesc"
                        label="订单描述"
                        getValueFromEvent={(editorState: EditorState) => editorState}
                        // initialValue={BraftEditor.createEditorState(null)}
                    >
                        <BraftEditor
                            className={styles.editor}
                            // value={orderDesc}
                            // onChange={(newState: any) => setOrderDesc((newState))}
                        />
                    </ProForm.Item>

                    <ProForm.Item
                        required
                        name="orderRemark"
                        label="订单备注"
                        getValueFromEvent={(editorState: EditorState) => editorState}
                        // initialValue={BraftEditor.createEditorState(null)}
                    >
                        <BraftEditor
                            className={styles.editor}
                            // value={orderRemark}
                            // onChange={(newState: any) => setOrderRemark((newState))}
                        />
                    </ProForm.Item>
                </Card>
            </ProForm>
        </PageContainer>
    </>
}

export default Index;
