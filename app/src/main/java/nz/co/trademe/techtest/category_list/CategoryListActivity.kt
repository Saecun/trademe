package nz.co.trademe.techtest.category_list

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_category_listing.*
import nz.co.trademe.techtest.R
import nz.co.trademe.wrapper.models.Category
import java.util.*
import nz.co.trademe.techtest.listing_list.ListingsActivity

class CategoryListActivity : AppCompatActivity() {

    private var viewModel: CategoryViewModel? = null
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var listAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_listing)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.category_list)

        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        initList()
        fetchCategories()
    }

    override fun onResume() {
        super.onResume()
        listAdapter.resetSelection()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.category_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_refresh -> refreshCategories()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initList() {
        layoutManager = LinearLayoutManager(this)
        listAdapter = CategoryAdapter(object: CategoryAdapterListener {
            override fun onSelected(categoryId: String) {
                startActivity(ListingsActivity.createIntent(this@CategoryListActivity, categoryId))
            }
        })

        listings.layoutManager = layoutManager
        listings.setHasFixedSize(true)
        listAdapter.setHasStableIds(true)

        listings.adapter = listAdapter
    }

    private fun refreshCategories() {
        listAdapter.set(Collections.emptyList())
        listAdapter.resetSelection()
        fetchCategories()
    }

    private fun fetchCategories() {
        loading.visibility = View.VISIBLE
        viewModel?.getCategories(object: CategoryFetchListener {
            override fun onError(error: CategoryFetchListener.Error) {
                loading.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, R.string.category_list_unknown_error, Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(categoryList: List<Category>?) {
                loading.visibility = View.INVISIBLE

                if( categoryList == null || categoryList.isEmpty() ) {
                    Toast.makeText(applicationContext, R.string.category_list_no_results, Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    listAdapter.set(categoryList.orEmpty())
                }
            }
        })
    }
}
