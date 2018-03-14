package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.content.Context;
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
 * Activities that contain this fragment must implement the
 * {@link OnSalesChatFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SalesAgentsFragment extends Fragment implements SalesAgentView{
    @BindView(R.id.chat_empty_state)TextView emptyStateMessage;

    private OnSalesChatFragmentInteractionListener mListener;
    private SalesAgentPresenter presenter;

    public SalesAgentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, v);
        presenter = new SalesAgentPresenterImp(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSalesChatFragmentInteractionListener) {
            mListener = (OnSalesChatFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSalesChatFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onUserInputChanged(CharSequence charSequence) {
        presenter.onUserInputChanged(charSequence.toString());
    }

    public void onSendMessageClicked() {
        presenter.onSendMessageClicked();
    }

    public interface OnSalesChatFragmentInteractionListener {

    }
}
