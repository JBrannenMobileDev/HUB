package jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.UserColorUtil;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {
    private List<UserRealm> dataModel;
    private Context context;
    private BaseCallback<UserRealm> rowSelectedCallback;
    private BaseCallback<UserRealm> directMessageSelectedCallback;
    private BaseCallback<UserRealm> emailSelectedCallback;
    private BaseCallback<UserRealm> callSelectedCallback;

    public ContactsRecyclerAdapter(Context context, List<UserRealm> dataModel, BaseCallback<UserRealm> rowSelectedCallback,
                                   BaseCallback<UserRealm> directMessageSelectedCallback,
                                   BaseCallback<UserRealm> emailSelectedCallback,
                                   BaseCallback<UserRealm> callSelectedCallback) {
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
        this.directMessageSelectedCallback = directMessageSelectedCallback;
        this.emailSelectedCallback = emailSelectedCallback;
        this.callSelectedCallback = callSelectedCallback;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        TextView email;
        TextView userIcon;
        FrameLayout root;
        LinearLayout directMessage;
        LinearLayout emailLayout;
        FrameLayout greyDivider;
        LinearLayout call;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.account_contact_frame_layout);
            name = v.findViewById(R.id.contact_name_tv);
            number = v.findViewById(R.id.contact_phone_tv);
            email = v.findViewById(R.id.contact_email_tv);
            userIcon = v.findViewById(R.id.customer_user_icon_tv);
            directMessage = v.findViewById(R.id.direct_message_layout);
            emailLayout = v.findViewById(R.id.send_email_layout);
            call = v.findViewById(R.id.call_layout);
            greyDivider = v.findViewById(R.id.grey_divider_frame_layout);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });

            directMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    directMessageSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
            emailLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
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
        UserRealm user = dataModel.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        holder.email.setText("E-mail: " + user.getEmail());
        holder.number.setText("Phone number: " + getFormattedNumber(user.getPhoneNumber()));
        holder.userIcon.setText(String.valueOf(user.getFirstName().charAt(0)));
        holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(user.getUserColor())));
        if(position == 0){
            holder.greyDivider.setVisibility(View.GONE);
        }
    }

    private String getFormattedNumber(String phoneNumber) {
        return phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3");
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    public void OnDataSetChanged(List<UserRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
