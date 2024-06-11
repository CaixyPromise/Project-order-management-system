import React, {useEffect, useState} from "react";
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
import UploadBox from "@/pages/OrderForm/components/UploadBox";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import EditableTags from "@/pages/OrderForm/components/EditableTags";
import useStyles from "@/pages/OrderForm/style.style";
import {fetchCategory, fetchLangType, postOrderInfo} from "@/pages/OrderForm/server";
import {OptionProps} from "@/typings";


const Index: React.FC = () =>
{
    const { styles } = useStyles();
    const { id }: { id: string | undefined } = useParams<{ id?: string }>();
    const [ formRef ] = useForm();
    type CategoryResponse = { [key: string]: API.OrderCategoryVO }
    type LangTypeResponse = { [key: string]: API.LanguageTypeVO }


    const [ categoryHandle, isCategoryLoading ] = useAsyncHandler<CategoryResponse>();
    const [ langTypeHandle, isLangTypeLoading ] = useAsyncHandler<LangTypeResponse>();
    const [ categoryOption, setCategoryOption ] = useState<OptionProps[]>([]);
    const [ langTypeOption, setLangTypeOption ] = useState<OptionProps[]>([]);
    const [ orderTag, setOrderTag ] = useState<string[]>([ "1", "2" ]);
    const [ fileInfo, setFileInfo ] = useState<OrderFormType.FileInfo[]>([]);
    const [ submitHandler, isPending ] = useAsyncHandler<string>()

    const fetchOption = async () =>
    {
        const categoryOption = await categoryHandle(fetchCategory, () => message.error("获取分类信息失败"))
        const langTypeOption = await langTypeHandle(fetchLangType, () => message.error("获取语言类型失败"))
        if (categoryOption && langTypeOption)
        {
            const tmpCategoryOption: OptionProps[] = [];
            Object.entries(categoryOption).forEach(([ key, value ]) =>
            {
                tmpCategoryOption.push({ value: key, label: value.categoryName } as OptionProps)
            })
            const tmpLangTypeOption: OptionProps[] = [];
            Object.entries(langTypeOption).forEach(([ key, value ]) =>
            {
                tmpLangTypeOption.push({ value: key, label: value.languageName } as OptionProps)
            })
            console.log(tmpCategoryOption);
            setCategoryOption(tmpCategoryOption);
            setLangTypeOption(tmpLangTypeOption);
        }
        else
        {
            message.error("获取分类或语言类型信息失败")
        }
    }

    useEffect(() =>
    {
        fetchOption();
    }, []);

    const submitOrder = async (values: any) =>
    {
        // 将 BraftEditor 的内容转换为 HTML 字符串
        const orderDescHtml = values.orderDesc ? values.orderDesc.toHTML() : '';
        const orderRemarkHtml = values.orderRemark ? values.orderRemark.toHTML() : '';

        // 构造提交的数据
        const dataToSubmit: API.OrderInfoAddRequest = {
            ...values,
            orderTag,
            attachmentList: fileInfo, // 包含文件 UID
            orderDesc: orderDescHtml, // 使用转换后的 HTML 字符串
            orderRemark: orderRemarkHtml // 使用转换后的 HTML 字符串
        };
        const response = await submitHandler(() => postOrderInfo(dataToSubmit),
            (error) => message.error(`提交异常: ${error}`)
        )
        if (response)
        {
            // todo: 拿到token，上传文件
            message.success("提交成功");
        }
        else
        {
            message.error("提交失败");
        }
    }

    const handleFileUidChange = (uids: string[]) =>
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
        "orderDeadline": "2024-06-18",
        "orderCompletionTime": "2024-06-09",
        "orderCommissionRate": 60,
        "orderAssignToWxId": "4",
        "orderRemark": "<p>123</p>",
        "orderDesc": "<p>123</p>",
        "paymentMethod": 1
    }


    return <>
        <PageContainer title={(id === undefined ? "添加" : "更新") + "订单信息"}
                       loading={isCategoryLoading && isLangTypeLoading}>
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
                            options={[
                                { value: 1, label: '闲鱼' },
                                { value: 2, label: '微信群' },
                                { value: 3, label: '微信好友' },
                                { value: 4, label: 'QQ群' },
                                { value: 5, label: 'QQ好友' },
                            ]}
                        />
                        <ProFormText width={"lg"} name="orderId" label="订单号" placeholder="请输入订单号"
                                     rules={[ { required: true } ]}/>

                    </Space.Compact>

                    <ProForm.Item
                        label="订单标签"
                        name="orderTags"
                        getValueFromEvent={(value) => console.log("orderTags", value)}

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
                            options={[
                                { value: 1, label: '是' },
                                { value: 0, label: '否' }
                            ]}
                            rules={[ { required: true } ]}
                        />
                        <ProFormSelect
                            width="md"
                            name="isPaid"
                            label="已支付全款"
                            initialValue={0}
                            options={[
                                { value: 1, label: '是' },
                                { value: 0, label: '否' }
                            ]}
                            rules={[ { required: true } ]}
                        />

                        <ProFormSelect
                            width="md"
                            name="orderStatus"
                            label="订单状态"
                            required
                            initialValue={1}
                            options={[
                                { value: 1, label: '待分配' },
                                { value: 2, label: '已支付-正在进行' },
                                { value: 3, label: '已退款' },
                                { value: 4, label: '已取消' },
                                { value: 5, label: '已完成' },
                                { value: 6, label: '部分结算' }
                            ]}
                        />

                        <ProFormSelect
                            width="md"
                            name="paymentMethod"
                            label="支付方式"
                            required
                            initialValue={1}
                            options={[
                                { value: 1, label: '支付宝支付' },
                                { value: 2, label: '微信支付' },
                                { value: 3, label: '银联支付' },
                                { value: 4, label: '现金支付' },
                                { value: 5, label: '刷卡支付' },
                            ]}
                        />
                    </ProForm.Group>

                    <ProForm.Group title="客户信息">
                        <ProFormSelect
                            width="md"
                            name="customerContactType"
                            label="顾客联系方式类型"
                            options={[
                                { value: 0, label: '闲鱼' },
                                { value: 1, label: '邮箱' },
                                { value: 2, label: '微信' },
                                { value: 3, label: 'QQ' },
                                { value: 4, label: '钉钉' },
                                { value: 5, label: '企业微信' },
                                { value: 6, label: '手机' }
                            ]}
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
                        <UploadBox onFileUidChange={handleFileUidChange}/>
                    </ProForm.Item>

                    <ProForm.Item
                        required
                        name="orderDesc"
                        label="订单描述"
                        getValueFromEvent={(value: EditorState) => value?.toHTML()}
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
                        getValueFromEvent={(value: EditorState) => value?.toHTML()}
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
