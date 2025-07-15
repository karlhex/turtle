-- Add missing created_at and updated_at columns to tables that need them

-- Departments table
ALTER TABLE departments ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE departments ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Employees table
ALTER TABLE employees ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE employees ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Positions table
ALTER TABLE positions ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE positions ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Employee Education table
ALTER TABLE employee_education ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE employee_education ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Employee Payroll table
ALTER TABLE employee_payroll ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE employee_payroll ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Companies table
ALTER TABLE companies ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Contacts table
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Products table
ALTER TABLE products ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE products ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Persons table
ALTER TABLE persons ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE persons ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Projects table
ALTER TABLE projects ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE projects ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Contracts table
ALTER TABLE contracts ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE contracts ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Contract Items table
ALTER TABLE contract_items ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE contract_items ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Inventory table
ALTER TABLE inventory ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE inventory ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Bank Accounts table
ALTER TABLE bank_accounts ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE bank_accounts ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Currencies table
ALTER TABLE currencies ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE currencies ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Tax Info table
ALTER TABLE tax_info ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE tax_info ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Reimbursements table
ALTER TABLE reimbursements ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE reimbursements ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Reimbursement Items table
ALTER TABLE reimbursement_items ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE reimbursement_items ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Invoices table
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP; 