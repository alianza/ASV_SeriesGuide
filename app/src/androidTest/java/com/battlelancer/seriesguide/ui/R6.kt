package com.battlelancer.seriesguide.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.utils.AdapterViewItemCountAssertion
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.GlobalUtils.Companion.refreshApp
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class R6 {

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
                withId(R.id.buttonShowsAdd),
                withContentDescription("Add show"),
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

        val appCompatAutoCompleteTextViewDiscover = onView(
            allOf(
                withId(R.id.auto_complete_view_toolbar),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.text_input_layout_toolbar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextViewDiscover.perform(
            ViewActions.typeText("Spot"),
            ViewActions.closeSoftKeyboard()
        )

        sleep(1000)

        val appCompatAutoCompleteTextView1 = onView(
            allOf(
                withId(R.id.auto_complete_view_toolbar), withText("Spot"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.text_input_layout_toolbar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView1.perform(ViewActions.pressImeActionButton())

        onView(withId(R.id.recyclerViewShowsDiscover))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        sleep(1000)

        onView(
            allOf(
                withId(R.id.buttonPositive),
                withText("Add show"),
                isDisplayed()
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
                            ViewMatchers.withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        refreshApp(mActivityTestRule)

        sleep(1000)

        GlobalUtils.closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

        recyclerViewShows.check(matches(isDisplayed()))

        GlobalUtils.closeFirstRunNotice()

//        recyclerViewShows.check(RecyclerViewItemCountAssertion.withItemCount(Matchers.equalTo(1)))

//        recyclerViewShows.check(matches(atPosition(0, hasDescendant(withId(R.id.seriesname)))))

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.menu_search),
                withContentDescription("Search"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.sgToolbar),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val appCompatAutoCompleteTextViewShows = onView(
            allOf(
                withId(R.id.auto_complete_view_toolbar),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.text_input_layout_toolbar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextViewShows.perform(
            ViewActions.typeText("Spot"),
            ViewActions.closeSoftKeyboard()
        )

        sleep(2000)

//        onData(hasDescendant(withSubstring("Spot")))
//            .inAdapterView(allOf(withId(R.id.gridViewSearch), isDisplayed()))
//            .atPosition(0)

        onView(allOf(withId(R.id.gridViewSearch), isDisplayed())).check(AdapterViewItemCountAssertion.withItemCount(Matchers.greaterThanOrEqualTo(1)))
    }
}