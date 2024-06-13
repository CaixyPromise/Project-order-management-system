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

  type BaseResponseOrderInfoAddResponse_ = {
    code?: number;
    data?: OrderInfoAddResponse;
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

  type BaseResponsePageOrderInfoVO_ = {
    code?: number;
    data?: PageOrderInfoVO_;
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

  type getLanguageTypeVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getOrderCategoryVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getOrderInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
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

  type OrderInfoAddResponse = {
    isFinish?: boolean;
    tokenMap?: Record<string, any>;
  };

  type OrderInfoQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: number;
    id?: number;
    notId?: number;
    orTags?: string[];
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type OrderInfoUpdateRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type OrderInfoVO = {
    amount?: number;
    amountPaid?: number;
    createTime?: string;
    creatorName?: string;
    hasOrderAttachment?: boolean;
    id?: number;
    isAssigned?: boolean;
    isPaid?: boolean;
    langName?: string;
    orderCategoryName?: string;
    orderDeadline?: string;
    orderId?: string;
    orderSource?: string;
    orderStatus?: string;
    orderTitle?: string;
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

  type PageOrderInfoVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: OrderInfoVO[];
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

  type uploadFileToLocalUsingPOST1Params = {
    biz?: string;
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
