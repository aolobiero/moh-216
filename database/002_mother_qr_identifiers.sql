-- MOH 216: Unique QR identifiers for mothers

ALTER TABLE mothers
ADD COLUMN IF NOT EXISTS qr_token UUID;

-- Assign identifiers to existing records
UPDATE mothers
SET qr_token = gen_random_uuid()
WHERE qr_token IS NULL;

-- Automatically generate identifiers for new records
ALTER TABLE mothers
ALTER COLUMN qr_token SET DEFAULT gen_random_uuid();

-- Prevent missing identifiers
ALTER TABLE mothers
ALTER COLUMN qr_token SET NOT NULL;

-- Prevent duplicate identifiers
CREATE UNIQUE INDEX IF NOT EXISTS mothers_qr_token_unique
ON mothers (qr_token);
