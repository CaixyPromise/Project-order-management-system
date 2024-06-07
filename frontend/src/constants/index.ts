const isDev = process.env.NODE_ENV === 'development';

/**
 * 本地后端地址
 */
export const BACKEND_HOST_LOCAL = "http://localhost:7529";

/**
 * 线上后端地址
 */
export const BACKEND_HOST_PROD = "https://www.baidu.com";


export const baseURL = isDev ? BACKEND_HOST_LOCAL : BACKEND_HOST_PROD

export const API_URL = baseURL + "/api";

export const STATIC_URL = API_URL + "/static";
