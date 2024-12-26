-- Drop existing constraint if any
ALTER TABLE roles DROP CONSTRAINT IF EXISTS roles_role_name_check;

-- Add new check constraint for role_name
ALTER TABLE roles ADD CONSTRAINT roles_role_name_check 
CHECK (role_name IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SYSTEM', 'ROLE_GUEST'));
