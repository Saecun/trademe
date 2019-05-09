package nz.co.trademe.techtest.listing_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListingsViewModelFactory(private val categoryId: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        return ListingsViewModel(categoryId) as T
    }
}