package nz.co.trademe.techtest.listing_list

import nz.co.trademe.wrapper.models.SearchListing

interface ListingsFetchListener {
    enum class Error {
        UNKNOWN
    }

    fun onError(error: Error)

    fun onSuccess(searchListings: List<SearchListing>?)
}