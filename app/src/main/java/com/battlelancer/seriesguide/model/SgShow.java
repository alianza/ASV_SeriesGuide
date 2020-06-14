package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.CONTENTRATING;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.FAVORITE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.FIRST_RELEASE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.GENRES;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.HEXAGON_MERGE_COMPLETE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.HIDDEN;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.IMDBID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.LANGUAGE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.LASTEDIT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.LASTUPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.LASTWATCHEDID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.LASTWATCHED_MS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.NETWORK;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.NEXTAIRDATEMS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.NEXTEPISODE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.NEXTTEXT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.NOTIFY;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.OVERVIEW;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.POSTER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.POSTER_SMALL;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RATING_GLOBAL;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RATING_USER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RATING_VOTES;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RELEASE_COUNTRY;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RELEASE_TIME;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RELEASE_TIMEZONE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RELEASE_WEEKDAY;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.RUNTIME;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.SLUG;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.STATUS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.TRAKT_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.UNWATCHED_COUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.TITLE_NOARTICLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;
import com.battlelancer.seriesguide.util.DBUtils;

@Entity(tableName = Tables.SHOWS)
public class SgShow {

    @PrimaryKey
    @ColumnInfo(name = _ID)
    public int tvdbId;

    @ColumnInfo(name = SLUG)
    public String slug = "";

    /**
     * Ensure this is NOT null (enforced through database constraint).
     */
    @ColumnInfo(name = TITLE)
    @NonNull
    public String title = "";

    /**
     * The title without any articles (e.g. 'the' or 'an'). Added with db version 33.
     */
    @ColumnInfo(name = TITLE_NOARTICLE)
    public String titleNoArticle;

    @ColumnInfo(name = OVERVIEW)
    public String overview = "";

    /**
     * Local release time. Encoded as integer (hhmm).
     *
     * <pre>
     * Example: 2035
     * Default: -1
     * </pre>
     */
    @ColumnInfo(name = RELEASE_TIME)
    public Integer releaseTime;
    /**
     * Local release week day. Encoded as integer.
     * <pre>
     * Range:   1-7
     * Daily:   0
     * Default: -1
     * </pre>
     */
    @ColumnInfo(name = RELEASE_WEEKDAY)
    public Integer releaseWeekDay;
    @ColumnInfo(name = RELEASE_COUNTRY)
    public String releaseCountry;
    @ColumnInfo(name = RELEASE_TIMEZONE)
    public String releaseTimeZone;

    @ColumnInfo(name = FIRST_RELEASE)
    public String firstRelease;

    @ColumnInfo(name = GENRES)
    public String genres = "";
    @ColumnInfo(name = NETWORK)
    public String network = "";

    @ColumnInfo(name = RATING_GLOBAL)
    public Double ratingGlobal;
    @ColumnInfo(name = RATING_VOTES)
    public Integer ratingVotes;
    @ColumnInfo(name = RATING_USER)
    public Integer ratingUser;

    @ColumnInfo(name = RUNTIME)
    public String runtime = "";
    @ColumnInfo(name = STATUS)
    public String status = "";
    @ColumnInfo(name = CONTENTRATING)
    public String contentRating = "";

    @ColumnInfo(name = NEXTEPISODE)
    public String nextEpisode = "";

    @ColumnInfo(name = POSTER)
    public String poster = "";

    @ColumnInfo(name = POSTER_SMALL)
    public String posterSmall = "";

    @ColumnInfo(name = NEXTAIRDATEMS)
    public Long nextAirdateMs;
    @ColumnInfo(name = NEXTTEXT)
    public String nextText = "";

    @ColumnInfo(name = IMDBID)
    public String imdbId = "";
    @ColumnInfo(name = TRAKT_ID)
    public Integer traktId = 0;

    @ColumnInfo(name = FAVORITE)
    public boolean favorite = false;

    @ColumnInfo(name = HEXAGON_MERGE_COMPLETE)
    public boolean hexagonMergeComplete = true;

    @ColumnInfo(name = HIDDEN)
    public boolean hidden = false;

    @ColumnInfo(name = LASTUPDATED)
    public long lastUpdatedMs = 0L;
    @ColumnInfo(name = LASTEDIT)
    public long lastEditedSec = 0L;

    @ColumnInfo(name = LASTWATCHEDID)
    public int lastWatchedEpisodeId = 0;
    @ColumnInfo(name = LASTWATCHED_MS)
    public long lastWatchedMs = 0L;

    @ColumnInfo(name = LANGUAGE)
    public String language = "";

    @ColumnInfo(name = UNWATCHED_COUNT)
    public int unwatchedCount = DBUtils.UNKNOWN_UNWATCHED_COUNT;

    @ColumnInfo(name = NOTIFY)
    public boolean notifySg = true;
}
