class ContactTypeEnum
{
    private static allValues: ContactTypeEnum[] = [];

    static readonly XIANYU = new ContactTypeEnum(0, "闲鱼");
    static readonly EMAIL = new ContactTypeEnum(1, "邮箱");
    static readonly WECHAT = new ContactTypeEnum(2, "微信");
    static readonly QQ = new ContactTypeEnum(3, "QQ");
    static readonly DINGTALK = new ContactTypeEnum(4, "钉钉");
    static readonly WORK_WEIXIN = new ContactTypeEnum(5, "企业微信");
    static readonly PHONE = new ContactTypeEnum(6, "手机");

    private constructor(private readonly value: number, private readonly text: string)
    {
        ContactTypeEnum.allValues.push(this);
    }

    static getValues(): number[]
    {
        return ContactTypeEnum.allValues.map(type => type.value);
    }

    static getEnumByValue(value: number): ContactTypeEnum | null
    {
        for (let type of ContactTypeEnum.allValues)
        {
            if (type.value === value)
            {
                return type;
            }
        }
        return null;
    }

    getValue(): number
    {
        return this.value;
    }

    getText(): string
    {
        return this.text;
    }

    toString(): string
    {
        return `${this.text} (${this.value})`;
    }
}

export {
    ContactTypeEnum
}
