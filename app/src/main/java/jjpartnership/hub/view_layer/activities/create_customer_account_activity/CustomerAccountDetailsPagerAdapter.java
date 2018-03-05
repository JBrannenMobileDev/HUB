package jjpartnership.hub.view_layer.activities.create_customer_account_activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import jjpartnership.hub.view_layer.activities.create_agent_account_activity.UserInfoNamePagerFragment;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.UserPositionInfoPagerFragment;

/**
 * Created by jbrannen on 5/22/17.
 */

public class CustomerAccountDetailsPagerAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;
    private UserInfoNamePagerFragment step1Fragment;
    private UserPositionInfoPagerFragment step2Fragment;

    public CustomerAccountDetailsPagerAdapter(FragmentManager fm) {
        super(fm);
        step1Fragment = new UserInfoNamePagerFragment();
        step2Fragment = new UserPositionInfoPagerFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return step1Fragment;
            case 1:
                return step2Fragment;
            default:
               return step1Fragment;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
