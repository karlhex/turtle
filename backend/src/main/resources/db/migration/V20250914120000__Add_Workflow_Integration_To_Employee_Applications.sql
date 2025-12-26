-- 为员工入职申请表添加工作流集成支持
-- 添加workflow_instance_id字段来关联Flowable工作流实例

ALTER TABLE employee_applications 
ADD COLUMN workflow_instance_id VARCHAR(255);

-- 为workflow_instance_id字段添加索引以提高查询性能
CREATE INDEX idx_employee_applications_workflow_instance_id 
ON employee_applications(workflow_instance_id);

-- 为现有的申请记录添加注释（可选）
COMMENT ON COLUMN employee_applications.workflow_instance_id IS '工作流实例ID - 关联Flowable流程实例';