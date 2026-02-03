-- Legg til antall dager per uke, og fyll inn verdien fra raw_json i feltet "antallDagerPerUke"
ALTER TABLE avtale ADD COLUMN antall_dager_per_uke DOUBLE PRECISION;

-- En nyanse vi ikke har i avtaleløsningen er at stillingprosent-feltet gjelder mentor for mentoravtaler,
-- ikke deltakeren! Det kan vi jo rette opp i her.
ALTER TABLE avtale ADD COLUMN stillingprosent DOUBLE PRECISION;

-- [jooq ignore start]
UPDATE avtale SET antall_dager_per_uke = (raw_json->>'antallDagerPerUke')::NUMERIC
WHERE (raw_json->>'antallDagerPerUke') IS NOT NULL;

UPDATE avtale SET stillingprosent = (raw_json->>'stillingprosent')::NUMERIC
WHERE (raw_json->>'stillingprosent') IS NOT NULL AND tiltakstype != 'MENTOR';
-- [jooq ignore stop]
