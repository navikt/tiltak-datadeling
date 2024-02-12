# tiltak-datadeling
API for deling av team tiltak-data internt i Nav

## Kjøre tester med Colima
> During my tests, I couldn't make ryuk work with colima out of the box. I managed to do it by starting the VM with an assigned IP and then overriding TESTCONTAINERS_HOST_OVERRIDE with it.
From that point onwards, together with what is described in the documentation, all my project's tests worked flawlessly.

> Install Colima: brew install colima
Install docker: brew install docker
Start Colima with an assigned IP address: colima start --network-address
Colima automatically creates and sets a docker context named colima, therefore docker commands on the command line work out of the box
To use with testcontainers, make sure the following environment variables are set on your session:
export TESTCONTAINERS_HOST_OVERRIDE=$(colima ls -j | jq -r '.address')
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
export DOCKER_HOST=unix://$HOME/.colima/default/docker.sock

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