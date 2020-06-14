package com.battlelancer.seriesguide.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.ABSOLUTE_NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.COLLECTED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.DIRECTORS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.DVDNUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.FIRSTAIREDMS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.GUESTSTARS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.IMAGE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.IMDBID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.LAST_EDITED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.LAST_UPDATED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.OVERVIEW;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_GLOBAL;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_USER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.RATING_VOTES;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.SEASON;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.TITLE;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.WATCHED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.WRITERS;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes._ID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.SeasonsColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.Shows;
import com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns;
import com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables;

@Entity(
        tableName = Tables.EPISODES,
        foreignKeys = {
                @ForeignKey(entity = SgSeason.class,
                        parentColumns = Seasons._ID, childColumns = SeasonsColumns.REF_SEASON_ID),
                @ForeignKey(entity = SgShow.class,
                        parentColumns = Shows._ID, childColumns = ShowsColumns.REF_SHOW_ID)
        },
        indices = {
                @Index(SeasonsColumns.REF_SEASON_ID),
                @Index(ShowsColumns.REF_SHOW_ID)
        }
)
public class SgEpisode {

    @PrimaryKey
    @ColumnInfo(name = _ID)
    public int tvdbId;

    @NonNull
    @ColumnInfo(name = TITLE)
    public String title = "";
    @ColumnInfo(name = OVERVIEW)
    public String overview;

    @ColumnInfo(name = NUMBER)
    public int number = 0;
    @ColumnInfo(name = SEASON)
    public int season = 0;
    @ColumnInfo(name = DVDNUMBER)
    public Double dvdNumber;

    @ColumnInfo(name = SeasonsColumns.REF_SEASON_ID)
    public int seasonTvdbId;
    @ColumnInfo(name = ShowsColumns.REF_SHOW_ID)
    public int showTvdbId;

    @ColumnInfo(name = WATCHED)
    public int watched = 0;

    @ColumnInfo(name = DIRECTORS)
    public String directors = "";
    @ColumnInfo(name = GUESTSTARS)
    public String guestStars = "";
    @ColumnInfo(name = WRITERS)
    public String writers = "";
    @ColumnInfo(name = IMAGE)
    public String image = "";

    @ColumnInfo(name = FIRSTAIREDMS)
    public long firstReleasedMs = -1L;

    @ColumnInfo(name = COLLECTED)
    public boolean collected = false;

    @ColumnInfo(name = RATING_GLOBAL)
    public Double ratingGlobal;
    @ColumnInfo(name = RATING_VOTES)
    public Integer ratingVotes;
    @ColumnInfo(name = RATING_USER)
    public Integer ratingUser;

    @ColumnInfo(name = IMDBID)
    public String imdbId = "";

    @ColumnInfo(name = LAST_EDITED)
    public long lastEditedSec = 0L;

    @ColumnInfo(name = ABSOLUTE_NUMBER)
    public Integer absoluteNumber;

    @ColumnInfo(name = LAST_UPDATED)
    public long lastUpdatedSec = 0L;
}
