package com.battlelancer.seriesguide.service;

import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.NUMBER;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.Episodes.SEASON;
import static com.battlelancer.seriesguide.provider.SeriesGuideContract.ShowsColumns.REF_SHOW_ID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.core.app.NotificationManagerCompat;
import com.battlelancer.seriesguide.SgApp;
import com.battlelancer.seriesguide.ui.episodes.EpisodeFlags;
import com.battlelancer.seriesguide.ui.episodes.EpisodeTools;

/**
 * Listens to notification actions, currently only setting an episode watched.
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NotificationService.ACTION_CLEARED.equals(intent.getAction())) {
            NotificationService.handleDeleteIntent(context, intent);
            return;
        }

        int episodeTvdbvId = intent.getIntExtra(NotificationService.EXTRA_EPISODE_TVDBID, -1);
        if (episodeTvdbvId <= 0) {
            return; // not notification set watched action
        }

        // query for episode details
        Cursor query = context.getContentResolver()
                .query(Episodes.buildEpisodeWithShowUri(episodeTvdbvId),
                        new String[] {
                                REF_SHOW_ID,
                                SEASON,
                                NUMBER }, null, null, null);
        if (query == null) {
            return;
        }
        if (!query.moveToFirst()) {
            query.close();
            return;
        }
        int showTvdbId = query.getInt(0);
        int season = query.getInt(1);
        int episode = query.getInt(2);
        query.close();

        // mark episode watched
        EpisodeTools.episodeWatched(context, showTvdbId, episodeTvdbvId, season, episode,
                EpisodeFlags.WATCHED);

        // dismiss the notification
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.cancel(SgApp.NOTIFICATION_EPISODE_ID);
        // replicate delete intent
        NotificationService.handleDeleteIntent(context, intent);
    }
}
