package jjpartnership.hub.view_layer.activities.account_activity;

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

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<MessageRealm> dataModel;
    private HashMap<String, Long> userColorMap;
    private BaseCallback<MessageRealm> messageSelectedCallback;

    public ChatRecyclerAdapter(@NonNull Context context, List<MessageRealm> dataModel, BaseCallback<MessageRealm> messageSelectedCallback, HashMap<String, Long> userColorMap) {
        this.context = context;
        this.dataModel = dataModel;
        this.messageSelectedCallback = messageSelectedCallback;
        this.userColorMap = userColorMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        LinearLayout nameDateTimeLayout;
        LinearLayout messageContentLayout;
        LinearLayout messageOnlyContentLayout;
        LinearLayout nameDateOnlyLayout;
        TextView userName;
        TextView timeDate;
        TextView messageContent;
        TextView userIcon;
        MKLoader sendingMk;
        TextView status;
        TextView newDateTv;

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
            messageOnlyContentLayout = v.findViewById(R.id.message_only_content_layout);
            nameDateOnlyLayout = v.findViewById(R.id.name_date_only_layout);
            newDateTv = v.findViewById(R.id.new_date_tv);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(messageSelectedCallback != null)messageSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public ChatRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageRealm message = dataModel.get(position);
        boolean isUserMessage = message.getUid().equals(UserPreferences.getInstance().getUid());
        if(position == 0){
            if(message.getCreatedDate() != 0) {
                holder.timeDate.setText(createFormattedTime(message.getCreatedDate()));
            }
            if(dataModel.size() == 1){
                if(isUserMessage){
                    holder.userIcon.setVisibility(View.GONE);
                    holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                    setLayoutMargin(holder, 48, 8, 8, 32);
                }else{
                    holder.userIcon.setVisibility(View.VISIBLE);
                    holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                    setLayoutMargin(holder, 8, 8, 48, 32);
                }
            }else {
                if(isUserMessage){
                    holder.userIcon.setVisibility(View.GONE);
                    if(dataModel.get(position).getUid().equals(dataModel.get(position + 1).getUid()) &&
                            !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                        holder.nameDateTimeLayout.setVisibility(View.GONE);
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_bottom_right));
                    }else{
                        holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                    }
                    setLayoutMargin(holder, 48, 8, 8, 0);
                }else{
                    holder.userIcon.setVisibility(View.VISIBLE);
                    if(dataModel.get(position).getUid().equals(dataModel.get(position + 1).getUid()) &&
                            !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                        holder.nameDateTimeLayout.setVisibility(View.GONE);
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_bottom_left));
                    }else{
                        holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                    }
                    setLayoutMargin(holder, 8, 8, 48, 0);
                }
            }
            if(message.getCreatedDate() != 0) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                String formattedTodaysDate = dateFormat.format(new Date());
                String formattedYesterdaysDate = dateFormat.format(new Date(calendar.getTimeInMillis()));
                String formattedCreatedDate = dateFormat.format(new Date(message.getCreatedDate()));
                if(formattedCreatedDate.equals(formattedTodaysDate)){
                    holder.newDateTv.setText("Today - " + timeFormat.format(new Date(message.getCreatedDate())));
                }else if(formattedCreatedDate.equals(formattedYesterdaysDate)){
                    holder.newDateTv.setText("Yesterday - " + timeFormat.format(new Date(message.getCreatedDate())));
                }else{
                    holder.newDateTv.setText(formattedCreatedDate);
                }
                holder.newDateTv.setVisibility(View.VISIBLE);
            }
        }else {
            String previousMessageUid = dataModel.get(position - 1).getUid();
            String currentMessageUid = dataModel.get(position).getUid();

            if (previousMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position).getCreatedDate(), dataModel.get(position-1).getCreatedDate())) {
                holder.userIcon.setVisibility(View.GONE);
                if(position == dataModel.size()-1){
                    holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                    if(isUserMessage){
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_top_right));
                        setLayoutMargin(holder, 48, 2, 8, 32);
                    }else{
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        setLayoutMargin(holder, 8, 2, 48, 32);
                    }
                }else{
                    holder.nameDateTimeLayout.setVisibility(View.GONE);
                    String nextMessageUid = dataModel.get(position + 1).getUid();
                    if(isUserMessage){
                        if(nextMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_top_bottom_right));
                        }else{
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_top_right));
                            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                        }
                        setLayoutMargin(holder, 48, 2, 8, 0);
                    }else{
                        if(nextMessageUid.equals(currentMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_bottom_left));
                        }else{
                            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        }
                        setLayoutMargin(holder, 8, 2, 48, 0);
                    }
                }
            } else {
                holder.userIcon.setVisibility(View.VISIBLE);
                if(position == dataModel.size()-1){
                    holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                    if(isUserMessage){
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                        setLayoutMargin(holder, 48, 24, 8, 32);
                    }else{
                        holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        setLayoutMargin(holder, 8, 24, 48, 32);
                    }
                }else{
                    String nextMessageUid = dataModel.get(position + 1).getUid();
                    if(isUserMessage){
                        holder.nameDateTimeLayout.setVisibility(View.GONE);
                        if(currentMessageUid.equals(nextMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user_bottom_right));
                        }else{
                            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_user));
                        }
                        setLayoutMargin(holder, 48, 24, 8, 0);
                    }else{
                        if(currentMessageUid.equals(nextMessageUid) && !isTimeSinceLastMessageGreaterThan4Min(dataModel.get(position+1).getCreatedDate(), dataModel.get(position).getCreatedDate())) {
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_bottom_left));
                            holder.nameDateTimeLayout.setVisibility(View.GONE);
                        }else{
                            holder.nameDateTimeLayout.setVisibility(View.VISIBLE);
                            holder.messageOnlyContentLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_non_user_top_left));
                        }
                        setLayoutMargin(holder, 8, 24, 48, 0);
                    }
                }
            }
            if(message.getCreatedDate() != 0) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                String formattedTodaysDate = dateFormat.format(new Date());
                String formattedYesterdaysDate = dateFormat.format(new Date(calendar.getTimeInMillis()));
                String formattedCreatedDate = dateFormat.format(new Date(message.getCreatedDate()));
                String formattedPreviousCreatedDate = dateFormat.format(new Date(dataModel.get(position - 1).getCreatedDate() ));
                if(!formattedCreatedDate.equals(formattedPreviousCreatedDate)){
                    if(formattedCreatedDate.equals(formattedTodaysDate)){
                        holder.newDateTv.setText("Today - " + timeFormat.format(new Date(message.getCreatedDate())));
                    }else if(formattedCreatedDate.equals(formattedYesterdaysDate)){
                        holder.newDateTv.setText(String.format("Yesterday - " + timeFormat.format(new Date(message.getCreatedDate()))));
                    }else{
                        holder.newDateTv.setText(formattedCreatedDate);
                    }
                    holder.newDateTv.setVisibility(View.VISIBLE);
                }else{
                    holder.newDateTv.setVisibility(View.GONE);
                }
                holder.timeDate.setText(createFormattedTime(message.getCreatedDate()));
            }
        }
        setMessageStatus(holder, message);

        holder.userName.setText(message.getMessageOwnerName());

        if(message.getMessageContent() != null && !message.getMessageContent().isEmpty()) {
            holder.messageContent.setText(message.getMessageContent());
        }
        holder.userIcon.setText(String.valueOf(message.getMessageOwnerName().charAt(0)));


        FrameLayout.LayoutParams paramsMessageContentLayhout = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams paramsMessageOnlyContentLayout = (LinearLayout.LayoutParams)holder.messageOnlyContentLayout.getLayoutParams();
        LinearLayout.LayoutParams paramsNameDateTimeLayout = (LinearLayout.LayoutParams)holder.nameDateTimeLayout.getLayoutParams();
        if(isUserMessage){
            paramsMessageOnlyContentLayout.gravity = Gravity.RIGHT;
            paramsNameDateTimeLayout.gravity = Gravity.RIGHT;
            holder.userIcon.setVisibility(View.GONE);
            holder.userName.setVisibility(View.GONE);
            paramsMessageContentLayhout.setMargins((int)DpUtil.pxFromDp(context, 34), (int)DpUtil.pxFromDp(context, 0),
                    (int)DpUtil.pxFromDp(context, 0), (int)DpUtil.pxFromDp(context, 0));
            paramsMessageContentLayhout.gravity = Gravity.RIGHT;
            holder.messageContent.setTextColor(Color.BLACK);
            holder.messageOnlyContentLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
        }else{
            holder.userName.setVisibility(View.VISIBLE);
            paramsMessageOnlyContentLayout.gravity = Gravity.LEFT;
            paramsNameDateTimeLayout.gravity = Gravity.LEFT;
            if(userColorMap != null && !userColorMap.isEmpty()) {
                holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(userColorMap.get(message.getUid()).intValue())));
                holder.messageOnlyContentLayout.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(userColorMap.get(message.getUid()).intValue())));
            }
            holder.messageContent.setTextColor(context.getResources().getColor(R.color.white));
            paramsMessageContentLayhout.setMargins((int)DpUtil.pxFromDp(context, 34), (int)DpUtil.pxFromDp(context, 0),
                    (int)DpUtil.pxFromDp(context, 0), (int)DpUtil.pxFromDp(context, 0));
            paramsMessageContentLayhout.gravity = Gravity.LEFT;
        }
        holder.messageContentLayout.setLayoutParams(paramsMessageContentLayhout);
        holder.messageOnlyContentLayout.setLayoutParams(paramsMessageOnlyContentLayout);
        holder.nameDateTimeLayout.setLayoutParams(paramsNameDateTimeLayout);
    }


    private void setLayoutMargin(ViewHolder holder, float leftDp, float topDp, float rightDp, float bottomDp){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins((int)DpUtil.pxFromDp(context, leftDp), (int)DpUtil.pxFromDp(context, topDp),
                (int)DpUtil.pxFromDp(context, rightDp), (int)DpUtil.pxFromDp(context, bottomDp));
        holder.root.setLayoutParams(params);
    }

    private void setMessageStatus(ViewHolder holder, MessageRealm message){
        if (message.isSavedToFirebase()) {
            holder.sendingMk.setVisibility(View.GONE);
            holder.status.setVisibility(View.GONE);
        }else{
            holder.sendingMk.setVisibility(View.VISIBLE);
            holder.status.setVisibility(View.VISIBLE);
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
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa");
        return timeFormat.format(new Date(createdDate));
    }

    public void OnDataSetChanged(List<MessageRealm> dataModel) {
        this.dataModel = dataModel;
    }
}