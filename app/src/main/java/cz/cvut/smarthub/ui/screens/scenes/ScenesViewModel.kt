package cz.cvut.smarthub.ui.screens.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.smarthub.database.Scene
import cz.cvut.smarthub.database.SceneRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import cz.cvut.smarthub.model.common.Result
import cz.cvut.smarthub.model.common.asResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScenesViewModel(
    private val sceneRepository: SceneRepository,
) : ViewModel() {

    val scenes: StateFlow<ScenesUiState> =
        sceneRepository
            .getAllScenesStream()
            .asResult()
            .map { allScenesToResult ->
                when (allScenesToResult) {
                    is Result.Error -> ScenesUiState.Error(Exception(allScenesToResult.exception))
                    is Result.Loading -> ScenesUiState.Loading
                    is Result.Success -> ScenesUiState.Success(allScenesToResult.data)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ScenesUiState.Loading
            )

    fun addScenes(vararg scenes: Scene) {
        viewModelScope.launch {
            sceneRepository.insertScenes(*scenes)
        }
    }

    fun updateScenes(vararg scenes: Scene) {
        viewModelScope.launch {
            sceneRepository.updateScenes(*scenes)
        }
    }

    fun deleteScene(scene: Scene) {
        viewModelScope.launch {
            sceneRepository.deleteScene(scene)
        }
    }

    fun deleteAllScenes() {
        viewModelScope.launch {
            sceneRepository.deleteAllScenes()
        }
    }
}

sealed interface ScenesUiState {
    data object Loading : ScenesUiState
    data class Success(val itemList: List<Scene> = listOf()) : ScenesUiState
    data class Error(val exception: Exception) : ScenesUiState
}