package jjpartnership.hub.view_layer.activities.account_activity.account_details_restricted_access_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDetailsRestrictedAccessFragment extends Fragment {

    @BindView(R.id.restricted_access_message_tv)TextView message;

    public AccountDetailsRestrictedAccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_details_restricted_access, container, false);
        ButterKnife.bind(this, v);
        message.setText(getArguments().getString("message"));
        return v;
    }

}
