import type {ReactElement} from "react";
import {RuleType, StoreValue} from "rc-field-form/lib/interface";

interface BaseRule
{
    warningOnly?: boolean;
    enum?: StoreValue[];
    len?: number;
    max?: number;
    message?: string | ReactElement;
    min?: number;
    pattern?: RegExp;
    required?: boolean;
    transform?: (value: StoreValue) => StoreValue;
    type?: RuleType;
    whitespace?: boolean;
    validateTrigger?: string | string[];
}

export const EMAIL_REGEX: BaseRule = {
    pattern: /[\w!#$%&'*+\/=?^_`{|}~-]+(?:\.[\w!#$%&'*+\/=?^_`{|}~-]+)*@(?:\w(?:[\w-]*\w)?\.)+\w(?:[\w-]*\w)?/,
    message: "请输入有效的邮箱",
}

export const ACCOUNT_REGEX: BaseRule = {
    pattern: /^[a-zA-Z][a-zA-Z0-9]{4,15}$/,
    message: "用户登录账号必须以字母开头，长度在5到16字节之间，且只允许包含字母和数字（不允许下划线"
}

export const NAME_REGEX: BaseRule = {
    pattern: /^[\u4e00-\u9fa5a-zA-Z]{1,30}$/,
    message: "姓名必须为中文或英文，长度在1到30之间"
}

export const PASSWORD_REGEX: BaseRule = {
    pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/,
    message: "密码必须包含大小写字母和数字，长度在8到20之间"
}

export const PHONE_REGEX: BaseRule = {
    pattern: /^1[3-9]\d{9}$/,
    message: "请输入有效的手机号",
}
