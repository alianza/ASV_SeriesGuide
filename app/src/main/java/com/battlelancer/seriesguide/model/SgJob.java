package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs.CREATED_MS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs.EXTRAS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs.TYPE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Jobs._ID;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

/**
 * Note: ensure to use CONFLICT_REPLACE when inserting to mimic SQLite UNIQUE x ON CONFLICT REPLACE.
 */
@Entity(tableName = Tables.JOBS,
        indices = {@Index(value = CREATED_MS, unique = true)})
public class SgJob {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    public Integer id;

    @ColumnInfo(name = CREATED_MS)
    public Long createdMs;

    @ColumnInfo(name = TYPE)
    public Integer type;

    @ColumnInfo(name = EXTRAS)
    public byte[] extras;
}
