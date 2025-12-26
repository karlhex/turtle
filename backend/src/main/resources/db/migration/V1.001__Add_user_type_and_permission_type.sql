-- Add user_type column to users table (nullable first)
ALTER TABLE users ADD COLUMN user_type VARCHAR(20);

-- Update existing records with default value
UPDATE users SET user_type = 'EMPLOYEE' WHERE user_type IS NULL;

-- Make the column NOT NULL and add default
ALTER TABLE users ALTER COLUMN user_type SET NOT NULL;
ALTER TABLE users ALTER COLUMN user_type SET DEFAULT 'EMPLOYEE';

-- Create check constraint for user_type
ALTER TABLE users ADD CONSTRAINT users_user_type_check 
    CHECK (user_type IN ('SYSTEM', 'EMPLOYEE', 'GUEST'));

-- Add permission_type column to role_permissions table (nullable first)
ALTER TABLE role_permissions ADD COLUMN permission_type VARCHAR(20);

-- Update existing records with default value
UPDATE role_permissions SET permission_type = 'SPECIFIC' WHERE permission_type IS NULL;

-- Make the column NOT NULL and add default
ALTER TABLE role_permissions ALTER COLUMN permission_type SET NOT NULL;
ALTER TABLE role_permissions ALTER COLUMN permission_type SET DEFAULT 'SPECIFIC';

-- Create check constraint for permission_type
ALTER TABLE role_permissions ADD CONSTRAINT role_permissions_permission_type_check 
    CHECK (permission_type IN ('SPECIFIC', 'ALL'));

-- Create index for user_type for better query performance
CREATE INDEX idx_users_user_type ON users(user_type);

-- Create index for permission_type for better query performance
CREATE INDEX idx_role_permissions_permission_type ON role_permissions(permission_type);