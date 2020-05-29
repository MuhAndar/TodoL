package gopns.com.tdlist.Database

import android.app.Application
import androidx.lifecycle.LiveData
import gopns.com.tdlist.TodoL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//Penyimpanan
class ToDoRepository(application: Application){
    private val todoDao: TodoLDao?
    private var todos: LiveData<List<TodoL>>? = null

    init{
        val db =
            AppDatabase.getInstance(
                application.applicationContext
            )
        todoDao = db?.todoDao()
        todos = todoDao?.getTodos()
    }

    fun getTodos(): LiveData<List<TodoL>>?{
        return todos
    }

    fun insert(todo: TodoL) = runBlocking {
        this.launch(Dispatchers.IO){
            todoDao?.insertTodo(todo)
        }
    }

    fun update(todo: TodoL) = runBlocking {
        this.launch(Dispatchers.IO){
            todoDao?.updateTodo(todo)
        }
    }

    fun delete(todo: TodoL) = runBlocking {
        this.launch(Dispatchers.IO){
            todoDao?.deleteTodo(todo)
        }
    }

}