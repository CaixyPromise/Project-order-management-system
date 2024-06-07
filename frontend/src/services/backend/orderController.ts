// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addOrderInfo POST /api/order/add */
export async function addOrderInfoUsingPost1(
  body: API.OrderInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/order/add', {
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

/** editOrderInfo POST /api/order/edit */
export async function editOrderInfoUsingPost1(
  body: API.OrderInfoEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/order/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
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

/** listOrderInfoByPage POST /api/order/list/page */
export async function listOrderInfoByPageUsingPost1(
  body: API.OrderInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageOrderInfo_>('/api/order/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listOrderInfoVOByPage POST /api/order/list/page/vo */
export async function listOrderInfoVoByPageUsingPost1(
  body: API.OrderInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageOrderInfoVO_>('/api/order/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyOrderInfoVOByPage POST /api/order/my/list/page/vo */
export async function listMyOrderInfoVoByPageUsingPost1(
  body: API.OrderInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageOrderInfoVO_>('/api/order/my/list/page/vo', {
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
  return request<API.BaseResponsePageOrderInfoVO_>('/api/order/search/page/vo', {
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
  return request<API.BaseResponseBoolean_>('/api/order/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
