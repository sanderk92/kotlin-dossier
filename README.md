# Kotlin Dossier

A Kotlin state managing model, with the following characteristics:
- State of the model is stored as a type instead of a variable
- Only functions applicable to a specific state can be called
- High reusability of recurring state transitions 

```kotlin
val case = CivilCase(defendant = "Bill Wellington", prosecutor = "Juan Marques")
val openDossier: OpenDossier<CivilCase> = OpenDossier(case)

val startTime = Instant.now()
val processingDossier: ProcessingDossier<CivilCase> = openDossier.process(startTime)

val ruling = CourtRuling(result = "Case dismissed")
val finalizedDossier: FinalizedDossier<CivilCase> = processingDossier.finalize(ruling)

val reopenedDossier: OpenDossier<CivilCase> = finalizedDossier.reopen()
```