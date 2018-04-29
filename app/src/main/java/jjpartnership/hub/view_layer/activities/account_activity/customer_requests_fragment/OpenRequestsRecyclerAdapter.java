package jjpartnership.hub.view_layer.activities.account_activity.customer_requests_fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class OpenRequestsRecyclerAdapter extends RecyclerView.Adapter<OpenRequestsRecyclerAdapter.ViewHolder> {
    private Context context;
    private RealmList<CustomerRequestRealm> openRequests;
    private BaseCallback<CustomerRequestRealm> openRequestSelectedCallback;

    public OpenRequestsRecyclerAdapter(@NonNull Context context, RealmList<CustomerRequestRealm> openRequests, BaseCallback<CustomerRequestRealm> openRequestSelectedCallback) {
        this.context = context;
        this.openRequests = openRequests;
        this.openRequestSelectedCallback = openRequestSelectedCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        TextView customerName;
        TextView messageTime;
        TextView messageContent;
        TextView userIcon;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.request_item_frame_layout);
            customerName = v.findViewById(R.id.customer_name_tv);
            messageTime = v.findViewById(R.id.message_time_tv);
            messageContent = v.findViewById(R.id.message_content_tv);
            userIcon = v.findViewById(R.id.user_icon_tv);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRequestSelectedCallback.onResponse(openRequests.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public OpenRequestsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerRequestRealm request = openRequests.get(position);
        UserRealm requester = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", request.getCustomerUid()).findFirst();
        if(request != null){
            holder.customerName.setText(request.getCustomerName());
            holder.userIcon.setText(String.valueOf(request.getCustomerName().charAt(0)));
        }

        if(request.getMostRecentMessageTime() != 0) {
            holder.messageTime.setText(createFormattedTime(request.getMostRecentMessageTime()));
        }
        if(request.getRequestMessage() != null) {
            holder.messageContent.setText(request.getRequestMessage());
        }
        if(requester != null) holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(requester.getUserColor())));
    }

    @Override
    public int getItemCount() {
        return openRequests.size();
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm aaa - M/dd/yy");
        return dateFormatter.format(new Date(createdDate));
    }

    public void OnDataSetChanged(RealmList<CustomerRequestRealm> openRequests) {
        this.openRequests = openRequests;
    }
}
