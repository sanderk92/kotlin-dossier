import java.time.Instant
import java.util.*

fun main() {
    val repo = StateRepository()

    val open = OpenDossier(CivilCase(DossierId(UUID.randomUUID())))
    repo.store(open)
    repo.printAll()

    val processing = open.process(Instant.now())
    repo.update(processing)
    repo.printAll()

    val finalized = processing.finalize(CourtRuling("Case denied"))
    repo.update(finalized)
    repo.printAll()
}

private fun StateRepository.printAll() {
    println("Printing all dossiers!")
    println(findOpenDossiers())
    println(findProcessingDossiers())
    println(findFinalizedDossiers())
    println()
}
