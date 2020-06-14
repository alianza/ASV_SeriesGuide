package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.CERTIFICATION;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.GENRES;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.IMDB_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.IN_COLLECTION;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.IN_WATCHLIST;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.LAST_UPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.OVERVIEW;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.PLAYS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.POSTER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_TMDB;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_TRAKT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_USER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_VOTES_TMDB;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RATING_VOTES_TRAKT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RELEASED_UTC_MS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.RUNTIME_MIN;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TITLE_NOARTICLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TMDB_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TRAILER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.WATCHED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies._ID;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

/**
 * Note: ensure to use CONFLICT_REPLACE when inserting to mimic SQLite UNIQUE x ON CONFLICT REPLACE.
 */
@Entity(
        tableName = Tables.MOVIES,
        indices = {@Index(value = TMDB_ID, unique = true)}
)
public class SgMovie {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    public Integer id;

    @ColumnInfo(name = TMDB_ID)
    public int tmdbId;

    @ColumnInfo(name = IMDB_ID)
    public String imdbId;

    @ColumnInfo(name = TITLE)
    public String title;
    @ColumnInfo(name = TITLE_NOARTICLE)
    public String titleNoArticle;

    @ColumnInfo(name = POSTER)
    public String poster;
    @ColumnInfo(name = GENRES)
    public String genres;
    @ColumnInfo(name = OVERVIEW)
    public String overview;
    @ColumnInfo(name = RELEASED_UTC_MS)
    public Long releasedMs;
    @ColumnInfo(name = RUNTIME_MIN)
    public Integer runtimeMin = 0;
    @ColumnInfo(name = TRAILER)
    public String trailer;
    @ColumnInfo(name = CERTIFICATION)
    public String certification;

    @ColumnInfo(name = IN_COLLECTION)
    public Boolean inCollection = false;
    @ColumnInfo(name = IN_WATCHLIST)
    public Boolean inWatchlist = false;
    @ColumnInfo(name = PLAYS)
    public Integer plays = 0;
    @ColumnInfo(name = WATCHED)
    public Boolean watched = false;

    @ColumnInfo(name = RATING_TMDB)
    public Double ratingTmdb = 0.0;
    @ColumnInfo(name = RATING_VOTES_TMDB)
    public Integer ratingVotesTmdb = 0;
    @ColumnInfo(name = RATING_TRAKT)
    public Integer ratingTrakt = 0;
    @ColumnInfo(name = RATING_VOTES_TRAKT)
    public Integer ratingVotesTrakt = 0;
    @ColumnInfo(name = RATING_USER)
    public Integer ratingUser;

    @ColumnInfo(name = LAST_UPDATED)
    public Long lastUpdated;
}
