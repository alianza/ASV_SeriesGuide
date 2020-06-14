@file:Suppress("DEPRECATION")

package com.battlelancer.seriesguide.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
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
class R4 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java, false, false)
//    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java)

    @Before
    fun setUp() {
        GlobalUtils.setUp()
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

        onView(withId(R.id.recyclerViewShowsDiscover))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click())
            )

        sleep(1000)

        onView(
            allOf(
                withId(R.id.buttonPositive), withText("Add show"), isDisplayed()
            )
        ).perform(click())

        sleep(1000)

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.sgToolbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
//
//        sleep(5000)

        GlobalUtils.refreshApp(mActivityTestRule)

        sleep(1000)

        GlobalUtils.closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

//        doRepeatedCheck(recyclerViewShows, matches(isDisplayed()))

        recyclerViewShows.check(matches(isDisplayed()))

        GlobalUtils.closeFirstRunNotice()

        recyclerViewShows
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.imageButtonFavorite), withContentDescription("Add to favorites"),
                childAtPosition(
                    allOf(
                        withId(R.id.containerOverviewShow),
                        childAtPosition(
                            withId(R.id.overview_container),
                            0
                        )
                    ),
                    1
                )
            )
        )
        appCompatImageButton3.perform(ViewActions.scrollTo(), click())

        val appCompatImageButton4 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.sgToolbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.menu_action_shows_filter), withContentDescription("Filter shows"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.sgToolbar),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val filterBox = onView(
            allOf(
                withId(R.id.checkbox_shows_filter_favorites),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.viewPagerShowsDistillation),
                        0
                    ),
                    3
                )
            )
        )
        filterBox.perform(ViewActions.scrollTo(), click())

        Espresso.pressBack()

        sleep(1000)

        recyclerViewShows.check(withItemCount(Matchers.equalTo(1)))

//        doRepeatedCheck(recyclerViewShows, withItemCount(Matchers.equalTo(1)))
    }
}
