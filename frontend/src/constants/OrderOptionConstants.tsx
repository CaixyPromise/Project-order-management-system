import {OptionArray} from "@/typings";
import {PaymentMethodEnum} from "@/enums/PaymentMethodEnum";
import {OrderStatusEnum} from "@/enums/OrderStatusEnum";
import {ContactTypeEnum} from "@/enums/ContactTypeEnum";
import {OrderSourceEnum} from "@/enums/OrderSourceEnum";

export const statusOptions: OptionArray<number> = OrderStatusEnum.getAllOptions()

export const paymentMethodOptions: OptionArray<number> = PaymentMethodEnum.getAllOptions();

export const customerContactTypeOptions: OptionArray<number> = ContactTypeEnum.getAllOptions()

export const orderSourceOptions: OptionArray<number> = OrderSourceEnum.getAllOptions()

const YesNoOptions: OptionArray<number> = [
    { value: 1, label: '是' },
    { value: 0, label: '否' }
]

export const isPaidOptions: OptionArray<number> = YesNoOptions;
export const isAssignedOption: OptionArray<number> = YesNoOptions;
