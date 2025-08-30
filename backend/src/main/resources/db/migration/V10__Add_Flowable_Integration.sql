-- Flowable工作流集成数据库迁移
-- 添加与Flowable流程实例的关联字段

-- 1. 为approval_requests表添加Flowable集成字段
ALTER TABLE approval_requests 
ADD COLUMN flowable_process_instance_id VARCHAR(64),
ADD COLUMN flowable_process_definition_key VARCHAR(255);

-- 2. 创建索引以提高查询性能
CREATE INDEX idx_approval_requests_flowable_process 
ON approval_requests(flowable_process_instance_id);

CREATE INDEX idx_approval_requests_flowable_definition 
ON approval_requests(flowable_process_definition_key);

-- 3. 添加注释
COMMENT ON COLUMN approval_requests.flowable_process_instance_id 
IS 'Flowable流程实例ID，用于关联Flowable工作流';

COMMENT ON COLUMN approval_requests.flowable_process_definition_key 
IS 'Flowable流程定义键，标识使用的流程模板';

-- 4. Flowable会自动创建以下表（无需手动创建）:
-- - ACT_RE_* : Repository 流程定义和部署相关表
-- - ACT_RU_* : Runtime 流程实例运行时相关表  
-- - ACT_HI_* : History 历史数据相关表
-- - ACT_GE_* : General 通用数据表
-- - ACT_ID_* : Identity 用户和组相关表

-- 注意：如果需要自定义Flowable表前缀，可以在application.yml中配置:
-- flowable.database-table-prefix: FLOWABLE_