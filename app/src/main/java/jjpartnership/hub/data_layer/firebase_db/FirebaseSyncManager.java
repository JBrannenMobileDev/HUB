package jjpartnership.hub.data_layer.firebase_db;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.RealmMessageBatchUtil;
import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CustomerRequest;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.DirectItem;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.MessageThread;
import jjpartnership.hub.data_layer.data_models.NewMessageNotification;
import jjpartnership.hub.data_layer.data_models.RowItem;
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
    private RealmMessageBatchUtil batchUtil;

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
    private List<Message> allMessages;
    private BaseCallback<Boolean> syncCompleteCallback;
    private MainRecentModel mainRecentModel;
    private MainAccountsModel mainAccountsModel;
    private MainDirectMessagesModel mainDirectMessagesModel;

    private List<ChildEventListener> childEventListenersChatMessages;
    private List<ValueEventListener> valueEventListeners;

    public FirebaseSyncManager() {

    }

    private void initData(){
        users = new ArrayList<>();
        accountIds = new ArrayList<>();
        userAccounts = new ArrayList<>();
        customerRequests = new ArrayList<>();
        companies = new ArrayList<>();
        allGroupChats = new ArrayList<>();
        directChats = new ArrayList<>();
        userColors = new ArrayList<>();
        allMessages = new ArrayList<>();
        childEventListenersChatMessages = new ArrayList<>();
        valueEventListeners = new ArrayList<>();
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
        currentUid = UserPreferences.getInstance().getUid();
        batchUtil = new RealmMessageBatchUtil();
    }

    public void syncFirebaseToLocalDb(BaseCallback<Boolean> syncCompleteCallback){
        initData();
        this.syncCompleteCallback = syncCompleteCallback;
        getThisUser();
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
                    if(companies.size() == userAccounts.size()){
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
        final Set<String> uids = new HashSet<>();
        for(GroupChat chat : allGroupChats){
            if(chat.getUserIdsList() != null) uids.addAll(chat.getUserIdsList());
        }

        if(companies != null && companies.size() > 0){
            for(Company company : companies) {
                uids.addAll(company.getEmployeeList());
            }
        }

        if(directChats != null && directChats.size() > 0){
            for(DirectChat chat : directChats){
                if(chat.getUserIdA().equals(UserPreferences.getInstance().getUid())){
                    uids.add(chat.getUserIdB());
                }else{
                    uids.add(chat.getUserIdA());
                }
            }
        }

        if(uids.size() > 0) {
            for (String uid : uids) {
                ValueEventListener usersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user != null){
                            users.add(user);
                        }
                        if(users.size() == uids.size()){
                            getUserColors(new ArrayList<>(uids));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                usersReference.child(uid).addListenerForSingleValueEvent(usersListener);
            }
        }else{
            getUserColors(new ArrayList<>(uids));
        }
    }

    private void getUserColors(final List<String> uids) {
        if(uids != null && uids.size() > 0) {
            for (final String uid : uids) {
                ValueEventListener userColorsListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long color = (long) dataSnapshot.getValue();
                        userColors.add(new UserColor(uid, color));
                        if (userColors.size() == uids.size()) {
                            getAllUserMessages();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                database.getReference("user_colors").child(uid).addListenerForSingleValueEvent(userColorsListener);
            }
        }else{
            getAllUserMessages();
        }
    }

    private int count;
    private void getAllUserMessages() {
        final List<String> chatIds = new ArrayList<>();
        for(GroupChat chat : allGroupChats){
            chatIds.add(chat.getChatId());
        }
        for(DirectChat chat : directChats){
            chatIds.add(chat.getChatId());
        }
        count = 0;
        if(chatIds != null && chatIds.size() > 0) {
            for (String chatId : chatIds) {
                ValueEventListener chatMessagesListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Message message = data.getValue(Message.class);
                            message.setSavedToFirebase(true);
                            allMessages.add(message);
                        }
                        count++;
                        if (chatIds.size() == count) {
                            saveAllDataToLocalDb();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                if (chatId != null) {
                    chatMessagesReference.child(chatId).child("messages").addListenerForSingleValueEvent(chatMessagesListener);
                }
            }
        }else{
            saveAllDataToLocalDb();
        }
    }

    private void saveAllDataToLocalDb() {
        users.add(currentUser);
        final BaseCallback<Boolean> syncCompleteListener = new BaseCallback<Boolean>() {
            @Override
            public void onResponse(Boolean object) {
                buildMainModels();
            }

            @Override
            public void onFailure(Exception e) {
                syncCompleteCallback.onFailure(e);
            }
        };
        DataManager.getInstance().syncBootDataToLocal(users, userAccounts, customerRequests, companies,
                allGroupChats, directChats, userColors, allMessages, syncCompleteListener);
    }

    private void buildMainModels() {
        Calendar twoWeeksAgo = Calendar.getInstance();
        twoWeeksAgo.add(Calendar.DAY_OF_YEAR, -14);
        mainRecentModel = new MainRecentModel();
        mainAccountsModel = new MainAccountsModel();
        mainDirectMessagesModel = new MainDirectMessagesModel();

        RealmList<RowItem> requestRowItems = new RealmList<>();
        RealmList<RowItem> recentRowItems = new RealmList<>();


        //Creates all the groupChatRowItems.
        for(GroupChat chat : allGroupChats){
            RowItem recentRow = new RowItem();
            if(chat.getCustomerRequestIds() == null || chat.getCustomerRequestIds().size() == 0){
                recentRow.setItemType(RowItem.TYPE_GROUP_CHAT);
            }
            recentRow.setMessageContent(chat.getMostRecentMessage().getMessageContent());
            recentRow.setChatId(chat.getChatId());
            recentRow.setAccountId(chat.getAccountId());
            recentRow.setAccountName(chat.getGroupName());
            recentRow.setMessageContent(chat.getMostRecentMessage().getMessageContent());
            recentRow.setMessageOwnerName(chat.getMostRecentMessage().getMessageOwnerName());
            recentRow.setMessageCreatedAtTime(chat.getMostRecentMessage().getCreatedDate());
            if(!chat.getMostRecentMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                recentRow.setNewMessage(true);
            }else{
                recentRow.setNewMessage(false);
            }

            if(recentRow.isNewMessage()) {
                recentRowItems.add(recentRow);
            }else if(recentRow.getMessageCreatedAtTime() > twoWeeksAgo.getTimeInMillis()){
                recentRowItems.add(recentRow);
            }
        }


        //Creates all request row items for the request tab on the account activity.
        for(CustomerRequest request : customerRequests){
            if(request != null && request.isOpen()){
                RowItem requestItem = new RowItem();
                requestItem.setMessageContent(request.getRequestMessage());
                requestItem.setMessageCreatedAtTime(request.getMostRecentMessageTime());
                requestItem.setMessageOwnerName(request.getCustomerName());
                requestItem.setChatId(request.getGroupChatId());
                if(request.getMostRecentGroupMessage() != null &&
                        !request.getMostRecentGroupMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())){
                    requestItem.setNewMessage(true);
                }
                requestRowItems.add(requestItem);
            }
        }

        //adds the recent request items to the recent model.
        for(RowItem item : requestRowItems){
            if(item.isNewMessage()) {
                recentRowItems.add(item);
            }else if(item.getMessageCreatedAtTime() > twoWeeksAgo.getTimeInMillis()){
                recentRowItems.add(item);
            }
        }

        RealmList<AccountRowItem> accountRowItems = new RealmList<>();
        for(int i = 0; i < userAccounts.size(); i++) {
            Company company = getAccountCompany(userAccounts.get(i));
            if (company != null) {
                AccountRowItem temp = new AccountRowItem();
                temp.setAccountName(company.getName());
                temp.setAccountIdFire(userAccounts.get(i).getAccountIdFire());
                accountRowItems.add(temp);
            }
        }
        Collections.sort(accountRowItems);
        mainAccountsModel.setRowItems(accountRowItems);


        //create all directChat items
        RealmList<DirectItem> directItems = new RealmList<>();
        for(DirectChat directChat : directChats){
            DirectItem newItem = new DirectItem();
            MessageRealm mostRecentMessage = RealmUISingleton.getInstance().getRealmInstance().where(MessageRealm.class).equalTo("createdDate", directChat.getMessageCreatedTime()).findFirst();
            newItem.setDirectChatId(directChat.getChatId());
            if(mostRecentMessage != null) {
                newItem.setMessageOwnerName(mostRecentMessage.getMessageOwnerName());
                newItem.setMessageOwnerUid(mostRecentMessage.getUid());
                newItem.setMessageContent(mostRecentMessage.getMessageContent());
                newItem.setMessageCreatedAtTime(mostRecentMessage.getCreatedDate());
                if (!mostRecentMessage.getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                    newItem.setNewMessage(true);
                } else {
                    newItem.setNewMessage(false);
                }
                directItems.add(newItem);
            }
        }
        Collections.sort(directItems);
        mainDirectMessagesModel.setDirectItems(directItems);


        //add direct chat items to recent items
        for(DirectItem item : directItems){
            RowItem newRowItem = new RowItem();
            newRowItem.setItemType(RowItem.TYPE_DIRECT);
            newRowItem.setAccountId(item.getDirectChatId());
            newRowItem.setNewMessage(item.isNewMessage());
            newRowItem.setChatId(item.getDirectChatId());
            newRowItem.setMessageContent(item.getMessageContent());
            newRowItem.setMessageCreatedAtTime(item.getMessageCreatedAtTime());
            newRowItem.setMessageOwnerName(item.getMessageOwnerName());
            if(newRowItem.isNewMessage()) {
                recentRowItems.add(newRowItem);
            }else if(item.getMessageCreatedAtTime() > twoWeeksAgo.getTimeInMillis()){
                recentRowItems.add(newRowItem);
            }
        }
        Collections.sort(recentRowItems, RowItem.createdAtComparator);
        Collections.reverse(recentRowItems);
        mainRecentModel.setRowItems(recentRowItems);

        BaseCallback<Boolean> mainModelsSaveCompleteListener = new BaseCallback<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                syncCompleteCallback.onResponse(success);
                initDataListeners();
            }

            @Override
            public void onFailure(Exception e) {
                syncCompleteCallback.onFailure(e);
            }
        };

        DataManager.getInstance().updateRealmMainModels(mainAccountsModel, mainRecentModel, mainDirectMessagesModel, mainModelsSaveCompleteListener);
    }

    private void initDataListeners() {
        initChatMessageListeners();
        initGroupChatListeners();
        initDirectChatListeners();
        initRequestListeners();
        initAccountListeners();
    }

    private void initChatMessageListeners() {
        final List<String> messageThreadIds = new ArrayList<>();
        final List<String> chatIds = new ArrayList<>();
        for(GroupChat chat : allGroupChats){
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

        for(int i = 0; i < chatIds.size(); i++){
            childEventListenersChatMessages.add(new ChildEventListener() {
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if(message != null) {
                        boolean needsToBeUpdated = false;
                        MessageRealm localMessage = RealmUISingleton.getInstance().getRealmInstance().
                                where(MessageRealm.class).equalTo("messageId", message.getMessageId()).findFirst();
                        if(!message.getCreatedByUid().equals(UserPreferences.getInstance().getUid())
                                && !message.getReadByUids().contains(UserPreferences.getInstance().getUid())
                                && (message.getReceivedByUids() == null || (message.getReceivedByUids() != null && !message.getReceivedByUids().contains(UserPreferences.getInstance().getUid())))){
                            NewMessageNotification newMessageNotification = new NewMessageNotification();
                            newMessageNotification.setNewMessage(message.getMessageId());
                            DataManager.getInstance().updateOrInsertNewMessageNotification(newMessageNotification);
                        }

                        if(message.getReceivedByUids() == null || !message.getReceivedByUids().contains(UserPreferences.getInstance().getUid())){
                            if(message.getReceivedByUids() == null){
                                message.setReceivedByUids(new ArrayList<String>());
                            }
                            message.getReceivedByUids().add(UserPreferences.getInstance().getUid());
                            needsToBeUpdated = true;
                        }

                        if(localMessage != null && localMessage.isSavedToFirebase()) {
                            //Do nothing
                        }else{
                            message.setSavedToFirebase(true);
                            needsToBeUpdated = true;
                            updateMainModels(message);
                        }

                        if(needsToBeUpdated){
                            batchUtil.updateMessageRealm(message);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if(message != null) {
                        message.setSavedToFirebase(true);
                        updateMainModels(message);
                        batchUtil.updateMessageRealm(message);
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
            chatMessagesReference.child(chatIds.get(i)).child("messages").addChildEventListener(childEventListenersChatMessages.get(i));
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

    private void initGroupChatListeners() {

    }

    private void initDirectChatListeners() {

    }

    private void initRequestListeners() {

    }

    private void initAccountListeners() {

    }

    private void updateMainModels(Message message) {
        Calendar twoWeeksAgo = Calendar.getInstance();
        twoWeeksAgo.add(Calendar.DAY_OF_YEAR, -14);


        List<CustomerRequestRealm> customerRequestsRealm = RealmUISingleton.getInstance().getRealmInstance().where(CustomerRequestRealm.class).findAll();
        RealmList<RowItem> recentRowItems = new RealmList<>();
        for(int i = 0; i < customerRequestsRealm.size(); i++){
            CustomerRequestRealm request = customerRequestsRealm.get(i);
            if(request != null && request.isOpen()){
                RowItem temp = new RowItem();
                temp.setMessageContent(request.getRequestMessage());
                temp.setMessageCreatedAtTime(request.getMostRecentMessageTime());
                temp.setMessageOwnerName(request.getCustomerName());
                temp.setChatId(request.getGroupChatId());
                if(request.getMostRecentGroupMessage() != null && !request.getMostRecentGroupMessage().getReadByUids().contains(UserPreferences.getInstance().getUid())){
                    temp.setNewMessage(true);
                }
                if(temp.getMessageCreatedAtTime() > twoWeeksAgo.getTimeInMillis()){
                    recentRowItems.add(temp);
                }
            }
        }

        DirectChatRealm realmChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", message.getChatId()).findFirst();
        DirectItem newItem = null;
        if(realmChat != null){
            newItem = new DirectItem();
            newItem.setDirectChatId(message.getChatId());
            if(!message.getReadByUids().contains(UserPreferences.getInstance().getUid())){
                newItem.setNewMessage(true);
            }else{
                newItem.setNewMessage(false);
            }
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
            if(message.getChatId().equals(directItem.getDirectChatId())){
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
            newRowItem.setChatId(item.getDirectChatId());
            newRowItem.setNewMessage(item.isNewMessage());
            newRowItem.setMessageContent(item.getMessageContent());
            newRowItem.setMessageCreatedAtTime(item.getMessageCreatedAtTime());
            newRowItem.setMessageOwnerName(item.getMessageOwnerName());
            if(item.isNewMessage()) {
                recentRowItems.add(newRowItem);
            }else if(item.getMessageCreatedAtTime() > twoWeeksAgo.getTimeInMillis()){
                recentRowItems.add(newRowItem);
            }
        }

        List<GroupChatRealm> allRealmGroupChats = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).findAll();
        MainRecentModel currentRecentModel = RealmUISingleton.getInstance().getRealmInstance().where(MainRecentModel.class).
                                                equalTo("permanentId", MainRecentModel.PERM_ID).findFirst();
        if(currentRecentModel != null) {
            for (GroupChatRealm groupChat : allRealmGroupChats) {
                if (groupChat.getChatId().equals(message.getChatId())){
                    RowItem newRecentItem = new RowItem();
                    newRecentItem.setItemType(RowItem.TYPE_GROUP_CHAT);
                    newRecentItem.setChatId(message.getChatId());
                    newRecentItem.setMessageCreatedAtTime(message.getCreatedDate());
                    newRecentItem.setMessageOwnerName(message.getMessageOwnerName());
                    newRecentItem.setNewMessage(!message.getReadByUids().contains(UserPreferences.getInstance().getUid()));
                    newRecentItem.setMessageContent(message.getMessageContent());
                    newRecentItem.setAccountId(groupChat.getAccountId());
                    recentRowItems.add(newRecentItem);
                }else{
                    RowItem sameItem = new RowItem();
                    sameItem.setItemType(RowItem.TYPE_GROUP_CHAT);
                    sameItem.setChatId(groupChat.getChatId());
                    sameItem.setMessageCreatedAtTime(groupChat.getMostRecentMessage().getCreatedDate());
                    sameItem.setMessageOwnerName(groupChat.getMostRecentMessage().getMessageOwnerName());
                    sameItem.setNewMessage(!groupChat.getMostRecentMessage().getReadByUids().contains(UserPreferences.getInstance().getUid()));
                    sameItem.setMessageContent(groupChat.getMostRecentMessage().getMessageContent());
                    sameItem.setAccountId(groupChat.getAccountId());
                    recentRowItems.add(sameItem);
                }
            }
        }else{
            RowItem newRecentItem = new RowItem();
            newRecentItem.setMessageCreatedAtTime(message.getCreatedDate());
            newRecentItem.setMessageContent(message.getMessageContent());
            newRecentItem.setItemType(RowItem.TYPE_GROUP_CHAT);
            newRecentItem.setMessageOwnerName(message.getMessageOwnerName());
            if (!message.getReadByUids().contains(UserPreferences.getInstance().getUid())) {
                newRecentItem.setNewMessage(true);
            } else {
                newRecentItem.setNewMessage(false);
            }
            recentRowItems.add(newRecentItem);
        }





        Collections.sort(recentRowItems, RowItem.createdAtComparator);
        Collections.reverse(recentRowItems);
        MainRecentModel recentModel = new MainRecentModel();
        recentModel.setRowItems(recentRowItems);

        BaseCallback<Boolean> onMainModelsUpdatedListener = new BaseCallback<Boolean>() {
            @Override
            public void onResponse(Boolean object) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        DataManager.getInstance().updateRealmMainModels(null, recentModel, newDirectModel, onMainModelsUpdatedListener);
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
}
