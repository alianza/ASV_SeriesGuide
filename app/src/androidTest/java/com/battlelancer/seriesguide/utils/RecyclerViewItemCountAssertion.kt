package com.battlelancer.seriesguide.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert

/**
 * RecyclerView ItemCount Assertion class
 *
 * @property matcher Matcher to assert ItemCount with
 */

class RecyclerViewItemCountAssertion private constructor(private val matcher: Matcher<Int>) : ViewAssertion {
    @Throws(NoMatchingViewException::class)
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) { throw noViewFoundException }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        println("ItemCount: " + adapter!!.itemCount)
        MatcherAssert.assertThat(adapter.itemCount, matcher)
    }
    companion object {
        fun withItemCount(matcher: Matcher<Int>): RecyclerViewItemCountAssertion {
            return RecyclerViewItemCountAssertion(matcher)
        }
    }
}