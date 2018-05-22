package jjpartnership.hub.view_layer.activities.account_activity.new_group_message_fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;
import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by Jonathan on 4/21/2018.
 */

public class NewGroupMessageDialogFragment extends DialogFragment implements TagsEditText.TagsEditListener{

    @BindView(R.id.new_group_message_cancel)TextView cancelBt;
    @BindView(R.id.new_group_message_send)TextView sendBt;
    @BindView(R.id.new_group_message_editText)EditText userInput;
    @BindView(R.id.new_group_message_name)EditText groupName;
    @BindView(R.id.new_group_message_title)TextView titleTv;
    @BindView(R.id.new_group_message_tags)TagsEditText tags;

    private ArrayList<String> agentNames;
    private ArrayList<String> selectedAgentNames;
    private Map<String, UserRealm> agents;
    private List<String> selectedAgentIds;
    private String accountId;

    public NewGroupMessageDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static NewGroupMessageDialogFragment newInstance(String accountId, ArrayList<String> agentIds, ArrayList<String> selectedAgentIds) {
        NewGroupMessageDialogFragment frag = new NewGroupMessageDialogFragment();
        Bundle args = new Bundle();
        args.putString("accountId", accountId);
        args.putStringArrayList("agentIds", agentIds);
        args.putStringArrayList("selectedAgentIds", selectedAgentIds);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_group_message, container);
        ButterKnife.bind(this, v);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        agents = new HashMap<>();
        agentNames = new ArrayList<>();
        selectedAgentNames = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupName.requestFocus();
        this.accountId = getArguments().getString("accountId");
        fetchData(getArguments().getStringArrayList("agentIds"), getArguments().getStringArrayList("selectedAgentIds"));
        initTagsView();
    }

    private void fetchData(ArrayList<String> agentIds, List<String> selectedAgentIds) {
        this.selectedAgentIds = selectedAgentIds;
        for(String id : agentIds){
            UserRealm user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", id).findFirst();
            if(user != null){
                agents.put(user.getUid(), user);
                agentNames.add(user.getFirstName() + " " + user.getLastName());
            }
        }
        for(String id : selectedAgentIds){
            UserRealm user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", id).findFirst();
            if(user != null) selectedAgentNames.add(user.getFirstName() + " " + user.getLastName());
        }
    }

    private void initTagsView() {
        tags.setTagsListener(this);
        tags.setTagsWithSpacesEnabled(true);
        tags.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, agentNames));
        tags.setThreshold(1);
        String[] selectedAgentsArray = new String[selectedAgentNames.size()];
        selectedAgentsArray = selectedAgentNames.toArray(selectedAgentsArray);
        tags.setTags(selectedAgentsArray);
    }

    @OnClick(R.id.new_group_message_cancel)
    public void onCancelClicked(){
        dismiss();
    }

    @OnClick(R.id.new_group_message_send)
    public void onSendClicked(){
        if(tags.getTags().size() > 0) {
            if (!groupName.getText().toString().isEmpty() && groupName.getText().toString().length() > 0) {
                if (!userInput.getText().toString().isEmpty() && userInput.getText().toString().length() > 0) {
                    createNewGroupChat();
                } else {
                    Toast.makeText(getActivity(), "Please provide a message.", Toast.LENGTH_LONG).show();
                    userInput.requestFocus();
                    showKeyboard();
                }
            } else {
                Toast.makeText(getActivity(), "Please provide a Group Name.", Toast.LENGTH_LONG).show();
                groupName.requestFocus();
                showKeyboard();
            }
        }else{
            Toast.makeText(getActivity(), "Please add at least one person to this group.", Toast.LENGTH_SHORT).show();
            tags.requestFocus();
            showKeyboard();
        }
    }

    private void showKeyboard(){
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void createNewGroupChat() {
        DataManager.getInstance().createNewGroupChat(selectedAgentIds, accountId, UserPreferences.getInstance().getUid(), groupName.getText().toString(), userInput.getText().toString());
        GroupChatRealm createdChat = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).equalTo("groupName", groupName.getText().toString()).findFirst();
        if(createdChat != null) {
            Toast.makeText(getActivity(), "Group Created", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Unable to create Group. Check internet connection and try again.", Toast.LENGTH_LONG).show();
        }
        dismiss();
    }

    @Override
    public void onTagsChanged(Collection<String> collection) {
        selectedAgentNames = new ArrayList<>(collection);
    }

    @Override
    public void onEditingFinished() {

    }
}
