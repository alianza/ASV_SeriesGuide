package com.battlelancer.seriesguide.jobs;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs.CREATED_MS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs.EXTRAS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs.TYPE;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.battlelancer.seriesguide.jobs.episodes.JobAction;

public abstract class BaseJob {

    private final JobAction action;

    public BaseJob(JobAction action) {
        this.action = action;
    }

    protected boolean persistNetworkJob(Context context, @NonNull byte[] jobInfo) {
        ContentValues values = new ContentValues();
        values.put(TYPE, action.id);
        values.put(CREATED_MS, System.currentTimeMillis());
        values.put(EXTRAS, jobInfo);

        Uri insert = context.getContentResolver().insert(Jobs.CONTENT_URI, values);

        return insert != null;
    }
}
