package jjpartnership.hub.data_layer.firebase_db;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CustomerRequest;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

import static android.content.ContentValues.TAG;

/**
 * Created by Jonathan on 5/15/2018.
 */

public class FirebaseSyncManager {
    private DatabaseReference thisUserReference;
    private DatabaseReference thisUserAccountsReference;
    private DatabaseReference thisUserDirectMessagesReference;
    private DatabaseReference usersReference;
    private DatabaseReference companiesReference;
    private DatabaseReference accountsReference;
    private DatabaseReference directChatsReference;
    private DatabaseReference groupChatsReference;
    private DatabaseReference industriesReference;
    private DatabaseReference userColorsReference;
    private DatabaseReference chatMessagesReference;
    private DatabaseReference customerRequestsReference;
    private FirebaseDatabase database;

    private String currentUid;
    private User currentUser;
    private List<User> users;
    private List<String> accountIds;
    private List<Account> userAccounts;
    private List<CustomerRequest> customerRequests;
    private List<Company> companies;
    private List<GroupChat> allGroupChats;
    private List<DirectChat> directChats;
    private List<UserColor> userColors;
    private BaseCallback<Boolean> syncCompleteCallback;

    public FirebaseSyncManager(BaseCallback<Boolean> syncCompleteCallback) {
        thisUserReference = database.getReference("users").child(UserPreferences.getInstance().getUid());
        thisUserAccountsReference = database.getReference("users").child(UserPreferences.getInstance().getUid()).child("accountIds");
        thisUserDirectMessagesReference = database.getReference("users").child(UserPreferences.getInstance().getUid()).child("directChatIds");
        usersReference = database.getReference("users");
        chatMessagesReference = database.getReference("chat_messages");
        companiesReference = database.getReference("companies");
        accountsReference = database.getReference("accounts");
        directChatsReference = database.getReference("direct_chats");
        groupChatsReference = database.getReference("group_chats");
        industriesReference = database.getReference("industries");
        userColorsReference = database.getReference("user_colors");
        customerRequestsReference = database.getReference("customer_requests");
        users = new ArrayList<>();
        accountIds = new ArrayList<>();
        userAccounts = new ArrayList<>();
        customerRequests = new ArrayList<>();
        companies = new ArrayList<>();
        allGroupChats = new ArrayList<>();
        directChats = new ArrayList<>();
        userColors = new ArrayList<>();
        currentUid = UserPreferences.getInstance().getUid();
        this.syncCompleteCallback = syncCompleteCallback;
    }

    public void syncFirebaseToLocalDb(){
        getThisUser();
        saveAllDataToLocalDb();
    }

    private void getThisUser() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    currentUser = user;
                    UserPreferences.getInstance().setUserType(user.getUserType());
                    getAccountIds();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
            }
        };
        thisUserReference.addListenerForSingleValueEvent(userListener);
    }

    private void getAccountIds(){
        final ValueEventListener userAccountsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> accountIdsFire = (List<String>)dataSnapshot.getValue();
                if(accountIdsFire != null && accountIdsFire.size() > 0 ){
                    accountIds.addAll(accountIdsFire);
                    getAccounts();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadAccountIds:onCancelled", databaseError.toException());
                syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
            }
        };
        thisUserAccountsReference.addListenerForSingleValueEvent(userAccountsListener);
    }

    private void getAccounts() {
        for(String accountId : accountIds){
            ValueEventListener accountListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account account = dataSnapshot.getValue(Account.class);
                    if(account != null){
                        userAccounts.add(account);
                    }
                    if(userAccounts.size() == accountIds.size()){
                        getCompanies();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadAccounts:onCancelled", databaseError.toException());
                    syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
                }
            };
            accountsReference.child(accountId).addListenerForSingleValueEvent(accountListener);
        }
    }

    private void getCompanies() {
        for(Account account : userAccounts) {
            String companyId;
            if (UserPreferences.getInstance().getUserType().equalsIgnoreCase(UserRealm.TYPE_SALES)) {
                companyId = account.getCompanyCustomerId();
            } else {
                companyId = account.getCompanySalesId();
            }

            ValueEventListener companyListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Company company = dataSnapshot.getValue(Company.class);
                    if (company != null) {
                        companies.add(company);
                    }
                    if(companies.size() - 1 == userAccounts.size()){
                        getAccountRequests();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadCompanies:onCancelled", databaseError.toException());
                    syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
                }
            };
            companiesReference.child(companyId).addListenerForSingleValueEvent(companyListener);
        }
    }

    private void getAccountRequests() {
        int requestCount = 0;
        for(Account accountTemp : userAccounts){
            if(accountTemp.getCustomerRequestIds() != null){
                requestCount = requestCount + accountTemp.getCustomerRequestIds().size();
            }
        }
        final int finalCount = requestCount;

        if(userAccounts != null && userAccounts.size() > 0) {
            for (final Account localAccount : userAccounts) {
                if(localAccount.getCustomerRequestIds() != null) {
                    for (String requestId : localAccount.getCustomerRequestIds().values()) {
                        ValueEventListener chatListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                CustomerRequest request = dataSnapshot.getValue(CustomerRequest.class);
                                if (request != null) {
                                    customerRequests.add(request);
                                }
                                if (customerRequests.size() == finalCount) {
                                    getDirectChats();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "loadAccountRequests:onCancelled", databaseError.toException());
                                syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
                            }
                        };
                        if (requestId != null && !requestId.isEmpty()) {
                            customerRequestsReference.child(requestId).addListenerForSingleValueEvent(chatListener);
                        }
                    }
                }
            }
            if(finalCount == 0){
                getDirectChats();
            }
        }else{
            getDirectChats();
        }
    }

    private void getDirectChats() {
        Map<String,String> directChatIds = currentUser.getDirectChats();
        if(directChatIds != null && directChatIds.size() > 0) {
            for (String chatId : directChatIds.values()) {
                ValueEventListener chatListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DirectChat chat = dataSnapshot.getValue(DirectChat.class);
                        if (chat != null) {
                            directChats.add(chat);
                        }
                        if (directChats.size() == currentUser.getDirectChats().size()) {
                            getGroupChats();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadDirectChats:onCancelled", databaseError.toException());
                        syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
                    }
                };
                directChatsReference.child(chatId).addListenerForSingleValueEvent(chatListener);
            }
        }else{
            getGroupChats();
        }
    }

    private void getGroupChats() {
        final List<String> groupChatIds = new ArrayList<>();
        groupChatIds.addAll(currentUser.getGroupChats().values());
        for(CustomerRequest request : customerRequests){
            if(request != null){
                groupChatIds.add(request.getGroupChatId());
            }
        }
        final int groupChatInitSize = groupChatIds.size();

        if(groupChatIds.size() > 0) {
            for (String chatId : groupChatIds) {
                ValueEventListener groupChatListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GroupChat gChat = dataSnapshot.getValue(GroupChat.class);
                        if (gChat != null) {
                            allGroupChats.add(gChat);
                        }
                        if (allGroupChats.size() == groupChatInitSize) {
                            getUsers();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadGroupChats:onCancelled", databaseError.toException());
                        syncCompleteCallback.onFailure(new Exception(databaseError.getMessage()));
                    }
                };
                groupChatsReference.child(chatId).addListenerForSingleValueEvent(groupChatListener);
            }
        }else{
            getUsers();
        }
    }

    private void getUsers() {

    }

    private void getUserColors() {

    }

    private void getAllUserMessages() {

    }

    private void addMessageDataToGroupChats(){
        RealmResults<MessageRealm> realmMessages = RealmUISingleton.getInstance().getRealmInstance().where(MessageRealm.class).equalTo("chatId", gChat.getChatId()).findAll();
        if (realmMessages != null && realmMessages.size() > 0) {
            MessageRealm mostRecentMessage = realmMessages.get(realmMessages.size() - 1);
            gChat.setMostRecentMessage(new Message(mostRecentMessage));
            gChat.setMessageCreatedTime(mostRecentMessage.getCreatedDate());
            gChat.setMessageThreadId(mostRecentMessage.getMessageThreadId());
        }
    }

    private void saveAllDataToLocalDb() {

    }
}
