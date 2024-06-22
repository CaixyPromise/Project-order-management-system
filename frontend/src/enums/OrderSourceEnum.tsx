class OrderSourceEnum
{
    private static allValues: OrderSourceEnum[] = [];

    static readonly XIAN_YU = new OrderSourceEnum(1, "闲鱼");
    static readonly WX_GROUP = new OrderSourceEnum(2, "微信群");
    static readonly WX_FRIEND = new OrderSourceEnum(3, "微信好友");
    static readonly QQ_GROUP = new OrderSourceEnum(4, "QQ群");
    static readonly QQ_FRIEND = new OrderSourceEnum(5, "QQ好友");

    private constructor(private readonly code: number, private readonly text: string)
    {
        OrderSourceEnum.allValues.push(this);
    }

    static getByCode(value: number | undefined): OrderSourceEnum | null
    {
        if (value === null)
        {
            return null
        }
        for (let source of OrderSourceEnum.allValues)
        {
            if (source.code === value)
            {
                return source;
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
    OrderSourceEnum
}
