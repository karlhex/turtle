-- 为sequence表添加序列号长度字段并添加EMPLOYEE序列配置

-- 1. 添加序列号长度字段
ALTER TABLE sequence 
ADD COLUMN seq_length INTEGER NOT NULL DEFAULT 6;

-- 2. 为seq_length字段添加注释
COMMENT ON COLUMN sequence.seq_length IS '序列号长度';

-- 3. 为现有记录设置默认长度（如果有的话）
UPDATE sequence SET seq_length = 6 WHERE seq_length IS NULL;

-- 4. 添加EMPLOYEE流水号配置
INSERT INTO sequence (
    type, 
    prefix, 
    seq_length, 
    include_year, 
    include_month, 
    include_day, 
    current_value,
    created_at,
    updated_at
) VALUES (
    'EMPLOYEE',          -- 类型：员工
    'EMP',               -- 前缀：EMP
    10,                  -- 序列长度：10位
    false,               -- 不包含年
    false,               -- 不包含月
    false,               -- 不包含日
    0,                   -- 当前值：从0开始
    NOW(),               -- 创建时间
    NOW()                -- 更新时间
);

-- 5. 添加索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_sequence_type ON sequence(type);