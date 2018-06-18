package jjpartnership.hub.view_layer.activities.add_new_account_activity;

import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.view_layer.custom_views.AutoScrollAdjustableScrollView;
import jjpartnership.hub.view_layer.custom_views.BackAwareEditText;

public class AddNewAccountActivity extends AppCompatActivity {

    @BindView(R.id.scrollView)AutoScrollAdjustableScrollView scrollView;
    @BindView(R.id.company_name_back_et)BackAwareEditText companyEt;
    @BindView(R.id.address_1_back_et)BackAwareEditText address1Et;
    @BindView(R.id.address_2_back_et)BackAwareEditText address2Et;
    @BindView(R.id.city_et)BackAwareEditText cityEt;
    @BindView(R.id.state_et)AutoCompleteTextView statesEt;
    @BindView(R.id.zip_code_et)BackAwareEditText zipCodeEt;
    @BindView(R.id.send_account_request_tv)TextView sendRequest;

    private ActionBar mAppBarLayout;
    private Map<String,String> stateMap;
    private String[] statesArray;
    private String[] stateCodesArray;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_account);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentVeryDark, true, "Create New Account");
        mAppBarLayout = getSupportActionBar();
        scrollView.setScrollOffset((int)DpUtil.pxFromDp(this, 72f));
        initStatesSelector();
        initEtListeners();
        initScrollViewListener();
    }

    @OnClick(R.id.send_account_request_tv)
    public void onSendRequestClicked(){
        if(isFormValid()){
            Toast.makeText(this, "Request has been sent.", Toast.LENGTH_SHORT).show();
            finish();
        }
        //TODO send request to users manager.
        //TODO create accountRequest dataModel.
    }

    private boolean isFormValid(){
        if(companyEt.getText().length() > 0
                && address1Et.getText().length() > 0
                && cityEt.getText().length() > 0
                && statesEt.getText().length() > 0
                && zipCodeEt.getText().length() > 0){
            address1Et.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_bg));
            cityEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_bg));
            statesEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_bg));
            zipCodeEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_bg));
            return true;
        }else{
            if(companyEt.getText().length() == 0){
                companyEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_error_bg));
            }
            if(address1Et.getText().length() == 0){
                address1Et.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_error_bg));
            }
            if(cityEt.getText().length() == 0){
                cityEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_error_bg));
            }
            if(statesEt.getText().length() == 0){
                statesEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_error_bg));
            }
            if(zipCodeEt.getText().length() == 0){
                zipCodeEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_user_input_error_bg));
            }
            return false;
        }
    }

    private boolean isFormComplete() {
        if(companyEt.getText().length() > 0
                && address1Et.getText().length() > 0
                && cityEt.getText().length() > 0
                && statesEt.getText().length() > 0
                && zipCodeEt.getText().length() > 0){
            sendRequest.setTextColor(Color.WHITE);
            sendRequest.setEnabled(true);
            return true;
        }else{
            sendRequest.setEnabled(false);
            sendRequest.setTextColor(getResources().getColor(R.color.colorAccentDark));
            return false;
        }
    }

    @OnClick(R.id.clear_form_tv)
    public void onClearFormClicked(){
        companyEt.setText("");
        address1Et.setText("");
        address2Et.setText("");
        cityEt.setText("");
        statesEt.setText("");
        zipCodeEt.setText("");
    }

    private void initStatesSelector() {
        stateMap = new HashMap<>();
        statesArray = getResources().getStringArray(R.array.us_states);
        stateCodesArray = getResources().getStringArray(R.array.us_state_codes);
        for(int i = 0; i < statesArray.length; i++){
            stateMap.put(stateCodesArray[i], statesArray[i]);
        }
        stateAdapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item, statesArray);
        statesEt.setThreshold(1);
        statesEt.setAdapter(stateAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void initEtListeners() {
        companyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isFormComplete();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isFormComplete();
            }
        });

        address1Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isFormComplete();
            }
        });

        address2Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isFormComplete();
            }
        });

        cityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isFormComplete();
            }
        });

        statesEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isFormComplete();
            }
        });

        zipCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isFormComplete();
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
