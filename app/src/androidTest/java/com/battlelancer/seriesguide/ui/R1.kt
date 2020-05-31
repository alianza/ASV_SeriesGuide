package com.battlelancer.seriesguide.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.provider.SeriesGuideContract
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import com.battlelancer.seriesguide.utils.globalUtils
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for Requirement-ID: 1
 *
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class R1 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java, false, false)

    @Before
    fun setUp() {
        globalUtils.setUp()
    }

    @Test
    fun scenario_1() {
        sleep(1000)

        mActivityTestRule.launchActivity(null)

        sleep(1000)

        val floatingActionButton = onView(
            allOf(
                withId(R.id.buttonShowsAdd), withContentDescription("Add show"),
                childAtPosition(
                    allOf(
                        withId(R.id.rootLayoutShows),
                        childAtPosition(
                            withId(R.id.linearLayoutActivityShows),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        sleep(2500)

        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerViewShowsDiscover),
                childAtPosition(
                    allOf(
                        withId(R.id.constraintLayoutShowsDiscover),
                        childAtPosition(
                            withId(R.id.swipeRefreshLayoutShowsDiscover),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

//        onView(withId(R.id.recyclerViewShowsDiscover)).check(withItemCount(21));
        onView(withId(R.id.recyclerViewShowsDiscover)).check(withItemCount(Matchers.greaterThan(1)))
    }

    @Test
    fun scenario_2() {
        sleep(1000)

        mActivityTestRule.launchActivity(null)

        sleep(1000)

        val overflowMenuButton = onView(
            allOf(
                withContentDescription("More options"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.sgToolbar),
                        2
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        overflowMenuButton.perform(click())

        sleep(1000)

        val materialTextView = onView(
            allOf(
                withId(R.id.title), withText("Add show"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        sleep(2500)

        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerViewShowsDiscover),
                childAtPosition(
                    allOf(
                        withId(R.id.constraintLayoutShowsDiscover),
                        childAtPosition(
                            withId(R.id.swipeRefreshLayoutShowsDiscover),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        onView(withId(R.id.recyclerViewShowsDiscover))
            .check(withItemCount(Matchers.greaterThan(1)))
    }
}
