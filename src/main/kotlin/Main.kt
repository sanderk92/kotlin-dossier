import java.time.Instant

fun main() {
    val repo = StateRepository()

    val case = CivilCase(defendant = "Bill Wellington", prosecutor = "Juan Marques")
    val openDossier: OpenDossier<CivilCase> = OpenDossier(case)
    repo.store(openDossier)
    repo.printAll()

    val startTime = Instant.now()
    val processingDossier: ProcessingDossier<CivilCase> = openDossier.process(startTime)
    repo.update(processingDossier)
    repo.printAll()

    val ruling = CourtRuling(result = "Case dismissed")
    val finalizedDossier: FinalizedDossier<CivilCase> = processingDossier.finalize(ruling)
    repo.update(finalizedDossier)
    repo.printAll()

    val reopenedDossier: OpenDossier<CivilCase> = finalizedDossier.reopen()
    repo.update(reopenedDossier)
    repo.printAll()
}

private fun StateRepository.printAll() {
    println("Printing all dossiers!")
    println(findOpenDossiers())
    println(findProcessingDossiers())
    println(findFinalizedDossiers())
    println()
}
