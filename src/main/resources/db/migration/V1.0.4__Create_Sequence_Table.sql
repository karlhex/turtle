CREATE TABLE sequence (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL COMMENT '流水号类型',
    prefix VARCHAR(10) NOT NULL COMMENT '前缀',
    include_year BOOLEAN DEFAULT FALSE COMMENT '是否包含年',
    include_month BOOLEAN DEFAULT FALSE COMMENT '是否包含月',
    include_day BOOLEAN DEFAULT FALSE COMMENT '是否包含日',
    current_value BIGINT NOT NULL DEFAULT 0 COMMENT '当前序号',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '版本号，用于乐观锁',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    UNIQUE KEY uk_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流水号表';

-- 插入员工编号配置
INSERT INTO sequence (
    type,
    prefix,
    include_year,
    include_month,
    include_day,
    created_by
) VALUES (
    'EMPLOYEE_NUMBER',
    'EMP',
    TRUE,
    FALSE,
    FALSE,
    'system'
);
