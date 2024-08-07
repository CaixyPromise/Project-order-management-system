// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addOrderInfo POST /api/order/add */
export async function addOrderInfoUsingPost1(
  body: API.OrderInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseOrderInfoUploadResponse_>('/api/order/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteOrderInfo POST /api/order/delete */
export async function deleteOrderInfoUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/order/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getOrderFileDownloadUrlById GET /api/order/get/downloadUrl */
export async function getOrderFileDownloadUrlByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderFileDownloadUrlByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseDownloadFileVO_>('/api/order/get/downloadUrl', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getOrderInfoVOById GET /api/order/get/vo */
export async function getOrderInfoVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderInfoVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseOrderInfoVO_>('/api/order/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getOrderEventList GET /api/order/getEvent */
export async function getOrderEventListUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderEventListUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListEventVOOrderInfoVO_>('/api/order/getEvent', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listOrderInfoVOByPage POST /api/order/list/page/vo */
export async function listOrderInfoVoByPageUsingPost1(
  body: API.OrderInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseEsPageOrderInfoPageVO_>('/api/order/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** searchOrderInfoVOByPage POST /api/order/search/page/vo */
export async function searchOrderInfoVoByPageUsingPost1(
  body: API.OrderInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageOrderInfoPageVO_>('/api/order/search/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateOrderInfo POST /api/order/update */
export async function updateOrderInfoUsingPost1(
  body: API.OrderInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseOrderInfoUploadResponse_>('/api/order/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
