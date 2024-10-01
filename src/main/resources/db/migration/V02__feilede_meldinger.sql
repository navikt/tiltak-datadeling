CREATE TABLE feilede_meldinger (
    id SERIAL PRIMARY KEY,
    kafka_key TEXT,
    payload TEXT NOT NULL,
    feilmelding TEXT NOT NULL,
    opprettet timestamptz NOT NULL default CURRENT_TIMESTAMP
);
