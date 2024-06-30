import {OptionArray} from "@/typings";

class PaymentMethodEnum
{
    private static allValues: PaymentMethodEnum[] = [];

    static readonly ALIPAY = new PaymentMethodEnum(1, "支付宝支付");
    static readonly WECHATPAY = new PaymentMethodEnum(2, "微信支付");
    static readonly UNIONPAY = new PaymentMethodEnum(3, "银联支付");
    static readonly CASHPAY = new PaymentMethodEnum(4, "现金支付");
    static readonly SWIPEPAY = new PaymentMethodEnum(5, "刷卡支付");

    private constructor(private readonly code: number, private readonly text: string)
    {
        PaymentMethodEnum.allValues.push(this);
    }

    static getAllOptions(): OptionArray<number>
    {
        return PaymentMethodEnum.allValues.map(item => ({
            value: item.code,
            label: item.text
        }));
    }

    static getByCode(value: number | undefined): PaymentMethodEnum | null
    {
        if (value === null)
        {
            return null
        }
        for (let method of PaymentMethodEnum.allValues)
        {
            if (method.code === value)
            {
                return method;
            }
        }
        return null;
    }

    getCode(): number
    {
        return this.code;
    }

    getText(): string
    {
        return this.text;
    }

    toString(): string
    {
        return `${this.text} (${this.code})`;
    }
}

export {
    PaymentMethodEnum
}
