-- 修改EMPLOYEE序列长度从10位改为5位

UPDATE sequence 
SET seq_length = 5 
WHERE type = 'EMPLOYEE';

-- 添加注释说明修改
COMMENT ON TABLE sequence IS '流水号配置表 - EMPLOYEE序列长度已修改为5位';