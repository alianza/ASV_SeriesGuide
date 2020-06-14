package com.battlelancer.seriesguide.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.battlelancer.seriesguide.R;
import com.battlelancer.seriesguide.traktapi.TraktCredentials;
import com.battlelancer.seriesguide.ui.MoviesActivity;
import com.battlelancer.seriesguide.ui.NowFragmentHelper;
import com.battlelancer.seriesguide.ui.ShowsActivity;
import com.battlelancer.seriesguide.ui.shows.NowAdapter;
import com.battlelancer.seriesguide.ui.shows.NowAdapter.ItemClickListener;
import com.battlelancer.seriesguide.ui.shows.TraktRecentEpisodeHistoryLoader.Result;
import com.battlelancer.seriesguide.ui.streams.HistoryActivity;
import com.battlelancer.seriesguide.util.Utils;
import com.battlelancer.seriesguide.util.ViewTools;
import com.uwetrottmann.seriesguide.widgets.EmptyViewSwipeRefreshLayout;
import java.util.List;

/**
 * Displays recently watched movies, today's releases and recent watches from trakt friends (if
 * connected to trakt).
 */
public class MoviesNowFragment extends Fragment {

    @BindView(R.id.swipeRefreshLayoutNow) EmptyViewSwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerViewNow) RecyclerView recyclerView;
    @BindView(R.id.emptyViewNow) TextView emptyView;
    @BindView(R.id.containerSnackbar) View snackbar;
    @BindView(R.id.textViewSnackbar) TextView snackbarText;
    @BindView(R.id.buttonSnackbar) Button snackbarButton;

    private MoviesNowAdapter adapter;
    private boolean isLoadingRecentlyWatched;
    private boolean isLoadingFriends;
    private Unbinder unbinder;

    private NowFragmentHelper nowFragmentHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_now, container, false);
        unbinder = ButterKnife.bind(this, view);

        // define dataset
        adapter = new MoviesNowAdapter(getContext(), itemClickListener);

        nowFragmentHelper = new NowFragmentHelper(recyclerView, adapter,
                emptyView, snackbarText, snackbar, isLoadingRecentlyWatched, isLoadingFriends, this,
                swipeRefreshLayout);

        swipeRefreshLayout.setSwipeableChildren(R.id.scrollViewNow, R.id.recyclerViewNow);
        swipeRefreshLayout.setOnRefreshListener(this::refreshStream);
        swipeRefreshLayout.setProgressViewOffset(false,
                getResources().getDimensionPixelSize(
                        R.dimen.swipe_refresh_progress_bar_start_margin),
                getResources().getDimensionPixelSize(
                        R.dimen.swipe_refresh_progress_bar_end_margin));

        emptyView.setText(R.string.now_movies_empty);

        nowFragmentHelper.showError(null);
        snackbarButton.setText(R.string.refresh);
        snackbarButton.setOnClickListener(v -> refreshStream());

        // recycler view layout manager
        final int spanCount = getResources().getInteger(R.integer.grid_column_count);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter == null) {
                    return 1;
                }
                if (position >= adapter.getItemCount()) {
                    return 1;
                }
                // make headers and more links span all columns
                int type = adapter.getItem(position).type;
                return (type == NowAdapter.ItemType.HEADER || type == NowAdapter.ItemType.MORE_LINK)
                        ? spanCount : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        new ViewModelProvider(requireActivity()).get(MoviesActivityViewModel.class)
                .getScrollTabToTopLiveData()
                .observe(getViewLifecycleOwner(), event -> {
                    if (event != null
                            && event.getTabPosition() == MoviesActivity.TAB_POSITION_NOW) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewTools.setSwipeRefreshLayoutColors(requireActivity().getTheme(), swipeRefreshLayout);

        nowFragmentHelper.setMoviesNowFragmentAdapter();

        // if connected to trakt, replace local history with trakt history, show friends history
        if (TraktCredentials.get(getActivity()).hasCredentials()) {
            isLoadingRecentlyWatched = true;
            isLoadingFriends = true;
            nowFragmentHelper.showProgressBar(true);
            LoaderManager loaderManager = LoaderManager.getInstance(this);
            loaderManager.initLoader(MoviesActivity.NOW_TRAKT_USER_LOADER_ID, null,
                    recentlyTraktCallbacks);
            loaderManager.initLoader(MoviesActivity.NOW_TRAKT_FRIENDS_LOADER_ID, null,
                    traktFriendsHistoryCallbacks);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // guard against not attached to activity
        if (!isAdded()) {
            return;
        }

        inflater.inflate(R.menu.movies_now_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_action_movies_now_refresh) {
            refreshStream();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshStream() {
        nowFragmentHelper.showProgressBar(true);
        nowFragmentHelper.showError(null);

        // user might get disconnected during our life-time,
        // so properly clean up old loaders so they won't interfere
        if (TraktCredentials.get(getActivity()).hasCredentials()) {
            isLoadingRecentlyWatched = true;
            LoaderManager loaderManager = LoaderManager.getInstance(this);
            loaderManager.restartLoader(MoviesActivity.NOW_TRAKT_USER_LOADER_ID, null,
                    recentlyTraktCallbacks);
            isLoadingFriends = true;
            loaderManager.restartLoader(ShowsActivity.NOW_TRAKT_FRIENDS_LOADER_ID, null,
                    traktFriendsHistoryCallbacks);
        } else {
            // destroy trakt loaders and remove any shown error message
            nowFragmentHelper.destroyLoaderIfExists(MoviesActivity.NOW_TRAKT_USER_LOADER_ID);
            nowFragmentHelper.destroyLoaderIfExists(MoviesActivity.NOW_TRAKT_FRIENDS_LOADER_ID);
            nowFragmentHelper.showError(null);
        }
    }

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            NowAdapter.NowItem item = adapter.getItem(position);
            if (item == null) {
                return;
            }

            // more history link?
            if (item.type == NowAdapter.ItemType.MORE_LINK) {
                startActivity(new Intent(getActivity(), HistoryActivity.class).putExtra(
                        HistoryActivity.InitBundle.HISTORY_TYPE,
                        HistoryActivity.DISPLAY_MOVIE_HISTORY));
                return;
            }

            if (item.movieTmdbId == null) {
                return;
            }

            // display movie details
            Intent i = MovieDetailsActivity.intentMovie(getActivity(), item.movieTmdbId);

            // simple scale up animation as there are no images
            Utils.startActivityWithAnimation(getActivity(), i, view);
        }
    };

    private LoaderManager.LoaderCallbacks<Result>
            recentlyTraktCallbacks
            = new LoaderManager.LoaderCallbacks<Result>() {
        @Override
        public Loader<Result> onCreateLoader(int id, Bundle args) {
            return new TraktRecentMovieHistoryLoader(getActivity());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Result> loader,
                Result data) {
            if (!isAdded()) {
                return;
            }
            adapter.setRecentlyWatched(data.items);
            isLoadingRecentlyWatched = false;
            nowFragmentHelper.showProgressBar(false);
            nowFragmentHelper.showError(data.errorText);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Result> loader) {
            if (!isVisible()) {
                return;
            }
            // clear existing data
            adapter.setRecentlyWatched(null);
        }
    };

    private LoaderManager.LoaderCallbacks<List<NowAdapter.NowItem>> traktFriendsHistoryCallbacks
            = new LoaderManager.LoaderCallbacks<List<NowAdapter.NowItem>>() {
        @Override
        public Loader<List<NowAdapter.NowItem>> onCreateLoader(int id, Bundle args) {
            return new TraktFriendsMovieHistoryLoader(getActivity());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<NowAdapter.NowItem>> loader,
                List<NowAdapter.NowItem> data) {
            if (!isAdded()) {
                return;
            }
            adapter.setFriendsRecentlyWatched(data);
            isLoadingFriends = false;
            nowFragmentHelper.showProgressBar(false);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<NowAdapter.NowItem>> loader) {
            if (!isVisible()) {
                return;
            }
            // clear existing data
            adapter.setFriendsRecentlyWatched(null);
        }
    };
}
