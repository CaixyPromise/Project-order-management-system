

// 定义性别的属性接口
export interface UserSexProps {
    sex: number; // 直接使用数字类型
}

// 定义性别枚举
export enum UserSexEnumType {
    UNKNOWN = 0,
    MALE = 1,
    FEMALE = 2,
}

// 定义性别详细信息的接口
interface UserSexDetails {
    label: string;
    value: number;
    color: string;
}

// 定义性别的枚举映射
export const UserSexEnum: Record<number, UserSexDetails> = {
    [UserSexEnumType.UNKNOWN]: {
        label: '未知',
        value: 0,
        color: '#999',
    },
    [UserSexEnumType.MALE]: {
        label: '男',
        value: 1,
        color: 'blue',
    },
    [UserSexEnumType.FEMALE]: {
        label: '女',
        value: 2,
        color: 'pink',
    },
};
