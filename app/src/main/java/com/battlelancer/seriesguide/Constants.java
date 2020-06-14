package com.battlelancer.seriesguide;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.DVDNUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_GLOBAL;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.WATCHED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.SeasonsColumns.COMBINED;
import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.EPISODES;

import androidx.annotation.NonNull;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constants {

    /**
     * See {@link Episodes#FIRSTAIREDMS}.
     */
    public static final int EPISODE_UNKNOWN_RELEASE = -1;
    public static final String DESC = " DESC";

    public enum EpisodeSorting {
        LATEST_FIRST(0, "latestfirst", NUMBER + DESC),

        OLDEST_FIRST(1, "oldestfirst", NUMBER + " ASC"),

        UNWATCHED_FIRST(2, "unwatchedfirst", WATCHED + " ASC," + NUMBER + " ASC"),

        ALPHABETICAL_ASC(3, "atoz", TITLE + " COLLATE NOCASE ASC"),

        TOP_RATED(4, "toprated", EPISODES + "." + RATING_GLOBAL + " COLLATE NOCASE DESC"),

        DVDLATEST_FIRST(5, "dvdlatestfirst", DVDNUMBER + " DESC," + NUMBER
                + DESC),

        DVDOLDEST_FIRST(6, "dvdoldestfirst", DVDNUMBER + " ASC," + NUMBER
                + " ASC");

        private final int index;

        private final String value;

        private final String query;

        EpisodeSorting(int index, String value, String query) {
            this.index = index;
            this.value = value;
            this.query = query;
        }

        public int index() {
            return index;
        }

        public String value() {
            return value;
        }

        public String query() {
            return query;
        }

        @NonNull
        @Override
        public String toString() {
            return this.value;
        }

        private static final Map<String, EpisodeSorting> STRING_MAPPING = new HashMap<>();

        static {
            for (EpisodeSorting via : EpisodeSorting.values()) {
                STRING_MAPPING.put(via.toString().toUpperCase(Locale.US), via);
            }
        }

        public static EpisodeSorting fromValue(String value) {
            return STRING_MAPPING.get(value.toUpperCase(Locale.US));
        }
    }

    public enum SeasonSorting {
        LATEST_FIRST(0, "latestfirst", COMBINED + DESC),

        OLDEST_FIRST(1, "oldestfirst", COMBINED + " ASC");

        private final int index;

        private final String value;

        private final String query;

        SeasonSorting(int index, String value, String query) {
            this.index = index;
            this.value = value;
            this.query = query;
        }

        public int index() {
            return index;
        }

        public String value() {
            return value;
        }

        public String query() {
            return query;
        }

        @NonNull
        @Override
        public String toString() {
            return this.value;
        }

        private static final Map<String, SeasonSorting> STRING_MAPPING = new HashMap<>();

        static {
            for (SeasonSorting via : SeasonSorting.values()) {
                STRING_MAPPING.put(via.toString().toUpperCase(Locale.US), via);
            }
        }

        public static SeasonSorting fromValue(String value) {
            return STRING_MAPPING.get(value.toUpperCase(Locale.US));
        }
    }


}
