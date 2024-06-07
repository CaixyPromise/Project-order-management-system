import React, {useMemo, useRef, useState} from 'react';
import {Select, Spin} from 'antd';
import debounce from 'lodash/debounce';
import {DebounceSelectProps, SearchInputProps, SearchValueResult} from "@/components/SearchUserInput/typing";



function DebounceSelect<
    ValueType extends { key?: string; label: React.ReactNode; value: string | number } = any,
>({ fetchOptions, debounceTimeout = 800, ...props }: DebounceSelectProps<ValueType>)
{
    const [ fetching, setFetching ] = useState(false);
    const [ options, setOptions ] = useState<ValueType[]>([]);
    const fetchRef = useRef(0);

    const debounceFetcher = useMemo(() =>
    {
        const loadOptions = (value: string) =>
        {
            fetchRef.current += 1;
            const fetchId = fetchRef.current;
            setOptions([]);
            setFetching(true);

            fetchOptions(value).then((newOptions) =>
            {
                if (fetchId !== fetchRef.current)
                {
                    // for fetch callback order
                    return;
                }

                setOptions(newOptions);
                setFetching(false);
            });
        };

        return debounce(loadOptions, debounceTimeout);
    }, [ fetchOptions, debounceTimeout ]);

    return (
        <Select
            labelInValue
            filterOption={false}
            onSearch={debounceFetcher}
            notFoundContent={fetching ? <Spin size="small"/> : null}
            {...props}
            options={options}
        />
    );
}

export const SearchUserInput: React.FC<SearchInputProps> = ({
    value,
    setValue,
    placeholder,
    fetchOptions,
    style,
    mode,
    maxCount
}) =>
{
    return (
        <DebounceSelect
            mode={mode ?? "multiple"}
            value={value}
            placeholder={placeholder ?? "请输入需要查找的信息"}
            fetchOptions={fetchOptions}
            onChange={(newValue) =>
            {
                setValue(newValue as SearchValueResult[]);
            }}
            style={style ?? { width: '100%' }}
            maxCount={maxCount}
        />
    );
};
