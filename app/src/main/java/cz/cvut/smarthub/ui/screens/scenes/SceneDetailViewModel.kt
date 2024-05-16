package cz.cvut.smarthub.ui.screens.scenes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.smarthub.database.Scene
import cz.cvut.smarthub.database.SceneRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime

class SceneDetailViewModel(
    private val sceneRepository: SceneRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sceneId: Long = checkNotNull(savedStateHandle.get<Long>("ID"))

    val uiState: StateFlow<SceneUiState> =
        sceneRepository
            .getSceneForIdStream(sceneId)
            .map {
                if (it != null) {
                    SceneUiState.Success(it)
                } else if (it == null && sceneId == 0L) {
                    SceneUiState.Success(Scene())
                } else {
                    SceneUiState.Error(Exception("Scene not found"))
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = SceneUiState.Loading
            )

    fun saveScene(
        newName: String,
        newDescription: String,
        newIsActive: Boolean,
        newStartTime: LocalTime
    ) {
        viewModelScope.launch {
            val scene = Scene(
                id = sceneId,
                name = newName,
                description = newDescription,
                isActive = newIsActive,
                startTime = newStartTime
            )
            if (sceneId == 0L) {
                sceneRepository.insertScenes(scene)
            } else {
                sceneRepository.updateScenes(scene)
            }
        }
    }
}

sealed interface SceneUiState {
    data object Loading : SceneUiState
    data class Success(val scene: Scene) : SceneUiState
    data class Error(val exception: Exception) : SceneUiState
}