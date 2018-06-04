package jjpartnership.hub.view_layer.activities.main_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class AccountRecyclerAdapter extends RecyclerView.Adapter<AccountRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<AccountRowItem> dataModel;
    private BaseCallback<AccountRowItem> rowSelectedCallback;

    public AccountRecyclerAdapter(@NonNull Context context, MainAccountsModel dataModel, BaseCallback<AccountRowItem> rowSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel.getRowItems();
        this.rowSelectedCallback = rowSelectedCallback;
    }

    public AccountRecyclerAdapter(@NonNull Context context, List<AccountRowItem> dataModel, BaseCallback<AccountRowItem> rowSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        TextView accountName;
        TextView accountAddress;
        ImageView icon;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.accounts_item_frame_layout);
            accountName = v.findViewById(R.id.account_name_tv);
            accountAddress = v.findViewById(R.id.account_adress_tv);
            icon = v.findViewById(R.id.account_icon_iv);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.get(getLayoutPosition()));
                }
            });
        }
    }

    @Override
    public AccountRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountRowItem rowItem = dataModel.get(position);
        holder.accountName.setText(rowItem.getAccountName());
        AccountRealm account = RealmUISingleton.getInstance().getRealmInstance().where(AccountRealm.class)
                .equalTo("accountIdFire", rowItem.getAccountIdFire()).findFirst();
        CompanyRealm company = null;
        if(account != null) {
            company = RealmUISingleton.getInstance().getRealmInstance().where(CompanyRealm.class)
                    .equalTo("companyId", account.getCompanyCustomerId()).findFirst();
            if(company != null) {
                holder.accountAddress.setVisibility(View.VISIBLE);
                holder.accountAddress.setText(company.getAddress());
            }else{
                holder.accountAddress.setVisibility(View.GONE);
            }
        }else{
            holder.accountAddress.setVisibility(View.VISIBLE);
        }

        if(!account.getAccountSalesAgentUids().contains(UserPreferences.getInstance().getUid())){
            holder.icon.setImageTintList(context.getResources().getColorStateList(R.color.colorPrimaryLight));
        }
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    public void OnDataSetChanged(MainAccountsModel dataModel) {
        this.dataModel = dataModel.getRowItems();
    }

    public void OnDataSetChanged(List<AccountRowItem> dataModel) {
        this.dataModel = dataModel;
    }
}
