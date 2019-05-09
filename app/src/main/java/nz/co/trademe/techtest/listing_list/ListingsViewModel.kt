package nz.co.trademe.techtest.listing_list

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.util.Networking
import nz.co.trademe.wrapper.TradeMeApi
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


class ListingsViewModel(private val categoryId: String): ViewModel() {


    private var searchSubscriber : Disposable? = null

    fun getFeaturedCategoryItems(callback: ListingsFetchListener) {
        searchSubscriber?.dispose()

        searchSubscriber =
            TradeMeApi(RxJava2CallAdapterFactory.createAsync()).get()
                .generalSearch(getFeaturedFilters())
                .timeout(Networking.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { searchCollection -> callback.onSuccess(searchCollection.list) },
                    { callback.onError(ListingsFetchListener.Error.UNKNOWN) }
                )
    }

    private fun getFeaturedFilters(): Map<String,String> {
        return mapOf(
            FILTER_KEY_CATEGORY to categoryId,
            FILTER_KEY_EXPIRED to "false",
            FILTER_KEY_ROWS to MAX_LISTINGS_TO_FETCH.toString(),
            FILTER_KEY_SORT_ORDER to "FeaturedFirst")
    }

    override fun onCleared() {
        super.onCleared()
        searchSubscriber?.dispose()
    }

    companion object {
        private const val MAX_LISTINGS_TO_FETCH = 20
        private const val FILTER_KEY_CATEGORY = "category"
        private const val FILTER_KEY_EXPIRED = "expired"
        private const val FILTER_KEY_ROWS = "rows"
        private const val FILTER_KEY_SORT_ORDER = "sort_order"
    }
}