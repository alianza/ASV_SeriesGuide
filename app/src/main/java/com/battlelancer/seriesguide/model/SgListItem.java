package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.LIST_ITEM_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems.TYPE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ListItems._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.LIST_ID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

/**
 * Note: ensure to use CONFLICT_REPLACE when inserting to mimic SQLite UNIQUE x ON CONFLICT REPLACE.
 */
@Entity(tableName = Tables.LIST_ITEMS,
        foreignKeys = @ForeignKey(entity = SgList.class,
                parentColumns = LIST_ID, childColumns = LIST_ID),
        indices = {
                @Index(value = LIST_ITEM_ID, unique = true),
                @Index(LIST_ID)
        }
)
public class SgListItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    public Integer id;

    @ColumnInfo(name = LIST_ITEM_ID)
    @NonNull
    public String listItemId;

    @ColumnInfo(name = TYPE)
    public int type;

    /**
     * Unique string identifier.
     */
    @ColumnInfo(name = LIST_ID)
    public String listId;
}
