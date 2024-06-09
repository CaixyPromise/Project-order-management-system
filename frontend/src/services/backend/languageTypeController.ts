// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addLanguageType POST /api/languageType/add */
export async function addLanguageTypeUsingPost1(
  body: API.LanguageTypeAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/languageType/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteLanguageType POST /api/languageType/delete */
export async function deleteLanguageTypeUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/languageType/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getLangTypeVoS GET /api/languageType/get/vo/list */
export async function getLangTypeVoSUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseMapLongLanguageTypeVO_>('/api/languageType/get/vo/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getLanguageTypeVOById GET /api/languageType/get/voById */
export async function getLanguageTypeVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLanguageTypeVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLanguageTypeVO_>('/api/languageType/get/voById', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listLanguageTypeByPage POST /api/languageType/list/page */
export async function listLanguageTypeByPageUsingPost1(
  body: API.LanguageTypeQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageLanguageTypeVO_>('/api/languageType/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateLanguageType POST /api/languageType/update */
export async function updateLanguageTypeUsingPost1(
  body: API.LanguageTypeUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/languageType/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
