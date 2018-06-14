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
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.DirectItem;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class DirectMessageRecyclerAdapter extends RecyclerView.Adapter<DirectMessageRecyclerAdapter.ViewHolder> {
    private Context context;
    private MainDirectMessagesModel dataModel;
    private BaseCallback<DirectItem> rowSelectedCallback;

    public DirectMessageRecyclerAdapter(@NonNull Context context, MainDirectMessagesModel dataModel, BaseCallback<DirectItem> rowSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        TextView userName;
        TextView messageTime;
        TextView messageContent;
        TextView userIcon;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.direct_message_item_frame_layout);
            userName = v.findViewById(R.id.user_name);
            messageTime = v.findViewById(R.id.most_recent_message_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            userIcon = v.findViewById(R.id.user_icon_tv);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.getDirectItems().get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public DirectMessageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.direct_message_list_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DirectItem directItem = dataModel.getDirectItems().get(position);
        DirectChatRealm dChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", directItem.getDirectChatId()).findFirst();
        UserRealm user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", dChat.getDirectChatUid(UserPreferences.getInstance().getUid())).findFirst();
        if(user != null){
            holder.userName.setText(user.getFirstName() + " " + user.getLastName());
            holder.userIcon.setText(String.valueOf(user.getFirstName().charAt(0)));
        }

        if(directItem.isNewMessage()){
            holder.userName.setTextColor(Color.BLACK);
            holder.messageTime.setTextColor(Color.BLACK);
            holder.messageContent.setTextColor(Color.BLACK);
        }else{
            holder.userName.setTextColor(context.getResources().getColor(R.color.colorPrimaryLight));
            holder.messageTime.setTextColor(context.getResources().getColor(R.color.colorPrimaryLight));
            holder.messageContent.setTextColor(context.getResources().getColor(R.color.colorPrimaryLight));
        }

        if(directItem.getMessageCreatedAtTime() != 0) {
            holder.messageTime.setText(createFormattedTime(directItem.getMessageCreatedAtTime()));
        }
        if(directItem.getMessageContent() != null && !directItem.getMessageContent().isEmpty()) {
            holder.messageContent.setText(directItem.getMessageContent());
        }
        if(user != null){
            holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(user.getUserColor())));
        }
    }

    @Override
    public int getItemCount() {
        return dataModel.getDirectItems().size();
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm aaa - M/dd/yy");
        return dateFormatter.format(new Date(createdDate));
    }

    public void OnDataSetChanged(MainDirectMessagesModel dataModel) {
        this.dataModel = dataModel;
    }
}
