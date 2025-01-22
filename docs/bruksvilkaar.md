# Bruksvilkår for konsumering av data

## Hvilke data leverer vi

`tiltak-datadeling` leverer data om arbeidsmarkedstiltak. Det betyr at man for eksempel kan få svar på spørsmål som:

- Hvor mange deltakere er på varig lønnstilskudd nå?
- Hvilke arbeidsmarkedstiltak er deltaker X på, og hvor jobber hen?
- Hvilke deltakere er på tiltak i bedrift X?
- Hva er status for et arbeidsmarkedstiltak? Er det påbegynt, under gjennomføring, avsluttet eller annullert?
- Når ble avtalen godkjent av hver avtalepart? (deltaker, arbeidsgiver, veileder...)

Vi leverer med andre ord personopplysninger som:
- personnummer på deltaker på arbeidsmarkedstiltak
- hvor deltakeren jobber
- nav-ident til veileder på tiltaket

**Merk at det også kan forekomme deltaker med adressebeskyttelse i dataene**.

## Hva er dere ansvarlige for?

Konsumenter må utvise aktsomhet når de tar i bruk informasjon fra våre systemer.

Siden våre data inneholder personopplysninger og potensielt opplysninger om innbyggere med adressesperre
er dere ansvarlige for å sikre at opplysninger ikke kommer på avveie. Bruk PDL for å identifisere/filtrere
data om deltakere med adressebeskyttelse ved behov.

Unngå gjerne "vide spørringer" som feks "hent fødselsnummer til alle som er på tiltak X", men spør heller
så spesifikt som mulig, og inkluder så få felter som mulig i graphql-spørringen.

Vi inkluderer ikke informasjon som kan identifisere om en deltaker har adressebeskyttelse eller ikke, dette
har konsumenter ansvar for å slå opp selv via PDL ved behov.

**Konsumenter som mottar data med personopplysninger må bekrefte at det foreligger et behandlingsgrunnlag.**
Dette gjøres ved å lenke til behandlingskatalogen, feks: https://behandlingskatalog.intern.nav.no/process/system/TILTAKSGJENNOMFORING/780e3168-cf58-46ec-9764-a6cdd3da3b8f
