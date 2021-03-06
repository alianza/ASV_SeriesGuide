
package com.battlelancer.seriesguide.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;
import androidx.preference.PreferenceManager;
import com.battlelancer.seriesguide.BuildConfig;
import com.battlelancer.seriesguide.provider.SeriesGuideContract;
import com.battlelancer.seriesguide.util.DBUtils;
import timber.log.Timber;

public class AppSettings {

    private AppSettings() {
        throw new IllegalStateException("Utility class");
    }

    public static final String KEY_VERSION = "oldversioncode";

    public static final String KEY_GOOGLEANALYTICS = "enableGAnalytics";

    /**
 * @deprecated ASV
 */

    @SuppressWarnings("unused")
    @Deprecated
    public static final String KEY_HAS_SEEN_NAV_DRAWER = "hasSeenNavDrawer";

    public static final String KEY_ASKED_FOR_FEEDBACK = "askedForFeedback";

    public static final String KEY_USER_DEBUG_MODE_ENBALED = "com.battlelancer.seriesguide.userDebugModeEnabled";

    /**
     * Returns the version code of the previously installed version. Is the current version on fresh
     * installs.
     */
    public static int getLastVersionCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int lastVersionCode = prefs.getInt(KEY_VERSION, -1);
        if (lastVersionCode == -1) {
            // set current version as default value
            lastVersionCode = BuildConfig.VERSION_CODE;
            prefs.edit().putInt(KEY_VERSION, lastVersionCode).apply();
        }

        return lastVersionCode;
    }

    public static boolean isGaEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_GOOGLEANALYTICS, true);
    }

    public static boolean shouldAskForFeedback(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean(KEY_ASKED_FOR_FEEDBACK, false)) {
            return false; // already asked for feedback
        }

        try {
            PackageInfo ourPackageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            boolean installedRecently = System.currentTimeMillis()
                    < ourPackageInfo.firstInstallTime + 30 * DateUtils.DAY_IN_MILLIS;
            if (installedRecently) {
                return false; // was only installed recently
            }
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Failed to find our package info.");
            return false; // failed to find our package
        }

        int showsCount = DBUtils.getCountOf(context.getContentResolver(),
                SeriesGuideContract.Shows.CONTENT_URI, null, null, -1);

        return showsCount >= 5; // only if 5+ shows are added
    }

    public static void setAskedForFeedback(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_ASKED_FOR_FEEDBACK, true)
                .apply();
    }

    /**
     * Returns if user-visible debug components should be enabled
     * (e.g. logging to logcat, debug views). Always true for debug builds.
     */
    public static boolean isUserDebugModeEnabled(Context context) {
        return BuildConfig.DEBUG || PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_USER_DEBUG_MODE_ENBALED, false);
    }
}
