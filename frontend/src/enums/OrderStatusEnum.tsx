import {OptionArray} from "@/typings";

type statusEnum = "Success" | "Error" | "Default" | "Processing" | "Warning";
type StatusDict = { [key: string]: { text: string, status: statusEnum } }

class OrderStatusEnum
{
    private static allValues: OrderStatusEnum[] = [];
    static readonly PAYING = new OrderStatusEnum(1, "待支付", "Warning");
    static readonly PAYED = new OrderStatusEnum(2, "已支付-正在进行", "Processing");
    static readonly REFUNDED = new OrderStatusEnum(3, "已退款", "Error");
    static readonly CANCELED = new OrderStatusEnum(4, "已取消", "Default");
    static readonly FINISHED = new OrderStatusEnum(5, "已完成", "Success");

    private constructor(private readonly code: number, private readonly text: string, private readonly status: statusEnum)
    {
        OrderStatusEnum.allValues.push(this)
    }

    static getAllStatus() : StatusDict
    {
        const result: StatusDict = {}
        for (const prop of OrderStatusEnum.allValues)
        {
            result[prop.text] = {
                text: prop.text,
                status: prop.status
            }
        }
        console.log(result)
        return result;
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

    static getAllValues(): OrderStatusEnum[]
    {
        return OrderStatusEnum.allValues;
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
