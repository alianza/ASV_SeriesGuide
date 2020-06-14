@file:Suppress("DEPRECATION")

package com.battlelancer.seriesguide.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random.Default.nextInt

/**
 * Tests for Requirement-ID: 1
 *
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class R5 {

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

        val episodes = nextInt(1, 6)

        for (x in 0 until episodes) {
            val materialButton2 = onView(
                allOf(
                    withId(R.id.buttonEpisodeWatched),
                    withText("Set watched"),
                    withContentDescription("Set watched"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.include_buttons),
                            0
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            materialButton2.perform(click())
            sleep(250)
        }

        val appCompatImageButton2 = onView(
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
        appCompatImageButton2.perform(click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_item_stats), withContentDescription("Statistics"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigation),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        sleep(1000)

        val textView = onView(
            allOf(
                withId(R.id.textViewStatsEpisodesWatched), withSubstring("WATCHED"),
                isDisplayed()
            )
        )
        textView.check(matches(withText("$episodes WATCHED")))
    }
}
