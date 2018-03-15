package jjpartnership.hub.view_layer.activities.account_chat_activity.cutomer_chat_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jjpartnership.hub.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerChatFragment.OnCustomerChatInteractionListener} interface
 * to handle interaction events.
 */
public class CustomerChatFragment extends Fragment {

    private OnCustomerChatInteractionListener mListener;

    public CustomerChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCustomerChatInteractionListener) {
            mListener = (OnCustomerChatInteractionListener) context;
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

    public void setAccountData(String account_name, String account_id) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCustomerChatInteractionListener {

    }
}