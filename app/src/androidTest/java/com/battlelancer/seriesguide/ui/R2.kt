@file:Suppress("DEPRECATION")

package com.battlelancer.seriesguide.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.GlobalUtils.Companion.closeFiltersNotice
import com.battlelancer.seriesguide.utils.GlobalUtils.Companion.closeFirstRunNotice
import com.battlelancer.seriesguide.utils.GlobalUtils.Companion.refreshApp
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class R2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java, false, false)
//    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java)

    @Before
    open fun setUp() {
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

        sleep(1000)

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
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
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
        refreshApp(mActivityTestRule)

        sleep(1000)

        closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

//        doRepeatedCheck(recyclerViewShows, matches(isDisplayed()))

        recyclerViewShows.check(matches(isDisplayed()))

        closeFirstRunNotice()

        sleep(1000)

        recyclerViewShows.check(withItemCount(Matchers.equalTo(1)))

//        doRepeatedCheck(recyclerViewShows,  withItemCount(Matchers.equalTo(1)))

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

        sleep(1000)

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
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

//        sleep(5000)

        refreshApp(mActivityTestRule)

        sleep(1000)

        closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

//        doRepeatedCheck(recyclerViewShows, matches(isDisplayed()))

        recyclerViewShows.check(matches(isDisplayed()))

        closeFirstRunNotice()

        sleep(1000)

        recyclerViewShows.check(withItemCount(Matchers.equalTo(1)))

//        doRepeatedCheck(recyclerViewShows,  withItemCount(Matchers.equalTo(1)))
    }
}
