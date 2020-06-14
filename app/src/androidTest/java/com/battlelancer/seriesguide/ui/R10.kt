@file:Suppress("DEPRECATION")

package com.battlelancer.seriesguide.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.settings.WidgetSettings
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class R10 {

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

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_item_more), ViewMatchers.withContentDescription("More"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigation),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(ViewActions.click())

        val materialButton = onView(
            allOf(
                withId(R.id.buttonSettings), ViewMatchers.withText("Settings"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view),
                childAtPosition(
                    withId(android.R.id.list_container),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                8,
                ViewActions.click()
            )
        )

        val appCompatCheckedTextView = Espresso.onData(Matchers.anything())
            .inAdapterView(
                allOf(
                    withId(R.id.select_dialog_listview),
                    childAtPosition(
                        withId(R.id.contentPanel),
                        0
                    )
                )
            )
            .atPosition(1)
        appCompatCheckedTextView.perform(ViewActions.click())

        val appCompatImageButton = onView(
            allOf(
                ViewMatchers.withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.sgToolbar),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(ViewActions.click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navigation_item_shows), ViewMatchers.withContentDescription("Shows"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigation),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(ViewActions.click())

        onView(
            allOf(withId(R.id.textViewTabStripItem), withText("SHOWS"))
        ).check(matches(hasTextColor(R.color.sg_color_primary_dark)))

        bottomNavigationItemView.perform(ViewActions.click())

        materialButton.perform(ViewActions.click())

        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                8,
                ViewActions.click()
            )
        )

        val appCompatCheckedTextView1 = Espresso.onData(Matchers.anything())
            .inAdapterView(
                allOf(
                    withId(R.id.select_dialog_listview),
                    childAtPosition(
                        withId(R.id.contentPanel),
                        0
                    )
                )
            )
            .atPosition(0)
        appCompatCheckedTextView1.perform(ViewActions.click())
    }
}