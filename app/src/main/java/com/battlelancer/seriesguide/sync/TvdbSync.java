package com.battlelancer.seriesguide.sync;

import static android.provider.BaseColumns._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.CONTENT_URI;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.LASTUPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.RELEASE_WEEKDAY;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import androidx.annotation.Nullable;
import com.battlelancer.seriesguide.provider.SeriesGuideContract;
import com.battlelancer.seriesguide.provider.SgRoomDatabase;
import com.battlelancer.seriesguide.sync.SyncOptions.SyncType;
import com.battlelancer.seriesguide.thetvdbapi.TvdbException;
import com.battlelancer.seriesguide.thetvdbapi.TvdbTools;
import com.battlelancer.seriesguide.util.TimeTools;
import com.uwetrottmann.androidutils.AndroidUtils;
import dagger.Lazy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import timber.log.Timber;

public class TvdbSync {

    // Values based on the assumption that sync runs about every 24 hours
    private static final long UPDATE_THRESHOLD_WEEKLYS_MS = 6 * DateUtils.DAY_IN_MILLIS +
            12 * DateUtils.HOUR_IN_MILLIS;
    private static final long UPDATE_THRESHOLD_DAILYS_MS = DateUtils.DAY_IN_MILLIS
            + 12 * DateUtils.HOUR_IN_MILLIS;

    private final SyncType syncType;
    private final int singleShowTvdbId;
    private boolean hasUpdatedShows;

    public TvdbSync(SyncType syncType, int singleShowTvdbId) {
        this.syncType = syncType;
        this.singleShowTvdbId = singleShowTvdbId;
    }

    /**
     * Update shows based on the sync type.
     */
    @SuppressLint("TimberExceptionLogging")
    @Nullable
    public SgSyncAdapter.UpdateResult sync(
            Context context,
            ContentResolver resolver,
            Lazy<TvdbTools> tvdbTools,
            long currentTime,
            SyncProgress progress
    ) {
        hasUpdatedShows = false;

        int[] showsToUpdate = getShowsToUpdate(context, resolver, currentTime);

        Timber.d("Updating %d show(s)...", Objects.requireNonNull(showsToUpdate).length);

        // from here on we need more sophisticated abort handling, so keep track of errors
        SgSyncAdapter.UpdateResult resultCode = SgSyncAdapter.UpdateResult.SUCCESS;

        // loop through shows and download latest data from TVDb
        int consecutiveTimeouts = 0;
        for (int i = 0; i < showsToUpdate.length; i++) {
            int showTvdbId = showsToUpdate[i];

            // stop sync if connectivity is lost
            if (!AndroidUtils.isNetworkConnected(context)) {
                resultCode = SgSyncAdapter.UpdateResult.INCOMPLETE;
                break;
            }

            try {
                tvdbTools.get().updateShow(showTvdbId);
                hasUpdatedShows = true;

                // make sure other loaders (activity, overview, details) are notified
                resolver.notifyChange(SeriesGuideContract.Episodes.CONTENT_URI_WITHSHOW, null);
            } catch (TvdbException e) {
                // failed, continue with other shows
                resultCode = SgSyncAdapter.UpdateResult.INCOMPLETE;

                String showTitle = SgRoomDatabase.getInstance(context)
                        .showHelper()
                        .getShowTitle(showTvdbId);
                String message = String
                        .format("Failed to update show ('%s', TVDB id %s).", showTitle, showTvdbId);
                if (e.itemDoesNotExist()) {
                    message += " It no longer exists.";
                }
                progress.setImportantErrorIfNone(message);
                Timber.e(e, message);

                Throwable cause = e.getCause();
                if (cause instanceof SocketTimeoutException) {
                    consecutiveTimeouts++;
                } else if (consecutiveTimeouts > 0) {
                    consecutiveTimeouts--;
                }
                // skip after multiple consecutive timeouts (around 3 * 15/20 seconds)
                if (consecutiveTimeouts == 3) {
                    Timber.e("Connection unstable, give up.");
                    return resultCode;
                }
            }
        }

        return resultCode;
    }

    /**
     * Returns an array of show ids to update.
     */
    @Nullable
    private int[] getShowsToUpdate(Context context, ContentResolver resolver, long currentTime) {
        switch (syncType) {
            case SINGLE:
                int showTvdbId = singleShowTvdbId;
                if (showTvdbId == 0) {
                    Timber.e("Syncing...ABORT_INVALID_SHOW_TVDB_ID");
                    return new int[0];
                }
                return new int[]{showTvdbId};
            case FULL:
                // get all show IDs for a full update
                final Cursor showsQuery = resolver.query(
                        CONTENT_URI,
                        new String[]{
                                _ID
                        }, null, null, null
                );
                if (showsQuery == null) {
                    Timber.e("Syncing...ABORT_SHOW_QUERY_FAILED");
                    return new int[0];
                }

                int[] showIds = new int[showsQuery.getCount()];
                int i = 0;
                while (showsQuery.moveToNext()) {
                    showIds[i] = showsQuery.getInt(0);
                    i++;
                }
                showsQuery.close();
                return showIds;
            case DELTA:
                return getShowsToDeltaUpdate(context, resolver, currentTime);
            default:
                throw new IllegalArgumentException("Sync type " + syncType + " is not supported.");
        }
    }

    /**
     * Return list of show TVDb ids that have not been updated for a certain time.
     */
    @Nullable
    private int[] getShowsToDeltaUpdate(Context context, ContentResolver resolver,
            long currentTime) {
        // get existing show ids
        final Cursor shows = resolver
                .query(CONTENT_URI, new String[]{
                        _ID, LASTUPDATED,
                        RELEASE_WEEKDAY
                }, null, null, null);
        if (shows == null) {
            Timber.e("Syncing...ABORT_SHOW_QUERY_FAILED");
            return new int[0];
        }

        final List<Integer> updatableShowIds = new ArrayList<>();
        while (shows.moveToNext()) {
            boolean isDailyShow = shows.getInt(2) == TimeTools.RELEASE_WEEKDAY_DAILY;
            long lastUpdatedTime = shows.getLong(1);
            // update daily shows more frequently than weekly shows
            if (currentTime - lastUpdatedTime >
                    (isDailyShow ? UPDATE_THRESHOLD_DAILYS_MS : UPDATE_THRESHOLD_WEEKLYS_MS)) {
                // add shows that are due for updating
                updatableShowIds.add(shows.getInt(0));
            }
        }

        shows.close();

        // copy to int array
        int[] showTvdbIds = new int[updatableShowIds.size()];
        for (int i = 0; i < updatableShowIds.size(); i++) {
            showTvdbIds[i] = updatableShowIds.get(i);
        }
        return showTvdbIds;
    }

    public boolean isSyncMultiple() {
        return syncType == SyncType.DELTA || syncType == SyncType.FULL;
    }

    public boolean hasUpdatedShows() {
        return hasUpdatedShows;
    }
}
