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
import UploadBox from "@/pages/AddOrder/components/UploadBox";
import useAsyncHandler from "@/hooks/useAsyncHandler";
import {getLangTypeVoSUsingGet1} from "@/services/backend/languageTypeController";
import {getOrderCategoryVosUsingGet1} from "@/services/backend/orderCategoryController";
import EditableTags from "@/pages/AddOrder/components/EditableTags";
import useStyles from "@/pages/AddOrder/style.style";

interface OptionProps
{
    value: string;
    label: string;
}

const Index: React.FC = () =>
{
    const { styles } = useStyles();
    const { id }: { id: string | undefined } = useParams();
    const [ formRef ] = useForm();
    type CategoryResponse = { [key: string]: API.OrderCategoryVO }
    type LangTypeResponse = { [key: string]: API.LanguageTypeVO }


    const [ orderDesc, setOrderDesc ] = useState<EditorState>(BraftEditor.createEditorState(null));
    const [ orderRemark, setOrderRemark ] = useState<EditorState>(BraftEditor.createEditorState(null));
    const [ categoryHandle, isCategoryLoading ] = useAsyncHandler<CategoryResponse>();
    const [ langTypeHandle, isLangTypeLoading ] = useAsyncHandler<LangTypeResponse>();
    const [ categoryOption, setCategoryOption ] = useState<OptionProps[]>([]);
    const [ langTypeOption, setLangTypeOption ] = useState<OptionProps[]>([]);
    const [ orderTag, setOrderTag ] = useState<string[]>([]);


    const fetchCategory = async () =>
    {
        const { data, code } = await getOrderCategoryVosUsingGet1()
        if (code === 0)
        {
            return data as CategoryResponse
        }
        return {} as CategoryResponse
    }

    const fetchLangType = async () =>
    {
        const { data, code } = await getLangTypeVoSUsingGet1()
        if (code === 0)
        {
            return data as LangTypeResponse
        }
        return {} as CategoryResponse
    }

    const fetchOption = async () =>
    {
        const categoryOption = await categoryHandle(fetchCategory, () => message.error("获取分类信息失败"))
        const langTypeOption = await langTypeHandle(fetchLangType, () => message.error("获取语言类型失败"))
        if (categoryOption && langTypeOption)
        {
            const tmpCategoryOption: OptionProps[] = [];
            Object.entries(categoryOption).forEach(([ key, value ]) =>
            {
                tmpCategoryOption.push({ value: key, label: value.categoryName })
            })
            const tmpLangTypeOption: OptionProps[] = [];
            Object.entries(langTypeOption).forEach(([ key, value ]) =>
            {
                tmpLangTypeOption.push({ value: key, label: value.languageName })
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
        console.log(values)
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

                    <ProForm.Item label="订单标签">
                        <EditableTags setTags={setOrderTag} tags={orderTag}/>
                    </ProForm.Item>

                    <ProForm.Group title="基础信息">
                        <ProFormSelect width="md"
                                       name="orderCategory"
                                       label="订单分类"
                                       options={categoryOption}
                                       rules={[ { required: true } ]}
                        />
                        <ProFormSelect width="md"
                                       name="orderLangType"
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
                            name="orderStatus"
                            label="支付方式"
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
                        <ProFormDependency name={["orderStatus"]}>
                            {({ orderStatus}) => {
                                return orderStatus === 5 && <ProFormDatePicker width="md" name="orderCompletionTime" label="订单完成时间" />
                            }}
                        </ProFormDependency>
                    </ProForm.Group>

                    <ProForm.Item label="订单附件">
                        <UploadBox/>
                    </ProForm.Item>

                    <Card bordered={false} title={"订单备注"} className={styles.card}>
                        <BraftEditor
                            className={styles.editor}
                            value={orderRemark}
                            onChange={(newState: any) => setOrderRemark((newState))}
                        />
                    </Card>

                    <Card bordered={false} title={"订单描述"} className={styles.card}>
                        <BraftEditor
                            className={styles.editor}
                            value={orderDesc}
                            onChange={(newState: any) => setOrderDesc((newState))}
                        />
                    </Card>
                </Card>
            </ProForm>
        </PageContainer>
    </>
}

export default Index;
