package gopns.com.tdlist.Model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import gopns.com.tdlist.Database.TodoLRepo
import gopns.com.tdlist.TodoL

class TodoLVM (application: Application): AndroidViewModel(application) {
    private var todoRepository =
        TodoLRepo(application)
    private var todos: LiveData<List<TodoL>>? = todoRepository.getTodos()


    fun getTodos(): LiveData<List<TodoL>>? {
        return todos
    }

    fun insertTodo(todo: TodoL) {
        todoRepository.insert(todo)
    }

    fun deleteTodo(todo: TodoL) {
        todoRepository.delete(todo)
    }

    fun updateTodo(todo: TodoL) {
        todoRepository.update(todo)
    }
}