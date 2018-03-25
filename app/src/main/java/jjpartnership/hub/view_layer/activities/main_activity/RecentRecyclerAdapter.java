package jjpartnership.hub.view_layer.activities.main_activity;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.utils.BaseCallback;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class RecentRecyclerAdapter extends RecyclerView.Adapter<RecentRecyclerAdapter.ViewHolder> {
    private Context context;
    private MainRecentModel dataModel;
    private BaseCallback<RowItem> rowSelectedCallback;

    public RecentRecyclerAdapter(@NonNull Context context, MainRecentModel dataModel, BaseCallback<RowItem> rowSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
    }

    public void onDataSetChanged(MainRecentModel recentModel) {
        this.dataModel = recentModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        TextView accountName;
        TextView messageTime;
        TextView messageOwnerName;
        TextView messageContent;
        TextView accountIcon;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.accounts_item_frame_layout);
            accountName = v.findViewById(R.id.account_name_tv);
            messageTime = v.findViewById(R.id.most_recent_message_tv);
            messageOwnerName = v.findViewById(R.id.message_owner_name_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            accountIcon = v.findViewById(R.id.account_icon_tv);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.getRowItems().get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public RecentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RowItem rowItem = dataModel.getRowItems().get(position);
        holder.accountName.setText(rowItem.getAccountName());
        if(rowItem.getMessageCreatedAtTime() != 0) {
            holder.messageTime.setText(createFormattedTime(rowItem.getMessageCreatedAtTime()));
        }
        if(rowItem.getMessageOwnerName() != null && !rowItem.getMessageOwnerName().isEmpty()) {
            holder.messageOwnerName.setText(rowItem.getMessageOwnerName() + " - ");
        }
        if(rowItem.getMessageContent() != null && !rowItem.getMessageContent().isEmpty()) {
            holder.messageContent.setText(rowItem.getMessageContent());
        }
        holder.accountIcon.setText(String.valueOf(rowItem.getAccountName().charAt(0)));
        if(rowItem.isNewMessage()){
            holder.accountName.setTextColor(Color.BLACK);
            holder.messageOwnerName.setTextColor(Color.BLACK);
            holder.messageTime.setTextColor(Color.BLACK);
            holder.messageContent.setTextColor(Color.BLACK);
        }else{
            holder.accountName.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.messageOwnerName.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.messageTime.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.messageContent.setTextColor(context.getResources().getColor(R.color.grey_text));
        }
    }

    @Override
    public int getItemCount() {
        return dataModel.getRowItems().size();
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm aaa - M/dd/yy");
        return dateFormatter.format(new Date(createdDate));
    }
}
