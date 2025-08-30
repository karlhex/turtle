-- Make project_id nullable in reimbursements table
ALTER TABLE reimbursements 
ALTER COLUMN project_id DROP NOT NULL;