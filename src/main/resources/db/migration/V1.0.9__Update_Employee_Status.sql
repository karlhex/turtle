-- 添加新的status字段
ALTER TABLE employees ADD COLUMN status VARCHAR(20);

-- 将现有的is_active数据迁移到新的status字段
UPDATE employees 
SET status = CASE 
    WHEN is_active = true THEN 'ACTIVE'
    ELSE 'RESIGNED'
    END;

-- 设置status字段为非空
ALTER TABLE employees ALTER COLUMN status SET NOT NULL;

-- 删除is_active字段
ALTER TABLE employees DROP COLUMN is_active;
