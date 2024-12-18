-- Drop the phone column
ALTER TABLE people DROP COLUMN IF EXISTS phone;

-- Add new phone number columns
ALTER TABLE people ADD COLUMN IF NOT EXISTS mobile_phone VARCHAR(255);
ALTER TABLE people ADD COLUMN IF NOT EXISTS work_phone VARCHAR(255);
ALTER TABLE people ADD COLUMN IF NOT EXISTS home_phone VARCHAR(255);

-- Add company related columns
ALTER TABLE people ADD COLUMN IF NOT EXISTS company_name VARCHAR(255);
ALTER TABLE people ADD COLUMN IF NOT EXISTS department VARCHAR(255);
ALTER TABLE people ADD COLUMN IF NOT EXISTS position VARCHAR(255);

COMMENT ON COLUMN people.mobile_phone IS '手机号码';
COMMENT ON COLUMN people.work_phone IS '工作电话';
COMMENT ON COLUMN people.home_phone IS '家庭电话';
COMMENT ON COLUMN people.company_name IS '公司名称';
COMMENT ON COLUMN people.department IS '部门';
COMMENT ON COLUMN people.position IS '职务';
