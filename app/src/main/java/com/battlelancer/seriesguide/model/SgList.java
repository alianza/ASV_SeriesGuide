package com.battlelancer.seriesguide.model;

import static android.provider.BaseColumns._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.LIST_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.NAME;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.ORDER;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

/**
 * Note: ensure to use CONFLICT_REPLACE when inserting to mimic SQLite UNIQUE x ON CONFLICT REPLACE.
 */
@Entity(tableName = Tables.LISTS,
        indices = {@Index(value = LIST_ID, unique = true)})
public class SgList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    public Integer id;

    /**
     * Unique string identifier.
     */
    @ColumnInfo(name = LIST_ID)
    @NonNull
    public String listId;

    @ColumnInfo(name = NAME)
    @NonNull
    public String name;

    /**
     * Helps determine list order in addition to the list name. Integer.
     * <pre>
     * Range: 0 to MAX INT
     * Default: 0
     * </pre>
     */
    @ColumnInfo(name = ORDER)
    public Integer order = 0;

}
