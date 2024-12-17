-- Drop the unique constraint on tax_info_id
ALTER TABLE companies
    DROP CONSTRAINT IF EXISTS ukcrncj61ul7ijc7w12s2r5a8wi;
