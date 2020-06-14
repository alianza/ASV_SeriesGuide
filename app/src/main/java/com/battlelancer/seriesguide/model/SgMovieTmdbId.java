package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Movies.TMDB_ID;

import androidx.room.ColumnInfo;

public class SgMovieTmdbId {

    @ColumnInfo(name = TMDB_ID)
    public int tmdbId;

}
