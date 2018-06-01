package jjpartnership.hub.data_layer.realm_db;

import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.CustomerRequest;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.MessageThread;
import jjpartnership.hub.data_layer.data_models.MessageThreadRealm;
import jjpartnership.hub.data_layer.data_models.NewMessageNotification;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;

/**
 * Created by jbrannen on 2/24/18.
 */

public class RealmManager {

    public RealmManager() {
    }

    public void insertOrUpdateUser(final User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new UserRealm(user));
            }
        });
        realm.close();
    }

    public void updateAccount(final List<Account> accounts) {
        final RealmList<AccountRealm> realmAccounts = new RealmList<>();
        for(Account account : accounts){
            realmAccounts.add(new AccountRealm(account));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmAccounts);
            }
        });
        realm.close();
    }

    public void updateDirectChats(final List<DirectChat> chats) {
        final RealmList<DirectChatRealm> realmChats = new RealmList<>();
        for(DirectChat chat : chats){
            realmChats.add(new DirectChatRealm(chat));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmChats);
            }
        });
        realm.close();
    }
    public void insertOrUpdateGroupChat(final GroupChat chat) {
        final GroupChatRealm realmChat = new GroupChatRealm(chat);
        if(chat != null) {
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(realmChat);
                }
            });
            realm.close();
        }
    }

    public void updateGroupChat(final List<GroupChat> gChats) {
        final RealmList<GroupChatRealm> realmChats = new RealmList<>();
        for(GroupChat chat : gChats){
            realmChats.add(new GroupChatRealm(chat));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmChats);
            }
        });
        realm.close();
    }

    public void updateCompany(final List<Company> companies) {
        final RealmList<CompanyRealm> realmCompanies = new RealmList<>();
        for(Company company : companies){
            realmCompanies.add(new CompanyRealm(company));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmCompanies);
            }
        });
        realm.close();
    }

    public void updateRealmMessages(List<Message> messages) {
        final RealmList<MessageRealm> realmMessages = new RealmList<>();
        for(Message message : messages){
            realmMessages.add(new MessageRealm(message));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmMessages);
            }
        });
        realm.close();
    }

    public void updateRealmMessage(final Message message) {
        final MessageRealm realmMessage = new MessageRealm(message);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmMessage);
            }
        });
        realm.close();
    }

    public void initInitialDataIfDBisEmpty() {
        Realm realm = Realm.getDefaultInstance();
        MainAccountsModel accounts = realm.where(MainAccountsModel.class).findFirst();
        MainDirectMessagesModel directMessages = realm.where(MainDirectMessagesModel.class).findFirst();
        MainRecentModel recentMessages = realm.where(MainRecentModel.class).findFirst();
        if(accounts == null && directMessages == null && recentMessages == null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(new MainAccountsModel());
                    bgRealm.copyToRealmOrUpdate(new MainDirectMessagesModel());
                    bgRealm.copyToRealmOrUpdate(new MainRecentModel());
                    bgRealm.copyToRealmOrUpdate(new NewMessageNotification());
                }
            });
        }
        realm.close();
    }

    public void updateMainAccountsModel(final MainAccountsModel accountsModel, final MainRecentModel recentModel, final MainDirectMessagesModel directModel, final BaseCallback<Boolean> mainModelsSaveCompleteListener) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                if(accountsModel != null) bgRealm.copyToRealmOrUpdate(accountsModel);
                if(recentModel != null) bgRealm.copyToRealmOrUpdate(recentModel);
                if(directModel != null) bgRealm.copyToRealmOrUpdate(directModel);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                mainModelsSaveCompleteListener.onResponse(true);
            }
        }, new Realm.Transaction.OnError(){
            @Override
            public void onError(Throwable error){
                mainModelsSaveCompleteListener.onFailure(new Exception(error.getMessage()));
            }
        });
        realm.close();
    }

    public void insertOrUpdateMessage(final MessageRealm messageRealm) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(messageRealm);
            }
        });
        realm.close();
    }

    public void nukeDb() {
        RealmUISingleton.getInstance().closeRealmInstance();
        Realm.deleteRealm(Realm.getDefaultConfiguration());
    }

    public void insertOrUpdateUserColor(final long color, final String uid) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new UserColor(uid, color));
            }
        });
        realm.close();
    }

    public void insertOrUpdateMessageThread(final MessageThread thread) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new MessageThreadRealm(thread));
            }
        });
        realm.close();
    }

    public void insertOrUpdateMessages(List<Message> messageList) {
        final RealmList<MessageRealm> realmMessages = new RealmList<>();
        for(Message message : messageList){
            realmMessages.add(new MessageRealm(message));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmMessages);
            }
        });
        realm.close();
    }

    public void insertOrUpdateDirectChat(final DirectChat newDirectChat) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new DirectChatRealm(newDirectChat));
            }
        });
        realm.close();
    }

    public void insertOrUpdateDirectChatRealm(final DirectChatRealm directChat) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(directChat);
            }
        });
        realm.close();
    }

    public void insertOrUpdateGroupChatRealm(final GroupChatRealm groupChatRealm) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(groupChatRealm);
            }
        });
        realm.close();
    }

    public void insertOrUpdateCustomerRequest(final List<CustomerRequest> requests) {
        final RealmList<CustomerRequestRealm> realmRequests = new RealmList<>();
        for(CustomerRequest request : requests){
            realmRequests.add(new CustomerRequestRealm(request));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmRequests);
            }
        });
        realm.close();
    }

    public void insertOrUpdateCustomerRequest(final CustomerRequestRealm request) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(request);
            }
        });
        realm.close();
    }

    public void saveBootData(final List<User> users, final List<Account> userAccounts,
                             final List<CustomerRequest> customerRequests, final List<Company> companies,
                             final List<GroupChat> allGroupChats, final List<DirectChat> directChats,
                             final List<UserColor> userColors, final List<Message> allMessages,
                             final BaseCallback<Boolean> syncCompleteListener) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                if(users != null)bgRealm.copyToRealmOrUpdate(createRealmUserList(users));
                if(userAccounts != null)bgRealm.copyToRealmOrUpdate(createRealmAccountList(userAccounts));
                if(customerRequests != null)bgRealm.copyToRealmOrUpdate(createRealmCustomerRequestList(customerRequests));
                if(companies != null)bgRealm.copyToRealmOrUpdate(createRealmCompaniesList(companies));
                if(allGroupChats != null)bgRealm.copyToRealmOrUpdate(createRealmAllGroupChatsList(allGroupChats));
                if(directChats != null)bgRealm.copyToRealmOrUpdate(createRealmDirectChatsList(directChats));
                if(userColors != null)bgRealm.copyToRealmOrUpdate(userColors);
                if(allMessages != null)bgRealm.copyToRealmOrUpdate(createRealmALlMessagesList(allMessages));
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                syncCompleteListener.onResponse(true);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                syncCompleteListener.onFailure(new Exception(error.getMessage()));
            }
        });
        realm.close();
    }

    private List<UserRealm> createRealmUserList(List<User> users) {
        List<UserRealm> realmUserList = new ArrayList<>();
        for(User user : users){
            realmUserList.add(new UserRealm(user));
        }
        return realmUserList;
    }

    private List<AccountRealm> createRealmAccountList(List<Account> accounts) {
        List<AccountRealm> realmAccountList = new ArrayList<>();
        for(Account account : accounts){
            realmAccountList.add(new AccountRealm(account));
        }
        return realmAccountList;
    }

    private List<CustomerRequestRealm> createRealmCustomerRequestList(List<CustomerRequest> requests) {
        List<CustomerRequestRealm> realmRequestList = new ArrayList<>();
        for(CustomerRequest request : requests){
            realmRequestList.add(new CustomerRequestRealm(request));
        }
        return realmRequestList;
    }

    private List<CompanyRealm> createRealmCompaniesList(List<Company> companies) {
        List<CompanyRealm> realmCompanyList = new ArrayList<>();
        for(Company company : companies){
            realmCompanyList.add(new CompanyRealm(company));
        }
        return realmCompanyList;
    }

    private List<GroupChatRealm> createRealmAllGroupChatsList(List<GroupChat> groupChats) {
        List<GroupChatRealm> realmGroupChatList = new ArrayList<>();
        for(GroupChat chat : groupChats){
            realmGroupChatList.add(new GroupChatRealm(chat));
        }
        return realmGroupChatList;
    }

    private List<DirectChatRealm> createRealmDirectChatsList(List<DirectChat> directChats) {
        List<DirectChatRealm> realmDirectChatList = new ArrayList<>();
        for(DirectChat chat : directChats){
            realmDirectChatList.add(new DirectChatRealm(chat));
        }
        return realmDirectChatList;
    }

    private List<MessageRealm> createRealmALlMessagesList(List<Message> messages) {
        List<MessageRealm> realmMessageList = new ArrayList<>();
        for(Message message : messages){
            realmMessageList.add(new MessageRealm(message));
        }
        return realmMessageList;
    }

    public void updateOrInserNewMessageNotification(final NewMessageNotification newMessageNotification) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(newMessageNotification);
            }
        });
        realm.close();
    }

    public void inserOrUpdateRowItem(final RowItem recentRowItem) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                MainRecentModel recentModel = bgRealm.where(MainRecentModel.class).equalTo("permanentId", MainRecentModel.PERM_ID).findFirst();
                bgRealm.copyToRealmOrUpdate(recentRowItem);
                for(int i = 0; i < recentModel.getRowItems().size(); i++){
                    if(recentRowItem.getChatId().equals(recentModel.getRowItems().get(i).getChatId())){
                        recentModel.getRowItems().set(i, recentRowItem);
                    }
                }
                bgRealm.copyToRealmOrUpdate(recentModel);
            }
        });
        realm.close();
    }

    public void updateUserColor(final int colorId, final String uid) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                UserRealm user = bgRealm.where(UserRealm.class).equalTo("uid", uid).findFirst();
                if(user != null){
                    user.setUserColor(colorId);
                    bgRealm.copyToRealmOrUpdate(user);
                }
            }
        });
        realm.close();
    }
}
