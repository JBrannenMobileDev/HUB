package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuyenmonkey.mkloader.MKLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.utils.BaseCallback;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class SalesAgentRecyclerAdapter extends RecyclerView.Adapter<SalesAgentRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<MessageRealm> dataModel;
    private BaseCallback<MessageRealm> messageSelectedCallback;

    public SalesAgentRecyclerAdapter(@NonNull Context context, List<MessageRealm> dataModel, BaseCallback<MessageRealm> messageSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel;
        this.messageSelectedCallback = messageSelectedCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        LinearLayout nameDateTimeLayout;
        TextView userName;
        TextView timeDate;
        TextView messageContent;
        TextView userIcon;
        MKLoader sendingMk;
        TextView status;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.message_frame_layout);
            userName = v.findViewById(R.id.user_name_tv);
            timeDate = v.findViewById(R.id.time_date_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            userIcon = v.findViewById(R.id.user_icon_tv);
            nameDateTimeLayout = v.findViewById(R.id.name_date_time_layout);
            sendingMk = v.findViewById(R.id.sending_animation_mk);
            status = v.findViewById(R.id.status_tv);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public SalesAgentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageRealm message = dataModel.get(position);
        if(position == 0){
            holder.userIcon.setVisibility(View.VISIBLE);
            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
        }else {
            String previousMessageUid = dataModel.get(position - 1).getUid();
            String currentMessageUid = dataModel.get(position).getUid();
            if (previousMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position).getCreatedDate(), dataModel.get(position-1).getCreatedDate())) {
                holder.userIcon.setVisibility(View.GONE);
                holder.nameDateTimeLayout.setVisibility(View.GONE);
            } else {
                holder.userIcon.setVisibility(View.VISIBLE);
                holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
            }
        }
        setMessageStatus(holder, message);

        holder.userName.setText(message.getMessageOwnerName());
        if(message.getCreatedDate() != 0) {
            holder.timeDate.setText(createFormattedTime(message.getCreatedDate()));
        }

        if(message.getMessageContent() != null && !message.getMessageContent().isEmpty()) {
            holder.messageContent.setText(message.getMessageContent());
        }
        holder.userIcon.setText(String.valueOf(message.getMessageOwnerName().charAt(0)));
    }

    private void setMessageStatus(ViewHolder holder, MessageRealm message){
        if (message.isSavedToFirebase()) {
            holder.sendingMk.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
        }else{
            holder.sendingMk.setVisibility(View.VISIBLE);
            holder.status.setVisibility(View.GONE);
        }
    }

    private boolean isTimeSinceLastMessageGreaterThan4Min(long createdDate, long createdDatePrevious) {
        long min_4 = 240000;
        long timeLapsedInMilli = createdDate - createdDatePrevious;
        if(timeLapsedInMilli > min_4){
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm aaa - M/dd/yy");
        return dateFormatter.format(new Date(createdDate));
    }

    public void OnDataSetChanged(List<MessageRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
