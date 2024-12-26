-- Update existing roles to new format
UPDATE role SET name = 'ROLE_USER' WHERE name = 'USER';
UPDATE role SET name = 'ROLE_ADMIN' WHERE name = 'ADMIN';
UPDATE role SET name = 'ROLE_SYSTEM' WHERE name = 'SYSTEM';
UPDATE role SET name = 'ROLE_GUEST' WHERE name = 'GUEST';

-- Insert ROLE_GUEST if it doesn't exist
INSERT INTO role (name)
SELECT 'ROLE_GUEST'
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE name = 'ROLE_GUEST'
);
