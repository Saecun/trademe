package nz.co.trademe.techtest.listing_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_listings.*
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.category_list.ListingsAdapterListener
import nz.co.trademe.techtest.util.RecyclerView
import nz.co.trademe.wrapper.models.SearchListing

class ListingsActivity: AppCompatActivity() {
    private var viewModel: ListingsViewModel? = null
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var listAdapter: ListingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listings)

        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.featured_listings)

        val categoryId = getCategoryIdFromIntent(intent)
        categoryId ?: throw InsufficientInputException("Require CategoryId from calling activity")

        viewModel = ViewModelProviders.of(this, ListingsViewModelFactory(categoryId)).get(ListingsViewModel::class.java)

        initList()

        fetchListings()
    }

    override fun onResume() {
        super.onResume()
        listAdapter.resetSelection()
    }

    private fun initList() {
        layoutManager = GridLayoutManager(this, RecyclerView.Utility.calculateNoOfColumns(this, 150f))
        layoutManager.isAutoMeasureEnabled
        listAdapter = ListingsAdapter(object: ListingsAdapterListener {
            override fun onSelected(listingId: Long) {
                System.out.println("Here")
            }
        })

        listings.layoutManager = layoutManager
        listings.setHasFixedSize(true)
        listAdapter.setHasStableIds(true)
        listings.adapter = listAdapter
    }

    private fun fetchListings() {
        loading.visibility = View.VISIBLE
        viewModel?.getFeaturedCategoryItems(object: ListingsFetchListener {
            override fun onError(error: ListingsFetchListener.Error) {
                loading.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, R.string.featured_listings_unknown_error, Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(searchListings: List<SearchListing>?) {
                if( searchListings == null || searchListings.isEmpty() ) {
                    Toast.makeText(applicationContext, R.string.featured_listings_no_results, Toast.LENGTH_SHORT).show()
                    finish()
                }

                listAdapter.set(searchListings.orEmpty())
                loading.visibility = View.INVISIBLE
            }
        })
    }

    companion object {
        private var CATEGORY_ID = "CATEGORY_ID"

        fun createIntent(context: Context, categoryId: String) : Intent {
            return Intent(context, ListingsActivity::class.java)
                .putExtra(CATEGORY_ID, categoryId)
        }

        private fun getCategoryIdFromIntent(intent:Intent): String? {
            return intent.getStringExtra(CATEGORY_ID)
        }
    }


}