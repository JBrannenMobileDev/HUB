package jjpartnership.hub.view_layer.activities.main_activity;

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
    private MainAccountsModel dataModel;
    private BaseCallback<AccountRowItem> rowSelectedCallback;

    public AccountRecyclerAdapter(@NonNull Context context, MainAccountsModel dataModel, BaseCallback<AccountRowItem> rowSelectedCallback) {
        this.context = context;
        this.dataModel = dataModel;
        this.rowSelectedCallback = rowSelectedCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout root;
        TextView accountName;
        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.accounts_item_frame_layout);
            accountName = v.findViewById(R.id.account_name_tv);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rowSelectedCallback.onResponse(dataModel.getRowItems().get(getLayoutPosition()));
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
        AccountRowItem rowItem = dataModel.getRowItems().get(position);
        holder.accountName.setText(rowItem.getAccountName());
    }

    @Override
    public int getItemCount() {
        return dataModel.getRowItems().size();
    }

    public void OnDataSetChanged(MainAccountsModel dataModel) {
        this.dataModel = dataModel;
    }
}
