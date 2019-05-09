package nz.co.trademe.techtest.category_list


interface CategoryAdapterListener {
    /**
     * Indicates when the user has selected a specifc category
     */
    fun onSelected(categoryId: String)
}