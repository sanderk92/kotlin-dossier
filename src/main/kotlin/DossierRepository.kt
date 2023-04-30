import java.time.Instant
import java.util.*

class StateRepository {

    private val store: MutableList<CivilLawDossierEntity> = mutableListOf()

    fun store(dossier: Dossier<CivilCase>) {
        store.add(dossier.asDossierEntity())
    }

    fun update(dossier: Dossier<CivilCase>) {
        store.removeAll { it.id == dossier.id.value }
        store.add(dossier.asDossierEntity())
    }

    fun find(dossierId: DossierId): Dossier<CivilCase>? =
        store
            .firstOrNull { it.id == dossierId.value }
            ?.let(CivilLawDossierEntity::asDossierModel)

    fun findOpenDossiers(): List<OpenDossier<CivilCase>> =
        store
            .filter { it.state == State.Open }
            .map(CivilLawDossierEntity::asOpenDossierModel)

    fun findProcessingDossiers(): List<ProcessingDossier<CivilCase>> =
        store
            .filter { it.state == State.Processing }
            .map(CivilLawDossierEntity::asProcessingDossierModel)

    fun findFinalizedDossiers(): List<FinalizedDossier<CivilCase>> =
        store
            .filter { it.state == State.Finalized }
            .map(CivilLawDossierEntity::asFinalizedDossierModel)
}

private fun Dossier<CivilCase>.asDossierEntity(): CivilLawDossierEntity = when (this) {
    is OpenDossier -> asOpenDossierEntity()
    is ProcessingDossier -> asProcessingDossierEntity()
    is FinalizedDossier -> asFinalizedDossierEntity()
}

private fun OpenDossier<CivilCase>.asOpenDossierEntity() = CivilLawDossierEntity(
    id = id.value,
    state = State.Open,
    case = case,
    started = null,
    ruling = null,
)

private fun ProcessingDossier<CivilCase>.asProcessingDossierEntity() = CivilLawDossierEntity(
    id = id.value,
    state = State.Processing,
    case = case,
    started = started,
    ruling = null,
)

private fun FinalizedDossier<CivilCase>.asFinalizedDossierEntity() = CivilLawDossierEntity(
    id = id.value,
    state = State.Finalized,
    case = case,
    started = started,
    ruling = ruling,
)

private fun CivilLawDossierEntity.asDossierModel(): Dossier<CivilCase> = when (this.state) {
    State.Open -> asOpenDossierModel()
    State.Processing -> asProcessingDossierModel()
    State.Finalized -> asFinalizedDossierModel()
}

private fun CivilLawDossierEntity.asOpenDossierModel() = OpenDossier(
    id = DossierId(id),
    case = case,
)

private fun CivilLawDossierEntity.asProcessingDossierModel() = ProcessingDossier(
    id = DossierId(id),
    case = case,
    started = started!!,
)

private fun CivilLawDossierEntity.asFinalizedDossierModel() = FinalizedDossier(
    id = DossierId(id),
    case = case,
    started = started!!,
    ruling = ruling!!,
)

private data class CivilLawDossierEntity(
    val id: UUID,
    val state: State,
    val case: CivilCase,
    val started: Instant?,
    val ruling: CourtRuling?,
)

private enum class State {
    Open,
    Processing,
    Finalized,
}
