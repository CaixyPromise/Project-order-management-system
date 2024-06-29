import React, {useEffect, useMemo, useState} from "react";
import {Tag} from "antd";

interface BooleanTagProps
{
    text: boolean | undefined,
}

type ColorPropsType = {
    color?: string,
    text?: string
}

type ColorDictType = {
    [key: number] : ColorPropsType
}


const BooleanTag: React.FC<BooleanTagProps> = ({text}) =>
{
    const [ config, setConfig ] = useState<ColorPropsType>({});
    const colorDict: ColorDictType = useMemo(() =>
    {
        return {
            1: {
                color: "green",
                text: "是"
            },
            0: {
                color: "red",
                text: "否"
            }
        }
    }, [])
    useEffect(() =>
    {
        setConfig(colorDict[text === true ? 1 : 0])
    }, [text])
    return <Tag color={config.color}>{config.text}</Tag>
}

export default BooleanTag;
