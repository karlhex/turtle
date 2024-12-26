-- Insert all role types with descriptions
INSERT INTO roles (role_name, role_description) VALUES
('ROLE_USER', 'Regular user with basic access privileges'),
('ROLE_ADMIN', 'Administrator with full system access'),
('ROLE_SYSTEM', 'System level access for automated processes'),
('ROLE_GUEST', 'Limited access for guest users')
ON CONFLICT (role_name) DO UPDATE 
SET role_description = EXCLUDED.role_description;
