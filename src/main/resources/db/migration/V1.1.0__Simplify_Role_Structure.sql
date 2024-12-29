-- Drop role_type column if it exists
DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'role' 
        AND column_name = 'role_type'
    ) THEN
        ALTER TABLE role DROP COLUMN role_type;
    END IF;
END $$;

-- Ensure role_name column has correct constraints
ALTER TABLE role 
    ALTER COLUMN role_name SET NOT NULL,
    ADD CONSTRAINT role_name_unique UNIQUE (role_name);

-- Update existing data to ensure consistency
UPDATE role 
SET role_name = name 
WHERE role_name IS NULL AND name IS NOT NULL;

-- Add is_system column if it doesn't exist
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'role' 
        AND column_name = 'is_system'
    ) THEN
        ALTER TABLE role ADD COLUMN is_system BOOLEAN NOT NULL DEFAULT false;
    END IF;
END $$;

-- Set is_system flag for system roles
UPDATE role 
SET is_system = true 
WHERE role_name = 'ROLE_SYSTEM';
