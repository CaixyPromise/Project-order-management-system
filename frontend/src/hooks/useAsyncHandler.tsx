import { useCallback, useEffect, useRef, useState } from 'react';

export type AsyncHandlerFunc<T> = (...args: any[]) => Promise<T>;
export type AsyncHandlerErrorFunc = (error: any) => void;
export type AsyncHandlerVoidFunc = () => void;

type useAsyncHandlerCallbackProps<T> = (
    action: AsyncHandlerFunc<T>,
    actionParams: any[],
    onError?: AsyncHandlerErrorFunc,
    onCleanup?: AsyncHandlerVoidFunc
) => Promise<T | null>;

/**
 * 自定义 React 钩子，用于执行异步操作并管理其执行状态及错误处理。钩子还允许指定一个在组件卸载时执行的清理函数。
 * Custom React hook for performing asynchronous operations with state and error management.
 * This hook also allows specifying a cleanup function to be executed when the component unmounts.
 *
 * @template T 指定异步操作返回的数据类型。
 *           Specifies the type of data returned by the asynchronous operation.
 *
 * @returns 返回一个数组，其中包含：
 *          Returns an array containing:
 *  - run: 一个函数，用于执行异步操作。这个函数接受三个参数：
 *         A function for performing the asynchronous operation. This function accepts three parameters:
 *      - action: 一个返回 Promise<T> 的函数，表示要执行的异步操作。
 *                A function returning a Promise<T>, representing the asynchronous operation to be performed.
 *      - onError: 可选参数，一个处理错误的回调函数，接受一个 Error 类型的参数。
 *                 Optional parameter, a callback function for handling errors, accepting an Error type parameter.
 *      - onCleanup: 可选参数，一个在组件卸载时执行的清理函数。
 *                   Optional parameter, a cleanup function to be executed when the component unmounts.
 *  - isPending: 布尔值，表示异步操作是否正在进行中。
 *               Boolean indicating whether the asynchronous operation is in progress.
 *  - error: Error 实例，如果异步操作执行中发生错误，则此变量会被设置；否则为 null。
 *           Error instance, set if an error occurs during the asynchronous operation; otherwise null.
 *
 * 使用示例 (Usage Example):
 * ```
 * const [fetchData, isLoading, fetchError] = useAsyncHandler<number>();
 * const onError = (err) => console.error(err);
 * const onCleanup = () => console.log('Cleanup called');
 * fetchData(() => asyncFetchSomeData(), [param1, param2], onError, onCleanup);
 * ```
 *
 * 注意 (Note): 确保传递给 run 函数的 action、onError 和 onCleanup 函数参数均为函数类型，否则会抛出错误。
 * Ensure that the action, onError, and onCleanup function parameters passed to the run function are all of function type, otherwise an error will be thrown.
 */
const useAsyncHandler = <T extends any>(): [
    (action: AsyncHandlerFunc<T>, actionParams: any[], onError?: AsyncHandlerErrorFunc, onCleanup?: AsyncHandlerVoidFunc) => Promise<T | null>,
    boolean,
        T | null,
] => {
    const [isPending, setIsPending] = useState<boolean>(false);
    const [data, setData] = useState<T | null>(null);
    const cleanupRef = useRef<(() => void) | null>(null);

    const run: useAsyncHandlerCallbackProps<T> = useCallback(async (
        action,
        actionParams,
        onError = (error: any) => console.error(error),
        onCleanup
    ) => {
        if (typeof action !== 'function' ||
            (onError !== undefined && typeof onError !== 'function') ||
            (onCleanup !== undefined && typeof onCleanup !== 'function')) {
            throw new Error("action, onError, and onCleanup must be functions when provided.");
        }

        cleanupRef.current = onCleanup || null;

        try {
            setIsPending(true);
            const response: Awaited<T> = await action(...actionParams);
            setIsPending(false);
            setData(response);
            return response;
        } catch (err: any) {
            setIsPending(false);
            onError?.call(null, err);
            return null;
        }
    }, []);

    useEffect(() => {
        return () => {
            if (cleanupRef.current) {
                cleanupRef.current?.call(null);
            }
        };
    }, []);

    return [run, isPending, data];
};

export default useAsyncHandler;
