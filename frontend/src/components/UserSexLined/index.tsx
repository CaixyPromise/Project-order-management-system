import React from "react";
import {UserOutlined} from "@ant-design/icons";
import {UserSexEnum, UserSexProps} from "./typing.d";


const UserSexLined: React.FC<UserSexProps> = ({ sex }) =>
{
    if (sex === undefined)
    {
        return null;
    }
    const sexInfo = UserSexEnum[sex];
    if (!sexInfo)
    {
        console.log("无效的 sex 值: ", sex);
        return null;
    }

    const { label, color } = sexInfo;

    return (
        <span aria-label={label} style={{ color }}>
            <UserOutlined style={{ color }}/><a>{label}</a>
        </span>
    );
}

export default UserSexLined;
