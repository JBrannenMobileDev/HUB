package jjpartnership.hub.view_layer.activities.main_activity;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.custom_views.GroupIcon;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class RecentRecyclerAdapter extends RecyclerView.Adapter<RecentRecyclerAdapter.ViewHolder> {
    private Context context;
    private MainRecentModel dataModel;
    private BaseCallback<RowItem> rowSelectedCallback;
    private int lastPosition = -1;

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
        GroupIcon accountIcon;

        FrameLayout accountItemLayout;
        FrameLayout directMessageLayout;
        TextView userName;
        TextView directMessageContent;
        TextView directMessageTime;
        TextView userIcon;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            accountName = v.findViewById(R.id.account_name_tv);
            messageTime = v.findViewById(R.id.most_recent_message_tv);
            messageOwnerName = v.findViewById(R.id.message_owner_name_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            accountIcon = v.findViewById(R.id.account_icon_tv);
            userName = v.findViewById(R.id.user_name);
            userIcon = v.findViewById(R.id.user_icon_tv);
            directMessageTime = v.findViewById(R.id.message_time_tv);
            directMessageContent = v.findViewById(R.id.direct_message_content_tv);
            directMessageLayout = v.findViewById(R.id.direct_message_frame_layout);
            accountItemLayout = v.findViewById(R.id.accounts_item_frame_layout);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
                RowItem rowItem = dataModel.getRowItems().get(position);
        UserRealm user;
                //TYPE_GROUP_CHAT is actually type SHARED_LEAD
                if(rowItem.getItemType().equals(RowItem.TYPE_GROUP_CHAT)) {
                    GroupChatRealm gChat = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).equalTo("chatId", rowItem.getChatId()).findFirst();
                    user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", UserPreferences.getInstance().getUid()).findFirst();
                    holder.directMessageLayout.setVisibility(View.GONE);
                    holder.accountItemLayout.setVisibility(View.VISIBLE);
                    holder.accountName.setText(gChat.getGroupName());
                    if (rowItem.getMessageCreatedAtTime() != 0) {
                        holder.messageTime.setText(createFormattedTime(rowItem.getMessageCreatedAtTime()));
                    }
                    if (rowItem.getMessageOwnerName() != null && !rowItem.getMessageOwnerName().isEmpty()) {
                        if(rowItem.getMessageOwnerName().equals(user.getFirstName() + " " + user.getLastName())){
                            holder.messageOwnerName.setText("You: ");
                        }else {
                            holder.messageOwnerName.setText(rowItem.getMessageOwnerName() + " - ");
                        }
                    }
                    if (rowItem.getMessageContent() != null && !rowItem.getMessageContent().isEmpty()) {
                        holder.messageContent.setText(rowItem.getMessageContent());
                    }
                    GroupChatRealm chatToUse = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).equalTo("chatId", rowItem.getChatId()).findFirst();
                    createIconDataLists(chatToUse, holder.accountIcon);
//                    if(rowItem.getAccountName() != null)holder.accountIcon.setText(String.valueOf(rowItem.getAccountName().charAt(0)));
                    if (rowItem.isNewMessage()) {
                        holder.accountName.setTextColor(Color.BLACK);
                        holder.messageOwnerName.setTextColor(Color.BLACK);
                        holder.messageTime.setTextColor(Color.BLACK);
                        holder.messageContent.setTextColor(Color.BLACK);
                    } else {
                        holder.accountName.setTextColor(context.getResources().getColor(R.color.grey_text));
                        holder.messageOwnerName.setTextColor(context.getResources().getColor(R.color.grey_text));
                        holder.messageTime.setTextColor(context.getResources().getColor(R.color.grey_text));
                        holder.messageContent.setTextColor(context.getResources().getColor(R.color.grey_text));
                    }
                }else{
                    DirectChatRealm dChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", rowItem.getAccountId()).findFirst();
                    user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", dChat.getDirectChatUid(UserPreferences.getInstance().getUid())).findFirst();
                    holder.directMessageLayout.setVisibility(View.VISIBLE);
                    holder.accountItemLayout.setVisibility(View.GONE);
                    if(user != null){
                        holder.userName.setText(user.getFirstName() + " " + user.getLastName());
                        holder.userIcon.setText(String.valueOf(user.getFirstName().charAt(0)));
                    }

                    if(rowItem.getMessageCreatedAtTime() != 0) {
                        holder.directMessageTime.setText(createFormattedTime(rowItem.getMessageCreatedAtTime()));
                    }
                    if(rowItem.getMessageContent() != null && !rowItem.getMessageContent().isEmpty()) {
                        if(!rowItem.getMessageOwnerName().equals(user.getFirstName() + " " + user.getLastName())){
                            holder.directMessageContent.setText("You: " + rowItem.getMessageContent());
                        }else {
                            holder.directMessageContent.setText(rowItem.getMessageContent());
                        }
                    }

                    if (rowItem.isNewMessage()) {
                        holder.directMessageTime.setTextColor(Color.BLACK);
                        holder.directMessageContent.setTextColor(Color.BLACK);
                        holder.userName.setTextColor(Color.BLACK);
                    } else {
                        holder.directMessageTime.setTextColor(context.getResources().getColor(R.color.grey_text));
                        holder.directMessageContent.setTextColor(context.getResources().getColor(R.color.grey_text));
                        holder.userName.setTextColor(context.getResources().getColor(R.color.grey_text));
                    }

                    if(user != null){
                        holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(user.getUserColor())));
                    }
                }
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            animation.setDuration(600);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
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
    public int getItemViewType(int position) {
        if(dataModel.getRowItems().get(position) instanceof RowItem){
            return 0;
        }
//        if(dataModel.getRowObjects().get(position) instanceof DirectItem){
//            return 1;
//        }
        return 0;
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
