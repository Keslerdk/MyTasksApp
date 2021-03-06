package com.example.mytasksapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

//    @Update
//    suspend fun update(task: Task)

    @Query("update task set status =:status where id = :id")
    suspend fun update(status: String, id:Int)

    @Delete
    suspend fun delete(task: Task)

    @Query("select * from task where id= :id")
    fun getTask(id: Int): Flow<Task>

    @Query( "select * from task order by id asc")
    fun getTasks(): Flow<List<Task>>

}