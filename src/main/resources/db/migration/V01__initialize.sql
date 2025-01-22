CREATE TABLE avtale (
    id serial primary key,
    avtale_id uuid NOT NULL,
    avtale_nr integer NOT NULL,
    deltaker_fnr text NOT NULL,
    bedrift_nr text NOT NULL,
    veileder_nav_ident text,
    tiltakstype text NOT NULL,
    versjon integer NOT NULL,
    avtale_status text NOT NULL,
    avtale_inngått timestamp with time zone,
    start_dato date,
    slutt_dato date,
    godkjent_av_deltaker timestamp with time zone,
    godkjent_av_arbeidsgiver timestamp with time zone,
    godkjent_av_veileder timestamp with time zone,
    godkjent_av_beslutter timestamp with time zone,
    godkjent_på_vegne_av boolean,
    godkjent_på_vegne_av_arbeidsgiver boolean,
    stillingstype text,
    godkjent_av_nav_ident text,
    godkjent_av_beslutter_nav_ident text,
    opprettet_tidspunkt timestamp with time zone NOT NULL,
    endret_tidspunkt timestamp with time zone NOT NULL,
    endring_mottatt_tidspunkt timestamp with time zone NOT NULL
);

create unique index avtale__avtale_id on avtale (avtale_id);
