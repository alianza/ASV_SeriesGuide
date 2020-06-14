package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.NOAIRDATECOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.TAGS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.TOTALCOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.UNAIREDCOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.WATCHCOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.SeasonsColumns.COMBINED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.REF_SHOW_ID;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

@Entity(
        tableName = Tables.SEASONS,
        foreignKeys = @ForeignKey(entity = SgShow.class,
                parentColumns = Shows._ID, childColumns = REF_SHOW_ID),
        indices = {@Index(REF_SHOW_ID)}
)
public class SgSeason {

    @PrimaryKey
    @ColumnInfo(name = _ID)
    public Integer tvdbId;

    @ColumnInfo(name = COMBINED)
    public Integer number;

    @ColumnInfo(name = REF_SHOW_ID)
    public String showTvdbId;

    @ColumnInfo(name = WATCHCOUNT)
    public Integer watchCount = 0;

    @ColumnInfo(name = UNAIREDCOUNT)
    public Integer notReleasedCount = 0;

    @ColumnInfo(name = NOAIRDATECOUNT)
    public Integer noReleaseDateCount = 0;

    @ColumnInfo(name = TAGS)
    public String tags = "";

    @ColumnInfo(name = TOTALCOUNT)
    public Integer totalCount = 0;
}
