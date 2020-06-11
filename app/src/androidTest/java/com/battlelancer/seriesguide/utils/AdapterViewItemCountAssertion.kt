package com.battlelancer.seriesguide.utils

import android.view.View
import android.widget.AdapterView
import android.widget.GridView
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

class AdapterViewItemCountAssertion private constructor(private val matcher: Matcher<Int>) : ViewAssertion {
    @Throws(NoMatchingViewException::class)
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) { throw noViewFoundException }
        val recyclerView = view as GridView
        val adapter = recyclerView.adapter
        println("ItemCount: " + adapter!!.count)
        MatcherAssert.assertThat(adapter.count, matcher)
    }
    companion object {
        fun withItemCount(matcher: Matcher<Int>): AdapterViewItemCountAssertion {
            return AdapterViewItemCountAssertion(matcher)
        }
    }
}