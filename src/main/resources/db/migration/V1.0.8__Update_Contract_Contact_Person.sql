-- Add new contact person relationship
ALTER TABLE contracts ADD COLUMN contact_person_id BIGINT;

-- Add foreign key constraint
ALTER TABLE contracts ADD CONSTRAINT fk_contract_contact_person 
    FOREIGN KEY (contact_person_id) REFERENCES people(id);

-- Copy existing contact person data to people table and update references
INSERT INTO people (first_name, mobile_phone, email)
SELECT 
    contact_person as first_name,
    contact_phone as mobile_phone,
    contact_email as email
FROM contracts 
WHERE contact_person IS NOT NULL
ON CONFLICT DO NOTHING;

-- Update contract references to point to the newly created people
UPDATE contracts c
SET contact_person_id = p.id
FROM people p
WHERE p.first_name = c.contact_person
AND c.contact_person IS NOT NULL;

-- Drop old columns
ALTER TABLE contracts DROP COLUMN contact_person;
ALTER TABLE contracts DROP COLUMN contact_phone;
ALTER TABLE contracts DROP COLUMN contact_email;
