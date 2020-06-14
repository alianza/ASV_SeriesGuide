
package com.battlelancer.seriesguide.dataliberation.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.COMBINED;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.NOAIRDATECOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.TOTALCOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.UNAIREDCOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons.WATCHCOUNT;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Seasons._ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.REF_SHOW_ID;

import android.content.ContentValues;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Season {

    @SerializedName("tvdb_id")
    public int tvdbId;

    public int seasonProp;

    public List<Episode> episodes;

    public ContentValues toContentValues(int showTvdbId) {
        ContentValues values = new ContentValues();
        values.put(_ID, tvdbId);
        values.put(REF_SHOW_ID, showTvdbId);
        values.put(COMBINED, seasonProp >= 0 ? seasonProp : 0);
        // set default values
        values.put(WATCHCOUNT, 0);
        values.put(UNAIREDCOUNT, 0);
        values.put(NOAIRDATECOUNT, 0);
        values.put(TOTALCOUNT, 0);
        return values;
    }

}
