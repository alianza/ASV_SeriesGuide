package com.battlelancer.seriesguide.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.provider.SeriesGuideContract
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import com.battlelancer.seriesguide.utils.globalUtils
import com.battlelancer.seriesguide.utils.globalUtils.Companion.closeFiltersNotice
import com.battlelancer.seriesguide.utils.globalUtils.Companion.closeFirstRunNotice
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

        sleep(2500)

        pressBack()

        sleep(5000)

        closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

        recyclerViewShows.check(matches(isDisplayed()))

        sleep(1000)

        closeFirstRunNotice()

        sleep(1000)

        recyclerViewShows.check(withItemCount(Matchers.equalTo(1)))
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

        sleep(2500)

        pressBack()

        sleep(5000)

        closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

        recyclerViewShows.check(matches(isDisplayed()))

        sleep(1000)

        closeFirstRunNotice()

        sleep(1000)

        recyclerViewShows.check(withItemCount(Matchers.equalTo(1)))
    }
}
