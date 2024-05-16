package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

class OfflineSceneRepository(private val sceneDao: SceneDao) : SceneRepository {
    override fun getAllScenesStream(): Flow<List<Scene>> = sceneDao.getAllScenes()
    override fun getSceneForIdStream(id: Long): Flow<Scene?> = sceneDao.getSceneById(id)
    override suspend fun insertScenes(vararg scenes: Scene) = sceneDao.insertScenes(*scenes)
    override suspend fun deleteScene(scene: Scene) = sceneDao.deleteScene(scene)
    override suspend fun deleteAllScenes() = sceneDao.deleteAllScenes()
    override suspend fun updateScenes(vararg scenes: Scene) = sceneDao.updateScenes(*scenes)
}