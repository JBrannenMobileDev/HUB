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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import jjpartnership.hub.view_layer.activities.search_activities.AccountSearchResultsFragment;
import jjpartnership.hub.view_layer.activities.search_activities.AddAccountActivity;
import jjpartnership.hub.view_layer.activities.search_activities.NewDirectMessageActivity;
import jjpartnership.hub.view_layer.activities.search_activities.SearchAllManager;
import jjpartnership.hub.view_layer.activities.search_activities.SharedLeadsSearchResultFragment;
import jjpartnership.hub.view_layer.activities.search_activities.UsersSearchResultFragment;
import jjpartnership.hub.view_layer.activities.settings_activity.SettingsActivity;
import jjpartnership.hub.view_layer.activities.share_lead_activity.ShareLeadActivity;
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
    @BindView(R.id.show_all_recent_tv) TextView showAllRecentTv;
    @BindView(R.id.show_all_accounts_tv)TextView showAllAccountsTv;
    @BindView(R.id.show_all_direct_messages_tv)TextView showAllDirectMessageTv;
    @BindView(R.id.show_all_shared_leads_tv)TextView showAllSharedLeadsTv;
    @BindView(R.id.new_message_icon) FrameLayout newMessagesIcon;
    @BindView(R.id.new_message_count_tv) TextView newMessageCountTv;
    @BindView(R.id.hide_recent_tv) TextView hideTv;
    @BindView(R.id.recent_frame_layout) FrameLayout recentFrameLayout;
    @BindView(R.id.search_results_pager)ViewPager searchResultPager;
    @BindView(R.id.search_results_tabs)TabLayout searchResultsTabs;

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
    private RealmList<RowItem> newRecentRealmList;
    private RealmList<AccountRowItem> newAccountRealmList;
    private RealmList<DirectItem> newDirectRealmList;
    private RealmList<GroupChatRealm> newGroupChatRealmList;
    private boolean recentExpanded;
    private boolean accountsExpanded;
    private boolean directMessagesExpanded;
    private boolean sharedLeadsExpanded;
    private int[] hideBtLocation;
    private TextView nav_user_name;
    private TextView nav_user_email;
    private TextView nav_user_icon;
    private LinearLayout nav_main_header_layout;
    private BackAwareSearchView searchViewBackAware;
    private BackAwareSearchView.BackPressedListener backPressedListenerPhone;
    private SearchAllManager searchManager;
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
                new int[]{android.R.attr.state_checked}
        };

        int[] color = new int[]{
                getResources().getColor(R.color.colorPrimaryLight),
                getResources().getColor(R.color.colorAccentVeryDark)
        };
        ColorStateList csl = new ColorStateList(state, color);
        navigationView.setItemTextColor(csl);

        setNavIconColors(navigationView);
        nav_user_name = hView.findViewById(R.id.nav_user_name);
        nav_user_email = hView.findViewById(R.id.nav_user_email);
        nav_user_icon = hView.findViewById(R.id.nav_user_icon);
        nav_main_header_layout = hView.findViewById(R.id.main_header_linear_layout);
        initDrawerHeaderClickListeners();

        searchManager = new SearchAllManager(getSupportFragmentManager(), searchResultPager, searchResultsTabs);



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

    private void launchShareLeadIntent() {
        startActivity(new Intent(this, ShareLeadActivity.class));
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
                searchManager.initAllMode();
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
                    searchManager.onQueryResults(new ArrayList<AccountRowItem>(), new ArrayList<UserRealm>(),
                            new ArrayList<GroupChatRealm>(), "");
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

        if (id == R.id.nav_add_account) {
            launchAddAccountIntent();
        } else if (id == R.id.nav_search) {
            showSearchSelectedView();
        } else if (id == R.id.nav_new_direct_message) {
            startActivity(new Intent(getApplicationContext(), NewDirectMessageActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        } else if(id == R.id.nav_share_lead){
            launchShareLeadIntent();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchAddAccountIntent() {
        startActivity(new Intent(getApplicationContext(), AddAccountActivity.class));
    }

    @OnClick(R.id.show_all_recent_tv)
    public void onShowAllRecentClicked() {
        recentExpanded = true;
        if (scrollView.getScrollY() == 0) {
            hideTv.setElevation(0);
        }
        presenter.onShowAllRecentClicked();
        animateHideButtonShow();
        showAllRecentTv.setVisibility(View.GONE);
    }

    @OnClick(R.id.show_all_accounts_tv)
    public void onShowAllAccountsClicked() {
        if(!accountsExpanded){
            showAllAccountsTv.setText("Hide");
            accountsExpanded = true;
            presenter.onShowAllAccountsClicked();
        }else{
            accountsExpanded = false;
            presenter.onRestoreAccounts();
        }
    }

    @OnClick(R.id.show_all_direct_messages_tv)
    public void onShowAllDirectMessagesClicked() {
        if(!directMessagesExpanded){
            showAllDirectMessageTv.setText("Hide");
            directMessagesExpanded = true;
            presenter.onShowAllDirectMessagesClicked();
        }else{
            directMessagesExpanded = false;
            presenter.onRestoreDirectMessages();
        }
    }

    @OnClick(R.id.show_all_shared_leads_tv)
    public void onShowAllSharedLeadsClicked() {
        if(!sharedLeadsExpanded){
            showAllSharedLeadsTv.setText("Hide");
            sharedLeadsExpanded = true;
            presenter.onShowAllSharedLeadsClicked();
        }else{
            sharedLeadsExpanded = false;
            presenter.onRestoreSharedLeads();
        }
    }

    @OnClick(R.id.hide_recent_tv)
    public void onHideClicked() {
        recentExpanded = false;
        presenter.onRestoreRecentModel();
        animateHideBottonHide();
        showAllRecentTv.setVisibility(View.VISIBLE);
        scrollView.scrollTo(0, 0);
        hideTv.setElevation(0);
    }

    @Override
    public void onShowAllRecent(MainRecentModel recentModel) {
        if (recentModel.getRowItems() != null && recentModel.getRowItems().size() > 0) {
            recent_empty_layout.setVisibility(View.GONE);
            if (recentRecyclerAdapter == null) {
                recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), new MainRecentModel(new RealmList<RowItem>()), recentSelectedCallback);
                recentRecyclerView.setAdapter(recentRecyclerAdapter);
            } else {
                recentRecyclerAdapter.onDataSetChanged(recentModel);
            }
            if (newRecentRealmList.size() > recentModel.getRowItems().size()) {
                recentRecyclerAdapter.notifyItemRangeInserted(newRecentRealmList.size(), recentModel.getRowItems().size() - newRecentRealmList.size());
            } else {
                recentRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onShowAllAccounts(MainAccountsModel accountsModel) {
        if (accountsModel.getRowItems() != null && accountsModel.getRowItems().size() > 0) {
            accounts_empty_layout.setVisibility(View.GONE);
            if (accountsAdapter == null) {
                accountsAdapter = new AccountRecyclerAdapter(getApplicationContext(), new MainAccountsModel(new RealmList<AccountRowItem>()), accountSelectedCallback);
                accountsRecyclerView.setAdapter(accountsAdapter);
            } else {
                accountsAdapter.OnDataSetChanged(accountsModel);
            }
            if (newAccountRealmList.size() > accountsModel.getRowItems().size()) {
                accountsAdapter.notifyItemRangeInserted(newAccountRealmList.size(), accountsModel.getRowItems().size() - newAccountRealmList.size());
            } else {
                accountsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onShowAllDirectMessages(MainDirectMessagesModel directModel) {
        if (directModel.getDirectItems() != null && directModel.getDirectItems().size() > 0) {
            direct_messages_empty_state.setVisibility(View.GONE);
            if (directRecyclerAdapter == null) {
                directRecyclerAdapter = new DirectMessageRecyclerAdapter(getApplicationContext(), new MainDirectMessagesModel(new RealmList<DirectItem>()), directMessageSelectedCallback);
                directMessagesRecyclerView.setAdapter(directRecyclerAdapter);
            } else {
                directRecyclerAdapter.OnDataSetChanged(directModel);
            }
            if (newDirectRealmList.size() > directModel.getDirectItems().size()) {
                directRecyclerAdapter.notifyItemRangeInserted(newDirectRealmList.size(), directModel.getDirectItems().size() - newDirectRealmList.size());
            } else {
                directRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onShowAllSharedLeads(List<GroupChatRealm> groupChats) {
        if (groupChats != null && groupChats.size() > 0) {
            group_messages_empty_state.setVisibility(View.GONE);
            if (groupRecyclerAdapter == null) {
                groupRecyclerAdapter = new GroupMessagesRecyclerAdapter(getApplicationContext(), groupChats, groupMessageSelectedCallback);
                groupMessagesRecyclerView.setAdapter(groupRecyclerAdapter);
            } else {
                groupRecyclerAdapter.OnDataSetChanged(groupChats);
            }
            if (newGroupChatRealmList.size() > groupChats.size()) {
                groupRecyclerAdapter.notifyItemRangeInserted(newGroupChatRealmList.size(), groupChats.size() - newGroupChatRealmList.size());
            } else {
                groupRecyclerAdapter.notifyDataSetChanged();
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
        showAllRecentTv.setText("(" + recentModel.getRowItems().size() + ") Show All");
        if (recentExpanded) {
            onShowAllRecent(recentModel);
        } else {
            int newMessagesCount = 0;
            if (recentModel.getRowItems() != null && recentModel.getRowItems().size() > 0) {
                recent_empty_layout.setVisibility(View.GONE);
                if (recentRecyclerAdapter == null) {
                    recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), recentModel, recentSelectedCallback);
                    recentRecyclerView.setAdapter(recentRecyclerAdapter);
                } else {
                    for (int i = 0; i < recentModel.getRowItems().size(); i++) {
                        if (recentModel.getRowItems().get(i).isNewMessage()) {
                            newMessagesCount++;
                        }
                    }
                    if (recentModel.getRowItems().size() > 5) {
                        showAllRecentTv.setEnabled(true);
                        showAllRecentTv.setTextColor(Color.WHITE);
                        List<RowItem> newList = recentModel.getRowItems().subList(0, 5);
                        newRecentRealmList = new RealmList<>();
                        for (RowItem item : newList) {
                            newRecentRealmList.add(item);
                        }
                        MainRecentModel copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(recentModel);
                        copy.setRowItems(newRecentRealmList);
                        recentRecyclerAdapter.onDataSetChanged(copy);
                        if (newMessagesCount > 0) {
                            showAllRecentTv.setTextColor(Color.WHITE);
                            showAllRecentTv.setEnabled(true);
                        }
                    } else {
                        showAllRecentTv.setEnabled(false);
                        showAllRecentTv.setTextColor(getResources().getColor(R.color.colorAccentVeryDark));
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
        showAllAccountsTv.setText("(" + dataModel.getRowItems().size() + ") Show All");
        if (accountsExpanded) {
            onShowAllAccounts(dataModel);
        } else {
            if (dataModel.getRowItems() != null && dataModel.getRowItems().size() > 0) {
                accounts_empty_layout.setVisibility(View.GONE);
                if (accountsAdapter == null) {
                    accountsAdapter = new AccountRecyclerAdapter(getApplicationContext(), dataModel, accountSelectedCallback);
                    accountsRecyclerView.setAdapter(accountsAdapter);
                } else {
                    if (dataModel.getRowItems().size() > 5) {
                        showAllAccountsTv.setEnabled(true);
                        showAllAccountsTv.setTextColor(getResources().getColor(R.color.colorAccentDark));
                        List<AccountRowItem> newList = dataModel.getRowItems().subList(0, 5);
                        newAccountRealmList = new RealmList<>();
                        for (AccountRowItem item : newList) {
                            newAccountRealmList.add(item);
                        }
                        MainAccountsModel copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(dataModel);
                        copy.setRowItems(newAccountRealmList);
                        accountsAdapter.OnDataSetChanged(copy);
                    } else {
                        showAllAccountsTv.setEnabled(false);
                        showAllAccountsTv.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        accountsAdapter.OnDataSetChanged(dataModel);
                    }
                }
                accountsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDirectMessagesModelReceived(MainDirectMessagesModel dataModel) {
        showAllDirectMessageTv.setText("(" + dataModel.getDirectItems().size() + ") Show All");
        if (directMessagesExpanded) {
            onShowAllDirectMessages(dataModel);
        } else {
            if (dataModel.getDirectItems() != null && dataModel.getDirectItems().size() > 0) {
                direct_messages_empty_state.setVisibility(View.GONE);
                if (directRecyclerAdapter == null) {
                    directRecyclerAdapter = new DirectMessageRecyclerAdapter(getApplicationContext(), dataModel, directMessageSelectedCallback);
                    directMessagesRecyclerView.setAdapter(directRecyclerAdapter);
                } else {
                    if (dataModel.getDirectItems().size() > 5) {
                        showAllDirectMessageTv.setEnabled(true);
                        showAllDirectMessageTv.setTextColor(getResources().getColor(R.color.colorAccentDark));
                        List<DirectItem> newList = dataModel.getDirectItems().subList(0, 5);
                        newDirectRealmList = new RealmList<>();
                        for (DirectItem item : newList) {
                            newDirectRealmList.add(item);
                        }
                        MainDirectMessagesModel copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(dataModel);
                        copy.setDirectItems(newDirectRealmList);
                        directRecyclerAdapter.OnDataSetChanged(copy);
                    } else {
                        showAllDirectMessageTv.setEnabled(false);
                        showAllDirectMessageTv.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        directRecyclerAdapter.OnDataSetChanged(dataModel);
                    }
                }
                directRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGroupMessagesReceived(List<GroupChatRealm> dataModel) {
        showAllSharedLeadsTv.setText("(" + dataModel.size() + ") Show All");
        if (sharedLeadsExpanded) {
            onShowAllSharedLeads(dataModel);
        } else {
            if (dataModel != null && dataModel.size() > 0) {
                group_messages_empty_state.setVisibility(View.GONE);
                if (groupRecyclerAdapter == null) {
                    groupRecyclerAdapter = new GroupMessagesRecyclerAdapter(getApplicationContext(), dataModel, groupMessageSelectedCallback);
                    groupMessagesRecyclerView.setAdapter(groupRecyclerAdapter);
                } else {
                    if (dataModel.size() > 5) {
                        showAllSharedLeadsTv.setEnabled(true);
                        showAllSharedLeadsTv.setTextColor(getResources().getColor(R.color.colorAccentDark));
                        List<GroupChatRealm> newList = dataModel.subList(0, 5);
                        newGroupChatRealmList = new RealmList<>();
                        for (GroupChatRealm item : newList) {
                            newGroupChatRealmList.add(item);
                        }

                        groupRecyclerAdapter.OnDataSetChanged(newGroupChatRealmList);
                    } else {
                        showAllSharedLeadsTv.setEnabled(false);
                        showAllSharedLeadsTv.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        groupRecyclerAdapter.OnDataSetChanged(dataModel);
                    }
                }
                groupRecyclerAdapter.notifyDataSetChanged();
            }
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
        searchManager.onQueryResults(searchResults, userSearchResults, sharedLeads, query);
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
}
