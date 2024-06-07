import React from "react";
import {SearchValueResult} from "@/components/SearchUserInput/index";
import type {SelectProps} from "antd";

export interface SearchInputProps
{
    mode?: "multiple" | "tags";
    style?: React.CSSProperties; // Use React.CSSProperties for style objects
    placeholder?: string;
    fetchOptions: (text: string) => Promise<SearchValueResult[]>;
    value: SearchValueResult[];
    setValue: React.Dispatch<React.SetStateAction<SearchValueResult[]>>;
    maxCount: number;
}

export interface DebounceSelectProps<ValueType = any>
    extends Omit<SelectProps<ValueType | ValueType[]>, 'options' | 'children'>
{
    fetchOptions: (search: string) => Promise<ValueType[]>;
    debounceTimeout?: number;
}

export interface SearchValueResult
{
    label: string;
    value: string;
}
