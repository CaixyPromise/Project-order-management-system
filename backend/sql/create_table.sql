# 数据库初始化

-- 创建库
create database if not exists order_sys;

-- 切换库
use order_sys;

-- 用户表
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(20)                            not null comment '账号',
    userPassword varchar(60)                            not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    userPhone    varchar(20)                            null comment '用户手机号(后期允许拓展区号和国际号码）',
    userEmail    varchar(254)                           null comment '用户邮箱',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(30)                            null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint user_pk
        unique (userAccount),
    constraint user_pk_2
        unique (userEmail)
)
    comment '用户' collate = utf8mb4_unicode_ci;

create index idx_unionId
    on user (unionId);

create index user_userAccount_index
    on user (userAccount);

create index user_userEmail_index
    on user (userEmail);


-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';


create table order_info
(
    id                  bigint auto_increment comment 'id' primary key,
    creatorId           bigint                             not null comment '创建用户id',
    orderId             varchar(256)                       not null comment '订单号',
    amount              decimal(10, 2)                     not null comment '订单金额',
    customerContactType    bigint                        not null comment '订单联系方式类型',
    customerContact        varchar(256)                       not null comment '订单联系方式',
    customerEmail       varchar(256)                       not null comment '顾客邮箱',
    isAssigned          tinyint(11)                        not null comment '是否是对外分配',
    isPaid              tinyint  default 0                 not null comment '是否支付',
    paymentMethod       varchar(256)                       not null comment '支付方式',
    orderSource         tinyint(11)                        not null comment '订单来源',
    orderAssignToWxId   varchar(256) comment '订单分配人微信Id',
    orderCommissionRate int comment '订单佣金比例',
    orderCategoryId     bigint                             not null comment '订单分类',
    orderLangId         bigint                             not null comment '订单语言',

    orderDesc           TEXT                               not null comment '订单描述',
    orderAttachment     varchar(256) comment '订单附件',
    orderRemark         TEXT comment '订单备注',
    orderDeadline       datetime                           not null comment '交付截止日期',
    orderCompletionTime datetime comment '订单完成时间',
    orderStartDate      datetime default CURRENT_TIMESTAMP not null comment '订单开始日期',
    orderEndDate        datetime                           not null comment '订单结束日期',
    orderStatus         tinyint(11)                        not null comment '订单状态',
    createTime          datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete            tinyint  default 0                 not null comment '是否删除',
    index (creatorId),
    index (orderId),
    index (orderStatus),
    index (orderAssignToWxId),
    index (orderDeadline),
    index (customerEmail),
    index (customerContact)
) comment '订单信息';


create table language_type
(
    id            bigint auto_increment comment 'id' primary key,
    languageName  varchar(32)           not null comment '编程语言名称',
    creatorId     bigint                 not null comment '创建用户id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index (languageName)
) comment '编程语言信息';


create table order_category
(
    id            bigint auto_increment comment 'id' primary key,
    categoryName  varchar(256)           not null comment '订单分类名称',
    creatorId     bigint                 not null comment '创建用户id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index (categoryName)
) comment '订单分类信息';


create table contact_type
(

    id            bigint auto_increment comment 'id' primary key,
    contactTypeName  varchar(256)           not null comment '联系方式类型名称',
    creatorId     bigint                 not null comment '创建用户id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index (contactTypeName)
) comment '联系方式类型信息';