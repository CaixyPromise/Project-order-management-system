import {message} from "antd";

// 还没有实现功能的预留函数接口
export const UNIMPLEMENTED = async (name: string = "该功能") =>
{

    message.info(`${name}-功能正在开发中，敬请期待:)`)
}
