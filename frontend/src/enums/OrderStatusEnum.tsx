class OrderStatusEnum
{
    private static allValues: OrderStatusEnum[] = [];
    static readonly PAYING = new OrderStatusEnum(1, "待支付");
    static readonly PAYED = new OrderStatusEnum(2, "已支付-正在进行");
    static readonly REFUNDED = new OrderStatusEnum(3, "已退款");
    static readonly CANCELED = new OrderStatusEnum(4, "已取消");
    static readonly FINISHED = new OrderStatusEnum(5, "已完成");
    static readonly PART_SETTLED = new OrderStatusEnum(6, "部分结算");

    private constructor(private readonly code: number, private readonly desc: string)
    {
        OrderStatusEnum.allValues.push(this)
    }

    static getByCode(code: number): OrderStatusEnum | null
    {
        for (let prop of OrderStatusEnum.allValues)
        {
            if (prop.code === code)
            {
                return prop;
            }
        }
        return null;
    }

    toString(): string
    {
        return `${this.desc} (${this.code})`;
    }
}

export {
    OrderStatusEnum
}
