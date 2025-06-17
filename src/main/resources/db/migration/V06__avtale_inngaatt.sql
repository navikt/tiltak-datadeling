-- Datastream-koblingen til bigquery liker ikke norske tegn
ALTER TABLE avtale RENAME COLUMN avtale_inngått TO avtale_inngaatt;
ALTER TABLE avtale RENAME COLUMN godkjent_på_vegne_av TO godkjent_paa_vegne_av;
ALTER TABLE avtale RENAME COLUMN godkjent_på_vegne_av_arbeidsgiver TO godkjent_paa_vegne_av_arbeidsgiver;
