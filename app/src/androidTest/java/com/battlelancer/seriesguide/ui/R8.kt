package com.battlelancer.seriesguide.ui

import android.content.Context
import android.net.wifi.WifiManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.utils.ChildAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class R8 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java, false, false)
//    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java)

    @Before
    fun setUp() {
        GlobalUtils.setUp()
        // Grant WIFI permissions
    }

    @Test
    fun scenario_1() {
        sleep(1000)

        mActivityTestRule.launchActivity(null)

        sleep(1000)

        val wifiManager: WifiManager = mActivityTestRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val floatingActionButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.buttonShowsAdd),
                ViewMatchers.withContentDescription("Add show"),
                ChildAtPosition.childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rootLayoutShows),
                        ChildAtPosition.childAtPosition(
                            ViewMatchers.withId(R.id.linearLayoutActivityShows),
                            0
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        floatingActionButton.perform(ViewActions.click())

        sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.recyclerViewShowsDiscover))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2,
                    ViewActions.click()
                )
            )

        sleep(1000)

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.buttonPositive),
                ViewMatchers.withText("Add show"),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        sleep(1000)

        val appCompatImageButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Navigate up"),
                ChildAtPosition.childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.sgToolbar),
                        ChildAtPosition.childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageButton.perform(ViewActions.click())

        wifiManager.disconnect()

        GlobalUtils.refreshApp(mActivityTestRule)

        sleep(1000)

        GlobalUtils.closeFiltersNotice()

        val recyclerViewShows = Espresso.onView(ViewMatchers.withId(R.id.recyclerViewShows))

//        doRepeatedCheck(recyclerViewShows, matches(isDisplayed()))

        recyclerViewShows.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        GlobalUtils.closeFirstRunNotice()

        sleep(1000)

        recyclerViewShows.check(RecyclerViewItemCountAssertion.withItemCount(Matchers.equalTo(1)))

        wifiManager.reconnect()
    }
}