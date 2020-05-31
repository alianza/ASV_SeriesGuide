package com.battlelancer.seriesguide.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
import com.battlelancer.seriesguide.utils.AtPosition.Companion.atPosition
import com.battlelancer.seriesguide.utils.ChildAtPosition.Companion.childAtPosition
import com.battlelancer.seriesguide.utils.RecyclerViewItemCountAssertion
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import com.battlelancer.seriesguide.utils.globalUtils
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class R3 {

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
                withId(R.id.buttonShowsAdd),
                ViewMatchers.withContentDescription("Add show"),
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
                ViewMatchers.isDisplayed()
            )
        )
        floatingActionButton.perform(ViewActions.click())

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
                ViewMatchers.isDisplayed()
            )
        )
        appCompatAutoCompleteTextViewDiscover.perform(
            ViewActions.typeText("The"),
            ViewActions.closeSoftKeyboard()
        )

        sleep(2500)

        val appCompatAutoCompleteTextView1 = onView(
            allOf(
                withId(R.id.auto_complete_view_toolbar), withText("The"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.text_input_layout_toolbar),
                        0
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatAutoCompleteTextView1.perform(ViewActions.pressImeActionButton())

        onView(withId(R.id.recyclerViewShowsDiscover))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        sleep(1000)

        onView(
            allOf(
                withId(R.id.buttonPositive),
                withText("Add show"),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        sleep(2500)

        Espresso.pressBack()

        sleep(1000)

        globalUtils.closeFiltersNotice()

        val recyclerViewShows = onView(withId(R.id.recyclerViewShows))

        recyclerViewShows.check(matches(ViewMatchers.isDisplayed()))

        sleep(1000)

        globalUtils.closeFirstRunNotice()

        sleep(1000)

        recyclerViewShows.check(RecyclerViewItemCountAssertion.withItemCount(Matchers.equalTo(1)))

        sleep(1000)

//        recyclerViewShows.check(matches(atPosition(0, hasDescendant(withId(R.id.seriesname)))))

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.menu_search),
                ViewMatchers.withContentDescription("Search"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.sgToolbar),
                        2
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        actionMenuItemView.perform(ViewActions.click())

        sleep(1000)

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
                ViewMatchers.isDisplayed()
            )
        )
        appCompatAutoCompleteTextViewShows.perform(
            ViewActions.typeText("The"),
            ViewActions.closeSoftKeyboard()
        )

        sleep(2500)

        onData(allOf(withId(R.id.gridViewSearch), withContentDescription(R.string.searchresults))).atPosition(0).check(matches(hasDescendant(withSubstring("The"))))
    }
}