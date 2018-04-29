package jjpartnership.hub.data_layer.firebase_db;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.RealmList;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.CustomerRequest;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.DirectItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.MessageThread;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

import static android.content.ContentValues.TAG;

/**
 * Created by jbrannen on 2/24/18.
 */

public class FirebaseManager {
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

    private User currentUser;
    private List<Account> accounts;
    private List<GroupChat> groupChats;
    private List<DirectChat> directChats;
    private List<Company> companies;
    private List<Message> messages;
    private List<CustomerRequest> customerRequests;
    private String uid;

    private List<ChildEventListener> childEventListenersMessageThreads;
    private List<ValueEventListener> valueEventListeners;

    public FirebaseManager() {
        childEventListenersMessageThreads = new ArrayList<>();
        valueEventListeners = new ArrayList<>();
        accounts = new ArrayList<>();
        groupChats = new ArrayList<>();
        directChats = new ArrayList<>();
        companies = new ArrayList<>();
        messages = new ArrayList<>();
        customerRequests = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
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
    }

    public void initDataListeners() {
        final ValueEventListener userAccountsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> accountIds = (List<String>)dataSnapshot.getValue();
                if(accountIds != null && accountIds.size() > 0 ){
                    fetchAccounts(accountIds);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        final ValueEventListener userGroupChatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    GroupChat chat = dataSnapshot.getValue(GroupChat.class);
                    if(chat != null) {
                        groupChats.add(chat);
                    }
                    if(groupChats.size() == currentUser.getGroupChats().size()){
                        thisUserAccountsReference = database.getReference("users").child(uid).child("accountIds");
                        thisUserAccountsReference.addListenerForSingleValueEvent(userAccountsListener);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUserGroupChats:onCancelled", databaseError.toException());
            }
        };

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    currentUser = user;
                    UserPreferences.getInstance().setUserType(user.getUserType());
                    DataManager.getInstance().updateRealmUser(currentUser);
                    if(currentUser.getGroupChats() != null && currentUser.getGroupChats().size() > 0){
                        for(String chatId : currentUser.getGroupChats().values()){
                            groupChatsReference.child(chatId).addListenerForSingleValueEvent(userGroupChatsListener);
                        }
                    }else{
                        thisUserAccountsReference = database.getReference("users").child(uid).child("accountIds");
                        thisUserAccountsReference.addListenerForSingleValueEvent(userAccountsListener);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };
        thisUserReference = database.getReference("users").child(uid);
        thisUserReference.addListenerForSingleValueEvent(userListener);
    }

    private void fetchAccounts(final List<String> accountIds) {
        for(String accountId : accountIds){
            ValueEventListener accountListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account account = dataSnapshot.getValue(Account.class);
                    if(account != null){
                        accounts.add(account);
                    }
                    if(accounts.size() == accountIds.size()){
                        DataManager.getInstance().updateRealmAccounts(accounts);
                        getCustomerIds(accounts);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            accountsReference.child(accountId).addListenerForSingleValueEvent(accountListener);
        }

    }

    private void getCustomerIds(final List<Account> accountList) {
        ValueEventListener companySalesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                if (company != null) {
                    companies.add(company);
                }
                for(Account account : accountList) {
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
                            if(companies.size() - 1 == accounts.size()){
                                DataManager.getInstance().updateRealmCompanys(companies);
                                getGroupChat(accounts);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    companiesReference.child(companyId).addListenerForSingleValueEvent(companyListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        companiesReference.child(accountList.get(0).getCompanySalesId()).addListenerForSingleValueEvent(companySalesListener);
    }

    private void getGroupChat(final List<Account> accountList) {
        final int groupChatInitSize = groupChats.size();
        if(accountList.size() > 0) {

            for (Account account : accountList) {
                ValueEventListener groupChatListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GroupChat gChat = dataSnapshot.getValue(GroupChat.class);
                        if (gChat != null) {
                            RealmResults<MessageRealm> realmMessages = RealmUISingleton.getInstance().getRealmInstance().where(MessageRealm.class).equalTo("chatId", gChat.getChatId()).findAll();
                            if (realmMessages != null && realmMessages.size() > 0) {
                                MessageRealm mostRecentMessage = realmMessages.get(realmMessages.size() - 1);
                                gChat.setMostRecentMessage(new Message(mostRecentMessage));
                                gChat.setMessageCreatedTime(mostRecentMessage.getCreatedDate());
                                gChat.setMessageThreadId(mostRecentMessage.getMessageThreadId());
                            }
                            groupChats.add(gChat);
                        }
                        if ((groupChats.size() - groupChatInitSize) == accounts.size()) {
                            fetchUserColors(groupChats);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                groupChatsReference.child(account.getGroupChatSalesId()).addListenerForSingleValueEvent(groupChatListener);
            }
        }else{
            DataManager.getInstance().updateRealmGroupChats(groupChats);
            fetchUserColors(groupChats);
        }
    }

    private void getGroupChatsFromRequests(final List<CustomerRequest> requestList) {
        final int initGroupChatSize = groupChats.size();
        if(requestList.size() > 0) {
            for (CustomerRequest request : requestList) {
                ValueEventListener groupChatListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GroupChat gChat = dataSnapshot.getValue(GroupChat.class);
                        if (gChat != null) {
                            RealmResults<MessageRealm> realmMessages = RealmUISingleton.getInstance().getRealmInstance().where(MessageRealm.class).equalTo("chatId", gChat.getChatId()).findAll();
                            if (realmMessages != null && realmMessages.size() > 0) {
                                MessageRealm mostRecentMessage = realmMessages.get(realmMessages.size() - 1);
                                gChat.setMostRecentMessage(new Message(mostRecentMessage));
                                gChat.setMessageCreatedTime(mostRecentMessage.getCreatedDate());
                                gChat.setMessageThreadId(mostRecentMessage.getMessageThreadId());
                            }
                            groupChats.add(gChat);
                        }
                        if (initGroupChatSize + requestList.size() == groupChats.size()) {
                            DataManager.getInstance().updateRealmGroupChats(groupChats);
                            buildUiModels();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                groupChatsReference.child(request.getGroupChatId()).addListenerForSingleValueEvent(groupChatListener);
            }
        }else{
            DataManager.getInstance().updateRealmGroupChats(groupChats);
            buildUiModels();
        }
    }

    private void fetchUserColors(List<GroupChat> groupChats) {
        final List<Long> colorList = new ArrayList<>();
        final Set<String> uids = new HashSet<>();
        for(GroupChat chat : groupChats){
            if(chat.getUserIdsList() != null) uids.addAll(chat.getUserIdsList());
        }
        RealmResults<CompanyRealm> companies = RealmUISingleton.getInstance().getRealmInstance().where(CompanyRealm.class).findAll();
        if(companies != null && companies.size() > 0){
            for(CompanyRealm company : companies) {
                uids.addAll(company.getEmployeeList());
            }
        }


        if(uids.size() > 0) {
            for (String uid : uids) {
                ValueEventListener userColorsListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long color = (long) dataSnapshot.getValue();
                        String uid = dataSnapshot.getKey();
                        colorList.add(color);
                        DataManager.getInstance().updateRealmUserColor(color, uid);
                        if (colorList.size() == uids.size()) {
                            fetchDirectChats(currentUser.getDirectChats());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                ValueEventListener usersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user != null){
                            DataManager.getInstance().updateRealmUser(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                database.getReference("user_colors").child(uid).addListenerForSingleValueEvent(userColorsListener);
                usersReference.child(uid).addListenerForSingleValueEvent(usersListener);
            }
        }else{
            fetchDirectChats(currentUser.getDirectChats());
        }
    }

    private void fetchDirectChats(Map<String, String> directChatIds){
        if(directChatIds != null && directChatIds.size() > 0) {
            for (String chatIds : directChatIds.values()) {
                ValueEventListener chatListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DirectChat chat = dataSnapshot.getValue(DirectChat.class);
                        if (chat != null) {
                            directChats.add(chat);
                        }
                        if (directChats.size() == currentUser.getDirectChats().size()) {
                            DataManager.getInstance().updateRealmDirectChats(directChats);
                            fetchCustomerRequests(accounts);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                directChatsReference.child(chatIds).addListenerForSingleValueEvent(chatListener);
            }
        }else{
            fetchCustomerRequests(accounts);
            //TODO delete all directChats from realm
        }
    }

    private void fetchCustomerRequests(List<Account> accounts) {
        int requestCount = 0;
        for(Account accountTemp : accounts){
            if(accountTemp.getCustomerRequestIds() != null){
                requestCount = requestCount + accountTemp.getCustomerRequestIds().size();
            }
        }
        final int finalCount = requestCount;

        if(accounts != null && accounts.size() > 0) {
            for (final Account localAccount : accounts) {
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
                                    DataManager.getInstance().insertOrUpdateCustomerRequest(customerRequests);
                                    getGroupChatsFromRequests(customerRequests);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        if (requestId != null && !requestId.isEmpty()) {
                            customerRequestsReference.child(requestId).addListenerForSingleValueEvent(chatListener);
                        }
                    }
                }
            }
            if(finalCount == 0){
                getGroupChatsFromRequests(customerRequests);
            }
        }else{
            getGroupChatsFromRequests(customerRequests);
        }
    }

    private void buildUiModels() {
        MainAccountsModel accountsModel = new MainAccountsModel();
        RealmList<RowItem> accountRowItems = new RealmList<>();
        RealmList<RowItem> requestRowItems = new RealmList<>();
        for(int i = 0; i < accounts.size(); i++){
            accountRowItems.add(new RowItem());
        }

        for(int i = 0; i < customerRequests.size(); i++){
            requestRowItems.add(new RowItem());
        }

        for(int i = 0; i < accounts.size(); i++){
            Company company = getAccountCompany(accounts.get(i));
            if(company != null) {
                GroupChat groupChatSales = getGroupChat(accounts.get(i).getGroupChatSalesId());
                accountRowItems.get(i).setAccountName(company.getName());
                accountRowItems.get(i).setAccountId(accounts.get(i).getAccountIdFire());
                if (groupChatSales.getMostRecentMessage() != null) {
                    accountRowItems.get(i).setMessageContent(groupChatSales.getMostRecentMessage().getMessageContent());
                    accountRowItems.get(i).setMessageCreatedAtTime(groupChatSales.getMostRecentMessage().getCreatedDate());
                    accountRowItems.get(i).setMessageOwnerName(groupChatSales.getMostRecentMessage().getMessageOwnerName());
                    if(!groupChatSales.getMostRecentMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                        accountRowItems.get(i).setNewMessage(true);
                    }
                }
            }
        }

        for(int i = 0; i < customerRequests.size(); i++){
            CustomerRequest request = customerRequests.get(i);
            if(request != null && request.isOpen()){
                requestRowItems.get(i).setMessageContent(request.getRequestMessage());
                requestRowItems.get(i).setMessageCreatedAtTime(request.getMostRecentMessageTime());
                requestRowItems.get(i).setMessageOwnerName(request.getCustomerName());
                if(request.getMostRecentGroupMessage() != null && !request.getMostRecentGroupMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())){
                    requestRowItems.get(i).setNewMessage(true);
                }
            }
        }

        Collections.sort(accountRowItems);
        accountsModel.setRowItems(accountRowItems);

        RealmList<RowItem> sortedByMostRecent = new RealmList<>();
        for(RowItem item : accountRowItems){
            sortedByMostRecent.add(item);
        }

        for(RowItem item : requestRowItems){
            sortedByMostRecent.add(item);
        }


        MainDirectMessagesModel directModel = new MainDirectMessagesModel();
        RealmList<DirectItem> directItems = new RealmList<>();
        for(DirectChat directChat : directChats){
            DirectItem newItem = new DirectItem();
            newItem.setDirectChatId(directChat.getChatId());
            if(directChat.getMostRecentMessage() != null) {
                newItem.setMessageOwnerName(directChat.getMostRecentMessage().getMessageOwnerName());
                newItem.setMessageOwnerUid(directChat.getMostRecentMessage().getCreatedByUid());
                newItem.setMessageContent(directChat.getMostRecentMessage().getMessageContent());
                newItem.setMessageCreatedAtTime(directChat.getMostRecentMessage().getCreatedDate());
                if (!directChat.getMostRecentMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                    newItem.setNewMessage(true);
                } else {
                    newItem.setNewMessage(false);
                }
                directItems.add(newItem);
            }
        }
        Collections.sort(directItems);
        directModel.setDirectItems(directItems);

        for(DirectItem item : directItems){
            RowItem newRowItem = new RowItem();
            newRowItem.setItemType(RowItem.TYPE_DIRECT);
            newRowItem.setAccountId(item.getDirectChatId());
            newRowItem.setNewMessage(item.isNewMessage());
            newRowItem.setMessageContent(item.getMessageContent());
            newRowItem.setMessageCreatedAtTime(item.getMessageCreatedAtTime());
            sortedByMostRecent.add(newRowItem);
        }

        Collections.sort(sortedByMostRecent, RowItem.createdAtComparator);
        Collections.reverse(sortedByMostRecent);
        MainRecentModel recentModel = new MainRecentModel();
        RealmList<RowItem> recentRowItems = new RealmList<>();
        if(sortedByMostRecent.size() > 3) {
            recentRowItems.addAll(sortedByMostRecent.subList(0, 4));
        }else{
            recentRowItems.addAll(sortedByMostRecent.subList(0, sortedByMostRecent.size()));
        }
        recentModel.setRowItems(recentRowItems);

        DataManager.getInstance().updateRealmMainModels(accountsModel, filterModel(recentModel), directModel);
        initChatMessagesListener(groupChats, directChats);
        initGroupChatListener(groupChats, directChats);
    }

    private void initGroupChatListener(List<GroupChat> groupChatsLocal, List<DirectChat> directChatsLocal) {

    }

    private MainRecentModel filterModel(MainRecentModel recentModel) {
        RealmList<RowItem> filteredRowItems = new RealmList<>();
        for(RowItem item : recentModel.getRowItems()){
            if(item.getMessageCreatedAtTime() > 0){
                filteredRowItems.add(item);
            }
        }
        recentModel.setRowItems(filteredRowItems);
        return recentModel;
    }

    private void addDirectChatMessageListener(DirectChat newChat){

        childEventListenersMessageThreads.add(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if(message != null) {
                    message.setSavedToFirebase(true);
                    updateMainAccountModel(message);
                    DataManager.getInstance().updateRealmMessage(message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if(message != null) {
                    message.setSavedToFirebase(true);
                    updateMainAccountModel(message);
                    DataManager.getInstance().updateRealmMessage(message);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatMessagesReference.child(newChat.getChatId()).child("messages").addChildEventListener(childEventListenersMessageThreads.get(childEventListenersMessageThreads.size()-1));

        valueEventListeners.add(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessageThread thread = dataSnapshot.getValue(MessageThread.class);
                if(thread!= null){
                    DataManager.getInstance().insertOrUpdateMessageThread(thread);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatMessagesReference.child(newChat.getChatId()).child("message_thread").child(newChat.getMessageThreadId()).addValueEventListener(valueEventListeners.get(valueEventListeners.size()-1));
    }

    private void addGroupChatMessageListener(GroupChat newChat){

        childEventListenersMessageThreads.add(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if(message != null) {
                    message.setSavedToFirebase(true);
                    updateMainAccountModel(message);
                    DataManager.getInstance().updateRealmMessage(message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if(message != null) {
                    message.setSavedToFirebase(true);
                    updateMainAccountModel(message);
                    DataManager.getInstance().updateRealmMessage(message);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatMessagesReference.child(newChat.getChatId()).child("messages").addChildEventListener(childEventListenersMessageThreads.get(childEventListenersMessageThreads.size()-1));

        valueEventListeners.add(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessageThread thread = dataSnapshot.getValue(MessageThread.class);
                if(thread!= null){
                    DataManager.getInstance().insertOrUpdateMessageThread(thread);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatMessagesReference.child(newChat.getChatId()).child("message_thread").child(newChat.getMessageThreadId()).addValueEventListener(valueEventListeners.get(valueEventListeners.size()-1));
    }

    private void initChatMessagesListener(List<GroupChat> groupChats, List<DirectChat> directChats) {
        final List<String> messageThreadIds = new ArrayList<>();
        final List<String> chatIds = new ArrayList<>();
        for(GroupChat chat : groupChats){
            if(chat.getMessageThreadId() != null) {
                messageThreadIds.add(chat.getMessageThreadId());
                chatIds.add(chat.getChatId());
            }
        }
        for(DirectChat chat : directChats){
            if(chat.getMessageThreadId() != null) {
                messageThreadIds.add(chat.getMessageThreadId());
                chatIds.add(chat.getChatId());
            }
        }

        for(int i = 0; i < messageThreadIds.size(); i++){
            childEventListenersMessageThreads.add(new ChildEventListener() {
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if(message != null) {
                        MessageRealm localMessage = RealmUISingleton.getInstance().getRealmInstance().
                                where(MessageRealm.class).equalTo("messageId", message.getMessageId()).findFirst();
                        if(localMessage != null && localMessage.getReadByUids().size() == message.getReadByUids().size()) {
                            //Do nothing
                        }else{
                            message.setSavedToFirebase(true);
                            updateMainAccountModel(message);
                            DataManager.getInstance().updateRealmMessage(message);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if(message != null) {
                        message.setSavedToFirebase(true);
                        updateMainAccountModel(message);
                        DataManager.getInstance().updateRealmMessage(message);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        for(int i = 0; i < messageThreadIds.size(); i++){
            chatMessagesReference.child(chatIds.get(i)).child("messages").addChildEventListener(childEventListenersMessageThreads.get(i));
        }

        for(int i = 0; i < messageThreadIds.size(); i++){
            valueEventListeners.add(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MessageThread thread = dataSnapshot.getValue(MessageThread.class);
                    if(thread!= null){
                        DataManager.getInstance().insertOrUpdateMessageThread(thread);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        for(int i = 0; i < messageThreadIds.size(); i++){
            chatMessagesReference.child(chatIds.get(i)).child("message_thread").child(messageThreadIds.get(i)).addValueEventListener(valueEventListeners.get(i));
        }
    }

    private GroupChat getGroupChat(String groupChatId) {
        for(GroupChat chat : groupChats){
            if(chat.getChatId().equalsIgnoreCase(groupChatId)){
                return chat;
            }
        }
        return null;
    }

    private Company getAccountCompany(Account account) {
        Company company = null;
        if(UserPreferences.getInstance().getUserType().equalsIgnoreCase(UserRealm.TYPE_SALES)){
            for(Company companyLoop : companies){
                if(companyLoop.getCompanyId().equalsIgnoreCase(account.getCompanyCustomerId())){
                    company = companyLoop;
                    break;
                }
            }
        }else{
            for(Company companyLoop : companies){
                if(companyLoop.getCompanyId().equalsIgnoreCase(account.getCompanySalesId())){
                    company = companyLoop;
                    break;
                }
            }
        }
        return company;
    }

    public void writeNewUser(User user) {
        usersReference.child(user.getUid()).setValue(user);
    }

    public void updateMessages(List<Message> messageList) {
        for(Message message : messageList) {
            chatMessagesReference.child(message.getChatId()).child("messages").child(message.getMessageId()).setValue(message);
        }
    }

    private void updateMainAccountModel(Message message) {
        MainAccountsModel model = RealmUISingleton.getInstance().getRealmInstance().where(MainAccountsModel.class).equalTo("permanentId", MainAccountsModel.PERM_ID).findFirst();
        if(model != null) {
            MainAccountsModel copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(model);
            RealmList<RowItem> sortedByMostRecent = new RealmList<>();
            for (int i = 0; i < copy.getRowItems().size(); i++) {
                AccountRealm account = RealmUISingleton.getInstance().getRealmInstance().where(AccountRealm.class).equalTo("accountIdFire", copy.getRowItems().get(i).getAccountId()).findFirst();
                if (account.getGroupChatSalesId().equals(message.getChatId()) &&
                        message.getCreatedDate() >= copy.getRowItems().get(i).getMessageCreatedAtTime()) {
                    copy.getRowItems().get(i).setMessageContent(message.getMessageContent());
                    copy.getRowItems().get(i).setMessageCreatedAtTime(message.getCreatedDate());
                    copy.getRowItems().get(i).setMessageOwnerName(message.getMessageOwnerName());
                    if (!message.getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                        copy.getRowItems().get(i).setNewMessage(true);
                    } else {
                        copy.getRowItems().get(i).setNewMessage(false);
                    }
                    sortedByMostRecent.add(copy.getRowItems().get(i));
                } else {
                    sortedByMostRecent.add(copy.getRowItems().get(i));
                }
            }

            RealmList<RowItem> requestRowItems = new RealmList<>();
            for(int i = 0; i < customerRequests.size(); i++){
                requestRowItems.add(new RowItem());
            }

            for(int i = 0; i < customerRequests.size(); i++){
                CustomerRequest request = customerRequests.get(i);
                if(request != null && request.isOpen()){
                    requestRowItems.get(i).setMessageContent(request.getRequestMessage());
                    requestRowItems.get(i).setMessageCreatedAtTime(request.getMostRecentMessageTime());
                    requestRowItems.get(i).setMessageOwnerName(request.getCustomerName());
                    if(request.getMostRecentGroupMessage() != null && !request.getMostRecentGroupMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())){
                        requestRowItems.get(i).setNewMessage(true);
                    }
                }
            }

            for(RowItem item : requestRowItems){
                sortedByMostRecent.add(item);
            }

            DirectChatRealm newDChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", message.getChatId()).findFirst();
            DirectItem newItem = null;
            if(newDChat != null){
                newItem = new DirectItem();
                newItem.setDirectChatId(message.getChatId());
                newItem.setNewMessage(true);
                newItem.setMessageOwnerName(message.getMessageOwnerName());
                newItem.setMessageOwnerUid(message.getCreatedByUid());
                newItem.setMessageContent(message.getMessageContent());
                newItem.setMessageCreatedAtTime(message.getCreatedDate());
            }

            MainDirectMessagesModel previousModel = RealmUISingleton.getInstance().getRealmInstance().where(MainDirectMessagesModel.class).equalTo("permanentId", MainDirectMessagesModel.PERM_ID).findFirst();
            MainDirectMessagesModel newDirectModel = new MainDirectMessagesModel();
            RealmList<DirectItem> directItems = new RealmList<>();
            boolean alreadyExists = false;
            for(DirectItem directItem : previousModel.getDirectItems()){
                DirectItem copyItem = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(directItem);
                if((message.getCreatedDate() > directItem.getMessageCreatedAtTime()) && (message.getChatId().equals(directItem.getDirectChatId()))){
                    copyItem.setMessageCreatedAtTime(message.getCreatedDate());
                    copyItem.setMessageContent(message.getMessageContent());
                    copyItem.setMessageOwnerUid(message.getCreatedByUid());
                    copyItem.setMessageOwnerName(message.getMessageOwnerName());
                    if (!message.getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                        copyItem.setNewMessage(true);
                    } else {
                        copyItem.setNewMessage(false);
                    }
                    directItems.add(copyItem);
                }else{
                    directItems.add(copyItem);
                }
                if(newItem != null && newItem.getDirectChatId().equals(directItem.getDirectChatId())){
                    alreadyExists = true;
                }
            }

            if(newItem != null && !alreadyExists) directItems.add(newItem);
            Collections.sort(directItems);
            newDirectModel.setDirectItems(directItems);

            for(DirectItem item : directItems){
                RowItem newRowItem = new RowItem();
                newRowItem.setItemType(RowItem.TYPE_DIRECT);
                newRowItem.setAccountId(item.getDirectChatId());
                newRowItem.setNewMessage(item.isNewMessage());
                newRowItem.setMessageContent(item.getMessageContent());
                newRowItem.setMessageCreatedAtTime(item.getMessageCreatedAtTime());
                sortedByMostRecent.add(newRowItem);
            }


            Collections.sort(sortedByMostRecent, RowItem.createdAtComparator);
            Collections.reverse(sortedByMostRecent);
            MainRecentModel recentModel = new MainRecentModel();
            RealmList<RowItem> recentRowItems = new RealmList<>();
            if (sortedByMostRecent.size() > 3) {
                recentRowItems.addAll(sortedByMostRecent.subList(0, 4));
            } else {
                recentRowItems.addAll(sortedByMostRecent.subList(0, sortedByMostRecent.size()));
            }
            recentModel.setRowItems(recentRowItems);

            DataManager.getInstance().updateRealmMainModels(copy, filterModel(recentModel), newDirectModel);
        }
    }

    public void createNewMessage(Message newMessage) {
        DatabaseReference messageRef = chatMessagesReference.child(newMessage.getChatId()).child("messages").push();
        newMessage.setMessageId(messageRef.getKey());
        chatMessagesReference.child(newMessage.getChatId()).child("messages").child(newMessage.getMessageId()).setValue(newMessage);
        chatMessagesReference.child(newMessage.getChatId()).child("message_thread").child(newMessage.getMessageThreadId()).setValue(newMessage);
    }

    public void createNewDirectMessage(Message newMessage) {
        DatabaseReference messageRef = chatMessagesReference.child(newMessage.getChatId()).child("messages").push();
        newMessage.setMessageId(messageRef.getKey());
        chatMessagesReference.child(newMessage.getChatId()).child("messages").child(newMessage.getMessageId()).setValue(newMessage);
        DirectChatRealm dChatRealm = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", newMessage.getChatId()).findFirst();
        DirectChatRealm copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(dChatRealm);
        copy.setMessageCreatedTime(newMessage.getCreatedDate());
        copy.setMostRecentMessage(new MessageRealm(newMessage));
        DataManager.getInstance().insertOrUpdateDirectChat(copy);
        directChatsReference.child(copy.getChatId()).setValue(new DirectChat(copy));
    }

    public void createNewRequestMessage(Message newMessage) {
        DatabaseReference messageRef = chatMessagesReference.child(newMessage.getChatId()).child("messages").push();
        newMessage.setMessageId(messageRef.getKey());
        chatMessagesReference.child(newMessage.getChatId()).child("messages").child(newMessage.getMessageId()).setValue(newMessage);
        CustomerRequestRealm request = RealmUISingleton.getInstance().getRealmInstance().where(CustomerRequestRealm.class).equalTo("requestId", newMessage.getChatId()).findFirst();
        CustomerRequestRealm copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(request);
        copy.setMostRecentGroupMessage(new MessageRealm(newMessage));
        copy.setMostRecentMessageTime(newMessage.getCreatedDate());
        DataManager.getInstance().insertOrUpdateCustomerRequest(copy);
        customerRequestsReference.child(copy.getRequestId()).setValue(new CustomerRequest(copy));
    }

    private void updateRelatedGroupChatWithUserId(Message newMessage) {
        GroupChatRealm gChat = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).equalTo("chatId", newMessage.getChatId()).findFirst();
        if(gChat != null) {
            if (gChat.getUserIds() != null) {
                if (!gChat.getUserIds().contains(newMessage.getCreatedByUid())) {
                    groupChatsReference.child(newMessage.getChatId()).child("userIds").push().setValue(newMessage.getCreatedByUid());
                }
            }
        }
    }

    public void writeNewCompany(Company company){
        DatabaseReference newCompanyRef = companiesReference.push();
        company.setCompanyId(newCompanyRef.getKey());
        companiesReference.child(company.getCompanyId()).setValue(company);
    }

    public void writeNewAccount(Account account){
        DatabaseReference newAccountRef = accountsReference.push();
        account.setAccountIdFire(newAccountRef.getKey());
        accountsReference.child(account.getAccountIdFire()).setValue(account);
    }

    public void writeNewDirectChat(DirectChat chat) {
        DatabaseReference newChatRef = directChatsReference.push();
        chat.setChatId(newChatRef.getKey());
        directChatsReference.child(chat.getChatId()).setValue(chat);
    }


    public void writeNewGroupChat(GroupChat chat){
        DatabaseReference newChatRef = groupChatsReference.push();
        chat.setChatId(newChatRef.getKey());
        groupChatsReference.child(chat.getChatId()).setValue(chat);
    }

    public void addNewIndustry(String industry){
        industriesReference.push().setValue(industry);
    }

    public void updateOrCreateUserColor(int colorId, String uid){
        userColorsReference.child(uid).setValue(colorId);
    }

    public void updateUserInfo(String firstName, String lastName, String phoneNumber, String businessUnit, String role) {

    }

    public void getMatchingCompanyBoolean(final String emailDomain, final BaseCallback<Boolean> companyNameCallback) {
        companiesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Company> companyList = new ArrayList<>();
                Company matchingCompany = null;
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Company company = data.getValue(Company.class);
                    companyList.add(company);
                }
                for(Company company : companyList){
                    if(company.getCompanyEmailDomain() != null) {
                        if (company.getCompanyEmailDomain().equalsIgnoreCase(emailDomain)) {
                            matchingCompany = company;
                            break;
                        }
                    }
                }
                if(matchingCompany != null){
                    companyNameCallback.onResponse(true);
                }else{
                    companyNameCallback.onResponse(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getMatchingCompany(final String emailDomain, final BaseCallback<Company> companyNameCallback) {
        companiesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Company> companyList = new ArrayList<>();
                Company matchingCompany = null;
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Company company = data.getValue(Company.class);
                    companyList.add(company);
                }
                for(Company company : companyList){
                    if(company.getCompanyEmailDomain() != null) {
                        if (company.getCompanyEmailDomain().equalsIgnoreCase(emailDomain)) {
                            matchingCompany = company;
                            break;
                        }
                    }
                }
                if(matchingCompany != null){
                    companyNameCallback.onResponse(matchingCompany);
                }else{
                    companyNameCallback.onResponse(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateAccountSalesAgentUids(String accountId, String uid) {
        accountsReference.child(accountId).child("accountUsers").child(uid).setValue(uid);
    }

    public void onBoardNewSalesCompany(){
        DatabaseReference newCompRef = companiesReference.push();
        Company salesCompany = new Company();
        salesCompany.setName("Thermo Fisher");
        salesCompany.setAddress("39329 Via Zaragoza, Murrieta California 92563");
        salesCompany.setCompanyEmailDomain("@gmail.com");
        salesCompany.setCompanyId(newCompRef.getKey());
        salesCompany.addIndustry("Academia, Sciences and Education");
        salesCompany.addIndustry("Testing Labs and Health Institutes");
        salesCompany.addIndustry("Pharma and Biotechnology");
        salesCompany.addIndustry("Chemical");
        salesCompany.setSalesCompany(true);
        salesCompany.setBuyerCompany(false);

        DatabaseReference newCompARef = companiesReference.push();
        Company customerCompanyA = new Company();
        customerCompanyA.setName("Loma Linda University");
        customerCompanyA.setAddress("5005 Raspberry Rd, Anchorage Alaska 99502");
        customerCompanyA.setCompanyEmailDomain("@gmail.com");
        customerCompanyA.setCompanyId(newCompARef.getKey());
        customerCompanyA.setSalesCompany(false);
        customerCompanyA.setBuyerCompany(true);
        customerCompanyA.addIndustry("Academia, Sciences and Education");
        industriesReference.push().setValue("Academia, Sciences and Education");
        Account accountA = createNewAccount(salesCompany, customerCompanyA, "700023611");
        addAllCustomer1Employees(customerCompanyA, accountA.getAccountIdFire());

        DatabaseReference newCompBRef = companiesReference.push();
        Company customerCompanyB = new Company();
        customerCompanyB.setName("Abbott Chemicals In");
        customerCompanyB.setAddress("1360 S Loop Rd, Alameda California 94502");
        customerCompanyB.setCompanyEmailDomain("@gmail.com");
        customerCompanyB.setCompanyId(newCompBRef.getKey());
        customerCompanyB.setSalesCompany(false);
        customerCompanyB.setBuyerCompany(true);
        customerCompanyB.addIndustry("Testing Labs and Health Institutes");
        industriesReference.push().setValue("Testing Labs and Health Institutes");
        Account accountB = createNewAccount(salesCompany, customerCompanyB, "700031434");
        addAllCustomer2Employees(customerCompanyB, accountB.getAccountIdFire());

        DatabaseReference newCompCRef = companiesReference.push();
        Company customerCompanyC = new Company();
        customerCompanyC.setName("Abcam Inc");
        customerCompanyC.setAddress("1 Kendall Sq, Cambridge Massachusetts 2139");
        customerCompanyC.setCompanyEmailDomain("@gmail.com");
        customerCompanyC.setCompanyId(newCompCRef.getKey());
        customerCompanyC.setSalesCompany(false);
        customerCompanyC.setBuyerCompany(true);
        customerCompanyC.addIndustry("Pharma and Biotechnology");
        industriesReference.push().setValue("Pharma and Biotechnology");
        Account accountC = createNewAccount(salesCompany, customerCompanyC, "700019967");
        addAllCustomer3Employees(customerCompanyC, accountC.getAccountIdFire());

        DatabaseReference newCompDRef = companiesReference.push();
        Company customerCompanyD = new Company();
        customerCompanyD.setName("NewGenn Inc");
        customerCompanyD.setAddress("1827 S Industrial Dr, Portales New Mexico 88130");
        customerCompanyD.setCompanyEmailDomain("@gmail.com");
        customerCompanyD.setCompanyId(newCompDRef.getKey());
        customerCompanyD.setSalesCompany(false);
        customerCompanyD.setBuyerCompany(true);
        customerCompanyD.addIndustry("Chemical");
        industriesReference.push().setValue("Chemical");
        Account accountD = createNewAccount(salesCompany, customerCompanyD, "700010532");
        addAllCustomer4Employees(customerCompanyD, accountD.getAccountIdFire());

        salesCompany.addAccount(accountA.getAccountIdFire());
        salesCompany.addAccount(accountB.getAccountIdFire());
        salesCompany.addAccount(accountC.getAccountIdFire());
        salesCompany.addAccount(accountD.getAccountIdFire());

        addAllSalesEmployees(salesCompany, accountA, accountB, accountC, accountD);

        companiesReference.child(salesCompany.getCompanyId()).setValue(salesCompany);
        companiesReference.child(customerCompanyA.getCompanyId()).setValue(customerCompanyA);
        companiesReference.child(customerCompanyB.getCompanyId()).setValue(customerCompanyB);
        companiesReference.child(customerCompanyC.getCompanyId()).setValue(customerCompanyC);
        companiesReference.child(customerCompanyD.getCompanyId()).setValue(customerCompanyD);
    }

    private void addAllCustomer4Employees(Company customerCompanyD, String accountId) {
        DatabaseReference newUser1Ref = usersReference.push();
        User salesUser1 = new User();
        salesUser1.setUid(newUser1Ref.getKey());
        salesUser1.setCompanyId(customerCompanyD.getCompanyId());
        salesUser1.setEmail("HTrent@gmail.com");
        salesUser1.setFirstName("Hent");
        salesUser1.setLastName("Trent");
        salesUser1.setPhoneNumber("3467950355");
        salesUser1.setUserType("account_rep");
        salesUser1.setRole("replvl1");
        salesUser1.addAccount(accountId);
        salesUser1.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser1.getUserColor(), salesUser1.getUid());
        usersReference.child(salesUser1.getUid()).setValue(salesUser1);
        customerCompanyD.addNewEmployee(salesUser1.getUid());
    }

    private void addAllCustomer3Employees(Company customerCompanyC, String accountId) {
        DatabaseReference newUser1Ref = usersReference.push();
        User salesUser1 = new User();
        salesUser1.setUid(newUser1Ref.getKey());
        salesUser1.setCompanyId(customerCompanyC.getCompanyId());
        salesUser1.setEmail("jTout@gmail.com");
        salesUser1.setFirstName("Jimmy");
        salesUser1.setLastName("Trout");
        salesUser1.setPhoneNumber("3345950355");
        salesUser1.setUserType("account_rep");
        salesUser1.setRole("replvl1");
        salesUser1.addAccount(accountId);
        salesUser1.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser1.getUserColor(), salesUser1.getUid());
        usersReference.child(salesUser1.getUid()).setValue(salesUser1);
        customerCompanyC.addNewEmployee(salesUser1.getUid());
    }

    private void addAllCustomer2Employees(Company customerCompanyB, String accountId) {
        DatabaseReference newUser1Ref = usersReference.push();
        User salesUser1 = new User();
        salesUser1.setUid(newUser1Ref.getKey());
        salesUser1.setCompanyId(customerCompanyB.getCompanyId());
        salesUser1.setEmail("kTrent@gmail.com");
        salesUser1.setFirstName("Kay");
        salesUser1.setLastName("Trent");
        salesUser1.setPhoneNumber("3432950355");
        salesUser1.setUserType("account_rep");
        salesUser1.setRole("replvl1");
        salesUser1.addAccount(accountId);
        salesUser1.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser1.getUserColor(), salesUser1.getUid());
        usersReference.child(salesUser1.getUid()).setValue(salesUser1);
        customerCompanyB.addNewEmployee(salesUser1.getUid());
    }

    private void addAllCustomer1Employees(Company customerCompany, String accountId) {
        DatabaseReference newUser1Ref = usersReference.push();
        User salesUser1 = new User();
        salesUser1.setUid(newUser1Ref.getKey());
        salesUser1.setCompanyId(customerCompany.getCompanyId());
        salesUser1.setEmail("jonathan@clickfield.com");
        salesUser1.setFirstName("Jay");
        salesUser1.setLastName("Brannen");
        salesUser1.setPhoneNumber("9512950348");
        salesUser1.setUserType("account_rep");
        salesUser1.setRole("rep1");
        salesUser1.addAccount(accountId);
        salesUser1.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser1.getUserColor(), salesUser1.getUid());
        usersReference.child(salesUser1.getUid()).setValue(salesUser1);
        customerCompany.addNewEmployee(salesUser1.getUid());
    }

    private void addAllSalesEmployees(Company salesCompany, Account accountA, Account accountB, Account accountC, Account accountD) {
        DatabaseReference newUser1Ref = usersReference.push();
        User salesUser1 = new User();
        salesUser1.setUid(newUser1Ref.getKey());
        salesUser1.setCompanyId(salesCompany.getCompanyId());
        salesUser1.setEmail("jbinvestments15@gmail.com");
        salesUser1.setFirstName("Jonathan");
        salesUser1.setLastName("Brannen");
        salesUser1.setPhoneNumber("9512950348");
        salesUser1.setUserType("sales_agent");
        salesUser1.setRole("CTO");
        salesUser1.addAccount(accountA.getAccountIdFire());
        salesUser1.addAccount(accountB.getAccountIdFire());
        salesUser1.addAccount(accountC.getAccountIdFire());
        salesUser1.addAccount(accountD.getAccountIdFire());
        updateAccountSalesAgentUids(accountA.getAccountIdFire(), newUser1Ref.getKey());
        updateAccountSalesAgentUids(accountB.getAccountIdFire(), newUser1Ref.getKey());
        updateAccountSalesAgentUids(accountC.getAccountIdFire(), newUser1Ref.getKey());
        updateAccountSalesAgentUids(accountD.getAccountIdFire(), newUser1Ref.getKey());
        salesUser1.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser1.getUserColor(), salesUser1.getUid());
        usersReference.child(salesUser1.getUid()).setValue(salesUser1);
        salesCompany.addNewEmployee(salesUser1.getUid());

        DatabaseReference newUser2Ref = usersReference.push();
        User salesUser2 = new User();
        salesUser2.setUid(newUser2Ref.getKey());
        salesUser2.setCompanyId(salesCompany.getCompanyId());
        salesUser2.setEmail("johnraychilders@gmail.com");
        salesUser2.setFirstName("John");
        salesUser2.setLastName("Childers");
        salesUser2.setPhoneNumber("7072808918");
        salesUser2.setUserType("sales_agent");
        salesUser2.setRole("CEO");
        salesUser2.addAccount(accountA.getAccountIdFire());
        salesUser2.addAccount(accountB.getAccountIdFire());
        updateAccountSalesAgentUids(accountA.getAccountIdFire(), newUser2Ref.getKey());
        updateAccountSalesAgentUids(accountB.getAccountIdFire(), newUser2Ref.getKey());
        salesUser2.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser2.getUserColor(), salesUser2.getUid());
        usersReference.child(salesUser2.getUid()).setValue(salesUser2);
        salesCompany.addNewEmployee(salesUser2.getUid());

        DatabaseReference newUser3Ref = usersReference.push();
        User salesUser3 = new User();
        salesUser3.setUid(newUser3Ref.getKey());
        salesUser3.setCompanyId(salesCompany.getCompanyId());
        salesUser3.setEmail("adamSmith@gmail.com");
        salesUser3.setFirstName("Adam");
        salesUser3.setLastName("Smith");
        salesUser3.setPhoneNumber("9566950348");
        salesUser3.setUserType("sales_agent");
        salesUser3.setRole("sales_lvl_1");
        salesUser3.addAccount(accountC.getAccountIdFire());
        salesUser3.addAccount(accountD.getAccountIdFire());
        updateAccountSalesAgentUids(accountC.getAccountIdFire(), newUser3Ref.getKey());
        updateAccountSalesAgentUids(accountD.getAccountIdFire(), newUser3Ref.getKey());
        salesUser3.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser3.getUserColor(), salesUser3.getUid());
        usersReference.child(salesUser3.getUid()).setValue(salesUser3);
        salesCompany.addNewEmployee(salesUser3.getUid());

        DatabaseReference newUser4Ref = usersReference.push();
        User salesUser4 = new User();
        salesUser4.setUid(newUser4Ref.getKey());
        salesUser4.setCompanyId(salesCompany.getCompanyId());
        salesUser4.setEmail("hPeters@gmail.com");
        salesUser4.setFirstName("Harrald");
        salesUser4.setLastName("Peters");
        salesUser4.setPhoneNumber("8879897765");
        salesUser4.setUserType("sales_agent");
        salesUser4.setRole("sales_lvl_2");
        salesUser4.addAccount(accountA.getAccountIdFire());
        salesUser4.addAccount(accountD.getAccountIdFire());
        salesUser4.addAccount(accountB.getAccountIdFire());
        updateAccountSalesAgentUids(accountA.getAccountIdFire(), newUser4Ref.getKey());
        updateAccountSalesAgentUids(accountB.getAccountIdFire(), newUser4Ref.getKey());
        updateAccountSalesAgentUids(accountD.getAccountIdFire(), newUser4Ref.getKey());
        salesUser4.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser4.getUserColor(), salesUser4.getUid());
        usersReference.child(salesUser4.getUid()).setValue(salesUser4);
        salesCompany.addNewEmployee(salesUser4.getUid());

        DatabaseReference newUser5Ref = usersReference.push();
        User salesUser5 = new User();
        salesUser5.setUid(newUser5Ref.getKey());
        salesUser5.setCompanyId(salesCompany.getCompanyId());
        salesUser5.setEmail("shawnamccollom@yahoo.com");
        salesUser5.setFirstName("Shawna");
        salesUser5.setLastName("Brannen");
        salesUser5.setPhoneNumber("4543342409");
        salesUser5.setUserType("sales_agent");
        salesUser5.setRole("sales_lvl_1");
        salesUser5.addAccount(accountB.getAccountIdFire());
        salesUser5.addAccount(accountC.getAccountIdFire());
        salesUser5.addAccount(accountA.getAccountIdFire());
        updateAccountSalesAgentUids(accountA.getAccountIdFire(), newUser5Ref.getKey());
        updateAccountSalesAgentUids(accountB.getAccountIdFire(), newUser5Ref.getKey());
        updateAccountSalesAgentUids(accountC.getAccountIdFire(), newUser5Ref.getKey());
        salesUser5.setUserColor(UserColorUtil.getRandomUserColorId());
        updateOrCreateUserColor(salesUser5.getUserColor(), salesUser5.getUid());
        usersReference.child(salesUser5.getUid()).setValue(salesUser5);
        salesCompany.addNewEmployee(salesUser5.getUid());
    }

    private Account createNewAccount(Company salesCompany, Company customerCompany, String accountId){
        DatabaseReference newAccountRef = accountsReference.push();
        Account account = new Account();
        account.setAccountIdFire(newAccountRef.getKey());
        account.setAccountId(accountId);
        account.setCompanySalesId(salesCompany.getCompanyId());
        account.setCompanyCustomerId(customerCompany.getCompanyId());

        DatabaseReference newGroupChatRef = groupChatsReference.push();
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(newGroupChatRef.getKey());
        DatabaseReference newMessageThreadRef = database.getReference().child("messages").push();
        MessageThread thread = new MessageThread();
        thread.setMessageThreadId(newMessageThreadRef.getKey());
        thread.setChatId(groupChat.getChatId());
        database.getReference().child("messages").child(thread.getMessageThreadId()).setValue(thread);
        groupChat.setMessageThreadId(thread.getMessageThreadId());
        groupChatsReference.child(groupChat.getChatId()).setValue(groupChat);

        account.setGroupChatSalesId(groupChat.getChatId());
        accountsReference.child(account.getAccountIdFire()).setValue(account);

        List<String> accountIdList = new ArrayList<>();
        accountIdList.add(account.getAccountIdFire());

        salesCompany.setAccountList(accountIdList);
        customerCompany.setAccountList(accountIdList);

        return account;
    }

    //For sending emails to agent companies when new customer users sign up.
    public void writeEmailPassword(String password){
        database.getReference("refer_application").child("email_password").setValue(password);
    }

    public void loadCompaniesToFirebase(){

    }

    public GroupChat createNewGroupChat(List<String> memberIds, String accountId, String creatorUid, String groupName, String messageText) {
        DatabaseReference newGroupChatRef = groupChatsReference.push();
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(newGroupChatRef.getKey());
        groupChat.setUserIds(createMap(memberIds));
        groupChat.setGroupCreatorUid(creatorUid);
        groupChat.setGroupName(groupName);
        groupChat.setAccountId(accountId);

        DatabaseReference newMessageThreadRef = database.getReference().child("messages").push();
        MessageThread thread = new MessageThread();
        thread.setMessageThreadId(newMessageThreadRef.getKey());
        thread.setChatId(groupChat.getChatId());
        database.getReference().child("messages").child(thread.getMessageThreadId()).setValue(thread);
        groupChat.setMessageThreadId(thread.getMessageThreadId());
        groupChat.setNewChat(true);

        Message message = new Message();
        message.setChatId(groupChat.getChatId());
        message.setCreatedByUid(creatorUid);
        message.setCreatedDate(new Date().getTime());
        message.setMessageContent(messageText);
        List<String> readByUids = new ArrayList<>();
        readByUids.add(creatorUid);
        message.setReadByUids(readByUids);
        message.setMessageThreadId(thread.getMessageThreadId());
        UserRealm user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", creatorUid).findFirst();
        message.setMessageOwnerName(user.getFirstName() + " " + user.getLastName());
        groupChat.setMostRecentMessage(message);

        createNewMessage(message);
        groupChatsReference.child(groupChat.getChatId()).setValue(groupChat);


        for(String id : memberIds){
            usersReference.child(id).child("groupChats").child(groupChat.getChatId()).setValue(groupChat.getChatId());
        }
        usersReference.child(creatorUid).child("groupChats").child(groupChat.getChatId()).setValue(groupChat.getChatId());

        accountsReference.child(accountId).child("groupChats").child(groupChat.getChatId()).setValue(groupChat.getChatId());

        addGroupChatMessageListener(groupChat);
        return groupChat;
    }

    private Map<String, String> createMap(List<String> memberIds) {
        Map<String, String> userIdsMap = new HashMap<>();
        for(String id : memberIds){
            userIdsMap.put(id, id);
        }
        return userIdsMap;
    }

    public DirectChat createNewDirectChat(String fromUid, String toUid){
        DatabaseReference newDirectChatRef = directChatsReference.push();
        DirectChat directChat = new DirectChat();
        directChat.setChatId(newDirectChatRef.getKey());
        directChat.setUserIdA(fromUid);
        directChat.setUserIdB(toUid);
        DatabaseReference newMessageThreadRef = database.getReference().child("messages").push();
        MessageThread thread = new MessageThread();
        thread.setMessageThreadId(newMessageThreadRef.getKey());
        thread.setChatId(directChat.getChatId());
        database.getReference().child("messages").child(thread.getMessageThreadId()).setValue(thread);
        directChat.setMessageThreadId(thread.getMessageThreadId());
        directChatsReference.child(directChat.getChatId()).setValue(directChat);

        usersReference.child(fromUid).child("directChats").child(directChat.getChatId()).setValue(directChat.getChatId());
        usersReference.child(toUid).child("directChats").child(directChat.getChatId()).setValue(directChat.getChatId());

        addDirectChatMessageListener(directChat);
        return directChat;
    }

    public void verifyUserAccountExists(final String email, final BaseCallback<String> userAccountExistsCallback) {
        Query query = usersReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.getChildren().iterator().hasNext()){
                        uid = dataSnapshot.getChildren().iterator().next().getValue(User.class).getUid();
                        UserPreferences.getInstance().setUid(uid);
                        userAccountExistsCallback.onResponse(uid);
                    }else{
                        userAccountExistsCallback.onFailure(new Exception("User account does not exist."));
                    }
                }else{
                    userAccountExistsCallback.onFailure(new Exception("User account does not exist."));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateFirebaseMessageThreadTyping(final String chatId, final String messageThreadId, final String userName, final boolean isTyping) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessageThread thread = dataSnapshot.getValue(MessageThread.class);
                if(thread != null){
                    if(isTyping) {
                        chatMessagesReference.child(chatId).child("message_thread").child(messageThreadId).child("currentlyTypingUserNames").child(userName).setValue(userName);
                    }else{
                        chatMessagesReference.child(chatId).child("message_thread").child(messageThreadId).child("currentlyTypingUserNames").child(userName).removeValue();
                    }
                }else{
                    List<String> currentlyTypingNames = new ArrayList<>();
                    currentlyTypingNames.add(userName);
                    MessageThread newThread = new MessageThread(messageThreadId, chatId, null );
                    chatMessagesReference.child(chatId).child("message_thread").child(messageThreadId).setValue(newThread);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if(chatId != null && messageThreadId != null){
            chatMessagesReference.child(chatId).child("message_thread").child(messageThreadId).addListenerForSingleValueEvent(listener);
        }
    }

    public void createNewCustomerRequest(AccountRealm account, CompanyRealm company, String requestMessage, String createdByUid) {
        DatabaseReference newGroupChatRef = groupChatsReference.push();
        UserRealm thisUser = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class)
                .equalTo("uid", createdByUid).findFirst();
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(newGroupChatRef.getKey());
        DatabaseReference newMessageThreadRef = database.getReference().child("messages").push();
        MessageThread thread = new MessageThread();
        thread.setMessageThreadId(newMessageThreadRef.getKey());
        thread.setChatId(groupChat.getChatId());
        database.getReference().child("messages").child(thread.getMessageThreadId()).setValue(thread);
        groupChat.setMessageThreadId(thread.getMessageThreadId());

        DatabaseReference newRequestRef = customerRequestsReference.push();
        CustomerRequest request = new CustomerRequest();
        request.setRequestId(newRequestRef.getKey());
        request.setAccountId(account.getAccountIdFire());
        request.setGroupChatId(groupChat.getChatId());
        request.setCustomerUid(company.getEmployeeList().get(0));
        request.setOpen(true);
        request.setOpenDate(new Date().getTime());
        request.setRequestMessage(requestMessage);
        request.setCustomerName(thisUser.getFirstName() + " " + thisUser.getLastName());

        Map<String, String> customerRequestIds = new HashMap<>();
        customerRequestIds.put(request.getRequestId(), request.getRequestId());
        groupChat.setCustomerRequestIds(customerRequestIds);
        Account accountFire = new Account(RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(account));
        accountFire.setCustomerRequestIds(customerRequestIds);
        updateAccount(accountFire);
        groupChatsReference.child(groupChat.getChatId()).setValue(groupChat);
        customerRequestsReference.child(request.getRequestId()).setValue(request);

        Message newMessage = new Message();
        List<String> readByUids = new ArrayList<>();
        readByUids.add(createdByUid);
        newMessage.setMessageContent(requestMessage);
        newMessage.setCreatedByUid(createdByUid);
        newMessage.setChatId(groupChat.getChatId());
        newMessage.setCreatedDate(new Date().getTime());
        newMessage.setReadByUids(readByUids);
        newMessage.setSavedToFirebase(false);
        newMessage.setMessageOwnerName(thisUser.getFirstName() + " " + thisUser.getLastName());
        newMessage.setMessageThreadId(groupChat.getMessageThreadId());
        createNewMessage(newMessage);
    }

    private void updateAccount(Account accountFire) {
        accountsReference.child(accountFire.getAccountIdFire()).setValue(accountFire);
    }
}
