package com.battlelancer.seriesguide.util.tasks;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_USER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.SEASON;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.buildEpisodeUri;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.buildEpisodeWithShowUri;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.REF_SHOW_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.uwetrottmann.trakt5.entities.ShowIds;
import com.uwetrottmann.trakt5.entities.SyncEpisode;
import com.uwetrottmann.trakt5.entities.SyncItems;
import com.uwetrottmann.trakt5.entities.SyncSeason;
import com.uwetrottmann.trakt5.entities.SyncShow;
import com.uwetrottmann.trakt5.enums.Rating;

public class RateEpisodeTask extends BaseRateItemTask {

    private final int episodeTvdbId;

    /**
     * Stores the rating for the given episode in the database and sends it to trakt.
     */
    public RateEpisodeTask(Context context, Rating rating, int episodeTvdbId) {
        super(context, rating);
        this.episodeTvdbId = episodeTvdbId;
    }

    @NonNull
    @Override
    protected String getTraktAction() {
        return "rate episode";
    }

    @Nullable
    @Override
    protected SyncItems buildTraktSyncItems() {
        int season = -1;
        int episode = -1;
        int showTvdbId = -1;
        Cursor query = getContext().getContentResolver()
                .query(buildEpisodeUri(episodeTvdbId),
                        new String[] {
                                SEASON,
                                NUMBER,
                                REF_SHOW_ID }, null, null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                season = query.getInt(0);
                episode = query.getInt(1);
                showTvdbId = query.getInt(2);
            }
            query.close();
        }

        if (season == -1 || episode == -1 || showTvdbId == -1) {
            return null;
        }

        return new SyncItems()
                .shows(new SyncShow().id(ShowIds.tvdb(showTvdbId))
                        .seasons(new SyncSeason().number(season)
                                .episodes(new SyncEpisode().number(episode)
                                        .rating(getRating()))));
    }

    @Override
    protected boolean doDatabaseUpdate() {
        ContentValues values = new ContentValues();
        values.put(RATING_USER, getRating().value);

        int rowsUpdated = getContext().getContentResolver()
                .update(buildEpisodeUri(episodeTvdbId), values, null,
                        null);

        // notify withshow uri as well (used by episode details view)
        getContext().getContentResolver()
                .notifyChange(buildEpisodeWithShowUri(episodeTvdbId),
                        null);

        return rowsUpdated > 0;
    }
}
