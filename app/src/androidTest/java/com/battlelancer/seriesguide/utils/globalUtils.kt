package com.battlelancer.seriesguide.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.battlelancer.seriesguide.R
import com.battlelancer.seriesguide.provider.SeriesGuideContract
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase
import org.hamcrest.Matchers
import org.junit.Before

class globalUtils {
    companion object {
        @Before
        fun setUp() {
            println("Setup!")
            // delete the database and close the database helper inside the provider
            // to ensure a clean state for the tests
            val context =
                ApplicationProvider.getApplicationContext<Context>()
            context.deleteDatabase(SeriesGuideDatabase.DATABASE_NAME)
            context.contentResolver.query(
                SeriesGuideContract.Shows.CONTENT_URI_CLOSE,
                null, null, null, null
            )
        }

        fun closeFirstRunNotice() {
            try {
                val dismissFirstRunViewButton =
                    Espresso.onView(
                        Matchers.allOf(
                            ViewMatchers.withId(R.id.buttonDismiss),
                            ViewMatchers.withContentDescription(R.string.dismiss)
                        )
                    )
                dismissFirstRunViewButton.perform(ViewActions.click())
                SleepIdlingHelper.sleep(1000)
            } catch (e: NoMatchingViewException) {
                println("No first run dialog")
            }
        }

        fun closeFiltersNotice() {
            try {
                val removeAllFiltersButton =
                    Espresso.onView(
                        Matchers.allOf(
                            ViewMatchers.withId(R.id.emptyViewShowsFilter),
                            ViewMatchers.withText(R.string.empty_filter)
                        )
                    )
                removeAllFiltersButton.perform(ViewActions.click())
                SleepIdlingHelper.sleep(1000)
            } catch (e: PerformException) {
                println("No filters set")
            }
        }
    }
}