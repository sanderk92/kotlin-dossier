import java.time.Instant
import java.util.*

data class DossierId(val value: UUID)

interface CaseContext
data class CivilCase(val id: DossierId): CaseContext
data class CommonCase(val id: DossierId): CaseContext

data class CourtRuling (val message: String)

sealed interface Dossier<Case> {
    val case: Case
}

data class OpenDossier<Case: CaseContext>(
    override val case: Case,
) : Dossier<Case> {
    fun process(time: Instant) = ProcessingDossier(case, time)
}

data class ProcessingDossier<Case: CaseContext>(
    override val case: Case,
    val started: Instant,
) : Dossier<Case> {
    fun finalize(ruling: CourtRuling) = FinalizedDossier(case, started, ruling)
}

data class FinalizedDossier<Case: CaseContext>(
    override val case: Case,
    val started: Instant,
    val ruling: CourtRuling,
) : Dossier<Case> {
    fun reopen() = OpenDossier(case)
    fun reprocess(time: Instant) = ProcessingDossier(case, time)
}
