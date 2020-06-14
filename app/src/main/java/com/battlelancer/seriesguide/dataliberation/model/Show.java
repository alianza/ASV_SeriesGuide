
package com.battlelancer.seriesguide.dataliberation.model;

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
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.TITLE_NOARTICLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.TRAKT_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows.UNWATCHED_COUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.TITLE;
import static com.battlelancer.seriesguide.settings.DisplaySettings.LANGUAGE_EN;
import static com.battlelancer.seriesguide.util.DBUtils.UNKNOWN_NEXT_RELEASE_DATE;
import static com.battlelancer.seriesguide.util.DBUtils.UNKNOWN_UNWATCHED_COUNT;
import static com.battlelancer.seriesguide.util.DBUtils.trimLeadingArticle;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.battlelancer.seriesguide.R;
import com.battlelancer.seriesguide.dataliberation.DataLiberationTools;
import com.battlelancer.seriesguide.util.TimeTools;
import java.util.List;

/**
 * @see com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns ShowsColumns
 */
public class Show {

    public int tvdb_id;
    public String tvdb_slug;
    public String imdb_id;
    public Integer trakt_id;

    public String title;
    public String overview;

    public String language;

    public String first_aired;
    public int release_time;
    public int release_weekday;
    public String release_timezone;
    public String country;

    public String poster;
    public String poster_small;
    public String content_rating;
    public String status;
    public int runtime;
    public String genres;
    public String network;

    public double rating;
    public int rating_votes;
    public int rating_user;

    public long last_edited;

    /** SeriesGuide specific values */
    public boolean favorite;
    public Boolean notifySg;
    public boolean hidden;

    public long last_updated;
    public int last_watched_episode;
    public long last_watched_ms;

    public List<Season> seasons;
    
    public ContentValues toContentValues(Context context, boolean forInsert) {
        // note: if a value is explicitly inserted as NULL the DEFAULT value is not used
        // so ensure a NULL is never inserted if a DEFAULT constraint exists

        ContentValues values = new ContentValues();
        // values for new and existing shows
        values.put(SLUG, tvdb_slug);
        // if in any case the title is empty, show a place holder
        values.put(TITLE, TextUtils.isEmpty(title)
                ? context.getString(R.string.no_translation_title) : title);
        values.put(TITLE_NOARTICLE, trimLeadingArticle(title));
        values.put(OVERVIEW, overview != null ? overview : "");
        values.put(POSTER, poster != null ? poster : "");
        values.put(POSTER_SMALL, poster_small != null ? poster_small : "");
        values.put(CONTENTRATING, content_rating != null ? content_rating : "");
        values.put(STATUS, DataLiberationTools.encodeShowStatus(status));
        values.put(RUNTIME, runtime >= 0 ? runtime : 0);
        values.put(RATING_GLOBAL, (rating >= 0 && rating <= 10) ? rating : 0);
        values.put(NETWORK, network != null ? network : "");
        values.put(GENRES, genres != null ? genres : "");
        values.put(FIRST_RELEASE, first_aired);
        values.put(RELEASE_TIME, release_time);
        values.put(RELEASE_WEEKDAY, (release_weekday >= -1 && release_weekday <= 7)
                ? release_weekday : TimeTools.RELEASE_WEEKDAY_UNKNOWN);
        values.put(RELEASE_TIMEZONE, release_timezone);
        values.put(RELEASE_COUNTRY, country);
        values.put(IMDBID, imdb_id != null ? imdb_id : "");
        values.put(TRAKT_ID, (trakt_id != null && trakt_id > 0) ? trakt_id : 0);
        values.put(LASTUPDATED, last_updated);
        values.put(LASTEDIT, last_edited);
        if (forInsert) {
            values.put(_ID, tvdb_id);
            values.put(LANGUAGE, language != null ? language : LANGUAGE_EN);

            values.put(FAVORITE, favorite ? 1 : 0);
            values.put(NOTIFY, notifySg != null ? (notifySg ? 1 : 0) : 1);
            values.put(HIDDEN, hidden ? 1 : 0);

            values.put(RATING_VOTES, rating_votes >= 0 ? rating_votes : 0);
            values.put(RATING_USER, (rating_user >= 0 && rating_user <= 10)
                    ? rating_user : 0);

            values.put(LASTWATCHEDID, last_watched_episode);
            values.put(LASTWATCHED_MS, last_watched_ms);

            values.put(HEXAGON_MERGE_COMPLETE, 1);
            values.put(NEXTEPISODE, "");
            values.put(NEXTTEXT, "");
            values.put(NEXTAIRDATEMS, UNKNOWN_NEXT_RELEASE_DATE);
            values.put(UNWATCHED_COUNT, UNKNOWN_UNWATCHED_COUNT);
        }
        return values;
    }
}
