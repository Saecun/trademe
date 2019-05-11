package nz.co.trademe.techtest.listed_item_detail

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.util.Networking
import nz.co.trademe.wrapper.TradeMeApi
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class ListedItemDetailViewModel(private val listingId: Long): ViewModel() {


    private var listingSubscriber : Disposable? = null

    fun getListingInfo(callback: ListedItemDetailFetchListener) {
        listingSubscriber?.dispose()


        listingSubscriber =
            TradeMeApi(RxJava2CallAdapterFactory.createAsync()).get()
                .getListing(listingId)
                .timeout(Networking.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listedItemDetail -> callback.onSuccess(listedItemDetail) },
                    { callback.onError(ListedItemDetailFetchListener.Error.UNKNOWN) }
                )

    }

    override fun onCleared() {
        super.onCleared()
        listingSubscriber?.dispose()
    }
}