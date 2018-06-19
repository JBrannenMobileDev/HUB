package jjpartnership.hub.view_layer.activities.share_lead_activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.view_layer.custom_views.AutoScrollAdjustableScrollView;
import jjpartnership.hub.view_layer.custom_views.BackAwareAutofillMultiLineEditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComposeMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ComposeMessageFragment extends Fragment {

    @BindView(R.id.opportunity_title_input_et)BackAwareAutofillMultiLineEditText titleInput;
    @BindView(R.id.message_input_et)BackAwareAutofillMultiLineEditText messageInput;
    @BindView(R.id.compose_message_scroll_view)AutoScrollAdjustableScrollView scrollView;
    @BindView(R.id.send_shared_lead_tv)TextView sendLead;

    private OnFragmentInteractionListener mListener;

    public ComposeMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_compose_message, container, false);
        ButterKnife.bind(this, v);
        scrollView.setScrollOffset((int) DpUtil.pxFromDp(getActivity(), 72f));
        initListeners();

        return v;
    }

    private void initListeners() {
        messageInput.setBackPressedListener(new BackAwareAutofillMultiLineEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareAutofillMultiLineEditText editText) {
                messageInput.clearFocus();
                mListener.onSoftKeyboardStateChanged(false);
            }
        });

        titleInput.setBackPressedListener(new BackAwareAutofillMultiLineEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareAutofillMultiLineEditText editText) {
                titleInput.clearFocus();
                mListener.onSoftKeyboardStateChanged(false);
            }
        });

        messageInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    messageInput.requestFocus();
                    mListener.onSoftKeyboardStateChanged(true);
                }
            }
        });

        titleInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    titleInput.requestFocus();
                    mListener.onSoftKeyboardStateChanged(true);
                }
            }
        });

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSendBtState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSendBtState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setSendBtState() {
        if(messageInput.getText().length() > 0 && titleInput.getText().length() > 0){
            sendLead.setEnabled(true);
            sendLead.setTextColor(Color.WHITE);
        }else{
            sendLead.setEnabled(false);
            sendLead.setTextColor(getResources().getColor(R.color.colorAccentDark));
        }
    }

    @OnClick(R.id.send_shared_lead_tv)
    public void onSendLeadClicked(){
        mListener.onSendLeadClicked(messageInput.getText().toString(), titleInput.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onSoftKeyboardStateChanged(boolean visible);
        void onSendLeadClicked(String message, String title);
    }
}
