package jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;

public class AccountDetailsFragment extends Fragment implements AccountDetailsView{
    @BindView(R.id.account_details_address_tv)TextView addressTv;
    @BindView(R.id.account_details_industries_tv)TextView industriesTv;

    private OnAccountDetailsInteractionListener mListener;
    private AccountDetailsPresenter presenter;

    public AccountDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_details, container, false);
        ButterKnife.bind(this, v);
        presenter = new AccountDetailsPresenterImp(this, getArguments().getString("account_name"),
                getArguments().getString("account_id"));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountDetailsInteractionListener) {
            mListener = (OnAccountDetailsInteractionListener) context;
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

    @OnClick(R.id.driving_directions_iv)
    public void onGetDirectionsClicked(){
        presenter.onDirectionsClicked();
    }

    @Override
    public void onReceiveCompanyData(String address, String industries) {
        addressTv.setText(address);
        industriesTv.setText(industries);
    }

    public interface OnAccountDetailsInteractionListener {

    }
}
