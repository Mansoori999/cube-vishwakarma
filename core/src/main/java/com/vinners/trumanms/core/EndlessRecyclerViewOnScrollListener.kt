package com.vinners.trumanms.core

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewOnScrollListener : RecyclerView.OnScrollListener {
    private var mCurrentPage = 0
    private var mPreviousTotalItemCount = 0
    private var mLoading = true
    private val mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
        sVisibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        mLayoutManager = layoutManager
        sVisibleThreshold *= layoutManager.spanCount
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        val visibleItemCount = mLayoutManager.childCount


        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = mLayoutManager.findLastVisibleItemPositions(null)
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)

        } else if (mLayoutManager is LinearLayoutManager) {

            lastVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
        } else if (mLayoutManager is GridLayoutManager) {

            lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition()
        }


        if (totalItemCount < mPreviousTotalItemCount) { // List was cleared
            mCurrentPage =
                STARTING_PAGE_INDEX
            mPreviousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                mLoading = true
            }
        }

        /**
         * If it’s still loading, we check to see if the DataSet count has
         * changed, if so we conclude it has finished loading and update the current page
         * number and total item count (+ 1 due to loading view being added).
         */
        if (mLoading && totalItemCount > mPreviousTotalItemCount + 1) {
            mLoading = false
            mPreviousTotalItemCount = totalItemCount
        }

        /**
         * If it isn’t currently loading, we check to see if we have breached
         * + the visibleThreshold and need to reload more data.
         */
        if (!mLoading &&
            totalItemCount % PAGE_SIZE == 0 &&
            lastVisibleItemPosition + visibleItemCount >= totalItemCount
            && lastVisibleItemPosition >= 0
        ) {
            mCurrentPage++
            onLoadMore(mCurrentPage, totalItemCount)
            mLoading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    companion object {

        private const val STARTING_PAGE_INDEX = 0

        /**
         * Low threshold to show the onLoad()/Spinner functionality.
         * If you are going to use this for a production app set a higher value
         * for better UX
         */
        private var sVisibleThreshold = 6


        private var PAGE_SIZE = 20
    }
}