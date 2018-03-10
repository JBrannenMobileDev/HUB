package jjpartnership.hub.view_layer.activities.main_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.utils.BaseCallback;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class AccountListAdapter extends BaseAdapter {
    private Context context;
    private List<AccountRealm> accounts;
    private BaseCallback<AccountRealm> rowSelectedCallback;

    public AccountListAdapter(@NonNull Context context, List<AccountRealm> accounts, BaseCallback<AccountRealm> rowSelectedCallback) {
        this.context = context;
        this.accounts = accounts;
        this.rowSelectedCallback = rowSelectedCallback;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.account_list_row_item, parent, false);
        FrameLayout root = rowView.findViewById(R.id.accounts_item_frame_layout);
        TextView accountName = rowView.findViewById(R.id.account_name_tv);
        TextView messageTime = rowView.findViewById(R.id.most_recent_message_tv);
        TextView messageOwnerName = rowView.findViewById(R.id.message_owner_name_tv);
        TextView messageContent = rowView.findViewById(R.id.message_content_tv);
        TextView accountIcon = rowView.findViewById(R.id.account_icon_tv);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowSelectedCallback.onResponse(accounts.get(position));
            }
        });


        return rowView;
    }

    private String createFormattedTime(long createdDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aaa");
        return dateFormatter.format(new Date(createdDate));
    }

    public void OnDataSetChanged(RealmResults<AccountRealm> accountRealms) {
        this.accounts = accountRealms;
    }
}
