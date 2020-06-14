package com.battlelancer.seriesguide.ui.movies;

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
import static com.battlelancer.seriesguide.util.DBUtils.convertBooleanToInt;
import static com.battlelancer.seriesguide.util.DBUtils.trimLeadingArticle;

import android.content.ContentValues;
import androidx.annotation.Nullable;
import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.trakt5.entities.Ratings;
import java.util.Date;

/**
 * Holder object for trakt and TMDb entities related to a movie.
 */
public class MovieDetails {

    private Ratings traktRatings;
    private Movie tmdbMovie;

    private boolean inCollection;
    private boolean inWatchlist;
    private boolean isWatched;

    private int userRating;

    @Nullable
    public Ratings traktRatings() {
        return traktRatings;
    }

    public void traktRatings(Ratings traktRatings) {
        this.traktRatings = traktRatings;
    }

    @Nullable
    public Movie tmdbMovie() {
        return tmdbMovie;
    }

    public void tmdbMovie(Movie movie) {
        tmdbMovie = movie;
    }

    public boolean isInCollection() {
        return inCollection;
    }

    public void setInCollection(boolean inCollection) {
        this.inCollection = inCollection;
    }

    public boolean isInWatchlist() {
        return inWatchlist;
    }

    public void setInWatchlist(boolean inWatchlist) {
        this.inWatchlist = inWatchlist;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    /**
     * Extracts ratings from trakt, all other properties from TMDb data.
     *
     * <p> If either movie data is null, will still extract the properties of others.
     *
     * <p> Does not add TMDB id or collection and watchlist flag.
     */
    public ContentValues toContentValuesUpdate() {
        ContentValues values = new ContentValues();

        // data from trakt
        if (traktRatings != null) {
            values.put(RATING_TRAKT, traktRatings.rating != null ? traktRatings.rating : 0);
            values.put(RATING_VOTES_TRAKT, traktRatings.votes != null
                    ? traktRatings.votes : 0);
        }

        // data from TMDb
        if (tmdbMovie != null) {
            values.put(IMDB_ID, tmdbMovie.imdb_id);
            values.put(TITLE, tmdbMovie.title);
            values.put(TITLE_NOARTICLE,
                    trimLeadingArticle(tmdbMovie.title));
            values.put(OVERVIEW, tmdbMovie.overview);
            values.put(POSTER, tmdbMovie.poster_path);
            values.put(RUNTIME_MIN, tmdbMovie.runtime != null ? tmdbMovie.runtime : 0);
            values.put(RATING_TMDB, tmdbMovie.vote_average != null
                    ? tmdbMovie.vote_average : 0);
            values.put(RATING_VOTES_TMDB, tmdbMovie.vote_count != null
                    ? tmdbMovie.vote_count : 0);
            // if there is no release date, store Long.MAX as it is likely in the future
            // also helps correctly sorting movies by release date
            Date releaseDate = tmdbMovie.release_date;
            values.put(RELEASED_UTC_MS,
                    releaseDate == null ? Long.MAX_VALUE : releaseDate.getTime());
        }

        return values;
    }

    /**
     * Like {@link #toContentValuesUpdate()} and adds TMDB id and IN_COLLECTION and IN_WATCHLIST
     * values.
     */
    public ContentValues toContentValuesInsert() {
        ContentValues values = toContentValuesUpdate();
        values.put(TMDB_ID, tmdbMovie.id);
        values.put(IN_COLLECTION, convertBooleanToInt(inCollection));
        values.put(IN_WATCHLIST, convertBooleanToInt(inWatchlist));
        values.put(WATCHED, convertBooleanToInt(isWatched));
        // set default values
        values.put(PLAYS, 0);
        values.put(RATING_TMDB, 0);
        values.put(RATING_VOTES_TMDB, 0);
        values.put(RATING_TRAKT, 0);
        values.put(RATING_VOTES_TRAKT, 0);
        values.put(LAST_UPDATED, System.currentTimeMillis());
        return values;
    }
}
