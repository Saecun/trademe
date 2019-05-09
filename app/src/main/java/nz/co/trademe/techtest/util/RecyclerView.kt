package nz.co.trademe.techtest.util

import android.content.Context


class RecyclerView {
    object Utility {
        /**
         * Determines the number of columns for a GridLayoutManager given a desired column width of
         * {@code columnWidthDp}
         *
         * {@see https://stackoverflow.com/a/38472370}
         *
         * @return number of columns
         */
        fun calculateNoOfColumns(context: Context, columnWidthDp: Float): Int {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
    }
}