package jjpartnership.hub.view_layer.activities.account_activity.sales_agent_fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.custom_views.GroupIcon;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class SalesAgentRecyclerAdapter extends RecyclerView.Adapter<SalesAgentRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<GroupChatRealm> dataModel;
    private GroupChatRealm allAgentsChat;
    private HashMap<String, Long> userColorMap;
    private BaseCallback<GroupChatRealm> chatSelectedCallback;
    private String accountName;

    public SalesAgentRecyclerAdapter(@NonNull Context context, List<GroupChatRealm> dataModel, BaseCallback<GroupChatRealm> chatSelectedCallback, HashMap<String, Long> userColorMap) {
        this.context = context;
        this.dataModel = dataModel;
        this.chatSelectedCallback = chatSelectedCallback;
        this.userColorMap = userColorMap;
    }

    public SalesAgentRecyclerAdapter(Context applicationContext, GroupChatRealm groupChat, BaseCallback<GroupChatRealm> chatSelectedCallback, String accountName) {
        this.context = applicationContext;
        this.allAgentsChat = groupChat;
        this.chatSelectedCallback = chatSelectedCallback;
        this.accountName = accountName;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.group_chat_item_frame_layout) FrameLayout root;
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.group_icon_tv) GroupIcon icon;
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
        GroupChatRealm chatToUse;
        chatToUse = dataModel.get(position);
        UserRealm messageUser = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", chatToUse.getMostRecentMessage().getUid()).findFirst();
        createIconDataLists(chatToUse, holder.icon);
        holder.groupName.setText(chatToUse.getGroupName());
        if(messageUser != null && chatToUse.getMostRecentMessage() != null && chatToUse.getMostRecentMessage().getMessageContent() != null && !chatToUse.getMostRecentMessage().getMessageContent().isEmpty()) {
            holder.messageDateTime.setText(createFormattedTime(chatToUse.getMostRecentMessage().getCreatedDate()));
            holder.messageContent.setText(messageUser.getFirstName() + " " + messageUser.getLastName() + " - " + chatToUse.getMostRecentMessage().getMessageContent());
        }else{
            holder.messageContent.setText("Be the first to post a message.");
        }
        if(messageUser != null) {
            holder.icon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(messageUser.getUserColor())));
        }
    }

    private void createIconDataLists(GroupChatRealm chatToUse, GroupIcon icon) {
        List<String> names = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        for(String userId : chatToUse.getUserIds()){
            if(!userId.equals(UserPreferences.getInstance().getUid())) {
                UserRealm user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", userId).findFirst();
                if (user != null) {
                    names.add(user.getFirstName());
                    colors.add(user.getUserColor());
                }
            }
        }
        icon.initAllIcons(names, colors);
    }

    @Override
    public int getItemCount() {
        return dataModel != null ? dataModel.size() : 1;
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm aaa - M/dd/yy");
        return dateFormatter.format(new Date(createdDate));
    }

    public void OnDataSetChanged(List<GroupChatRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
