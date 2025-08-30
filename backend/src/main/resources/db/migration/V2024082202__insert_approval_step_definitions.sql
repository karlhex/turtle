-- 插入报销审批步骤定义
INSERT INTO approval_step_definitions (business_type, step_number, step_name, required_role, min_amount, max_amount, is_conditional, condition_expression) VALUES
('REIMBURSEMENT', 1, '部门领导审批', 'ROLE_DEPT_LEADER', 0, 999999.99, false, NULL),
('REIMBURSEMENT', 2, '财务审批', 'ROLE_FINANCE', 0, 999999.99, false, NULL),
('REIMBURSEMENT', 3, '总经理审批', 'ROLE_GENERAL_MANAGER', 1000.00, 999999.99, true, 'amount > 1000');