@file:Suppress("DEPRECATION")

package com.battlelancer.seriesguide.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.settings.WidgetSettings
import com.battlelancer.seriesguide.utils.AtPosition.Companion.atPosition
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class R9 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java, false, false)
//    var mActivityTestRule = ActivityTestRule(ShowsActivity::class.java)

    @Before
    fun setUp() {
        GlobalUtils.setUp()
    }

    @After
    fun tearDown() {
        mActivityTestRule.activity.applicationContext.deleteSharedPreferences(WidgetSettings.SETTINGS_FILE)
    }

    @Test
    fun scenario_1() {
        sleep(1000)

        mActivityTestRule.launchActivity(null)

        sleep(1000)

        val floatingActionButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.buttonShowsAdd),
                ViewMatchers.withContentDescription("Add show"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rootLayoutShows),
                        childAtPosition(
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

        val actionMenuItemView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.menu_action_shows_search_change_language),
                ViewMatchers.withContentDescription("Preferred content language"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.sgToolbar),
                        2
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        actionMenuItemView.perform(ViewActions.click())

        val appCompatCheckedTextView = Espresso.onData(Matchers.anything())
            .inAdapterView(
                Matchers.allOf(
                    ViewMatchers.withId(R.id.select_dialog_listview),
                    childAtPosition(
                        ViewMatchers.withId(R.id.contentPanel),
                        0
                    )
                )
            )
            .atPosition(2)
        appCompatCheckedTextView.perform(ViewActions.click())

        sleep(2000)

        onView(withId(R.id.recyclerViewShowsDiscover))
            .check(matches(atPosition(2, hasDescendant(withSubstring("(da)")))));

        actionMenuItemView.perform(ViewActions.click())

       appCompatCheckedTextView
            .atPosition(0)
        appCompatCheckedTextView.perform(ViewActions.click())
    }
}