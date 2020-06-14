package com.battlelancer.seriesguide.ui

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.ui.shows.NowAdapter

class NowFragmentHelper(
            private val recyclerView: RecyclerView,
            private val adapter: NowAdapter,
            private val emptyView: TextView,
            private val snackbarText: TextView,
            private val snackbar: View,
            private val isLoadingRecentlyWatched: Boolean,
            private val isLoadingFriends: Boolean,
            private val owner: Fragment,
            private val swipeRefreshLayout: SwipeRefreshLayout
) {


    fun setMoviesNowFragmentAdapter() {
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                updateEmptyState()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                updateEmptyState()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                updateEmptyState()
            }
        })
        recyclerView.adapter = adapter
    }

    private fun updateEmptyState() {
        val isEmpty = adapter.itemCount == 0
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    fun destroyLoaderIfExists(loaderId: Int) {
        val loaderManager =
            LoaderManager.getInstance(owner)
        if (loaderManager.getLoader<Any>(loaderId) != null) {
            loaderManager.destroyLoader(loaderId)
        }
    }

    fun showError(errorText: String?) {
        val show = errorText != null
        if (show) {
            snackbarText.setText(errorText)
        }
        if (snackbar.getVisibility() == (if (show) View.VISIBLE else View.GONE)) {
            // already in desired state, avoid replaying animation
            return
        }
        snackbar.startAnimation(
            AnimationUtils.loadAnimation(
                snackbar.getContext(),
                if (show) R.anim.fade_in else R.anim.fade_out
            )
        )
        snackbar.setVisibility(if (show) View.VISIBLE else View.GONE)
    }

    /**
     * Show or hide the progress bar of the [SwipeRefreshLayout]
     * wrapping view.
     */
    fun showProgressBar(show: Boolean) {
        // only hide if everybody has finished loading
        if (!show && (isLoadingRecentlyWatched || isLoadingFriends)) {
            return
        }
        swipeRefreshLayout.setRefreshing(show)
    }
}