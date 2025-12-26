-- 为员工入职申请表添加转换跟踪字段
-- 用于跟踪申请是否已经转换为正式员工

-- 添加转换跟踪字段
ALTER TABLE employee_applications 
ADD COLUMN converted_to_employee BOOLEAN DEFAULT FALSE NOT NULL;

ALTER TABLE employee_applications 
ADD COLUMN converted_employee_id BIGINT;

ALTER TABLE employee_applications 
ADD COLUMN converted_at TIMESTAMP;

-- 添加外键约束，关联到employees表
ALTER TABLE employee_applications 
ADD CONSTRAINT fk_employee_applications_converted_employee_id 
FOREIGN KEY (converted_employee_id) REFERENCES employees(id);

-- 添加索引以提高查询性能
CREATE INDEX idx_employee_applications_converted_to_employee 
ON employee_applications(converted_to_employee);

CREATE INDEX idx_employee_applications_converted_employee_id 
ON employee_applications(converted_employee_id);

-- 添加字段注释
COMMENT ON COLUMN employee_applications.converted_to_employee IS '是否已转换为员工';
COMMENT ON COLUMN employee_applications.converted_employee_id IS '转换后的员工ID';
COMMENT ON COLUMN employee_applications.converted_at IS '转换时间';