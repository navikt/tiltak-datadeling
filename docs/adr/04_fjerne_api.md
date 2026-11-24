# 04 - Vi fjerner API-et

**Status:** Godkjent

Vi fjerner GraphQL-API-et, det ubrukte admin-endepunktet og det som bare fantes for dem: Spring Security,
Entra ID-appen, tilgangsreglene og ingressene. Appen leser fortsatt avtaler fra Kafka og lagrer dem i
Postgres, og Datastream speiler dem videre til BigQuery som før.

ADR-en erstatter [ADR 03](03_apiteknologi.md) og delen av [ADR 01](01_datakilde.md) som handler om et API for
andre team. Pilen til API-konsumenter i diagrammet i [ADR 02](02_lagringssted.md) gjelder heller ikke lenger.

## Kontekst

I ADR 01 bestemte vi å lage tiltak-datadeling som en egen app som andre team kunne spørre om avtaler. I ADR 03
valgte vi GraphQL for API-et. API-et krevde token fra Entra ID, og i prod var det bare `tiltakshistorikk` fra
team-mulighetsrommet som fikk tilgang.

Tiltakshistorikk gikk 2. september 2026 over til å lese Kafka-topicet `arbeidsgiver.tiltak-avtale-hendelse-compact`
direkte i stedet for å kalle API-et ([navikt/mulighetsrommet#8091](https://github.com/navikt/mulighetsrommet/pull/8091)).
Det var et av alternativene ADR 01 forkastet. Etter det har ingen kalt API-et i prod.
Vi målte trafikken i Mimir 18. november 2026:

- Fram til 2. september fikk API-et mellom 6 800 og 26 600 kall i døgnet i prod. De siste kom mellom kl. 12:25
  og 12:30, rett etter at #8091 ble merget kl. 12:23.
- I de 77 døgnene etter det kom det ingen kall til `/graphql` i prod. Det eneste utenom `/internal` var fire
  forespørsler fra en sårbarhetsskanner, og alle fikk 401.
- Admin-endepunktet `/api/admin/feilede-meldinger` fikk ingen kall i prod i de 90 dagene Mimir har data for.
- Et søk i koden til navikt på GitHub fant ingen andre apper som bruker tiltak-datadeling.

## Konsekvenser

### Fordeler

- Vi har mindre kode og færre avhengigheter å vedlikeholde. GraphQL, Spring Security og OAuth2 forsvinner,
  og det gjør oppgraderingen til Spring Boot 4 enklere.
- Appen har ikke lenger et endepunkt som gir ut personopplysninger, og den kan ikke nås via ingress.
- Vi slipper å forvalte tilganger for API-konsumenter.

### Ulemper

- Team som trenger avtaledata, må lese Kafka-topicet selv eller få tilgang til dataene i BigQuery. Med Kafka
  må de bygge sin egen kopi av dataene, slik ADR 01 beskrev.
- Appen har ingen autentisering lenger. Et nytt API krever ny autentisering og en ny ADR.

## Alternativer

### Beholde API-et

Vi kunne latt API-et stå i tilfelle noen trenger det igjen. Da må vi fortsatt oppgradere og sikre kode som ingen
bruker, og API-et gir fortsatt ut personopplysninger.

### Stenge API-et uten å fjerne koden

Vi kunne fjernet tilgangsreglene og ingressen og latt koden stå. Det er lett å reversere, men vi må fortsatt
vedlikeholde koden og avhengighetene. Koden ligger uansett i git-historikken hvis vi trenger den igjen.
