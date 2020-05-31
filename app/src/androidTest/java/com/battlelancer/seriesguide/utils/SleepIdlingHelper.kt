package com.battlelancer.seriesguide.utils

/**
 * Helper class for handling sleep statements for idling during espresso tests
 *
 */

class SleepIdlingHelper {

companion object {
    fun sleep(ms: Long) {
        // Sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://developer.android.com/training/testing/espresso/idling-resource#kotlin
        try {
            Thread.sleep(ms)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
}