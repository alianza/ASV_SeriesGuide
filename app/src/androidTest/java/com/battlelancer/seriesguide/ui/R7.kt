@file:Suppress("DEPRECATION")

package com.battlelancer.seriesguide.ui

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.battlelancer.seriesguide.utils.GlobalUtils
import com.battlelancer.seriesguide.utils.SleepIdlingHelper.Companion.sleep
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class R7 {

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

        val startTime1 = System.currentTimeMillis()
        mActivityTestRule.launchActivity(null)
        val endTime1 = System.currentTimeMillis()

        val launchtime = endTime1 - startTime1

        println("App launch time: $launchtime ms")

        assert(launchtime < 2000)
    }
}