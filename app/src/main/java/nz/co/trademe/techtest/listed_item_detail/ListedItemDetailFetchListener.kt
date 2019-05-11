package nz.co.trademe.techtest.listed_item_detail

import nz.co.trademe.wrapper.models.ListedItemDetail


interface ListedItemDetailFetchListener {
    enum class Error {
        UNKNOWN
    }

    fun onError(error: Error)

    fun onSuccess(listedItemDetail: ListedItemDetail)
}