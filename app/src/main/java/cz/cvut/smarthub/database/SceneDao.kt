package cz.cvut.smarthub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneDao {
    @Query("SELECT * FROM scenes ORDER BY id ASC")
    fun getAllScenes(): Flow<List<Scene>>

    @Query("SELECT * FROM scenes WHERE id = :id")
    fun getSceneById(id: Long): Flow<Scene?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(vararg scenes: Scene)

    @Update
    suspend fun updateScenes(vararg scenes: Scene)

    @Delete
    suspend fun deleteScene(scene: Scene)

    @Query("DELETE FROM scenes")
    suspend fun deleteAllScenes()
}