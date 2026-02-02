-- Legg til antall dager per uke, og fyll inn verdien fra raw_json i feltet "antallDagerPerUke"
ALTER TABLE avtale ADD COLUMN antall_dager_per_uke DOUBLE PRECISION;

-- En nyanse vi ikke har i avtaleløsningen er at stillingsprosentfeltet gjelder mentor for mentoravtaler,
-- ikke deltakeren! Det kan vi jo rette opp i her.
ALTER TABLE avtale ADD COLUMN deltakers_stillingsprosent INTEGER;
ALTER TABLE avtale ADD COLUMN mentors_stillingsprosent INTEGER;

-- [jooq ignore start]
UPDATE avtale SET antall_dager_per_uke = (raw_json->>'antallDagerPerUke')::NUMERIC
WHERE (raw_json->>'antallDagerPerUke') IS NOT NULL;

UPDATE avtale SET deltakers_stillingsprosent = (raw_json->>'stillingprosent')::NUMERIC
WHERE (raw_json->>'stillingprosent') IS NOT NULL AND tiltakstype != 'MENTOR';

UPDATE avtale SET mentors_stillingsprosent = (raw_json->>'stillingprosent')::NUMERIC
WHERE (raw_json->>'stillingprosent') IS NOT NULL AND tiltakstype = 'MENTOR';
-- [jooq ignore stop]
