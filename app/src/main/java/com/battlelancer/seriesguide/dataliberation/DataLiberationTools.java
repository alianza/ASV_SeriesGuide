package com.battlelancer.seriesguide.dataliberation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.battlelancer.seriesguide.ui.shows.ShowTools;
import com.battlelancer.seriesguide.util.Utils;

public class DataLiberationTools {

    private DataLiberationTools() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Transform a string representation of {@link com.battlelancer.seriesguide.dataliberation.JsonExportTask.ShowStatusExport}
     * to a {@link ShowTools.Status} to be stored in the database.
     *
     * <p>Falls back to {@link ShowTools.Status#UNKNOWN}.
     */
    public static int encodeShowStatus(@Nullable String status) {
        if (status == null) {
            return ShowTools.Status.UNKNOWN;
        }
        switch (status) {
            case JsonExportTask.ShowStatusExport.UPCOMING:
                return ShowTools.Status.UPCOMING;
            case JsonExportTask.ShowStatusExport.CONTINUING:
                return ShowTools.Status.CONTINUING;
            case JsonExportTask.ShowStatusExport.ENDED:
                return ShowTools.Status.ENDED;
            default:
                return ShowTools.Status.UNKNOWN;
        }
    }

    /**
     * Transform an int representation of {@link ShowTools.Status}
     * to a {@link com.battlelancer.seriesguide.dataliberation.JsonExportTask.ShowStatusExport} to
     * be used for exporting data.
     *
     * @param encodedStatus Detection based on {@link ShowTools.Status}.
     */
    public static String decodeShowStatus(int encodedStatus) {
        switch (encodedStatus) {
            case ShowTools.Status.UPCOMING:
                return JsonExportTask.ShowStatusExport.UPCOMING;
            case ShowTools.Status.CONTINUING:
                return JsonExportTask.ShowStatusExport.CONTINUING;
            case ShowTools.Status.ENDED:
                return JsonExportTask.ShowStatusExport.ENDED;
            default:
                return JsonExportTask.ShowStatusExport.UNKNOWN;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void selectExportFile(Fragment fragment, String suggestedFileName,
            int requestCode) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // do NOT use the probably correct application/json as it would prevent selecting existing
        // backup files on Android, which re-classifies them as application/octet-stream.
        // also do NOT use application/octet-stream as it prevents selecting backup files from
        // providers where the correct application/json mime type is used, *sigh*
        // so, use application/* and let the provider decide
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE, suggestedFileName);

        Utils.tryStartActivityForResult(fragment, intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void selectImportFile(Fragment fragment, int requestCode) {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // json files might have mime type of "application/octet-stream"
        // but we are going to store them as "application/json"
        // so filter to show all application files
        intent.setType("application/*");

        Utils.tryStartActivityForResult(fragment, intent, requestCode);
    }
}
