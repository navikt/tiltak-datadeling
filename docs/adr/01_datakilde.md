# 01 - Vi oppretter et konsument-API via ny applikasjon

## Kontekst

Vår hovedkilde for data om avtaler er appen `tiltaksgjennomforing-api`. Når vi blir master for stadig flere tiltak,
vil vi være nødt til å tilby disse dataene til andre konsumenter i NAV.

For å tilby dataene velger vi å opprette en dedikert app for formålet. Appen henter data fra `tiltaksgjennomforing-api`
via en eksisterende Kafka-topic, og konsumenter vil kunne spørre den nye appen om avtaledetaljer.

For eksempel er det ofte ønskelig for andre team å vite om en person er på tiltak, og eventuelt hvilke, hvor mange,
hvor lenge, og så videre.

## Konsekvenser

Andre team får enkel tilgang til informasjon om arbeidsmarkedstiltak.

### Fordeler

- Ved å opprette ny applikasjon frikobler vi oss fra databasestrukturen til `tiltaksgjennomforing-api`, 
  og kan opprette et redusert datasett tilpasset konsumenter.
- Høyt trykk fra konsumenter påvirker ikke `tiltaksgjennomforing-api`.
- Vi kan eksponere data på en måte som er tilpasset konsumentenes behov.
- Vi reduserer friksjon ved migrering av `tiltaksgjennomforing-api` ved senere tidspunkt

### Ulemper

- Asynkron dataflyt er mer kompleks, spesielt i forhold til feilhåndtering.
- Potensiale for "split brain" mellom datakilder (en avtale i `tiltaksgjennomforing-api`
  er ikke i sync med `tiltak-datadeling`.
- Risiko for at endringer i tiltaksgjennomforing-databasen ikke reflekteres i dataene på
  kafka-køen (feks når endringer har blitt gjort i databasen, og ikke via APIet)

## Alternativer

### Tiltaksgjennomforing-api eksponerer et konsument-api

Foreløpig er konsumentene bare interessert i detaljer om avtaler og ikke refusjoner. 
Dermed kunne `tiltaksgjennomforing-api` eksponert et konsument-api for å levere ut
avtaledata.

Det ville gitt oss en slankere teknologistack, med den ulempe at tiltaksgjennomforing-api
ikke lenger er skjermet fra eksterne konsumenter, som kan påvirke endringstakten.

### Konsumenter bruker kafka-kø i stedet for et API

Hvorfor trenger vi et nytt API når konsumenter bare kan lese direkte fra en Kafka-topic
vi allerede har (og som allerede konsumeres av andre applikasjoner)?

Hvis dette var en grei løsning ville vi spart oss økt teknologi-infrastruktur. Applikasjoner
må jo tross alt forvaltes, og jo færre desto bedre.

Dessverre er ulempen ved å kun tilby data via kafka at eksterne konsumenter må bygge opp
en egen database-kopi for å svare ut om en deltaker er på tiltak eller ikke.
