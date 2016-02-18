package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.ViewPagerAdapter;
import itba.undiaparadar.interfaces.TitleProvider;

public class AchievementsFragment extends Fragment implements TitleProvider {

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
		final ViewPager viewPager = (ViewPager) view.findViewById(R.id.tabanim_viewpager);
		setupViewPager(viewPager);
		final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabanim_tabs);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
		adapter.addFrag(getString(R.string.total));
		adapter.addFrag(getString(R.string.pledges_done));
		adapter.addFrag(getString(R.string.pledges_neutral));
		adapter.addFrag(getString(R.string.pledges_failed));
		viewPager.setAdapter(adapter);
	}

	@Override
	public String getTitle() {
		return getString(R.string.achievements);
	}

}
