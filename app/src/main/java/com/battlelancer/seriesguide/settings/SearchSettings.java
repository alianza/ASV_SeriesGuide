package com.battlelancer.seriesguide.settings;

/**
 * {@link android.content.SharedPreferences} key suffixes for {@link
 * com.battlelancer.seriesguide.util.SearchHistory}.
 */
public class SearchSettings {

    private SearchSettings() {
        throw new IllegalStateException("Utility class");
    }

    public static final String KEY_SUFFIX_THETVDB = "thetvdb";
    public static final String KEY_SUFFIX_TMDB = "tmdb";
}
