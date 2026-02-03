ALTER TABLE avtale ADD COLUMN opphav TEXT;

-- [jooq ignore start]
UPDATE avtale SET opphav = (raw_json->>'opphav')::TEXT
WHERE (raw_json->>'opphav') IS NOT NULL;
-- [jooq ignore stop]
