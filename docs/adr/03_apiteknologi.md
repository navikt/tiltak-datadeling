# 03 - Vi eksponerer et graphql-api

## Kontekst

For å dele data med andre team skal vi sette opp et API. Dermed må vi ta en avgjørelse på hvordan
vi vil implementere dette. Vi ser for oss at vi har et sett med minimumskrav:

- Vi må tilby dataminimering (konsumenter må kunne velge ut et fåtall felter)
- Vi må tilby både "nåværende" og historisk tilstand (_hvilke tiltak er en person på, og hvilke har
hen vært på tidligere?_)
- Vi har per nå ikke krav om granulert autorisasjon
- Ulike team/konsumenter kan ha ulike behov
- Per nå har vi ikke krav om datahistorikk (endringer over tid), men det er en fordel om vi har
mulighet til å implementere dette på sikt, feks ved at konsument sender inn et tidspunkt de vil
ha data fra (_hvilke tiltak trodde systemet at en deltaker var på for en uke siden?_)
- Vi må kreve at konsumenter sender inn behandlingsgrunnlaget sitt
- Vi må logge hva slags datapunkter som det slås opp på, for å avdekke om konsumenter samler inn
for mye data i forhold til hva de har hjemmel til å behandle.

Teknologien vi velger må kunne støtte disse kravene som et minimum.

## Konsekvenser

Konsumenter får tilgang til dataene de trenger, og vi har god oversikt på hvordan data brukes av
konsumentene (deres behandlingsgrunnlag, hvilke data de henter ut, og så videre).

### Fordeler

- Konsumenter får api-dokumentasjon via graphql-schemaet
- Konsumenter kan bruke schemaet til å generere klient-kode
- Vi får innebygd støtte for dataminimering

### Ulemper

- GraphQL har høyere kompleksitet i forhold til tradisjonell REST, og særegen syntaks
- Teamet har ikke kjennskap til GraphQL (autorisasjon, versjonering, ytelse og lignende)
- Rest-apier er enklere å forstå og bruke (feks via Curl)

## Alternativer

## REST-api med OpenAPI-spec

Ved å lage et API som er dokumentert med OpenAPI kan vi oppnå tilsvarende fordeler som GraphQL
med hensyn til klient-generering og api-dokumentasjon.

Støtte for dataminimering får vi ikke herfra, men det er mulig å implementere dette selv, feks
ved å hente inspirasjon fra OData-speccen (`$select`-parameteret).

Å bruke et vanlig rest-api gir oss også en enklere teknologistack, og vi slipper å bekymre oss
for potensielle problemer med GraphQL (rekursive spørringer og lignende)

## REST-api som følger OData-spec

Ved å bruke [OData-spesifikasjonen](https://www.odata.org/) har vi mulighet til å definere et
enkelt api (feks via Swagger/OpenAPI-spec) som også legger til rette for dataminimering
(via `$select`-parameteret) og andre fordeler tilsvarende det man får fra GraphQL.

Man kan også implementere `$filter` for å tillate enkle spørringer på mer generelle endepunkter.

Ulempen med OData er i all hovedsak at det er en mindre kjent spesifikasjon, og bibliotek-støtten
er begrenset. Dermed blir man nødt til å forsøke å implementere støtten selv etter beste evne, som
kan føre til feil.

Når det er sagt kan selv et subsett av spesifikasjonen gi mye verdi (feks `$select`). Kanskje er
det mest hensiktsmessig å benytte OData-speccen som inspirasjonskilde, snarere enn å prøve å
implementere speccen 100%.
