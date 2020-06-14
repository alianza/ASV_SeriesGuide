package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Activity.EPISODE_TVDB_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Activity.SHOW_TVDB_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Activity.TIMESTAMP_MS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Activity._ID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

/**
 * Note: ensure to use CONFLICT_REPLACE when inserting to mimic SQLite UNIQUE x ON CONFLICT REPLACE.
 */
@Entity(tableName = Tables.ACTIVITY,
        indices = {@Index(value = EPISODE_TVDB_ID, unique = true)})
public class SgActivity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    public Integer id;

    /**
     * Unique string identifier.
     */
    @ColumnInfo(name = EPISODE_TVDB_ID)
    @NonNull
    public String episodeTvdbId;

    @ColumnInfo(name = SHOW_TVDB_ID)
    @NonNull
    public String showTvdbId;

    @ColumnInfo(name = TIMESTAMP_MS)
    public long timestampMs;
}
