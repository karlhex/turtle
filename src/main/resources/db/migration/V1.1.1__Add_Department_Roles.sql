-- Delete existing roles first to avoid duplicates
DELETE FROM role WHERE role_name IN (
    'ROLE_SYSTEM',
    'ROLE_GUEST',
    'ROLE_ADMIN',
    'ROLE_HR_DIRECTOR',      -- 人力总经理
    'ROLE_HR_SPECIALIST',    -- 人力专员
    'ROLE_ADMIN_DIRECTOR',   -- 行政总经理
    'ROLE_ADMIN_SPECIALIST', -- 行政专员
    'ROLE_FINANCE_DIRECTOR', -- 财务总经理
    'ROLE_ACCOUNTANT',       -- 会计
    'ROLE_CASHIER',         -- 出纳
    'ROLE_SALES_DIRECTOR',  -- 销售总经理
    'ROLE_SALES_LEADER',    -- 销售组长
    'ROLE_SALES'            -- 销售
);

-- Insert all roles
INSERT INTO role (role_name, role_description, is_system) VALUES
    ('ROLE_SYSTEM', 'System Administrator', true),
    ('ROLE_GUEST', 'Guest User', false),
    ('ROLE_ADMIN', 'Administrator', true),
    ('ROLE_HR_DIRECTOR', '人力总经理', false),
    ('ROLE_HR_SPECIALIST', '人力专员', false),
    ('ROLE_ADMIN_DIRECTOR', '行政总经理', false),
    ('ROLE_ADMIN_SPECIALIST', '行政专员', false),
    ('ROLE_FINANCE_DIRECTOR', '财务总经理', false),
    ('ROLE_ACCOUNTANT', '会计', false),
    ('ROLE_CASHIER', '出纳', false),
    ('ROLE_SALES_DIRECTOR', '销售总经理', false),
    ('ROLE_SALES_LEADER', '销售组长', false),
    ('ROLE_SALES', '销售', false);
