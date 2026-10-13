-- 用户表（会员表）
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(128) NOT NULL,
  `phone` varchar(20),
  `email` varchar(100),
  `status` tinyint DEFAULT 1 COMMENT '0禁用，1启用',
  `role` varchar(20) DEFAULT 'ROLE_MEMBER' COMMENT '角色',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(128) NOT NULL,
  `name` varchar(50),
  `phone` varchar(20),
  `status` tinyint DEFAULT 1,
  `role` varchar(20) DEFAULT 'ROLE_ADMIN',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID（0表示一级）',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态（1启用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 收藏夹表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`)
);

-- 图书表
CREATE TABLE IF NOT EXISTS `book` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `book_no` varchar(30) NOT NULL UNIQUE COMMENT '图书编号（业务唯一）',
  `isbn` varchar(20) COMMENT 'ISBN',
  `name` varchar(200) NOT NULL COMMENT '书名',
  `author` varchar(100) COMMENT '作者',
  `publisher` varchar(100) COMMENT '出版社',
  `publish_date` date COMMENT '出版日期',
  `category_id` bigint COMMENT '所属分类（二级）',
  `cover_url` varchar(255) COMMENT '封面图URL',
  `description` varchar(500) COMMENT '简介（短）',
  `detail` text COMMENT '详情（长文本）',
  `price` decimal(10,2) COMMENT '定价',
  `sale_price` decimal(10,2) COMMENT '销售价（会员价）',
  `cost_price` decimal(10,2) COMMENT '进价',
  `stock` int DEFAULT 0 COMMENT '库存数量',
  `stock_warning` int DEFAULT 5 COMMENT '预警阈值（默认5）',
  `status` tinyint DEFAULT 1 COMMENT '上架状态（0下架，1上架）',
  `recommend_status` tinyint DEFAULT 0 COMMENT '推荐状态（0否，1是）',
  `sales_count` int DEFAULT 0 COMMENT '销量（可冗余）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 收藏夹表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`)
);

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`)
);

-- 地址表
CREATE TABLE IF NOT EXISTS `address` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `receiver_name` varchar(50) COMMENT '收货人',
  `phone` varchar(20) COMMENT '手机号',
  `region` varchar(100) COMMENT '省市区',
  `detail_address` varchar(200) COMMENT '详细地址',
  `is_default` tinyint DEFAULT 0 COMMENT '是否默认',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart_item` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `quantity` int DEFAULT 1 COMMENT '数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `order_no` varchar(32) NOT NULL UNIQUE COMMENT '订单编号（唯一）',
  `user_id` bigint NOT NULL COMMENT '会员ID',
  `address_id` bigint COMMENT '收货地址ID',
  `total_amount` decimal(10,2) COMMENT '商品总金额',
  `discount_amount` decimal(10,2) DEFAULT 0 COMMENT '优惠金额（默认0）',
  `pay_amount` decimal(10,2) COMMENT '应付金额',
  `order_status` varchar(20) DEFAULT '待支付' COMMENT '待支付/待发货/已发货/已签收/已取消',
  `pay_status` varchar(20) DEFAULT '未支付' COMMENT '未支付/已支付',
  `pay_time` datetime COMMENT '支付时间',
  `logistics_status` varchar(20) DEFAULT '未发货' COMMENT '物流状态：未发货/已出库/配送中/已送达/已签收',
  `ship_time` datetime COMMENT '发货时间',
  `receipt_time` datetime COMMENT '签收时间',
  `cancel_time` datetime COMMENT '取消时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 收藏夹表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`)
);

-- 订单明细表
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `book_name` varchar(200) COMMENT '下单时书名（冗余）',
  `price` decimal(10,2) COMMENT '下单时单价',
  `quantity` int DEFAULT 1 COMMENT '数量',
  `subtotal` decimal(10,2) COMMENT '小计',
  `reviewed` tinyint DEFAULT 0 COMMENT '是否已评价'
);

-- 评价表
CREATE TABLE IF NOT EXISTS `review` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '会员ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `order_item_id` bigint NOT NULL COMMENT '订单明细ID（确保已购买）',
  `rating` tinyint DEFAULT 5 COMMENT '评分1~5',
  `content` varchar(500) COMMENT '评价内容',
  `status` tinyint DEFAULT 1 COMMENT '正常/删除（管理员删除）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间'
);

-- 留言表
CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '会员ID',
  `content` varchar(500) COMMENT '留言内容',
  `reply` varchar(500) COMMENT '管理员回复',
  `reply_time` datetime COMMENT '回复时间',
  `status` tinyint DEFAULT 1 COMMENT '显示/隐藏',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间'
);

-- 公告表
CREATE TABLE IF NOT EXISTS `news` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` text,
  `status` tinyint DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 收藏夹表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`)
);

-- 进货记录表
CREATE TABLE IF NOT EXISTS `purchase` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `quantity` int DEFAULT 0 COMMENT '进货数量',
  `purchase_price` decimal(10,2) COMMENT '进货单价',
  `supplier` varchar(100) COMMENT '供应商（可选）',
  `operator_id` varchar(100) COMMENT '经办人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '进货时间'
);

-- 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `type` varchar(20) DEFAULT '满减' COMMENT '满减/固定减',
  `threshold` decimal(10,2) DEFAULT 0 COMMENT '使用门槛',
  `discount` decimal(10,2) DEFAULT 0 COMMENT '优惠金额',
  `expire_start` datetime COMMENT '有效期开始',
  `expire_end` datetime COMMENT '有效期结束',
  `stock` int DEFAULT 0 COMMENT '发放数量',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `admin_id` bigint COMMENT '管理员ID',
  `operation` varchar(100) COMMENT '操作类型（如：新增图书、调整库存）',
  `target` varchar(200) COMMENT '操作对象（如：book_id=123）',
  `detail` varchar(500) COMMENT '操作详情（JSON）',
  `result` varchar(50) COMMENT '成功/失败',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
);

-- 系统配置表
CREATE TABLE IF NOT EXISTS `system_config` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `config_key` varchar(100) NOT NULL UNIQUE COMMENT '配置键',
  `config_value` varchar(500) NOT NULL COMMENT '配置值',
  `description` varchar(200) COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 收藏夹表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`)
);