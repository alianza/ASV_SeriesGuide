package com.battlelancer.seriesguide.provider;

import static android.provider.BaseColumns._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ActivityColumns;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.FIRSTAIREDMS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.LAST_UPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.OVERVIEW;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.SEASON;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.WATCHED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.ITEM_REF_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.LIST_ITEM_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.SELECTION_EPISODES;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.SELECTION_SEASONS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.SELECTION_SHOWS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.TYPE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.COMBINED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.LASTWATCHEDID;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.ACTIVITY;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.EPISODES;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.EPISODES_SEARCH;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.JOBS;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.LISTS;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.LIST_ITEMS;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.MOVIES;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.SEASONS;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.SHOWS;

import android.app.SearchManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.EpisodeSearch;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.EpisodeSearchColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.EpisodesColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.JobsColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItemsColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.ListsColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.MoviesColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.SeasonsColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns;
import com.battlelancer.seriesguide.util.DBUtils;
import timber.log.Timber;

public class SeriesGuideDatabase {

    public static final String DATABASE_NAME = "seriesdatabase";

    public static final int DBVER_17_FAVORITES = 17;
    public static final int DBVER_18_NEXTAIRDATETEXT = 18;
    public static final int DBVER_19_SETOTALCOUNT = 19;
    public static final int DBVER_20_SYNC = 20;
    public static final int DBVER_21_AIRTIMECOLUMN = 21;
    public static final int DBVER_22_PERSHOWUPDATEDATE = 22;
    public static final int DBVER_23_HIDDENSHOWS = 23;
    public static final int DBVER_24_AIRTIMEREFORM = 24;
    public static final int DBVER_25_NEXTAIRDATEMS = 25;
    public static final int DBVER_26_COLLECTED = 26;
    public static final int DBVER_27_IMDBIDSLASTEDIT = 27;
    public static final int DBVER_28_LISTS = 28;
    public static final int DBVER_29_GETGLUE_CHECKIN_FIX = 29;
    public static final int DBVER_30_ABSOLUTE_NUMBERS = 30;
    public static final int DBVER_31_LAST_WATCHED_ID = 31;
    public static final int DBVER_32_MOVIES = 32;
    public static final int DBVER_33_IGNORE_ARTICLE_SORT = 33;

    /**
     * Changes for trakt v2 compatibility, also for storing ratings offline.
     *
     * Shows:
     *
     * <ul>
     *
     * <li>changed release time encoding
     *
     * <li>changed release week day encoding
     *
     * <li>first release date now includes time
     *
     * <li>added time zone
     *
     * <li>added rating votes
     *
     * <li>added user rating
     *
     * </ul>
     *
     * Episodes:
     *
     * <ul>
     *
     * <li>added rating votes
     *
     * <li>added user rating
     *
     * </ul>
     *
     * Movies:
     *
     * <ul>
     *
     * <li>added user rating
     *
     * </ul>
     */
    public static final int DBVER_34_TRAKT_V2 = 34;

    /**
     * Added activity table to store recently watched episodes.
     */
    public static final int DBVER_35_ACTIVITY_TABLE = 35;

    /**
     * Support for re-ordering lists: added new column to lists table.
     */
    public static final int DBVER_36_ORDERABLE_LISTS = 36;

    /**
     * Added language column to shows table.
     */
    public static final int DBVER_37_LANGUAGE_PER_SERIES = 37;

    /**
     * Added trakt id column to shows table.
     */
    static final int DBVER_38_SHOW_TRAKT_ID = 38;

    /**
     * Added last watched time and unwatched counter to shows table.
     */
    static final int DBVER_39_SHOW_LAST_WATCHED = 39;

    /**
     * Add {@link Shows#NOTIFY} flag to shows table.
     */
    static final int DBVER_40_NOTIFY_PER_SHOW = 40;

    /**
     * Add {@link Episodes#LAST_UPDATED} flag to episodes table.
     */
    static final int DBVER_41_EPISODE_LAST_UPDATED = 41;

    /**
     * Added jobs table.
     */
    public static final int DBVER_42_JOBS = 42;

    public static final int DATABASE_VERSION = DBVER_42_JOBS;

    /**
     * Qualifies column names by prefixing their {@link Tables} name.
     */
    public interface Qualified {

        String SHOWS_ID = SHOWS + "." + Shows._ID;
        String SHOWS_LAST_EPISODE = SHOWS + "." + LASTWATCHEDID;
        String SHOWS_NEXT_EPISODE = SHOWS + "." + Shows.NEXTEPISODE;
        String EPISODES_ID = EPISODES + "." + _ID;
        String EPISODES_SHOW_ID = EPISODES + "." + Shows.REF_SHOW_ID;
        String SEASONS_ID = SEASONS + "." + _ID;
        String SEASONS_SHOW_ID = SEASONS + "." + Shows.REF_SHOW_ID;
        String LIST_ITEMS_REF_ID = LIST_ITEMS + "." + ITEM_REF_ID;
    }

    public interface Tables {

        String SHOWS = "series";

        String SEASONS = "seasons";

        String EPISODES = "episodes";

        String SHOWS_JOIN_EPISODES_ON_LAST_EPISODE = SHOWS + " LEFT OUTER JOIN " + EPISODES
                + " ON " + Qualified.SHOWS_LAST_EPISODE + "=" + Qualified.EPISODES_ID;

        String SHOWS_JOIN_EPISODES_ON_NEXT_EPISODE = SHOWS + " LEFT OUTER JOIN " + EPISODES
                + " ON " + Qualified.SHOWS_NEXT_EPISODE + "=" + Qualified.EPISODES_ID;

        String SEASONS_JOIN_SHOWS = SEASONS + " LEFT OUTER JOIN " + SHOWS
                + " ON " + Qualified.SEASONS_SHOW_ID + "=" + Qualified.SHOWS_ID;

        String EPISODES_JOIN_SHOWS = EPISODES + " LEFT OUTER JOIN " + SHOWS
                + " ON " + Qualified.EPISODES_SHOW_ID + "=" + Qualified.SHOWS_ID;

        String EPISODES_SEARCH = "searchtable";

        String LISTS = "lists";

        String LIST_ITEMS = "listitems";

        String LIST_ITEMS_WITH_DETAILS = "("
                // shows
                + "SELECT " + Selections.SHOWS_COLUMNS + " FROM "
                + "("
                + Selections.LIST_ITEMS_SHOWS
                + " LEFT OUTER JOIN " + SHOWS
                + " ON " + Qualified.LIST_ITEMS_REF_ID + "=" + Qualified.SHOWS_ID
                + ")"
                // seasons
                + " UNION SELECT " + Selections.SEASONS_COLUMNS + " FROM "
                + "("
                + Selections.LIST_ITEMS_SEASONS
                + " LEFT OUTER JOIN " + "(" + SEASONS_JOIN_SHOWS + ") AS " + SEASONS
                + " ON " + Qualified.LIST_ITEMS_REF_ID + "=" + Qualified.SEASONS_ID
                + ")"
                // episodes
                + " UNION SELECT " + Selections.EPISODES_COLUMNS + " FROM "
                + "("
                + Selections.LIST_ITEMS_EPISODES
                + " LEFT OUTER JOIN " + "(" + EPISODES_JOIN_SHOWS + ") AS " + EPISODES
                + " ON " + Qualified.LIST_ITEMS_REF_ID + "=" + Qualified.EPISODES_ID
                + ")"
                //
                + ")";

        String MOVIES = "movies";

        String ACTIVITY = "activity";

        String JOBS = "jobs";
    }

    private interface Selections {

        String LIST_ITEMS_SHOWS = "(SELECT " + Selections.LIST_ITEMS_COLUMNS_INTERNAL
                + " FROM " + LIST_ITEMS
                + " WHERE " + SELECTION_SHOWS + ")"
                + " AS " + LIST_ITEMS;

        String LIST_ITEMS_SEASONS = "(SELECT " + Selections.LIST_ITEMS_COLUMNS_INTERNAL
                + " FROM " + LIST_ITEMS
                + " WHERE " + SELECTION_SEASONS + ")"
                + " AS " + LIST_ITEMS;

        String LIST_ITEMS_EPISODES = "(SELECT " + Selections.LIST_ITEMS_COLUMNS_INTERNAL
                + " FROM " + LIST_ITEMS
                + " WHERE " + SELECTION_EPISODES + ")"
                + " AS " + LIST_ITEMS;

        String LIST_ITEMS_COLUMNS_INTERNAL =
                _ID + " as listitem_id,"
                        + LIST_ITEM_ID + ","
                        + Lists.LIST_ID + ","
                        + TYPE + ","
                        + ITEM_REF_ID;

        String COMMON_LIST_ITEMS_COLUMNS =
                // from list items table
                "listitem_id as " + _ID + ","
                        + LIST_ITEM_ID + ","
                        + Lists.LIST_ID + ","
                        + TYPE + ","
                        + ITEM_REF_ID + ","
                        // from shows table
                        + Shows.TITLE + ","
                        + Shows.TITLE_NOARTICLE + ","
                        + Shows.POSTER_SMALL + ","
                        + Shows.NETWORK + ","
                        + Shows.STATUS + ","
                        + Shows.FAVORITE + ","
                        + Shows.RELEASE_WEEKDAY + ","
                        + Shows.RELEASE_TIMEZONE + ","
                        + Shows.RELEASE_COUNTRY + ","
                        + Shows.LASTWATCHED_MS + ","
                        + Shows.UNWATCHED_COUNT;

        String SHOWS_COLUMNS = COMMON_LIST_ITEMS_COLUMNS + ","
                + Qualified.SHOWS_ID + " as " + Shows.REF_SHOW_ID + ","
                + Shows.OVERVIEW + ","
                + Shows.RELEASE_TIME + ","
                + Shows.NEXTTEXT + ","
                + Shows.NEXTEPISODE + ","
                + Shows.NEXTAIRDATEMS;

        String SEASONS_COLUMNS = COMMON_LIST_ITEMS_COLUMNS + ","
                + Shows.REF_SHOW_ID + ","
                + COMBINED + " as " + Shows.OVERVIEW + ","
                + Shows.RELEASE_TIME + ","
                + Shows.NEXTTEXT + ","
                + Shows.NEXTEPISODE + ","
                + Shows.NEXTAIRDATEMS;

        String EPISODES_COLUMNS = COMMON_LIST_ITEMS_COLUMNS + ","
                + Shows.REF_SHOW_ID + ","
                + TITLE + " as " + Shows.OVERVIEW + ","
                + FIRSTAIREDMS + " as " + Shows.RELEASE_TIME + ","
                + SEASON + " as " + Shows.NEXTTEXT + ","
                + NUMBER + " as " + Shows.NEXTEPISODE + ","
                + FIRSTAIREDMS + " as " + Shows.NEXTAIRDATEMS;
    }

    interface References {

        String SHOW_ID = "REFERENCES " + SHOWS + "(" + _ID + ")";

        String SEASON_ID = "REFERENCES " + SEASONS + "(" + _ID + ")";

        String LIST_ID = "REFERENCES " + LISTS + "(" + Lists.LIST_ID + ")";
    }

    @VisibleForTesting
    public static final String CREATE_SHOWS_TABLE = "CREATE TABLE " + SHOWS + " ("

            + _ID + " INTEGER PRIMARY KEY,"

            + ShowsColumns.TITLE + " TEXT NOT NULL,"

            + ShowsColumns.TITLE_NOARTICLE + " TEXT,"

            + ShowsColumns.OVERVIEW + " TEXT DEFAULT '',"

            + ShowsColumns.RELEASE_TIME + " INTEGER,"

            + ShowsColumns.RELEASE_WEEKDAY + " INTEGER,"

            + ShowsColumns.RELEASE_COUNTRY + " TEXT,"

            + ShowsColumns.RELEASE_TIMEZONE + " TEXT,"

            + ShowsColumns.FIRST_RELEASE + " TEXT,"

            + ShowsColumns.GENRES + " TEXT DEFAULT '',"

            + ShowsColumns.NETWORK + " TEXT DEFAULT '',"

            + ShowsColumns.RATING_GLOBAL + " REAL,"

            + ShowsColumns.RATING_VOTES + " INTEGER,"

            + ShowsColumns.RATING_USER + " INTEGER,"

            + ShowsColumns.RUNTIME + " TEXT DEFAULT '',"

            + ShowsColumns.STATUS + " TEXT DEFAULT '',"

            + ShowsColumns.CONTENTRATING + " TEXT DEFAULT '',"

            + ShowsColumns.NEXTEPISODE + " TEXT DEFAULT '',"

            + ShowsColumns.POSTER + " TEXT DEFAULT '',"

            + ShowsColumns.NEXTAIRDATEMS + " INTEGER,"

            + ShowsColumns.NEXTTEXT + " TEXT DEFAULT '',"

            + ShowsColumns.IMDBID + " TEXT DEFAULT '',"

            + ShowsColumns.TRAKT_ID + " INTEGER DEFAULT 0,"

            + ShowsColumns.FAVORITE + " INTEGER DEFAULT 0,"

            + ShowsColumns.NEXTAIRDATETEXT + " TEXT DEFAULT '',"

            + ShowsColumns.HEXAGON_MERGE_COMPLETE + " INTEGER DEFAULT 1,"

            + ShowsColumns.HIDDEN + " INTEGER DEFAULT 0,"

            + ShowsColumns.LASTUPDATED + " INTEGER DEFAULT 0,"

            + ShowsColumns.LASTEDIT + " INTEGER DEFAULT 0,"

            + LASTWATCHEDID + " INTEGER DEFAULT 0,"

            + ShowsColumns.LASTWATCHED_MS + " INTEGER DEFAULT 0,"

            + ShowsColumns.LANGUAGE + " TEXT DEFAULT '',"

            + ShowsColumns.UNWATCHED_COUNT + " INTEGER DEFAULT " + DBUtils.UNKNOWN_UNWATCHED_COUNT
            + ","

            + ShowsColumns.NOTIFY + " INTEGER DEFAULT 1"

            + ");";

    @VisibleForTesting
    public static final String CREATE_SEASONS_TABLE = "CREATE TABLE " + SEASONS + " ("

            + _ID + " INTEGER PRIMARY KEY,"

            + SeasonsColumns.COMBINED + " INTEGER,"

            + ShowsColumns.REF_SHOW_ID + " TEXT " + References.SHOW_ID + ","

            + SeasonsColumns.WATCHCOUNT + " INTEGER DEFAULT 0,"

            + SeasonsColumns.UNAIREDCOUNT + " INTEGER DEFAULT 0,"

            + SeasonsColumns.NOAIRDATECOUNT + " INTEGER DEFAULT 0,"

            + SeasonsColumns.TAGS + " TEXT DEFAULT '',"

            + SeasonsColumns.TOTALCOUNT + " INTEGER DEFAULT 0"

            + ");";

    @VisibleForTesting
    public static final String CREATE_EPISODES_TABLE = "CREATE TABLE " + EPISODES + " ("

            + _ID + " INTEGER PRIMARY KEY,"

            + EpisodesColumns.TITLE + " TEXT NOT NULL,"

            + EpisodesColumns.OVERVIEW + " TEXT,"

            + EpisodesColumns.NUMBER + " INTEGER DEFAULT 0,"

            + EpisodesColumns.SEASON + " INTEGER DEFAULT 0,"

            + EpisodesColumns.DVDNUMBER + " REAL,"

            + SeasonsColumns.REF_SEASON_ID + " TEXT " + References.SEASON_ID + ","

            + ShowsColumns.REF_SHOW_ID + " TEXT " + References.SHOW_ID + ","

            + EpisodesColumns.WATCHED + " INTEGER DEFAULT 0,"

            + EpisodesColumns.DIRECTORS + " TEXT DEFAULT '',"

            + EpisodesColumns.GUESTSTARS + " TEXT DEFAULT '',"

            + EpisodesColumns.WRITERS + " TEXT DEFAULT '',"

            + EpisodesColumns.IMAGE + " TEXT DEFAULT '',"

            + EpisodesColumns.FIRSTAIREDMS + " INTEGER DEFAULT -1,"

            + EpisodesColumns.COLLECTED + " INTEGER DEFAULT 0,"

            + EpisodesColumns.RATING_GLOBAL + " REAL,"

            + EpisodesColumns.RATING_VOTES + " INTEGER,"

            + EpisodesColumns.RATING_USER + " INTEGER,"

            + EpisodesColumns.IMDBID + " TEXT DEFAULT '',"

            + EpisodesColumns.LAST_EDITED + " INTEGER DEFAULT 0,"

            + EpisodesColumns.ABSOLUTE_NUMBER + " INTEGER,"

            + EpisodesColumns.LAST_UPDATED + " INTEGER DEFAULT 0"

            + ");";

    static final String CREATE_SEARCH_TABLE = "CREATE VIRTUAL TABLE "
            + EPISODES_SEARCH + " USING fts4("

            // set episodes table as external content table
            + "content='" + EPISODES + "',"

            + EpisodeSearchColumns.TITLE + ","

            + EpisodeSearchColumns.OVERVIEW

            + ");";

    @VisibleForTesting
    public static final String CREATE_LISTS_TABLE = "CREATE TABLE " + LISTS + " ("

            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

            + ListsColumns.LIST_ID + " TEXT NOT NULL,"

            + ListsColumns.NAME + " TEXT NOT NULL,"

            + ListsColumns.ORDER + " INTEGER DEFAULT 0,"

            + "UNIQUE (" + ListsColumns.LIST_ID + ") ON CONFLICT REPLACE"

            + ");";

    @VisibleForTesting
    public static final String CREATE_LIST_ITEMS_TABLE = "CREATE TABLE " + LIST_ITEMS
            + " ("

            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

            + ListItemsColumns.LIST_ITEM_ID + " TEXT NOT NULL,"

            + ListItemsColumns.ITEM_REF_ID + " TEXT NOT NULL,"

            + ListItemsColumns.TYPE + " INTEGER NOT NULL,"

            + ListsColumns.LIST_ID + " TEXT " + References.LIST_ID + ","

            + "UNIQUE (" + ListItemsColumns.LIST_ITEM_ID + ") ON CONFLICT REPLACE"

            + ");";

    @VisibleForTesting
    public static final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MOVIES
            + " ("

            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

            + MoviesColumns.TMDB_ID + " INTEGER NOT NULL,"

            + MoviesColumns.IMDB_ID + " TEXT,"

            + MoviesColumns.TITLE + " TEXT,"

            + MoviesColumns.TITLE_NOARTICLE + " TEXT,"

            + MoviesColumns.POSTER + " TEXT,"

            + MoviesColumns.GENRES + " TEXT,"

            + MoviesColumns.OVERVIEW + " TEXT,"

            + MoviesColumns.RELEASED_UTC_MS + " INTEGER,"

            + MoviesColumns.RUNTIME_MIN + " INTEGER DEFAULT 0,"

            + MoviesColumns.TRAILER + " TEXT,"

            + MoviesColumns.CERTIFICATION + " TEXT,"

            + MoviesColumns.IN_COLLECTION + " INTEGER DEFAULT 0,"

            + MoviesColumns.IN_WATCHLIST + " INTEGER DEFAULT 0,"

            + MoviesColumns.PLAYS + " INTEGER DEFAULT 0,"

            + MoviesColumns.WATCHED + " INTEGER DEFAULT 0,"

            + MoviesColumns.RATING_TMDB + " REAL DEFAULT 0,"

            + MoviesColumns.RATING_VOTES_TMDB + " INTEGER DEFAULT 0,"

            + MoviesColumns.RATING_TRAKT + " INTEGER DEFAULT 0,"

            + MoviesColumns.RATING_VOTES_TRAKT + " INTEGER DEFAULT 0,"

            + MoviesColumns.RATING_USER + " INTEGER,"

            + MoviesColumns.LAST_UPDATED + " INTEGER,"

            + "UNIQUE (" + MoviesColumns.TMDB_ID + ") ON CONFLICT REPLACE"

            + ");";

    private static final String ACTIVITY_TABLE = ACTIVITY
            + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ActivityColumns.EPISODE_TVDB_ID + " TEXT NOT NULL,"
            + ActivityColumns.SHOW_TVDB_ID + " TEXT NOT NULL,"
            + ActivityColumns.TIMESTAMP_MS + " INTEGER NOT NULL,"
            + "UNIQUE (" + ActivityColumns.EPISODE_TVDB_ID + ") ON CONFLICT REPLACE"
            + ");";
    @VisibleForTesting
    public static final String CREATE_ACTIVITY_TABLE = "CREATE TABLE " + ACTIVITY_TABLE;

    private static final String JOBS_TABLE = JOBS
            + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + JobsColumns.CREATED_MS + " INTEGER,"
            + JobsColumns.TYPE + " INTEGER,"
            + JobsColumns.EXTRAS + " BLOB,"
            + "UNIQUE (" + JobsColumns.CREATED_MS + ") ON CONFLICT REPLACE"
            + ");";

    @VisibleForTesting
    public static final String CREATE_JOBS_TABLE = "CREATE TABLE " + JOBS_TABLE;

    /**
     * See {@link #DBVER_42_JOBS}.
     */
    static void upgradeToFortyTwo(@NonNull SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + JOBS_TABLE);
    }

    /**
     * See {@link #DBVER_41_EPISODE_LAST_UPDATED}.
     */
    static void upgradeToFortyOne(@NonNull SupportSQLiteDatabase db) {
        if (isTableColumnMissing(db, EPISODES, LAST_UPDATED)) {
            db.execSQL("ALTER TABLE " + EPISODES + " ADD COLUMN "
                    + LAST_UPDATED + " INTEGER DEFAULT 0;");
        }
    }

    /**
     * See {@link #DBVER_40_NOTIFY_PER_SHOW}.
     */
    static void upgradeToForty(@NonNull SupportSQLiteDatabase db) {
        if (isTableColumnMissing(db, SHOWS, Shows.NOTIFY)) {
            db.execSQL("ALTER TABLE " + SHOWS + " ADD COLUMN "
                    + Shows.NOTIFY + " INTEGER DEFAULT 1;");

//            // check if notifications should be enabled only for favorite shows
//            // noinspection deprecation
//            boolean favoritesOnly = NotificationSettings.isNotifyAboutFavoritesOnly(context);
//            if (favoritesOnly) {
//                // disable notifications for all but favorite shows
//                ContentValues values = new ContentValues();
//                values.put(Shows.NOTIFY, 0);
//                db.update(Tables.SHOWS, SQLiteDatabase.CONFLICT_NONE, values,
//                        Shows.SELECTION_NOT_FAVORITES, null);
//            }
        }
    }

    /**
     * See {@link #DBVER_39_SHOW_LAST_WATCHED}.
     */
    static void upgradeToThirtyNine(@NonNull SupportSQLiteDatabase db) {
        if (isTableColumnMissing(db, SHOWS, Shows.LASTWATCHED_MS)) {
            db.execSQL("ALTER TABLE " + SHOWS + " ADD COLUMN "
                    + Shows.LASTWATCHED_MS + " INTEGER DEFAULT 0;");
        }
        if (isTableColumnMissing(db, SHOWS, Shows.UNWATCHED_COUNT)) {
            db.execSQL("ALTER TABLE " + SHOWS + " ADD COLUMN "
                    + Shows.UNWATCHED_COUNT + " INTEGER DEFAULT " + DBUtils.UNKNOWN_UNWATCHED_COUNT
                    + ";");
        }
    }

    /**
     * See {@link #DBVER_38_SHOW_TRAKT_ID}.
     */
    static void upgradeToThirtyEight(@NonNull SupportSQLiteDatabase db) {
        if (isTableColumnMissing(db, SHOWS, Shows.TRAKT_ID)) {
            db.execSQL("ALTER TABLE " + SHOWS + " ADD COLUMN "
                    + Shows.TRAKT_ID + " INTEGER DEFAULT 0;");
        }
    }

    /**
     * See {@link #DBVER_37_LANGUAGE_PER_SERIES}.
     */
    static void upgradeToThirtySeven(@NonNull SupportSQLiteDatabase db) {
        if (isTableColumnMissing(db, SHOWS, Shows.LANGUAGE)) {
            db.execSQL("ALTER TABLE " + SHOWS + " ADD COLUMN "
                    + Shows.LANGUAGE + " TEXT DEFAULT '';");
        }
    }

    /**
     * See {@link #DBVER_36_ORDERABLE_LISTS}.
     */
    static void upgradeToThirtySix(@NonNull SupportSQLiteDatabase db) {
        if (isTableColumnMissing(db, LISTS, Lists.ORDER)) {
            db.execSQL("ALTER TABLE " + LISTS + " ADD COLUMN "
                    + Lists.ORDER + " INTEGER DEFAULT 0;");
        }
    }

    /**
     * See {@link #DBVER_35_ACTIVITY_TABLE}.
     */
    static void upgradeToThirtyFive(@NonNull SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE);
    }

    /**
     * Drops the current {@link Tables#EPISODES_SEARCH} table and re-creates it with current data
     * from {@link Tables#EPISODES}.
     */
    public static void rebuildFtsTable(SupportSQLiteDatabase db) {
        if (!recreateFtsTable(db)) {
            return;
        }

        rebuildFtsTableJellyBean(db);
    }

    /**
     * Works with FTS4 search table.
     */
    private static void rebuildFtsTableJellyBean(SupportSQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                db.execSQL("INSERT OR IGNORE INTO " + EPISODES_SEARCH
                        + "(" + EPISODES_SEARCH + ") VALUES('rebuild')");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (SQLiteException e) {
            Timber.e(e, "rebuildFtsTableJellyBean: failed to populate table.");
            DBUtils.postDatabaseError(e);
        }
    }

    private static boolean recreateFtsTable(SupportSQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                db.execSQL("drop table if exists " + EPISODES_SEARCH);
                db.execSQL(CREATE_SEARCH_TABLE);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return true;
        } catch (SQLiteException e) {
            Timber.e(e, "recreateFtsTable: failed.");
            DBUtils.postDatabaseError(e);
            return false;
        }
    }

    // This should match QUERY_SEARCH_EPISODES//
    public interface EpisodeSearchQuery {
        String[] PROJECTION = new String[]{
                Episodes._ID,
                Episodes.TITLE,
                Episodes.NUMBER,
                Episodes.SEASON,
                Episodes.WATCHED,
                Episodes.OVERVIEW,
                Shows.TITLE,
                Shows.POSTER_SMALL
        };

        int _ID = 0;
        int TITLE = 1;
        int NUMBER = 2;
        int SEASON = 3;
        int WATCHED = 4;
        int OVERVIEW = 5;
        int SHOW_TITLE = 6;
        int SHOW_POSTER_SMALL = 7;
    }

    private final static String EPISODE_COLUMNS = _ID + ","
            + TITLE + ","
            + NUMBER + ","
            + SEASON + ","
            + WATCHED;

    private final static String SELECT_SHOWS = "SELECT "
            + _ID + " as sid,"
            + Shows.TITLE + ","
            + Shows.POSTER_SMALL
            + " FROM " + SHOWS;

    private final static String SELECT_MATCH = "SELECT "
            + EpisodeSearch._DOCID + ","
            + "snippet(" + EPISODES_SEARCH + ",'<b>','</b>','...') AS " + OVERVIEW
            + " FROM " + EPISODES_SEARCH
            + " WHERE " + EPISODES_SEARCH + " MATCH ?";

    private final static String SELECT_EPISODES = "SELECT "
            + EPISODE_COLUMNS + "," + Shows.REF_SHOW_ID
            + " FROM " + EPISODES;

    private final static String JOIN_MATCHES_EPISODES = "SELECT "
            + EPISODE_COLUMNS + "," + OVERVIEW + "," + Shows.REF_SHOW_ID
            + " FROM (" + SELECT_MATCH + ")"
            + " JOIN (" + SELECT_EPISODES + ")"
            + " ON " + EpisodeSearch._DOCID + "=" + _ID;

    private final static String QUERY_SEARCH_EPISODES = "SELECT "
            + EPISODE_COLUMNS + "," + OVERVIEW + "," + Shows.TITLE + "," + Shows.POSTER_SMALL
            + " FROM "
            + "("
            + "(" + SELECT_SHOWS + ") JOIN (" + JOIN_MATCHES_EPISODES + ") ON sid="
            + Shows.REF_SHOW_ID
            + ")";

    private final static String ORDER_SEARCH_EPISODES = " ORDER BY "
            + Shows.SORT_TITLE + ","
            + SEASON + " ASC,"
            + NUMBER + " ASC";

    @Nullable
    public static Cursor search(SupportSQLiteDatabase db, String selection,
            String[] selectionArgs) {
        StringBuilder query = new StringBuilder(QUERY_SEARCH_EPISODES);
        if (selection != null) {
            query.append(" WHERE (").append(selection).append(")");
        }
        query.append(ORDER_SEARCH_EPISODES);

        // ensure to strip double quotation marks (would break the MATCH query)
        String searchTerm = selectionArgs[0];
        if (searchTerm != null) {
            searchTerm = searchTerm.replace("\"", "");
        }
        // search for anything starting with the given search term
        selectionArgs[0] = "\"" + searchTerm + "*\"";

        try {
            return db.query(query.toString(), selectionArgs);
        } catch (SQLiteException e) {
            Timber.e(e, "search: failed, database error.");
            return null;
        }
    }

    private final static String QUERY_SEARCH_SHOWS = "select _id,"
            + TITLE + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1 + ","
            + Shows.TITLE + " as " + SearchManager.SUGGEST_COLUMN_TEXT_2 + ","
            + "_id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
            + " from ((select _id as sid," + Shows.TITLE + " from " + SHOWS + ")"
            + " join "
            + "(select _id," + TITLE + "," + Shows.REF_SHOW_ID
            + " from " + "(select docid" + " from " + EPISODES_SEARCH
            + " where " + EPISODES_SEARCH + " match " + "?)"
            + " join "
            + "(select _id," + TITLE + "," + Shows.REF_SHOW_ID + " from episodes)"
            + "on _id=docid)"
            + "on sid=" + Shows.REF_SHOW_ID + ")";

    @Nullable
    public static Cursor getSuggestions(SupportSQLiteDatabase db, String searchTerm) {
        // ensure to strip double quotation marks (would break the MATCH query)
        if (searchTerm != null) {
            searchTerm = searchTerm.replace("\"", "");
        }

        try {
            // search for anything starting with the given search term
            return db.query(QUERY_SEARCH_SHOWS, new String[]{
                    "\"" + searchTerm + "*\""
            });
        } catch (SQLiteException e) {
            Timber.e(e, "getSuggestions: failed, database error.");
            return null;
        }
    }

    /**
     * Checks whether the given column exists in the given table of the given database.
     */
    static boolean isTableColumnMissing(@NonNull SupportSQLiteDatabase db, String table,
            String column) {
        Cursor cursor = db.query(SQLiteQueryBuilder
                .buildQueryString(false, table, null, null,
                        null, null, null, "1"));
        if (cursor == null) {
            return true;
        }
        boolean isColumnExisting = cursor.getColumnIndex(column) != -1;
        cursor.close();
        return !isColumnExisting;
    }
}
