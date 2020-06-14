
package com.battlelancer.seriesguide.dataliberation.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.ABSOLUTE_NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.COLLECTED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.DIRECTORS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.DVDNUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.FIRSTAIREDMS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.GUESTSTARS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.IMAGE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.IMDBID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.LAST_EDITED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.LAST_UPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.OVERVIEW;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_GLOBAL;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_USER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_VOTES;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.SEASON;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.WATCHED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.WRITERS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.SeasonsColumns.REF_SEASON_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.REF_SHOW_ID;

import android.content.ContentValues;
import com.battlelancer.seriesguide.ui.episodes.EpisodeFlags;
import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("tvdb_id")
    public int tvdbId;

    public int episodeItem;

    @SerializedName("episode_absolute")
    public int episodeAbsolute;

    public String title;

    @SerializedName("first_aired")
    public long firstAired;

    public boolean watched;

    public boolean skipped;

    public boolean collected;

    @SerializedName("imdb_id")
    public String imdbId;

    /*
     * Full dump only follows.
     */

    @SerializedName("episode_dvd")
    public double episodeDvd;

    public String overview;

    public String image;

    public String writers;

    public String gueststars;

    public String directors;

    public double rating;
    public int ratingVotes;
    public int ratingUser;

    @SerializedName("last_edited")
    public long lastEdited;

    public ContentValues toContentValues(int showTvdbId, int seasonTvdbId, int seasonNumber) {
        ContentValues values = new ContentValues();
        values.put(_ID, tvdbId);

        values.put(TITLE, title != null ? title : "");
        values.put(OVERVIEW, overview);
        values.put(NUMBER, episodeItem >= 0 ? episodeItem : 0);
        values.put(SEASON, seasonNumber);
        values.put(DVDNUMBER, episodeDvd >= 0 ? episodeDvd : 0);

        values.put(REF_SHOW_ID, showTvdbId);
        values.put(REF_SEASON_ID, seasonTvdbId);

        // watched/skipped represented internally in watched flag
        values.put(WATCHED, skipped
                ? EpisodeFlags.SKIPPED : watched
                ? EpisodeFlags.WATCHED : EpisodeFlags.UNWATCHED);

        values.put(DIRECTORS, directors != null ? directors : "");
        values.put(GUESTSTARS, gueststars != null ? gueststars : "");
        values.put(WRITERS, writers != null ? writers : "");
        values.put(IMAGE, image != null ? image : "");

        values.put(FIRSTAIREDMS, firstAired);
        values.put(COLLECTED, collected ? 1 : 0);

        values.put(RATING_GLOBAL, (rating >= 0 && rating <= 10) ? rating : 0);
        values.put(RATING_VOTES, ratingVotes >= 0 ? ratingVotes : 0);
        values.put(RATING_USER,
                (ratingUser >= 0 && ratingUser <= 10) ? ratingUser : 0);

        values.put(IMDBID, imdbId != null ? imdbId : "");
        values.put(LAST_EDITED, lastEdited);
        values.put(ABSOLUTE_NUMBER, episodeAbsolute >= 0 ? episodeAbsolute : 0);

        // set default values
        values.put(LAST_UPDATED, 0);

        return values;
    }
}
