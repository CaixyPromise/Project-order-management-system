// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addOrderCategory POST /api/orderCategory/add */
export async function addOrderCategoryUsingPost1(
  body: API.OrderCategoryAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/orderCategory/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteOrderCategory POST /api/orderCategory/delete */
export async function deleteOrderCategoryUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/orderCategory/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getOrderCategoryVOS GET /api/orderCategory/get/vo/list */
export async function getOrderCategoryVosUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseMapLongOrderCategoryVO_>('/api/orderCategory/get/vo/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getOrderCategoryVOById GET /api/orderCategory/get/voById */
export async function getOrderCategoryVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderCategoryVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseOrderCategoryVO_>('/api/orderCategory/get/voById', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getCategoryOptionList GET /api/orderCategory/list/option */
export async function getCategoryOptionListUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListOptionVOLong_>('/api/orderCategory/list/option', {
    method: 'GET',
    ...(options || {}),
  });
}

/** listOrderCategoryByPage POST /api/orderCategory/list/page */
export async function listOrderCategoryByPageUsingPost1(
  body: API.OrderCategoryQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageOrderCategoryVO_>('/api/orderCategory/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateOrderCategory POST /api/orderCategory/update */
export async function updateOrderCategoryUsingPost1(
  body: API.OrderCategoryUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/orderCategory/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
