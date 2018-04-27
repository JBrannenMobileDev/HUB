package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuyenmonkey.mkloader.MKLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class SalesAgentRecyclerAdapter extends RecyclerView.Adapter<SalesAgentRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<GroupChatRealm> dataModel;
    private HashMap<String, Long> userColorMap;
    private BaseCallback<GroupChatRealm> chatSelectedCallback;

    public SalesAgentRecyclerAdapter(@NonNull Context context, List<GroupChatRealm> dataModel, BaseCallback<GroupChatRealm> chatSelectedCallback, HashMap<String, Long> userColorMap) {
        this.context = context;
        this.dataModel = dataModel;
        this.chatSelectedCallback = chatSelectedCallback;
        this.userColorMap = userColorMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.group_chat_item_frame_layout) FrameLayout root;
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.group_icon_tv) TextView iconTv;
        @BindView(R.id.message_content_tv) TextView messageContent;
        @BindView(R.id.most_recent_message_tv) TextView messageDateTime;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(chatSelectedCallback != null)chatSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public SalesAgentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_chat_list_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.groupName.setText(dataModel.get(position).getGroupName());
        holder.messageDateTime.setText(createFormattedTime(dataModel.get(position).getMostRecentMessage().getCreatedDate()));
        holder.iconTv.setText(String.valueOf(dataModel.get(position).getGroupName().charAt(0)));
        holder.messageContent.setText(dataModel.get(position).getMostRecentMessage().getMessageContent());
        holder.iconTv.setTextColor(UserColorUtil.getUserColor(userColorMap.get(dataModel.get(position).getGroupCreatorUid()).intValue()));
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa");
        return timeFormat.format(new Date(createdDate));
    }

    public void OnDataSetChanged(List<GroupChatRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
