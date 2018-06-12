package jjpartnership.hub.view_layer.activities.search_activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by Jonathan on 6/10/2018.
 */
public class SearchAllResultsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> pageTitles;

    public void addFragment(Fragment fragment, String pageTitle){
        fragments.add(fragment);
        pageTitles.add(pageTitle);
    }

    public SearchAllResultsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        pageTitles = new ArrayList<>();
    }

    public void onQueryResultsAccount(List<AccountRowItem> items, String query, int position){
        ((AccountSearchResultsFragment) fragments.get(position)).onResultsReceived(items, query);
    }

    public void onQueryResultsUser(List<UserRealm> items, String query, int position){
        ((UsersSearchResultFragment) fragments.get(position)).onResultsReceived(items, query);
    }

    public void onQueryResultsSharedLeads(List<GroupChatRealm> items, String query, int position){
        ((SharedLeadsSearchResultFragment) fragments.get(position)).onResultsReceived(items, query);
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }
}
