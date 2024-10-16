# tiltak-datadeling
API for deling av team tiltak-data internt i Nav

## Kjøre lokalt (og tester) med testcontainers og Colima
For å kjøre lokalt, og å kjøre tester, må man legge til disse miljøvariablene:
```shell
DOCKER_HOST=unix://{HOME_PATH}/.colima/default/docker.sock
TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
```

Socket-path til colima (`DOCKER_HOST`) finner man ved å skrive:
```shell
colima status 2>&1 | grep socket | awk '{print $NF}' | sed 's/"//'
```

### Legge til miljøvariabler i templates
For å legge til miljøvariabler i IntelliJ, gå til `Run/Debug Configurations` -> `Templates` -> `JUnit`/`Spring Boot`/`Kotlin` -> `Environment variables` og legg til variablene der.
![run_templates.png](run_templates.png)

## Eksempel på Graphql-spørring

```graphql
{
  avtalerForBedrift(organisasjonsnummer: "910825526") {
    avtaleId
    avtaleNr
    opprettetTidspunkt
    sistEndret
    bedriftNr
    deltakerFnr
    mentorFnr
    veilederNavIdent
    enhetGeografisk
    enhetsnavnGeografisk
    enhetOppfolging
    enhetsnavnOppfolging
    hendelseType
    avtaleStatus
    tiltakstype
    annullertTidspunkt
    annullertGrunn
    slettemerket
    opprettetAvArbeidsgiver
    godkjentForEtterregistrering
    kvalifiseringsgruppe
    formidlingsgruppe
    feilregistrert
    versjon
    deltakerFornavn
    deltakerEtternavn
    deltakerTlf
    bedriftNavn
    arbeidsgiverFornavn
    arbeidsgiverEtternavn
    arbeidsgiverTlf
    veilederFornavn
    veilederEtternavn
    veilederTlf
    oppfolging
    tilrettelegging
    sluttDato
    startDato
    stillingprosent
    journalpostId
    arbeidsoppgaver
    stillingstittel
    stillingStyrk08
    stillingKonseptId
    antallDagerPerUke
    refusjonKontaktperson {
      refusjonKontaktpersonFornavn
      refusjonKontaktpersonEtternavn
      refusjonKontaktpersonTlf
      onskerVarslingOmRefusjon
    }
    mentorFornavn
    mentorEtternavn
    mentorOppgaver
    mentorAntallTimer
    mentorTimelonn
    mentorTlf
    arbeidsgiverKontonummer
    lonnstilskuddProsent
    manedslonn
    feriepengesats
    arbeidsgiveravgift
    harFamilietilknytning
    familietilknytningForklaring
    feriepengerBelop
    otpSats
    otpBelop
    arbeidsgiveravgiftBelop
    sumLonnsutgifter
    sumLonnstilskudd
    manedslonn100pst
    sumLonnstilskuddRedusert
    datoForRedusertProsent
    stillingstype
    maal {
      id
      kategori
      beskrivelse
    }
    inkluderingstilskuddsutgift {
      id
      belop
      type
    }
    inkluderingstilskuddBegrunnelse
    inkluderingstilskuddTotalBelop
    godkjentAvDeltaker
    godkjentTaushetserklaeringAvMentor
    godkjentAvArbeidsgiver
    godkjentAvVeileder
    godkjentAvBeslutter
    avtaleInngaatt
    ikrafttredelsestidspunkt
    godkjentAvNavIdent
    godkjentAvBeslutterNavIdent
    enhetKostnadssted
    enhetsnavnKostnadssted
    godkjentPaVegneGrunn {
      ikkeBankId
      reservert
      digitalKompetanse
      arenaMigreringDeltaker
    }
    godkjentPaVegneAv
    godkjentPaVegneAvArbeidsgiverGrunn {
      klarerIkkeGiFaTilgang
      vetIkkeHvemSomKanGiTilgang
      farIkkeTilgangPersonvern
      arenaMigreringArbeidsgiver
    }
    godkjentPaVegneAvArbeidsgiver
    innholdType
    utfortAv
    utfortAvRolle
  }
}
```
