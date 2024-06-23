import {OptionArray} from "@/typings";

class OrderStatusEnum
{
    private static allValues: OrderStatusEnum[] = [];
    static readonly PAYING = new OrderStatusEnum(1, "待支付");
    static readonly PAYED = new OrderStatusEnum(2, "已支付-正在进行");
    static readonly REFUNDED = new OrderStatusEnum(3, "已退款");
    static readonly CANCELED = new OrderStatusEnum(4, "已取消");
    static readonly FINISHED = new OrderStatusEnum(5, "已完成");

    private constructor(private readonly code: number, private readonly text: string)
    {
        OrderStatusEnum.allValues.push(this)
    }

    static getAllOptions(): OptionArray<number>
    {
        return OrderStatusEnum.allValues.map(item =>
        {
            return {
                value: item.code,
                label: item.text
            }
        })
    }


    static getEnumByValue(code: number | undefined): OrderStatusEnum | null
    {
        if (code === undefined)
        {
            return null;
        }
        for (let prop of OrderStatusEnum.allValues)
        {
            if (prop.code === code)
            {
                return prop;
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
    OrderStatusEnum
}
