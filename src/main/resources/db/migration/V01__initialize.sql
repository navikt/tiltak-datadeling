CREATE TABLE avtale (
    id SERIAL PRIMARY KEY,
    avtale_data jsonb NOT NULL,
    gyldig_fra timestamp with time zone NOT NULL,
    gyldig_til timestamp with time zone,
    opprettet_tidpunkt timestamp with time zone NOT NULL DEFAULT now()
);
