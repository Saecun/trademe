package nz.co.trademe.techtest.category_list

import nz.co.trademe.wrapper.models.Category


interface CategoryFetchListener {
    enum class Error {
        UNKNOWN
    }

    fun onError(error: Error)

    fun onSuccess(categoryList: List<Category>?)
}