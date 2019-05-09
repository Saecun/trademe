package nz.co.trademe.techtest.category_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_item.view.*
import nz.co.trademe.techtest.R
import nz.co.trademe.wrapper.models.Category

class CategoryAdapter(private val listener: CategoryAdapterListener,
                      private val categories:ArrayList<Category> = ArrayList())
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    init {
        setHasStableIds(true)
    }

    var selectedItemPosition : Int? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.category_item, p0, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(p0: CategoryViewHolder, p1: Int) {
        p0.bind(categories[p1])
    }

    fun set(categories:List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }

    fun resetSelection() {
        if( selectedItemPosition != null ) {
            var oldSelection = selectedItemPosition as Int
            selectedItemPosition = null
            notifyItemChanged(oldSelection)
        }
    }

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category:Category) {
            itemView.categoryTitle.text = category.name
            itemView.isSelected = adapterPosition == selectedItemPosition

            itemView.setOnClickListener { _ ->
                selectedItemPosition = adapterPosition
                notifyDataSetChanged()
                listener.onSelected(category.id)
            }
        }
    }
}