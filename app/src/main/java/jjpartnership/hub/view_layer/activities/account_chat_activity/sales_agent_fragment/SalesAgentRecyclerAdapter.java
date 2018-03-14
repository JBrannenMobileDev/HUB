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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.utils.BaseCallback;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class SalesAgentRecyclerAdapter extends RecyclerView.Adapter<SalesAgentRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Message> dataModel;
    private BaseCallback<Message> messageSelectedCallback;

    public SalesAgentRecyclerAdapter(@NonNull Context context, List<Message> dataModel, BaseCallback<Message> messageSelectedCallback) {
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
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.message_frame_layout);
            userName = v.findViewById(R.id.user_name_tv);
            timeDate = v.findViewById(R.id.time_date_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            userIcon = v.findViewById(R.id.user_icon_tv);
            nameDateTimeLayout = v.findViewById(R.id.name_date_time_layout);

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
        String previousMessageUid = dataModel.get(position - 1).getCreatedByUid();
        String currentMessageUid = dataModel.get(position).getCreatedByUid();
        if(previousMessageUid.equals(currentMessageUid)){
            holder.userIcon.setVisibility(View.GONE);
            holder.nameDateTimeLayout.setVisibility(View.GONE);
        }else{
            holder.userIcon.setVisibility(View.VISIBLE);
            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
        }

        Message message = dataModel.get(position);
        holder.userName.setText(message.getMessageOwnerName());
        if(message.getCreatedDate() != 0) {
            holder.timeDate.setText(createFormattedTime(message.getCreatedDate()));
        }

        if(message.getMessageContent() != null && !message.getMessageContent().isEmpty()) {
            holder.messageContent.setText(message.getMessageContent());
        }
        holder.userIcon.setText(String.valueOf(message.getMessageOwnerName().charAt(0)));
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aaa - MM/dd/yy");
        return dateFormatter.format(new Date(createdDate));
    }

    public void OnDataSetChanged(List<Message> dataModel) {
        this.dataModel = dataModel;
    }
}
