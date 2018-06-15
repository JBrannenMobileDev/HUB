package jjpartnership.hub.view_layer.activities.add_new_account_activity;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.view_layer.custom_views.AutoScrollAdjustableScrollView;

public class AddNewAccountActivity extends AppCompatActivity {

    @BindView(R.id.scrollView)AutoScrollAdjustableScrollView scrollView;

    private ActionBar mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_account);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentDark, true, "Create New Account");
        mAppBarLayout = getSupportActionBar();
        scrollView.setScrollOffset((int)DpUtil.pxFromDp(this, 68f));
        initScrollViewListener();
    }

    public void setToolbarElevation(float height) {
        mAppBarLayout.setElevation(DpUtil.pxFromDp(this, height));
    }

    private void initScrollViewListener() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollView.canScrollVertically(-1)) {
                    setToolbarElevation(4);
                } else {
                    setToolbarElevation(0);
                }
            }
        });
    }
}
