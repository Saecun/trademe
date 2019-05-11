package nz.co.trademe.techtest.listed_item_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_listing_details.listingIdText
import kotlinx.android.synthetic.main.activity_listing_details.listingTitle
import kotlinx.android.synthetic.main.activity_listing_details.listingImage
import kotlinx.android.synthetic.main.activity_listing_details.loading
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.listing_list.InsufficientInputException
import nz.co.trademe.wrapper.models.ListedItemDetail


class ListedItemDetailActivity: AppCompatActivity() {
    private var viewModel: ListedItemDetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_details)

        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.listing_details)

        val listingId = getListingIdFromIntent(intent)
        listingId ?: throw InsufficientInputException("Require ListingId from calling activity")

        viewModel = ViewModelProviders.of(this, ListedItemDetailViewModelFactory(listingId)).get(ListedItemDetailViewModel::class.java)

        fetchListingDetails()
    }

    private fun fetchListingDetails() {
        loading.visibility = View.VISIBLE
        viewModel?.getListingInfo(object: ListedItemDetailFetchListener {
            override fun onError(error: ListedItemDetailFetchListener.Error) {
                var errorString = when(error) {
                    ListedItemDetailFetchListener.Error.UNKNOWN -> { R.string.listing_details_unknown_error }
                }

                Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onSuccess(listedItemDetail: ListedItemDetail) {
                listingTitle.text = listedItemDetail.title
                listingIdText.text = getString(R.string.listing_details_id, listedItemDetail.listingId)

                val myOptions = RequestOptions().placeholder(nz.co.trademe.techtest.R.drawable.loading)
                val photoUrl = listedItemDetail.photos?.getOrNull(0)?.value?.thumbnail
                Glide.with(applicationContext).load(photoUrl).apply(myOptions).into(listingImage)
                loading.visibility = View.INVISIBLE
            }
        })
    }

    companion object {
        private var LISTING_ID = "LISTING_ID"

        fun createIntent(context: Context, listingId: Long) : Intent {
            return Intent(context, ListedItemDetailActivity::class.java)
                .putExtra(LISTING_ID, listingId.toString())
        }

        private fun getListingIdFromIntent(intent: Intent): Long? {
            return intent.getStringExtra(LISTING_ID)?.toLong()
        }
    }


}