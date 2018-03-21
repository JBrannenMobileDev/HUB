package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.content.Context;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jjpartnership.hub.R;
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
    private List<MessageRealm> dataModel;
    private HashMap<String, Long> userColorMap;
    private BaseCallback<MessageRealm> messageSelectedCallback;

    public SalesAgentRecyclerAdapter(@NonNull Context context, List<MessageRealm> dataModel, BaseCallback<MessageRealm> messageSelectedCallback, HashMap<String, Long> userColorMap) {
        this.context = context;
        this.dataModel = dataModel;
        this.messageSelectedCallback = messageSelectedCallback;
        this.userColorMap = userColorMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        LinearLayout nameDateTimeLayout;
        LinearLayout messageContentLayout;
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
            messageContentLayout = v.findViewById(R.id.message_content_layout);

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
        boolean isUserMessage = message.getUid().equals(UserPreferences.getInstance().getUid());
        if(position == 0){
            holder.userIcon.setVisibility(View.VISIBLE);
            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
            if(message.getCreatedDate() != 0) {
                holder.timeDate.setText(createFormattedTime(message.getCreatedDate(), 0));
            }
            if(dataModel.size() == 1){
                if(isUserMessage){
                    holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                    setLayoutMargin(holder, 48, 8, 8, 16);
                }else{
                    holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                    setLayoutMargin(holder, 8, 8, 48, 16);
                }
            }else {
                if(isUserMessage){
                    if(dataModel.get(position).getUid().equals(dataModel.get(position + 1).getUid())) {
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_bottom_right));
                    }else{
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                    }
                    setLayoutMargin(holder, 48, 8, 8, 0);
                }else{
                    if(dataModel.get(position).getUid().equals(dataModel.get(position + 1).getUid())) {
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_bottom_left));
                    }else{
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                    }
                    setLayoutMargin(holder, 8, 8, 48, 0);
                }
            }
        }else {
            String previousMessageUid = dataModel.get(position - 1).getUid();
            String currentMessageUid = dataModel.get(position).getUid();

            if (previousMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position).getCreatedDate(), dataModel.get(position-1).getCreatedDate())) {
                holder.userIcon.setVisibility(View.GONE);
                holder.nameDateTimeLayout.setVisibility(View.GONE);
                if(position == dataModel.size()-1){
                    if(isUserMessage){
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_top_right));
                        setLayoutMargin(holder, 48, 2, 8, 16);
                    }else{
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        setLayoutMargin(holder, 8, 2, 48, 16);
                    }
                }else{
                    String nextMessageUid = dataModel.get(position + 1).getUid();
                    if(isUserMessage){
                        if(nextMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_top_bottom_right));
                        }else{
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_top_right));
                        }
                        setLayoutMargin(holder, 48, 2, 8, 0);
                    }else{
                        if(nextMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_bottom_left));
                        }else{
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        }
                        setLayoutMargin(holder, 8, 2, 48, 0);
                    }
                }
            } else {
                holder.userIcon.setVisibility(View.VISIBLE);
                holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                if(position == dataModel.size()-1){
                    if(isUserMessage){
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                        setLayoutMargin(holder, 48, 24, 8, 16);
                    }else{
                        holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        setLayoutMargin(holder, 8, 24, 48, 16);
                    }
                }else{
                    String nextMessageUid = dataModel.get(position + 1).getUid();
                    if(isUserMessage){
                        if(currentMessageUid.equals(nextMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_bottom_right));
                        }else{
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                        }
                        setLayoutMargin(holder, 48, 24, 8, 0);
                    }else{
                        if(currentMessageUid.equals(nextMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_bottom_left));
                        }else{
                            holder.messageContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        }
                        setLayoutMargin(holder, 8, 24, 48, 0);
                    }
                }
            }
            if(message.getCreatedDate() != 0) {
                holder.timeDate.setText(createFormattedTime(message.getCreatedDate(), dataModel.get(position - 1).getCreatedDate()));
            }
        }
        setMessageStatus(holder, message);

        holder.userName.setText(message.getMessageOwnerName());

        if(message.getMessageContent() != null && !message.getMessageContent().isEmpty()) {
            holder.messageContent.setText(message.getMessageContent());
        }
        holder.userIcon.setText(String.valueOf(message.getMessageOwnerName().charAt(0)));


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        if(isUserMessage){
            holder.userIcon.setVisibility(View.GONE);
            holder.userName.setVisibility(View.GONE);
            params.setMargins((int)DpUtil.pxFromDp(context, 39), (int)DpUtil.pxFromDp(context, 0),
                    (int)DpUtil.pxFromDp(context, 0), (int)DpUtil.pxFromDp(context, 0));
            params.gravity = Gravity.RIGHT;
            holder.timeDate.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.messageContent.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.messageContentLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
        }else{
            if(userColorMap != null && !userColorMap.isEmpty()) {
                holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(userColorMap.get(message.getUid()).intValue())));
                holder.messageContentLayout.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(userColorMap.get(message.getUid()).intValue())));
                holder.userIcon.setTextColor(context.getResources().getColor(UserColorUtil.getUserColorDark(userColorMap.get(message.getUid()).intValue())));
                holder.userName.setTextColor(context.getResources().getColor(UserColorUtil.getUserColorDark(userColorMap.get(message.getUid()).intValue())));
                holder.timeDate.setTextColor(context.getResources().getColor(UserColorUtil.getUserColorDark(userColorMap.get(message.getUid()).intValue())));
            }else{
                holder.userName.setTextColor(context.getResources().getColor(R.color.colorAccentDark));
                holder.timeDate.setTextColor(context.getResources().getColor(R.color.colorAccentDark));
                holder.userIcon.setTextColor(context.getResources().getColor(R.color.colorAccentDark));
            }
            holder.messageContent.setTextColor(context.getResources().getColor(R.color.white));
            params.setMargins((int)DpUtil.pxFromDp(context, 39), (int)DpUtil.pxFromDp(context, 0),
                    (int)DpUtil.pxFromDp(context, 0), (int)DpUtil.pxFromDp(context, 0));
            params.gravity = Gravity.LEFT;
            holder.userName.setVisibility(View.VISIBLE);
        }
        holder.messageContentLayout.setLayoutParams(params);
    }


    private void setLayoutMargin(ViewHolder holder, float leftDp, float topDp, float rightDp, float bottomDp){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins((int)DpUtil.pxFromDp(context, leftDp), (int)DpUtil.pxFromDp(context, topDp),
                (int)DpUtil.pxFromDp(context, rightDp), (int)DpUtil.pxFromDp(context, bottomDp));
        holder.root.setLayoutParams(params);
    }

    private void setMessageStatus(ViewHolder holder, MessageRealm message){
        if (message.isSavedToFirebase()) {
            holder.sendingMk.setVisibility(View.GONE);
        }else{
            holder.sendingMk.setVisibility(View.VISIBLE);
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

    private String createFormattedTime(long createdDate, long previousCreatedDate) {
        SimpleDateFormat timeDateFormat = new SimpleDateFormat("h:mm aaa - M/dd/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        if(previousCreatedDate > 0) {
            String formattedCreatedDate = dateFormat.format(new Date(createdDate));
            String formattedPreviousCreatedDate = dateFormat.format(new Date(previousCreatedDate));
            if(formattedCreatedDate.equals(formattedPreviousCreatedDate)){
                return timeFormat.format(new Date(createdDate));
            }
        }
        return timeDateFormat.format(new Date(createdDate));
    }

    public void OnDataSetChanged(List<MessageRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
