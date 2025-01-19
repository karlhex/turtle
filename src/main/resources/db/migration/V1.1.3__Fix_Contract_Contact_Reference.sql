-- First, identify and update any invalid contact_person_id references in contracts table
UPDATE contracts 
SET contact_person_id = NULL 
WHERE contact_person_id NOT IN (SELECT id FROM contacts);

-- Then add the foreign key constraint
ALTER TABLE contracts 
    DROP CONSTRAINT IF EXISTS FK9bagwhq1344c9bqsbkcxwyinl,
    ADD CONSTRAINT FK9bagwhq1344c9bqsbkcxwyinl 
    FOREIGN KEY (contact_person_id) 
    REFERENCES contacts;
