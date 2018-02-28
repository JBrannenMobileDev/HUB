package jjpartnership.hub.view_layer.activities.account_details_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.UserType;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.StringValidationUtil;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.main_activity.MainActivity;

public class AccountDetailsActivity extends AppCompatActivity {

    @BindView(R.id.field_email)EditText mEmailField;
    @BindView(R.id.field_password)EditText mPasswordField;
    @BindView(R.id.sales_agent_tv) TextView salesAgentTv;
    @BindView(R.id.customer_tv) TextView customerTv;
    @BindView(R.id.account_type_layout)LinearLayout accountTypeLayout;
    @BindView(R.id.email_create_account_button)Button createAccountBt;
    @BindView(R.id.login_button)Button loginBt;
    @BindView(R.id.show_login_view_tv)TextView showLoginViewTv;
    @BindView(R.id.show_create_account_view_tv)TextView showCreateAccountTv;
    @BindView(R.id.account_type_tv)TextView accountTypeTitle;

    private FirebaseAuth mAuth;
    private boolean accountJustCreated;
    private boolean salesAgentSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        ButterKnife.bind(this);
        hideStatusBar();
        Realm.init(getApplicationContext());
        UserPreferences.getInstance().setContext(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    private void launchMainActivity(FirebaseUser user) {
        if(user.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else{
            if(!accountJustCreated) {
                Toast.makeText(this, "Cannot login until email is verified.", Toast.LENGTH_LONG).show();
            }
        }
    }


}
