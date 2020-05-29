package gopns.com.tdlist.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import gopns.com.tdlist.TodoL

//Database Access Object
@Dao
interface TodoLDao {
    @Query("SELECT * FROM todo_table")
    fun getTodos(): LiveData<List<TodoL>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodo(todo: TodoL)

    @Update
    suspend fun updateTodo(todo: TodoL)

    @Delete
    suspend fun deleteTodo(todo: TodoL)
}