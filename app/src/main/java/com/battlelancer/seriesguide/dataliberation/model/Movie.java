package com.battlelancer.seriesguide.dataliberation.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.IMDB_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.IN_COLLECTION;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.IN_WATCHLIST;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.LAST_UPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.OVERVIEW;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.PLAYS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.POSTER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_TMDB;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_TRAKT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_VOTES_TMDB;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_VOTES_TRAKT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RELEASED_UTC_MS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RUNTIME_MIN;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TITLE_NOARTICLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TMDB_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.WATCHED;
import static com.battlelancer.seriesguide.util.DBUtils.trimLeadingArticle;

import android.content.ContentValues;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("tmdb_id")
    public int tmdbId;

    @SerializedName("imdb_id")
    public String imdbId;

    public String title;

    @SerializedName("released_utc_ms")
    public long releasedUtcMs;

    @SerializedName("runtime_min")
    public int runtimeMin;

    public String poster;

    public String overview;

    @SerializedName("in_collection")
    public boolean inCollection;

    @SerializedName("in_watchlist")
    public boolean inWatchlist;

    public boolean watched;

    @SerializedName("last_updated_ms")
    public long lastUpdatedMs;
    
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TMDB_ID, tmdbId);
        values.put(IMDB_ID, imdbId);
        values.put(TITLE, title);
        values.put(TITLE_NOARTICLE, trimLeadingArticle(title));
        values.put(RELEASED_UTC_MS, releasedUtcMs);
        values.put(RUNTIME_MIN, runtimeMin);
        values.put(POSTER, poster);
        values.put(IN_COLLECTION, inCollection ? 1 : 0);
        values.put(IN_WATCHLIST, inWatchlist ? 1 : 0);
        values.put(WATCHED, watched ? 1 : 0);
        values.put(LAST_UPDATED, lastUpdatedMs);
        // full dump values
        values.put(OVERVIEW, overview);
        // set default values
        values.put(PLAYS, 0);
        values.put(RATING_TMDB, 0);
        values.put(RATING_VOTES_TMDB, 0);
        values.put(RATING_TRAKT, 0);
        values.put(RATING_VOTES_TRAKT, 0);
        return values;
    }

}
