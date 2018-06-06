package jjpartnership.hub.view_layer.activities.main_activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.DirectItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.NewMessageVibrateUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.account_activity.AccountChatActivity;
import jjpartnership.hub.view_layer.activities.boot_activity.BootActivity;
import jjpartnership.hub.view_layer.activities.direct_message_activity.DirectMessageActivity;
import jjpartnership.hub.view_layer.activities.group_chat_activity.GroupChatActivity;
import jjpartnership.hub.view_layer.activities.user_profile_activity.UserProfileActivity;
import jjpartnership.hub.view_layer.custom_views.BackAwareSearchView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView,
        AccountSearchResultsFragment.OnFragmentInteractionListener, UsersSearchResultFragment.OnFragmentInteractionListener,
        SharedLeadsSearchResultFragment.OnFragmentInteractionListener{
    @BindView(R.id.search_selected_overlay) ImageView overlayImage;
    @BindView(R.id.search_results_layout) FrameLayout searchResultsLayout;
    @BindView(R.id.recent_list_view) RecyclerView recentRecyclerView;
    @BindView(R.id.recent_title_tv) TextView recentTitle;
    @BindView(R.id.accounts_list_view) RecyclerView accountsRecyclerView;
    @BindView(R.id.direct_messages_list_view) RecyclerView directMessagesRecyclerView;
    @BindView(R.id.group_messages_recycler_view) RecyclerView groupMessagesRecyclerView;
    @BindView(R.id.recent_empty_state_layout) LinearLayout recent_empty_layout;
    @BindView(R.id.accounts_empty_state_layout) LinearLayout accounts_empty_layout;
    @BindView(R.id.direc_messages_empty_state_layout) LinearLayout direct_messages_empty_state;
    @BindView(R.id.group_messages_empty_state_layout) LinearLayout group_messages_empty_state;
    @BindView(R.id.main_scrollview) NestedScrollView scrollView;
    @BindView(R.id.show_all_recent_tv) TextView showAllTv;
    @BindView(R.id.new_message_icon) FrameLayout newMessagesIcon;
    @BindView(R.id.new_message_count_tv) TextView newMessageCountTv;
    @BindView(R.id.hide_recent_tv) TextView hideTv;
    @BindView(R.id.recent_frame_layout) FrameLayout recentFrameLayout;
    @BindView(R.id.search_results_pager)ViewPager searchResultPager;
    @BindView(R.id.search_results_tabs)TabLayout searchResultsTabs;
    @BindView(R.id.add_account_iv)ImageView addAccountButton;
    @BindView(R.id.add_direct_message_iv)ImageView addDirectMessageIv;
    @BindView(R.id.add_group_message_iv)ImageView shareLeadIv;

    private Animation slideUpAnimation, slideDownAnimation, enterFromRightAnimation, exitToRightAnimation;
    private MainPresenter presenter;
    private AccountRecyclerAdapter accountsAdapter;
    private RecentRecyclerAdapter recentRecyclerAdapter;
    private DirectMessageRecyclerAdapter directRecyclerAdapter;
    private GroupMessagesRecyclerAdapter groupRecyclerAdapter;
    private BaseCallback<AccountRowItem> accountSelectedCallback;
    private BaseCallback<RowItem> recentSelectedCallback;
    private BaseCallback<DirectItem> directMessageSelectedCallback;
    private BaseCallback<GroupChatRealm> groupMessageSelectedCallback;
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;
    private boolean isVisible;
    private RealmList<RowItem> newRealmList;
    private boolean recentExpanded;
    private int[] hideBtLocation;
    private TextView nav_user_name;
    private TextView nav_user_email;
    private TextView nav_user_icon;
    private LinearLayout nav_main_header_layout;
    private BackAwareSearchView searchViewBackAware;
    private BackAwareSearchView.BackPressedListener backPressedListenerPhone;
    private SearchResultsPagerAdapter pagerAdapter;
    private MenuItem searchItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        mAppBarLayout = findViewById(R.id.mAppBarLayout);
        mAppBarLayout.setElevation(0);
        isVisible = true;
        hideBtLocation = new int[2];


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccentVeryDark));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        directMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        groupMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initAnimations();
        initAdapters();
        initScrollViewListener();

        View hView = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);

        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_checked}, // checked
                new int[]{-android.R.attr.state_checked}
        };

        int[] color = new int[]{
                getResources().getColor(R.color.colorPrimaryLight),
                getResources().getColor(R.color.colorPrimaryLight)
        };
        ColorStateList csl = new ColorStateList(state, color);
        navigationView.setItemTextColor(csl);

        setNavIconColors(navigationView);
        nav_user_name = hView.findViewById(R.id.nav_user_name);
        nav_user_email = hView.findViewById(R.id.nav_user_email);
        nav_user_icon = hView.findViewById(R.id.nav_user_icon);
        nav_main_header_layout = hView.findViewById(R.id.main_header_linear_layout);
        initDrawerHeaderClickListeners();

        pagerAdapter = new SearchResultsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new AccountSearchResultsFragment(), "Accounts(0)");
        pagerAdapter.addFragment(new UsersSearchResultFragment(), "Users(0)");
        pagerAdapter.addFragment(new SharedLeadsSearchResultFragment(), "Shared Leads(0)");
        searchResultPager.setAdapter(pagerAdapter);
        searchResultPager.setOffscreenPageLimit(3);
        searchResultsTabs.setupWithViewPager(searchResultPager);
        searchResultsTabs.setTabMode(TabLayout.MODE_FIXED);

        presenter = new MainPresenterImp(this);
    }

    private void initDrawerHeaderClickListeners() {
        nav_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class).putExtra("userId", UserPreferences.getInstance().getUid()));
            }
        });
        nav_user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class).putExtra("userId", UserPreferences.getInstance().getUid()));
            }
        });
        nav_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class).putExtra("userId", UserPreferences.getInstance().getUid()));
            }
        });
    }

    private void setNavIconColors(NavigationView navigationView) {
        navigationView.getMenu()
                .findItem(R.id.nav_search)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);

        navigationView.getMenu()
                .findItem(R.id.nav_share_lead)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.colorAccentVeryDark), PorterDuff.Mode.SRC_IN);

        navigationView.getMenu()
                .findItem(R.id.nav_new_direct_message)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.colorAccentVeryDark), PorterDuff.Mode.SRC_IN);

        navigationView.getMenu()
                .findItem(R.id.nav_add_account)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.colorAccentVeryDark), PorterDuff.Mode.SRC_IN);

        navigationView.getMenu()
                .findItem(R.id.nav_settings)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);
    }

    private void initScrollViewListener() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollView.canScrollVertically(-1)) {
                    hideTv.setElevation(4);
                    setToolbarElevation(4);
                    if (recentExpanded) {
                        recentFrameLayout.getLocationOnScreen(hideBtLocation);
                        if (hideBtLocation[1] + recentFrameLayout.getHeight() - 54 <= 0) {
                            animateHideBottonHide();
                        } else {
                            animateHideButtonShow();
                        }
                    }
                } else {
                    if (recentExpanded) hideTv.setElevation(0);
                    setToolbarElevation(0);
                }
            }
        });
    }

    @Override
    public void setToolbarElevation(float height) {
        mAppBarLayout.setElevation(DpUtil.pxFromDp(this, height));
    }

    @Override
    public void VibratePhone() {
        if (isVisible) NewMessageVibrateUtil.vibratePhone(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        isVisible = false;
        super.onDestroy();
    }

    private void initAdapters() {
        accountSelectedCallback = new BaseCallback<AccountRowItem>() {
            @Override
            public void onResponse(AccountRowItem rowItem) {
                launchAccountDetailsIntent(rowItem);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        recentSelectedCallback = new BaseCallback<RowItem>() {
            @Override
            public void onResponse(RowItem rowItem) {
                if (rowItem.getItemType().equals(RowItem.TYPE_GROUP_CHAT)) {
                    Intent groupChatIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
                    groupChatIntent.putExtra("chatId", rowItem.getChatId());
                    startActivity(groupChatIntent);
                } else {
                    Intent directMessageIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
                    DirectChatRealm dChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", rowItem.getAccountId()).findFirst();
                    if (dChat != null)
                        directMessageIntent.putExtra("toUid", dChat.getDirectChatUid(UserPreferences.getInstance().getUid()));
                    directMessageIntent.putExtra("uid", UserPreferences.getInstance().getUid());
                    startActivity(directMessageIntent);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        directMessageSelectedCallback = new BaseCallback<DirectItem>() {
            @Override
            public void onResponse(DirectItem directItem) {
                Intent directMessageIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
                DirectChatRealm dChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", directItem.getDirectChatId()).findFirst();
                if (dChat != null)
                    directMessageIntent.putExtra("toUid", dChat.getDirectChatUid(UserPreferences.getInstance().getUid()));
                directMessageIntent.putExtra("uid", UserPreferences.getInstance().getUid());
                startActivity(directMessageIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        groupMessageSelectedCallback = new BaseCallback<GroupChatRealm>() {
            @Override
            public void onResponse(GroupChatRealm chat) {
                launchGroupMessageIntent(chat);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), new MainRecentModel(new RealmList<RowItem>()), recentSelectedCallback);
        directRecyclerAdapter = new DirectMessageRecyclerAdapter(getApplicationContext(), new MainDirectMessagesModel(new RealmList<DirectItem>()), directMessageSelectedCallback);
        groupRecyclerAdapter = new GroupMessagesRecyclerAdapter(getApplicationContext(), new ArrayList<GroupChatRealm>(), groupMessageSelectedCallback);
        recentRecyclerView.setAdapter(recentRecyclerAdapter);
        directMessagesRecyclerView.setAdapter(directRecyclerAdapter);
        groupMessagesRecyclerView.setAdapter(groupRecyclerAdapter);
    }

    private void launchGroupMessageIntent(GroupChatRealm chat){
        Intent groupChatIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
        groupChatIntent.putExtra("chatId", chat.getChatId());
        startActivity(groupChatIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(searchResultsLayout.getVisibility() == View.VISIBLE){
                hideOverlayImage();
                searchResultsLayout.startAnimation(slideDownAnimation);
                searchResultsLayout.setVisibility(View.GONE);
                if (scrollView.canScrollVertically(-1)) {
                    setToolbarElevation(4);
                }else{
                    setToolbarElevation(0);
                }
                if(searchViewBackAware != null) {
                    if (!searchViewBackAware.isIconified()) {
                        searchViewBackAware.setIconified(true);
                    }
                    searchViewBackAware.clearFocus();
                    backPressedListenerPhone.onImeBack(searchViewBackAware);
                }
            }else {
                super.onBackPressed();
            }
        }
    }

    @OnClick(R.id.add_account_iv)
    public void onAddAccountClicked() {
        showSearchSelectedView();
        searchResultPager.setCurrentItem(0);
    }

    @OnClick(R.id.add_direct_message_iv)
    public void onNewDirectMessageClicked() {
        showSearchSelectedView();
        searchResultPager.setCurrentItem(1);
    }

    @OnClick(R.id.add_group_message_iv)
    public void onAddShareLeadClicked(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchItem = menu.findItem(R.id.app_bar_search);
        searchViewBackAware = (BackAwareSearchView) searchItem.getActionView();
        searchViewBackAware.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        initSearchView();
        return true;
    }

    private void initAnimations() {
        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_animation);
        enterFromRightAnimation = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        exitToRightAnimation = AnimationUtils.loadAnimation(this, R.anim.exit_to_right);
    }

    private void initSearchView() {
        backPressedListenerPhone = new BackAwareSearchView.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareSearchView searchView) {
                searchView.clearFocus();
                if (searchView.getQuery().length() == 0) {
                    hideOverlayImage();
                    searchResultsLayout.startAnimation(slideDownAnimation);
                    searchResultsLayout.setVisibility(View.GONE);
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                    }
                }
            }
        };
        searchViewBackAware.setBackPressedListener(backPressedListenerPhone);

        searchViewBackAware.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onHideSearchView();
                return false;
            }
        });

        searchViewBackAware.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlayImage.setVisibility(View.VISIBLE);
                searchResultsLayout.setVisibility(View.VISIBLE);
                searchResultsLayout.startAnimation(slideUpAnimation);
                setToolbarElevation(0);
            }
        });

        searchViewBackAware.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 1) {
                    presenter.onSearchQuery(newText);
                }else{
                    pagerAdapter.onQueryResultsAccount(new ArrayList<AccountRowItem>(), "");
                    pagerAdapter.onQueryResultsUser(new ArrayList<UserRealm>(), "");
                    pagerAdapter.onQueryResultsSharedLeads(new ArrayList<GroupChatRealm>(), "");
                    setAccountsTabTitle("Accounts(0)");
                    setUsersTabTitle("Users(0)");
                    setSharedLeadTitle("Shared Leads(0)");
                }
                return false;
            }
        });
    }

    private void onHideSearchView(){
        hideOverlayImage();
        searchResultsLayout.startAnimation(slideDownAnimation);
        searchResultsLayout.setVisibility(View.GONE);
        if (scrollView.canScrollVertically(-1)) {
            setToolbarElevation(4);
        }else{
            setToolbarElevation(0);
        }
    }

    private void hideOverlayImage() {
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animation.setDuration(200);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overlayImage.setVisibility(View.GONE);
            }
        }, 200);
    }

    private void showSearchSelectedView(){
        overlayImage.setVisibility(View.VISIBLE);
        searchResultsLayout.setVisibility(View.VISIBLE);
        searchResultsLayout.startAnimation(slideUpAnimation);
        setToolbarElevation(0);
        searchViewBackAware.setIconified(false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share_lead) {

        } else if (id == R.id.nav_search) {
            showSearchSelectedView();
        } else if (id == R.id.nav_new_direct_message) {
            showSearchSelectedView();
            searchResultPager.setCurrentItem(1);
        } else if (id == R.id.nav_settings) {
            FirebaseAuth.getInstance().signOut();
            DataManager.getInstance().clearRealmData();
            startActivity(new Intent(getApplicationContext(), BootActivity.class));
        } else if(id == R.id.nav_share_lead){
            showSearchSelectedView();
            searchResultPager.setCurrentItem(2);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.show_all_recent_tv)
    public void onShowAllClicked() {
        recentExpanded = true;
        if (scrollView.getScrollY() == 0) {
            hideTv.setElevation(0);
        }
        presenter.onShowAllClicked();
        animateHideButtonShow();
        showAllTv.setVisibility(View.GONE);
    }

    @OnClick(R.id.hide_recent_tv)
    public void onHideClicked() {
        recentExpanded = false;
        presenter.onRestoreRecentModel();
        animateHideBottonHide();
        showAllTv.setVisibility(View.VISIBLE);
        scrollView.scrollTo(0, 0);
        hideTv.setElevation(0);
    }

    @Override
    public void onShowAll(MainRecentModel recentModel) {
        if (recentModel.getRowItems() != null && recentModel.getRowItems().size() > 0) {
            recent_empty_layout.setVisibility(View.GONE);
            if (recentRecyclerAdapter == null) {
                recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), new MainRecentModel(new RealmList<RowItem>()), recentSelectedCallback);
                recentRecyclerView.setAdapter(recentRecyclerAdapter);
            } else {
                recentRecyclerAdapter.onDataSetChanged(recentModel);
            }
            if (newRealmList.size() > recentModel.getRowItems().size()) {
                recentRecyclerAdapter.notifyItemRangeInserted(newRealmList.size(), recentModel.getRowItems().size() - newRealmList.size());
            } else {
                recentRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void animateHideButtonShow() {
        hideTv.animate().scaleX(0).setDuration(0);
        hideTv.animate().scaleY(0).setDuration(0);
        hideTv.setVisibility(View.VISIBLE);
        hideTv.animate().scaleY(1).setDuration(100);
        hideTv.animate().scaleX(1).setDuration(100);
    }

    private void animateHideBottonHide() {
        hideTv.animate().scaleX(0).setDuration(100);
        hideTv.animate().scaleY(0).setDuration(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideTv.setVisibility(View.GONE);
            }
        }, 100);
    }


    @Override
    public void onRecentModelReceived(MainRecentModel recentModel) {
        if (recentExpanded) {
            onShowAll(recentModel);
        } else {
            int newMessagesCount = 0;
            if (recentModel.getRowItems() != null && recentModel.getRowItems().size() > 0) {
                recent_empty_layout.setVisibility(View.GONE);
                if (recentRecyclerAdapter == null) {
                    recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), new MainRecentModel(new RealmList<RowItem>()), recentSelectedCallback);
                    recentRecyclerView.setAdapter(recentRecyclerAdapter);
                } else {
                    for (int i = 0; i < recentModel.getRowItems().size(); i++) {
                        if (recentModel.getRowItems().get(i).isNewMessage()) {
                            newMessagesCount++;
                        }
                    }
                    if (recentModel.getRowItems().size() > 5) {
                        showAllTv.setEnabled(true);
                        showAllTv.setTextColor(Color.WHITE);
                        List<RowItem> newList = recentModel.getRowItems().subList(0, 5);
                        newRealmList = new RealmList<>();
                        for (RowItem item : newList) {
                            newRealmList.add(item);
                        }
                        MainRecentModel copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(recentModel);
                        copy.setRowItems(newRealmList);
                        recentRecyclerAdapter.onDataSetChanged(copy);
                        if (newMessagesCount > 0) {
                            showAllTv.setTextColor(Color.WHITE);
                            showAllTv.setEnabled(true);
                        }
                    } else {
                        showAllTv.setEnabled(false);
                        showAllTv.setTextColor(getResources().getColor(R.color.colorAccentVeryDark));
                        recentRecyclerAdapter.onDataSetChanged(recentModel);
                    }
                    if (newMessagesCount > 0) {
                        newMessagesIcon.setVisibility(View.VISIBLE);
                        newMessageCountTv.setText(String.valueOf(newMessagesCount));
                    } else {
                        newMessagesIcon.setVisibility(View.GONE);
                    }
                }
                recentRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onAccountModelReceived(MainAccountsModel dataModel) {
        if (dataModel != null && dataModel.getRowItems() != null && dataModel.getRowItems().size() > 0) {
            accounts_empty_layout.setVisibility(View.GONE);
            if (accountsAdapter == null) {
                accountsAdapter = new AccountRecyclerAdapter(getApplicationContext(), dataModel, accountSelectedCallback);
                accountsRecyclerView.setAdapter(accountsAdapter);
            }
            accountsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDirectMessagesModelReceived(MainDirectMessagesModel dataModel) {
        if (dataModel != null && dataModel.getDirectItems() != null && dataModel.getDirectItems().size() > 0) {
            direct_messages_empty_state.setVisibility(View.GONE);
            directRecyclerAdapter = new DirectMessageRecyclerAdapter(getApplicationContext(), dataModel, directMessageSelectedCallback);
            directMessagesRecyclerView.setAdapter(directRecyclerAdapter);
            directRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGroupMessagesReceived(List<GroupChatRealm> groupChats) {
        if (groupChats != null && groupChats.size() > 0) {
            group_messages_empty_state.setVisibility(View.GONE);
            groupRecyclerAdapter = new GroupMessagesRecyclerAdapter(getApplicationContext(), groupChats, groupMessageSelectedCallback);
            groupMessagesRecyclerView.setAdapter(groupRecyclerAdapter);
            groupRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setWelcomeMessage(String typeCustomer) {

    }

    @Override
    public void setPageTitle(String title) {
        setTitle(title);
    }

    @Override
    public void setNavHeaderData(String userName, String email, String iconLetter, int iconColor, int userColorDark) {
        nav_user_name.setText(userName);
        nav_user_email.setText(email);
        nav_user_icon.setText(iconLetter);
        nav_user_icon.setBackgroundTintList(getResources().getColorStateList(iconColor));
        nav_main_header_layout.setBackgroundTintList(getResources().getColorStateList(userColorDark));
    }

    @Override
    public void onQueryResults(List<AccountRowItem> searchResults, List<UserRealm> userSearchResults, List<GroupChatRealm> sharedLeads, String query) {
        pagerAdapter.onQueryResultsAccount(searchResults, query);
        pagerAdapter.onQueryResultsUser(userSearchResults, query);
        pagerAdapter.onQueryResultsSharedLeads(sharedLeads, query);
        setAccountsTabTitle("Accounts(" + searchResults.size() + ")");
        setUsersTabTitle("Users(" + userSearchResults.size() + ")");
        setSharedLeadTitle("Shared Leads(" + sharedLeads.size() + ")");
        if(searchResults.size() == 0 && userSearchResults.size() > 0 && sharedLeads.size() == 0){
            searchResultPager.setCurrentItem(1);
        }else if(searchResults.size() > 0 && userSearchResults.size() == 0 && sharedLeads.size() == 0){
            searchResultPager.setCurrentItem(0);
        }else if(searchResults.size() == 0 && userSearchResults.size() == 0 && sharedLeads.size() > 0){
            searchResultPager.setCurrentItem(2);
        }
    }

    private void setAccountsTabTitle(String title){
        searchResultsTabs.getTabAt(0).setText(title);
    }

    private void setUsersTabTitle(String title){
        searchResultsTabs.getTabAt(1).setText(title);
    }

    private void setSharedLeadTitle(String title){
        searchResultsTabs.getTabAt(2).setText(title);
    }

    @Override
    public void onUserSelected(UserRealm user) {
        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class).putExtra("userId", user.getUid()));
    }

    @Override
    public void onDirectMessageSelected(UserRealm user) {
        launchDirectMessageIntent(UserPreferences.getInstance().getUid(), user.getUid());
    }

    @Override
    public void onAccountSelected(AccountRowItem rowItem) {
        launchAccountDetailsIntent(rowItem);
    }

    @Override
    public void onChatSelected(GroupChatRealm chat) {
        launchGroupMessageIntent(chat);
    }

    private void launchDirectMessageIntent(String uid, String toUid){
        Intent directMessageIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
        directMessageIntent.putExtra("uid", uid);
        directMessageIntent.putExtra("toUid", toUid);
        startActivity(directMessageIntent);
    }

    private void launchAccountDetailsIntent(AccountRowItem rowItem){
        Intent accountChatIntent = new Intent(getApplicationContext(), AccountChatActivity.class);
        accountChatIntent.putExtra("account_id", rowItem.getAccountIdFire());
        accountChatIntent.putExtra("account_name", rowItem.getAccountName());
        startActivity(accountChatIntent);
    }

    private class SearchResultsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> pageTitles;

        private void addFragment(Fragment fragment, String pageTitle){
            fragments.add(fragment);
            pageTitles.add(pageTitle);
        }

        public SearchResultsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            pageTitles = new ArrayList<>();
        }

        public void onQueryResultsAccount(List<AccountRowItem> items, String query){
            if(fragments.get(0) != null){
                ((AccountSearchResultsFragment) fragments.get(0)).onResultsReceived(items, query);
            }
        }

        public void onQueryResultsUser(List<UserRealm> items, String query){
            if(fragments.get(1) != null){
                ((UsersSearchResultFragment) fragments.get(1)).onResultsReceived(items, query);
            }
        }

        public void onQueryResultsSharedLeads(List<GroupChatRealm> items, String query){
            if(fragments.get(2) != null){
                ((SharedLeadsSearchResultFragment) fragments.get(2)).onResultsReceived(items, query);
            }
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
}
