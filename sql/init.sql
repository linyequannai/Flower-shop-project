-- ============================================
-- 花卉商城系统 - 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS flower_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE flower_shop;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS user_coupon;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS flower;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    role TINYINT NOT NULL DEFAULT 0 COMMENT '0=用户, 1=管理员',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0=禁用, 1=正常',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 分类表
-- ============================================
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- ============================================
-- 3. 花卉商品表
-- ============================================
CREATE TABLE flower (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_id BIGINT NOT NULL,
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    original_price DECIMAL(10,2) COMMENT '原价',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    description TEXT COMMENT '描述',
    cover_image VARCHAR(255) COMMENT '封面图',
    images VARCHAR(2000) COMMENT '多图, 逗号分隔',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0=下架, 1=上架',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    INDEX idx_category_status (category_id, status),
    INDEX idx_sales (sales DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='花卉商品表';

-- ============================================
-- 4. 购物车表
-- ============================================
CREATE TABLE cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flower_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity >= 1),
    selected TINYINT NOT NULL DEFAULT 1 COMMENT '0=未选中, 1=选中',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (flower_id) REFERENCES flower(id),
    UNIQUE INDEX uk_user_flower (user_id, flower_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================
-- 5. 订单主表
-- ============================================
CREATE TABLE `order` (
    id BIGINT PRIMARY KEY COMMENT '雪花ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL COMMENT '商品总价',
    discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '优惠金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0=待付款, 1=已付款, 2=已发货, 3=已完成, 4=已取消, 5=已退款',
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address VARCHAR(255) NOT NULL,
    remark VARCHAR(500),
    coupon_id BIGINT,
    paid_at DATETIME,
    shipped_at DATETIME,
    completed_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- ============================================
-- 6. 订单明细表
-- ============================================
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    flower_id BIGINT NOT NULL,
    flower_name VARCHAR(100) NOT NULL COMMENT '商品快照',
    flower_image VARCHAR(255) COMMENT '商品图片快照',
    price DECIMAL(10,2) NOT NULL COMMENT '下单时单价',
    quantity INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (flower_id) REFERENCES flower(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ============================================
-- 7. 收藏表
-- ============================================
CREATE TABLE favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flower_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (flower_id) REFERENCES flower(id),
    UNIQUE INDEX uk_user_flower (user_id, flower_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ============================================
-- 8. 评价表
-- ============================================
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flower_id BIGINT NOT NULL,
    order_id BIGINT,
    rating TINYINT NOT NULL CHECK (rating >= 1 AND rating <= 5) COMMENT '评分 1-5',
    content VARCHAR(1000),
    images VARCHAR(2000) COMMENT '评价图片',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (flower_id) REFERENCES flower(id),
    UNIQUE INDEX uk_user_flower_order (user_id, flower_id, order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- ============================================
-- 9. 优惠券表
-- ============================================
CREATE TABLE coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type TINYINT NOT NULL COMMENT '0=满减, 1=折扣',
    value DECIMAL(10,2) NOT NULL COMMENT '满减金额(元) 或 折扣率(0.85=85折)',
    min_amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '最低消费门槛',
    total_count INT NOT NULL COMMENT '发放总量',
    used_count INT NOT NULL DEFAULT 0 COMMENT '已使用量',
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0=禁用, 1=启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status_time (status, start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- ============================================
-- 10. 用户优惠券表
-- ============================================
CREATE TABLE user_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0=未使用, 1=已使用, 2=已过期',
    used_order_id BIGINT,
    used_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id),
    INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- ============================================
-- 种子数据
-- ============================================

-- 管理员 (密码 admin123 的 BCrypt 哈希)
INSERT INTO user (username, password, nickname, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '系统管理员', 1, 1),
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '测试用户', 0, 1);

-- 分类
INSERT INTO category (name, description, sort_order) VALUES
('玫瑰花束', '浪漫玫瑰花束，表白首选', 1),
('百合花束', '清新百合，高雅芬芳', 2),
('康乃馨', '感恩祝福，温馨之选', 3),
('向日葵', '阳光温暖，积极向上', 4),
('混搭花束', '多花种混搭，五彩缤纷', 5),
('鲜花礼盒', '精致礼盒装，送礼体面', 6),
('干花花束', '永不凋零，持久保鲜', 7),
('绿植盆栽', '绿意盎然，净化空气', 8);

-- 花卉商品
INSERT INTO flower (name, category_id, price, original_price, stock, sales, description, cover_image, status) VALUES
('99朵红玫瑰', 1, 299.00, 399.00, 100, 520, '经典99朵红玫瑰花束，象征天长地久的爱情，适合情人节、求婚、纪念日等特殊场合。采用新鲜厄瓜多尔红玫瑰，花径饱满，色泽鲜艳，搭配精致包装。', '/uploads/flowers/rose99.jpg', 1),
('33朵粉玫瑰', 1, 169.00, 219.00, 150, 320, '33朵粉色玫瑰花束，花语"三生三世"，温柔浪漫的粉色玫瑰，适合送给女友、闺蜜或母亲。', '/uploads/flowers/pink-rose33.jpg', 1),
('19朵香槟玫瑰', 1, 139.00, 179.00, 200, 280, '19朵香槟玫瑰花束，优雅的香槟色玫瑰，花语"永恒的爱"，高贵典雅，适合送长辈或商务场合。', '/uploads/flowers/champagne-rose19.jpg', 1),
('白色百合花束', 2, 189.00, 239.00, 80, 156, '清新白色百合花束，6枝多头百合搭配绿叶，花语"纯洁高雅"，适合探病、新居入伙等场合。', '/uploads/flowers/white-lily.jpg', 1),
('粉色百合花束', 2, 199.00, 249.00, 60, 120, '粉色百合花束，温柔的粉色百合散发淡淡清香，花语"甜美幸福"，适合送给女性朋友。', '/uploads/flowers/pink-lily.jpg', 1),
('红色康乃馨花束', 3, 99.00, 129.00, 180, 430, '红色康乃馨花束，20枝康乃馨，花语"爱与感恩"，母亲节首选礼物。', '/uploads/flowers/red-carnation.jpg', 1),
('粉色康乃馨花束', 3, 89.00, 119.00, 200, 380, '粉色康乃馨花束，花语"温馨祝福"，适合送给母亲、长辈表达感恩之情。', '/uploads/flowers/pink-carnation.jpg', 1),
('向日葵花束', 4, 129.00, 159.00, 100, 210, '向日葵花束，6枝向日葵搭配配草，明亮温暖，花语"阳光开朗"，适合送给朋友、同事。', '/uploads/flowers/sunflower.jpg', 1),
('迷你向日葵花束', 4, 79.00, 99.00, 120, 145, '迷你向日葵花束，小巧可爱，适合日常装点家居或办公室。', '/uploads/flowers/mini-sunflower.jpg', 1),
('春日混搭花束', 5, 219.00, 279.00, 70, 190, '春季限定混搭花束，玫瑰+洋桔梗+绣球花，色彩缤纷，犹如走进花园般的浪漫体验。', '/uploads/flowers/spring-mix.jpg', 1),
('秋韵混搭花束', 5, 239.00, 299.00, 50, 125, '秋季主题混搭花束，向日葵+橙色玫瑰+雏菊，温暖色调，秋意浓浓。', '/uploads/flowers/autumn-mix.jpg', 1),
('高级鲜花礼盒', 6, 359.00, 459.00, 40, 95, '精美圆形鲜花礼盒，内含各种精选花材，搭配丝带和贺卡，送礼首选。', '/uploads/flowers/luxury-box.jpg', 1),
('玫瑰礼盒', 6, 259.00, 329.00, 60, 165, '方形玫瑰礼盒，内含12朵精选红玫瑰，精致礼盒装，适合告白和纪念日。', '/uploads/flowers/rose-box.jpg', 1),
('薰衣草干花花束', 7, 69.00, 89.00, 300, 400, '薰衣草干花花束，天然干燥处理，持久保存，散发薰衣草清香，适合家居装饰。', '/uploads/flowers/lavender-dry.jpg', 1),
('满天星干花花束', 7, 59.00, 79.00, 250, 350, '满天星干花花束，经典配花，干花状态可保存1年以上，适合长期装饰。', '/uploads/flowers/baby-breath-dry.jpg', 1),
('棉花干花花束', 7, 79.00, 99.00, 180, 220, '棉花干花花束，北欧ins风格，简约时尚，适合现代家居装饰。', '/uploads/flowers/cotton-dry.jpg', 1),
('绿萝盆栽', 8, 39.00, 49.00, 500, 600, '绿萝盆栽，生命力顽强，净化甲醛好帮手，适合办公室和家居，易打理。', '/uploads/flowers/pothos.jpg', 1),
('多肉组合盆栽', 8, 89.00, 119.00, 150, 260, '多肉植物组合盆栽，多种多肉搭配，可爱造型，适合桌面装饰。', '/uploads/flowers/succulent.jpg', 1),
('龟背竹盆栽', 8, 129.00, 169.00, 60, 180, '龟背竹盆栽，网红绿植，叶片独特美观，ins风必备，适合客厅摆放。', '/uploads/flowers/monstera.jpg', 1),
('蝴蝶兰盆栽', 8, 199.00, 259.00, 40, 110, '蝴蝶兰盆栽，优雅高贵，花色丰富，花期长达3-6个月，送礼佳品。', '/uploads/flowers/phalaenopsis.jpg', 1);

-- 示例优惠券
INSERT INTO coupon (name, type, value, min_amount, total_count, used_count, start_time, end_time, status) VALUES
('新人专享券', 0, 20.00, 99.00, 500, 12, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1),
('满199减30', 0, 30.00, 199.00, 300, 45, '2026-06-01 00:00:00', '2026-07-31 23:59:59', 1),
('全场9折券', 1, 0.90, 0.00, 200, 30, '2026-06-01 00:00:00', '2026-08-31 23:59:59', 1),
('满299减50', 0, 50.00, 299.00, 100, 18, '2026-06-01 00:00:00', '2026-09-30 23:59:59', 1),
('夏季特惠85折', 1, 0.85, 100.00, 150, 22, '2026-06-15 00:00:00', '2026-08-15 23:59:59', 1);
