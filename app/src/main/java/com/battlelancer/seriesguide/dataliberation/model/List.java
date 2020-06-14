
package com.battlelancer.seriesguide.dataliberation.model;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.LIST_ID;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.NAME;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.ORDER;

import android.content.ContentValues;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("list_id")
    public String listId;
    public String name;
    public int order;

    public java.util.List<ListItem> items;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(LIST_ID, listId);
        values.put(NAME, name);
        values.put(ORDER, order);
        return values;
    }
}
