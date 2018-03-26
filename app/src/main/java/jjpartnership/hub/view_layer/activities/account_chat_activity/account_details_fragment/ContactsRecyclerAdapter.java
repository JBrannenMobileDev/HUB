package jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<UserRealm> dataModel;
    private BaseCallback<UserRealm> rowSelectedCallback;

    public ContactsRecyclerAdapter(@NonNull Context context, List<UserRealm> dataModel, BaseCallback<UserRealm> rowSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        TextView email;
        FrameLayout root;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.account_contact_frame_layout);
            accountName = v.findViewById(R.id.account_name_tv);
            messageTime = v.findViewById(R.id.most_recent_message_tv);
            messageOwnerName = v.findViewById(R.id.message_owner_name_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            accountIcon = v.findViewById(R.id.account_icon_tv);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public ContactsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_layout_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RowItem rowItem = dataModel.getRowItems().get(position);
        holder.accountName.setText(rowItem.getAccountName());
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    public void OnDataSetChanged(List<UserRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
