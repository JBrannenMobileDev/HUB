package jjpartnership.hub.view_layer.activities.search_activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by Jonathan on 6/10/2018.
 */

public class SearchAllManager {
    private SearchAllResultsPagerAdapter allPagerAdapter;
    private ViewPager pager;
    private TabLayout tabs;

    private AccountSearchResultsFragment accountFragment;
    private UsersSearchResultFragment usersFragment;
    private SharedLeadsSearchResultFragment leadsFragment;

    public SearchAllManager(FragmentManager fm, ViewPager searchResultPager, TabLayout tabs){
        allPagerAdapter = new SearchAllResultsPagerAdapter(fm);
        pager = searchResultPager;
        accountFragment = new AccountSearchResultsFragment();
        usersFragment = new UsersSearchResultFragment();
        leadsFragment = new SharedLeadsSearchResultFragment();
        this.tabs = tabs;
    }

    private void initViewPager(FragmentPagerAdapter adapter){
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    public void initAllMode(){
        initViewPager(allPagerAdapter);
        if(allPagerAdapter.getCount() == 0) {
            allPagerAdapter.addFragment(accountFragment, "Accounts(0)");
            allPagerAdapter.addFragment(usersFragment, "Users(0)");
            allPagerAdapter.addFragment(leadsFragment, "Shared Leads(0)");
            allPagerAdapter.notifyDataSetChanged();
        }
    }

    public void onQueryResults(List<AccountRowItem> accountSearchResults, List<UserRealm> userSearchResults,
                               List<GroupChatRealm> sharedLeads, String query){

            allPagerAdapter.onQueryResultsAccount(accountSearchResults, query, 0);
            allPagerAdapter.onQueryResultsUser(userSearchResults, query, 1);
            allPagerAdapter.onQueryResultsSharedLeads(sharedLeads, query, 2);
            setTabTitle("Accounts(" + accountSearchResults.size() + ")", 0);
            setTabTitle("Users(" + userSearchResults.size() + ")", 1);
            setTabTitle("Shared Leads(" + sharedLeads.size() + ")", 2);
            if(accountSearchResults.size() == 0 && userSearchResults.size() > 0 && sharedLeads.size() == 0){
                setPagerPosition(1);
            }else if(accountSearchResults.size() > 0 && userSearchResults.size() == 0 && sharedLeads.size() == 0){
                setPagerPosition(0);
            }else if(accountSearchResults.size() == 0 && userSearchResults.size() == 0 && sharedLeads.size() > 0){
                setPagerPosition(2);
            }
    }

    public void setPagerPosition(int position){
        pager.setCurrentItem(position);
    }

    public void setTabTitle(String title, int position){
        tabs.getTabAt(position).setText(title);
    }
}
