package nz.co.trademe.techtest.listed_item_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListedItemDetailViewModelFactory(private val listingId: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListedItemDetailViewModel(listingId) as T
    }
}