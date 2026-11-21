-- 记账本项目 · 数据库结构
-- 重建方式：mysql -u root -p account_book < schema.sql
-- 或者在 IDEA 数据库控制台里选中 account_book 后整体执行
--
-- 约定：改了表结构，必须同步更新本文件，否则换机器时对不上

CREATE TABLE IF NOT EXISTS `record` (
    `id`          INT           NOT NULL AUTO_INCREMENT,
    `amount`      DECIMAL(12,2) NOT NULL COMMENT '金额，恒为正数，收支由 type 区分',
    `type`        TINYINT       NOT NULL COMMENT '1=支出 2=收入',
    `category`    VARCHAR(20)   NOT NULL COMMENT '分类：餐饮/交通/工资…',
    `note`        VARCHAR(200)           DEFAULT NULL COMMENT '备注',
    `record_date` DATE          NOT NULL COMMENT '记账日期，不含时间',
    `created_at`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    user_id int not null  COMMENT '所属用户 id',
    PRIMARY KEY (`id`),
    KEY `idx_record_date` (`record_date`),
    KEY `inde_type_data` (`type`, `record_date`),
    KEY `idx_user_date` (`user_id`, `record_date`),
    CONSTRAINT `chk_amount_positive` CHECK (`amount` > 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='记账记录';

CREATE TABLE IF NOT EXISTS `user` (
    `id`         INT         NOT NULL AUTO_INCREMENT,
    `username`   VARCHAR(30) NOT NULL COMMENT '用户名，唯一',
    `password`   VARCHAR(60) NOT NULL COMMENT 'BCrypt 密文，固定 60 字符，禁止存明文',
    `created_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户';
