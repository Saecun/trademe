package nz.co.trademe.techtest.listing_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.listing_detail_preview.view.*
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.category_list.ListingsAdapterListener
import nz.co.trademe.wrapper.models.SearchListing


class ListingsAdapter(private val listener: ListingsAdapterListener,
                      private val listings:ArrayList<SearchListing> = ArrayList())
    : RecyclerView.Adapter<ListingsAdapter.SearchListingViewHolder>() {

    init {
        setHasStableIds(true)
    }

    var selectedItemPosition : Int? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchListingViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.listing_detail_preview, p0, false)
        return SearchListingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(p0: SearchListingViewHolder, p1: Int) {
        p0.bind(listings[p1])
    }

    fun set(listings:List<SearchListing>) {
        this.listings.clear()
        this.listings.addAll(listings)
        notifyDataSetChanged()
    }

    fun resetSelection() {
        if( selectedItemPosition != null ) {
            var oldSelection = selectedItemPosition as Int
            selectedItemPosition = null
            notifyItemChanged(oldSelection)
        }
    }

    inner class SearchListingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(listing:SearchListing) {
            itemView.listingTitle.text = listing.title

            val myOptions = RequestOptions().placeholder(nz.co.trademe.techtest.R.drawable.loading)
            Glide.with(itemView.context.applicationContext).load(listing.pictureHref).apply(myOptions).into(itemView.listingImage)


            itemView.isSelected = adapterPosition == selectedItemPosition

            itemView.setOnClickListener { _ ->
                listener.onSelected(listing.listingId)
                selectedItemPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}