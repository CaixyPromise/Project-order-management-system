import {OptionArray} from "@/typings";

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

    private constructor(private readonly code: number, private readonly text: string)
    {
        ContactTypeEnum.allValues.push(this);
    }

    static getAllOptions(): OptionArray<number>
    {
        return ContactTypeEnum.allValues.map(item =>
        {
            return {
                value: item.code,
                label: item.text
            }
        })
    }


    static getValues(): number[]
    {
        return ContactTypeEnum.allValues.map(type => type.code);
    }

    static getEnumByValue(value: number | undefined): ContactTypeEnum | null
    {
        if (value === null)
        {
            return null
        }
        for (let type of ContactTypeEnum.allValues)
        {
            if (type.code === value)
            {
                return type;
            }
        }
        return null;
    }

    getValue(): number
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
    ContactTypeEnum
}
