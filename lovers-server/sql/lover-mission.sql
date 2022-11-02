create schema if not exists lovers_mission collate utf8mb4_0900_ai_ci;

create table if not exists eat_menu
(
    id bigint auto_increment
        primary key,
    text varchar(255) null,
    constraint eat_menu_text_uindex
        unique (text)
);

create table if not exists love_sentence
(
    id bigint auto_increment
        primary key,
    relation_id bigint null,
    love_info text null,
    create_user varchar(255) null,
    create_time datetime null,
    like_count int null
);

create table if not exists mission_clock
(
    id bigint auto_increment
        primary key,
    mission_id bigint not null,
    mission_day int null comment '任务天数',
    clock_day int null comment '打卡天数',
    mission_user varchar(255) null,
    create_time datetime null,
    update_time datetime null,
    end_Time datetime null comment '任务截止时间'
)
    comment '长期任务打卡';

create index mission_clock_mission_id_index
    on mission_clock (mission_id);

create table if not exists mission_info
(
    id bigint auto_increment
        primary key,
    mission_id bigint not null,
    mission_name varchar(255) not null comment '任务名称',
    mission_desc varchar(500) null comment '任务描述',
    mission_classify varchar(255) null comment '分类',
    mission_status tinyint default 0 null comment '任务状态：0：未完成，1：完成，-1：作废',
    mission_credit int null comment '完成后积分',
    create_user_openId varchar(255) not null,
    complete_user_openId varchar(255) null,
    create_time datetime null,
    update_time datetime null,
    complete_time datetime null,
    mission_type tinyint default 0 null comment '任务类型：0：短期，1：长期'
);

create index mission_info_mission_id_index
    on mission_info (mission_id);

create table if not exists product_info
(
    id bigint auto_increment
        primary key,
    product_id bigint null,
    product_classify varchar(255) null comment '商品分类',
    product_name varchar(255) null,
    product_desc varchar(255) null,
    product_status tinyint default 1 null comment '1：上架 -1：下架',
    product_credit int null comment '所需积分',
    create_user_openId varchar(255) null,
    create_time datetime null,
    update_time datetime null
);

create index product_info_product_id_create_user_openId_index
    on product_info (product_id, create_user_openId);

create table if not exists user_relation
(
    id bigint auto_increment
        primary key,
    user_openId varchar(255) null,
    user_cp_openId varchar(255) null,
    status tinyint default 0 null comment '0:未同意， 1:正常，-1：解绑',
    bind_time datetime null comment '在一起时间',
    create_time datetime null,
    update_time datetime null,
    constraint user_relation_openId_uindex
        unique (user_openId, user_cp_openId)
);

create table if not exists user_storage
(
    id bigint auto_increment
        primary key,
    storage_id bigint null,
    product_id bigint null,
    storage_user varchar(255) null,
    storage_status tinyint default 1 null comment '1：待使用，-1：已使用',
    create_time datetime null,
    update_time datetime null,
    storage_count int default 0 null comment '库存数量'
);

create index user_storage_storage_user_index
    on user_storage (storage_user);

create table if not exists wx_user
(
    id bigint auto_increment
        primary key,
    wx_openId varchar(500) null,
    wx_avatarUrl varchar(500) null,
    wx_userName varchar(500) null,
    wx_userNickName varchar(255) null,
    wx_userSex tinyint null,
    wx_userCredit int null comment '用户积分',
    wx_UserUid varchar(255) null,
    last_login_time datetime null comment '最后登录时间',
    country varchar(100) null,
    province varchar(100) null,
    city varchar(100) null,
    district varchar(100) null,
    isp varchar(100) null,
    ip varchar(100) null,
    constraint wx_UserUid_uindex
        unique (wx_UserUid)
);

create index wx_user_wx_openId_index
    on wx_user (wx_openId);

