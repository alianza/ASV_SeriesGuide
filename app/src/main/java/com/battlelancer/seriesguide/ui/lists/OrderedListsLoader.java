package com.battlelancer.seriesguide.ui.lists;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Lists.LIST_ID;

import android.content.Context;
import android.database.Cursor;
import com.uwetrottmann.androidutils.GenericSimpleLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads all user-created lists from the database into a list.
 */
class OrderedListsLoader extends GenericSimpleLoader<List<OrderedListsLoader.OrderedList>> {

    static class OrderedList {

        public String id;
        public String name;

        OrderedList(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    OrderedListsLoader(Context context) {
        super(context);
    }

    @Override
    public List<OrderedList> loadInBackground() {
        List<OrderedList> items = new ArrayList<>();

        Cursor query = getContext().getContentResolver()
                .query(Lists.CONTENT_URI,
                        ListsQuery.PROJECTION, null, null,
                        Lists.SORT_ORDER_THEN_NAME);
        if (query == null) {
            return items;
        }

        while (query.moveToNext()) {
            items.add(new OrderedList(
                    query.getString(ListsQuery.ID),
                    query.getString(ListsQuery.NAME)
            ));
        }

        query.close();

        return items;
    }

    private interface ListsQuery {
        String[] PROJECTION = new String[] {
                Lists._ID,
                LIST_ID,
                Lists.NAME
        };

        int ID = 1;
        int NAME = 2;
    }
}
