package jjpartnership.hub.view_layer.activities.direct_message_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;

public class DirectMessageActivity extends AppCompatActivity implements DirectMessageView{
    @BindView(R.id.user_infor_linear_layout)LinearLayout userInfoLayout;

    private DirectMessagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new DirectMessagePresenterImp(this, getIntent().getStringExtra("uid"),
                getIntent().getBooleanExtra("showUserInfo", false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void hideUserInfoLayout() {
        userInfoLayout.setVisibility(View.GONE);
    }

    @Override
    public void setActivityTitle(String userName) {
        setTitle(userName);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
