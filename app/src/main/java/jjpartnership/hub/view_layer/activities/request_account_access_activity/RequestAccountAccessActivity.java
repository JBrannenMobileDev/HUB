package jjpartnership.hub.view_layer.activities.request_account_access_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import jjpartnership.hub.R;
import jjpartnership.hub.utils.ActionBarUtil;

public class RequestAccountAccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_account_access);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentDark, true, "Request Account Access");
    }
}
