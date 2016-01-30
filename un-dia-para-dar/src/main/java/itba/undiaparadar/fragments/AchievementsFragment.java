package itba.undiaparadar.fragments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.AchievementsAdapter;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.services.PledgeService;
import itba.undiaparadar.services.UserService;
import itba.undiaparadar.utils.DividerItemDecoration;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.UnDiaParaDarDialog;

public class AchievementsFragment extends Fragment implements TitleProvider, FindCallback<Pledge> {
	@Inject
	private PledgeService pledgeService;
	@Inject
	private UserService userService;
	private AchievementsAdapter adapter;
	private Dialog dialog;

	public static Fragment newInstance() {
		return new AchievementsFragment();
	}

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_achievements, null, false);
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapter.clear();
				mSwipeRefreshLayout.setRefreshing(false);
				refreshItems();
			}
		});
		adapter = new AchievementsAdapter(getActivity());
		recyclerView.setAdapter(adapter);
	}

	private void refreshItems() {
		pledgeService.retrievePledges(userService.getUser().getUserId(), this);
	}

	@Override
	public void onResume() {
		super.onResume();
		final Drawable imageDrawable = new GifDrawable(R.raw.logo_loading, getActivity());
		dialog = new UnDiaParaDarDialog(getActivity(), imageDrawable);
		dialog.show();
		refreshItems();
	}

	@Override
	public String getTitle() {
		return getString(R.string.achievements);
	}

	@Override
	public void done(final List<Pledge> objects, final ParseException e) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		adapter.setPledgeList(objects);
	}
}
