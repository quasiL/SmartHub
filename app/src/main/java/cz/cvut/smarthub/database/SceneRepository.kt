package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

interface SceneRepository {
    fun getAllScenesStream(): Flow<List<Scene>>
    fun getSceneForIdStream(id: Long): Flow<Scene?>
    suspend fun insertScenes(vararg scenes: Scene)
    suspend fun deleteScene(scene: Scene)
    suspend fun deleteAllScenes()
    suspend fun updateScenes(vararg scenes: Scene)
}