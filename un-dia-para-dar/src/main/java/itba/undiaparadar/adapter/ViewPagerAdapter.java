package itba.undiaparadar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import itba.undiaparadar.fragments.AchievementsTabFragment;
import itba.undiaparadar.model.PledgeStatus;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AchievementsTabFragment.newInstance();
            case 1:
                return AchievementsTabFragment.newInstance(PledgeStatus.DONE);
            case 2:
                return AchievementsTabFragment.newInstance(PledgeStatus.NEUTRAL);
            case 3:
                return AchievementsTabFragment.newInstance(PledgeStatus.FAILED);
            default:
                return AchievementsTabFragment.newInstance(PledgeStatus.DONE);
        }
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    public void addFrag(String title) {
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}