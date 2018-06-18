package jjpartnership.hub.view_layer.activities.request_account_access_activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.view_layer.custom_views.BackAwareAutofillMultiLineEditText;
import jjpartnership.hub.view_layer.custom_views.BackAwareEditText;

public class RequestAccountAccessActivity extends AppCompatActivity {

    @BindView(R.id.send_request_tv)TextView sendRequestTv;
    @BindView(R.id.account_name_tv)TextView accountNameTv;
    @BindView(R.id.manager_name_tv)TextView managerNameTv;
    @BindView(R.id.request_reason_et)BackAwareAutofillMultiLineEditText requestReasonEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_account_access);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentVeryDark, true, "Request Account Access");

        requestReasonEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    sendRequestTv.setTextColor(Color.WHITE);
                    sendRequestTv.setEnabled(true);
                }else{
                    sendRequestTv.setTextColor(getResources().getColor(R.color.colorAccentDark));
                    sendRequestTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.send_request_tv)
    public void onSendRequestClicked(){
        if(requestReasonEt.getText().length() > 0){
            //TODO actually send request to manager
            Toast.makeText(this, "Request has been sent.", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            requestReasonEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_error_bg));
        }
    }
}
