declare namespace API {
  type AboutMeVO = {
    userAccount?: string;
    userAvatar?: string;
    userEmail?: string;
    userGender?: number;
    userName?: string;
    userPhone?: string;
  };

  type AddUserVO = {
    id?: number;
    userAccount?: string;
    userName?: string;
    userPassword?: string;
  };

  type BaseResponseAboutMeVO_ = {
    code?: number;
    data?: AboutMeVO;
    message?: string;
  };

  type BaseResponseAddUserVO_ = {
    code?: number;
    data?: AddUserVO;
    message?: string;
  };

  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseEsPageOrderInfoPageVO_ = {
    code?: number;
    data?: EsPageOrderInfoPageVO_;
    message?: string;
  };

  type BaseResponseInt_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseLanguageTypeVO_ = {
    code?: number;
    data?: LanguageTypeVO;
    message?: string;
  };

  type BaseResponseListEventVOOrderInfoVO_ = {
    code?: number;
    data?: EventVOOrderInfoVO_[];
    message?: string;
  };

  type BaseResponseListOptionVOLong_ = {
    code?: number;
    data?: OptionVOLong_[];
    message?: string;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseMapLongLanguageTypeVO_ = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseMapLongOrderCategoryVO_ = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseOrderCategoryVO_ = {
    code?: number;
    data?: OrderCategoryVO;
    message?: string;
  };

  type BaseResponseOrderInfoUploadResponse_ = {
    code?: number;
    data?: OrderInfoUploadResponse;
    message?: string;
  };

  type BaseResponseOrderInfoVO_ = {
    code?: number;
    data?: OrderInfoVO;
    message?: string;
  };

  type BaseResponsePageLanguageTypeVO_ = {
    code?: number;
    data?: PageLanguageTypeVO_;
    message?: string;
  };

  type BaseResponsePageOrderCategoryVO_ = {
    code?: number;
    data?: PageOrderCategoryVO_;
    message?: string;
  };

  type BaseResponsePageOrderInfoPageVO_ = {
    code?: number;
    data?: PageOrderInfoPageVO_;
    message?: string;
  };

  type BaseResponsePagePost_ = {
    code?: number;
    data?: PagePost_;
    message?: string;
  };

  type BaseResponsePagePostVO_ = {
    code?: number;
    data?: PagePostVO_;
    message?: string;
  };

  type BaseResponsePageUser_ = {
    code?: number;
    data?: PageUser_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponsePostVO_ = {
    code?: number;
    data?: PostVO;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUser_ = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserVO_ = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type checkUsingGET1Params = {
    /** echostr */
    echostr?: string;
    /** nonce */
    nonce?: string;
    /** signature */
    signature?: string;
    /** timestamp */
    timestamp?: string;
  };

  type DeleteRequest = {
    id?: number;
  };

  type EsPageOrderInfoPageVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: OrderInfoPageVO[];
    searchAfter?: Record<string, any>[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type EventVOOrderInfoVO_ = {
    content?: OrderInfoVO;
    date?: string;
    id?: number;
    level?: string;
  };

  type getLanguageTypeVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getOrderCategoryVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getOrderEventListUsingGET1Params = {
    /** startMonth */
    startMonth?: number;
    /** startYear */
    startYear?: number;
  };

  type getOrderInfoVOByIdUsingGET1Params = {
    /** id */
    id: number;
  };

  type getPostVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getUserByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getUserVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type LanguageTypeAddRequest = {
    languageName?: string;
  };

  type LanguageTypeQueryRequest = {
    content?: string;
    current?: number;
    id?: number;
    notId?: number;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type LanguageTypeUpdateRequest = {
    id?: number;
    languageName?: string;
  };

  type LanguageTypeVO = {
    createTime?: string;
    creatorName?: string;
    id?: number;
    languageName?: string;
    updateTime?: string;
    vos?: Record<string, any>;
  };

  type LoginUserVO = {
    createTime?: string;
    id?: number;
    updateTime?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type MapLongLanguageTypeVO_ = true;

  type MapLongOrderCategoryVO_ = true;

  type OptionVOLong_ = {
    label?: string;
    value?: number;
  };

  type OrderCategoryAddRequest = {
    categoryDesc?: string;
    categoryName?: string;
  };

  type OrderCategoryQueryRequest = {
    content?: string;
    current?: number;
    id?: number;
    notId?: number;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type OrderCategoryUpdateRequest = {
    categoryDesc?: string;
    categoryName?: string;
    id?: number;
  };

  type OrderCategoryVO = {
    categoryDesc?: string;
    categoryName?: string;
    createTime?: string;
    creatorName?: string;
    id?: number;
    updateTime?: string;
    vos?: Record<string, any>;
  };

  type OrderFilePageVO = {
    createTime?: string;
    fileName?: string;
    fileSize?: number;
    id?: number;
    orderId?: number;
    updateTime?: string;
  };

  type OrderFileVO = {
    createTime?: string;
    fileName?: string;
    fileSha256?: string;
    fileSize?: number;
    id?: string;
    orderId?: number;
    updateTime?: string;
    userId?: number;
  };

  type OrderInfoAddRequest = {
    amount?: number;
    amountPaid?: number;
    attachmentList?: UploadFileInfoDTO[];
    customerContact?: string;
    customerContactType?: number;
    customerEmail?: string;
    isAssigned?: number;
    isPaid?: number;
    orderAssignToWxId?: string;
    orderCategoryId?: number;
    orderCommissionRate?: number;
    orderCompletionTime?: string;
    orderDeadline?: string;
    orderDesc?: string;
    orderEndDate?: string;
    orderId?: string;
    orderLangId?: number;
    orderRemark?: string;
    orderSource?: number;
    orderStartDate?: string;
    orderStatus?: number;
    orderTag?: string[];
    orderTitle?: string;
    paymentMethod?: number;
  };

  type OrderInfoPageVO = {
    amount?: number;
    amountPaid?: number;
    createTime?: string;
    creatorName?: string;
    customerContact?: string;
    customerEmail?: string;
    hasOrderAttachment?: boolean;
    id?: number;
    isAssigned?: boolean;
    isPaid?: boolean;
    langName?: string;
    orderAssignToWxId?: string;
    orderAttachmentList?: OrderFilePageVO[];
    orderCategoryName?: string;
    orderDeadline?: string;
    orderId?: string;
    orderSource?: string;
    orderStatus?: string;
    orderTitle?: string;
    updateTime?: string;
  };

  type OrderInfoQueryRequest = {
    amount?: number;
    creatorName?: string;
    current?: number;
    customerContact?: string;
    customerEmail?: string;
    id?: number;
    isAssigned?: number;
    isPaid?: number;
    langName?: number;
    orderAssignToWxId?: string;
    orderCategoryName?: number;
    orderId?: string;
    orderSource?: string;
    orderStatus?: number;
    orderTitle?: string;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    sortField?: string;
    sortOrder?: string;
  };

  type OrderInfoUpdateRequest = {
    amount?: number;
    amountPaid?: number;
    attachmentList?: UploadFileInfoDTO[];
    customerContact?: string;
    customerContactType?: number;
    customerEmail?: string;
    id?: number;
    isAssigned?: number;
    isPaid?: number;
    orderAssignToWxId?: string;
    orderCategoryId?: number;
    orderCommissionRate?: number;
    orderCompletionTime?: string;
    orderDeadline?: string;
    orderDesc?: string;
    orderEndDate?: string;
    orderId?: string;
    orderLangId?: number;
    orderRemark?: string;
    orderSource?: number;
    orderStartDate?: string;
    orderStatus?: number;
    orderTag?: string[];
    orderTitle?: string;
    paymentMethod?: number;
  };

  type OrderInfoUploadResponse = {
    isFinish?: boolean;
    tokenMap?: Record<string, any>;
  };

  type OrderInfoVO = {
    amount?: number;
    amountPaid?: number;
    categoryName?: string;
    createTime?: string;
    creatorName?: string;
    customerContact?: string;
    customerContactType?: number;
    customerEmail?: string;
    id?: number;
    isAssigned?: number;
    isPaid?: number;
    isValid?: number;
    langName?: string;
    orderAssignToWxId?: string;
    orderAttachmentList?: OrderFileVO[];
    orderAttachmentNum?: number;
    orderCommissionRate?: number;
    orderCompletionTime?: string;
    orderDeadline?: string;
    orderDesc?: string;
    orderEndDate?: string;
    orderId?: string;
    orderRemark?: string;
    orderSource?: number;
    orderStartDate?: string;
    orderStatus?: number;
    orderTags?: string;
    orderTitle?: string;
    paymentMethod?: number;
    paymentMethodText?: string;
    updateTime?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageLanguageTypeVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: LanguageTypeVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageOrderCategoryVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: OrderCategoryVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageOrderInfoPageVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: OrderInfoPageVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PagePost_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Post[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PagePostVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: PostVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUser_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: User[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type Post = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    id?: number;
    isDelete?: number;
    tags?: string;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userId?: number;
  };

  type PostAddRequest = {
    content?: string;
    tags?: string[];
    title?: string;
  };

  type PostEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type PostFavourAddRequest = {
    postId?: number;
  };

  type PostFavourQueryRequest = {
    current?: number;
    pageSize?: number;
    postQueryRequest?: PostQueryRequest;
    searchAfter?: Record<string, any>[];
    sortField?: string;
    sortOrder?: string;
    userId?: number;
  };

  type PostQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: number;
    id?: number;
    notId?: number;
    orTags?: string[];
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type PostThumbAddRequest = {
    postId?: number;
  };

  type PostUpdateRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type PostVO = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    hasFavour?: boolean;
    hasThumb?: boolean;
    id?: number;
    tagList?: string[];
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    user?: UserVO;
    userId?: number;
  };

  type UploadFileInfoDTO = {
    fileName?: string;
    fileSha256?: string;
    fileUid?: string;
    token?: string;
  };

  type uploadFileUsingPOST1Params = {
    biz?: string;
    token?: string;
  };

  type User = {
    createTime?: string;
    id?: number;
    isDelete?: number;
    mpOpenId?: string;
    unionId?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userEmail?: string;
    userGender?: number;
    userName?: string;
    userPassword?: string;
    userPhone?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserAddRequest = {
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type userLoginByWxOpenUsingGET1Params = {
    /** code */
    code: string;
  };

  type UserLoginRequest = {
    captcha?: string;
    captchaId?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserModifyPasswordRequest = {
    confirmPassword?: string;
    newPassword?: string;
    oldPassword?: string;
  };

  type UserQueryRequest = {
    current?: number;
    id?: number;
    mpOpenId?: string;
    pageSize?: number;
    searchAfter?: Record<string, any>[];
    sortField?: string;
    sortOrder?: string;
    unionId?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userEmail?: string;
    userName?: string;
    userPassword?: string;
    userPhone?: string;
  };

  type UserUpdateMyRequest = {
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
  };

  type UserUpdateRequest = {
    id?: number;
    userAvatar?: string;
    userGender?: number;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserVO = {
    createTime?: string;
    id?: number;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };
}
