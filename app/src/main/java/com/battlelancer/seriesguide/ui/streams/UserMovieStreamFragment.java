package com.battlelancer.seriesguide.ui.streams;

import static com.battlelancer.seriesguide.ui.streams.TraktMovieHistoryLoader.Result;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.battlelancer.seriesguide.ui.movies.MovieDetailsActivity;
import com.battlelancer.seriesguide.ui.streams.SectionedHistoryAdapter.OnItemClickListener;

/**
 * Displays a stream of movies the user has recently watched on trakt.
 */
public class UserMovieStreamFragment extends StreamFragment {

    private MovieHistoryAdapter adapter;

    @NonNull
    @Override
    protected ListAdapter getListAdapter() {
        if (adapter == null) {
            adapter = new MovieHistoryAdapter(getActivity(), itemClickListener);
        }
        return adapter;
    }

    @Override
    protected void initializeStream() {
        LoaderManager.getInstance(this).initLoader(HistoryActivity.MOVIES_LOADER_ID, null,
                activityLoaderCallbacks);
    }

    @Override
    protected void refreshStream() {
        LoaderManager.getInstance(this).restartLoader(HistoryActivity.MOVIES_LOADER_ID, null,
                activityLoaderCallbacks);
    }

    private OnItemClickListener itemClickListener = (view, item) -> {
        if (item == null) {
            return;
        }

        // display movie details
        if (item.movie == null || item.movie.ids == null) {
            return;
        }
        Intent i = MovieDetailsActivity.intentMovie(getActivity(), item.movie.ids.tmdb);

        ActivityCompat.startActivity(requireContext(), i, ActivityOptionsCompat
                .makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight())
                .toBundle()
        );
    };

    private LoaderManager.LoaderCallbacks<Result> activityLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Result>() {
                @Override
                public Loader<Result> onCreateLoader(int id, Bundle args) {
                    showProgressBar(true);
                    return new TraktMovieHistoryLoader(getActivity());
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Result> loader,
                        Result data) {
                    if (!isAdded()) {
                        return;
                    }
                    adapter.setData(data.results);
                    setEmptyMessage(data.emptyText);
                    showProgressBar(false);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Result> loader) {
                    // keep current data
                }
            };
}
