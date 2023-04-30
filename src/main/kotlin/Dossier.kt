import java.time.Instant
import java.util.*

data class DossierId(val value: UUID)

data class CourtRuling (val result: String)

interface CaseContext
data class CivilCase(val defendant: String, val prosecutor: String): CaseContext
data class CommonCase(val defendant: String, val prosecutor: String): CaseContext

sealed interface Dossier<Case> {
    val id: DossierId
    val case: Case
}

data class OpenDossier<Case: CaseContext>(
    override val id: DossierId,
    override val case: Case,
) : Dossier<Case> {
    fun process(time: Instant) = ProcessingDossier(id, case, time)
    constructor(case: Case): this(DossierId(UUID.randomUUID()), case)
}

data class ProcessingDossier<Case: CaseContext>(
    override val id: DossierId,
    override val case: Case,
    val started: Instant,
) : Dossier<Case> {
    fun finalize(ruling: CourtRuling) = FinalizedDossier(id, case, started, ruling)
}

data class FinalizedDossier<Case: CaseContext>(
    override val id: DossierId,
    override val case: Case,
    val started: Instant,
    val ruling: CourtRuling,
) : Dossier<Case> {
    fun reopen() = OpenDossier(case)
}
