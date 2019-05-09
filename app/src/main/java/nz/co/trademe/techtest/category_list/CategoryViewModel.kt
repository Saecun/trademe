package nz.co.trademe.techtest.category_list

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.util.Networking
import nz.co.trademe.wrapper.TradeMeApi
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class CategoryViewModel: ViewModel() {

    private var categorySubscriber : Disposable? = null

    /**
     * Fetches the categories asynchronously, and returns the categories inside {@code callback}
     */
    fun getCategories(callback: CategoryFetchListener) {
        categorySubscriber?.dispose()

        categorySubscriber =
            TradeMeApi(RxJava2CallAdapterFactory.createAsync()).get()
                .getCategory(ROOT_CATEGORY_ID)
                .timeout(Networking.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { category -> callback.onSuccess(category.subcategories) },
                    { callback.onError(CategoryFetchListener.Error.UNKNOWN) })
    }

    override fun onCleared() {
        super.onCleared()
        categorySubscriber?.dispose()
    }

    companion object {
        private const val ROOT_CATEGORY_ID = "0"
    }
}