package jjpartnership.hub.view_layer.activities.search_activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.TwoResponseCallback;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class UserSearchResultsRecyclerAdapter extends RecyclerView.Adapter<UserSearchResultsRecyclerAdapter.ViewHolder> {
    private List<UserRealm> dataModel;
    private Context context;
    private BaseCallback<UserRealm> rowSelectedCallback;
    private BaseCallback<UserRealm> directMessageSelectedCallback;
    private TwoResponseCallback<UserRealm, Boolean> checkboxSelectedCallback;
    private String currentUserId;

    public UserSearchResultsRecyclerAdapter(Context context, List<UserRealm> dataModel, BaseCallback<UserRealm> rowSelectedCallback,
                                            BaseCallback<UserRealm> directMessageSelectedCallback,
                                            TwoResponseCallback<UserRealm, Boolean> checkboxSelectedCallback) {
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
        this.directMessageSelectedCallback = directMessageSelectedCallback;
        this.context = context;
        this.checkboxSelectedCallback = checkboxSelectedCallback;
        currentUserId = UserPreferences.getInstance().getUid();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView businessUnit;
        TextView role;
        TextView userIcon;
        FrameLayout root;
        FrameLayout greyDivider;
        ImageView directMessageIv;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.account_agents_frame_layout);
            name = v.findViewById(R.id.user_name_tv);
            userIcon = v.findViewById(R.id.user_icon);
            businessUnit = v.findViewById(R.id.info1_tv);
            role = v.findViewById(R.id.info2_tv);
            directMessageIv = v.findViewById(R.id.send_direct_message_iv);
            greyDivider = v.findViewById(R.id.grey_divider_frame_layout);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });

            directMessageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    directMessageSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public UserSearchResultsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_results_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRealm user = dataModel.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        if(user.getBusinessUnit() == null){
            holder.businessUnit.setText("Business unit: unavailable");
        }else{
            holder.businessUnit.setText("Business unit: " + user.getBusinessUnit());
        }
        if(user.getRole() == null){
            holder.role.setText("Role: unavailable");
        }else{
            holder.role.setText("Role: " + user.getRole());
        }
        if(position == 0){
            holder.greyDivider.setVisibility(View.GONE);
        }
        if(currentUserId.equals(user.getUid())){
            holder.directMessageIv.setVisibility(View.GONE);
        }else{
            holder.directMessageIv.setVisibility(View.VISIBLE);
        }
        holder.userIcon.setText(String.valueOf(user.getFirstName().charAt(0)));
        holder.userIcon.setBackgroundTintList(context.getResources().getColorStateList(UserColorUtil.getUserColor(user.getUserColor())));
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    public void OnDataSetChanged(List<UserRealm> dataModel) {
        this.dataModel = dataModel;
    }
}
