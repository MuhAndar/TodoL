package gopns.com.tdlist.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import gopns.com.tdlist.MainActivity
import gopns.com.tdlist.R
import gopns.com.tdlist.TodoL
import gopns.com.tdlist.Util.Common
import kotlinx.android.synthetic.main.item_empty.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

//mengatur tampilan list TodoL
class TodoLAdaptor(private val listener: (TodoL, Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_TODO = 1

    private var todoList = listOf<TodoL>()
    private var todoFilteredList = listOf<TodoL>()

    fun setTodoList(todoList: List<TodoL>) {
        this.todoList = todoList
        todoFilteredList = todoList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val keywords = constraint.toString()
                if (keywords.isEmpty())
                    todoFilteredList = todoList
                else{
                    val filteredList = ArrayList<TodoL>()
                    for (todo in todoList) {
                        if (todo.toString().toLowerCase(Locale.ROOT).contains(keywords.toLowerCase(Locale.ROOT)))
                            filteredList.add(todo)
                    }
                    todoFilteredList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = todoFilteredList
                return  filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                todoFilteredList = results?.values as List<TodoL>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (todoFilteredList.isEmpty())
            VIEW_TYPE_EMPTY
        else
            VIEW_TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {
            VIEW_TYPE_TODO -> TodoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
            )
            VIEW_TYPE_EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            )
            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int = if (todoFilteredList.isEmpty()) 1 else todoFilteredList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_EMPTY -> {
                val emptyHolder = holder as EmptyViewHolder
                emptyHolder.bindItem()
            }
            VIEW_TYPE_TODO -> {
                val todoHolder = holder as TodoViewHolder
                todoHolder.bindItem(todoList[position], listener)
                val sortedList = todoFilteredList.sortedWith(
                    if(MainActivity.isSortByDateCreated)
                        compareBy({it.dateCreated}, {it.dateUpdated})
                    else{
                        compareBy({it.dueDate}, {it.dueTime})
                    })
                todoHolder.bindItem(sortedList[position], listener)
            }
        }
    }

    class TodoViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(todo: TodoL, listener: (TodoL, Int) -> Unit) {
            val parsedDateCreated = SimpleDateFormat("dd/MM/yy", Locale.US).parse(todo.dateCreated) as Date
            val dateCreated = Common.formatDate(parsedDateCreated, "dd MMM yyyy")

            val parsedDateUpdated = SimpleDateFormat("dd/MM/yy", Locale.US).parse(todo.dateCreated) as Date
            val dateUpdated = Common.formatDate(parsedDateUpdated, "dd MMM yyyy")

            val date = if (todo.dateUpdated != todo.dateCreated) "Updated at $dateUpdated" else "Created at $dateCreated"

            val parsedDate = SimpleDateFormat("dd/MM/yy", Locale.US).parse(todo.dueDate) as Date
            val dueDate = Common.formatDate(parsedDate, "dd MMM yyyy")

            val dueDateTime = "Due ${dueDate}, ${todo.dueTime}"

            itemView.tv_title.text = todo.title
            itemView.tv_note.text = todo.note
            itemView.tv_due_date.text = dueDateTime
            itemView.tv_date_created_updated.text = date

            itemView.setOnClickListener{
                listener(todo, layoutPosition)
            }
        }
    }

    class EmptyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(){
            itemView.tv_empty.text = "No data found"
        }
    }
}