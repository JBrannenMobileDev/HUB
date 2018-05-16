package jjpartnership.hub.data_layer.realm_db;

import android.os.Handler;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
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
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;

/**
 * Created by jbrannen on 2/24/18.
 */

public class RealmManager {
    private BaseCallback<Boolean> onSyncSuccess;

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
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new GroupChatRealm(chat));
            }
        });
        realm.close();
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
                }
            });
        }
        realm.close();
    }

    public void updateMainAccountsModel(final MainAccountsModel accountsModel, final MainRecentModel recentModel, final MainDirectMessagesModel directModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(accountsModel);
                bgRealm.copyToRealmOrUpdate(recentModel);
                bgRealm.copyToRealmOrUpdate(directModel);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                DataManager.getInstance().initChatMessageListeners();
                if(onSyncSuccess != null){
                    onSyncSuccess.onResponse(true);
                }
            }
        }, new Realm.Transaction.OnError(){
            @Override
            public void onError(Throwable error){
                onSyncSuccess.onFailure(new Exception(error.getMessage()));
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

    public void setOnDataSyncSuccessCallback(BaseCallback<Boolean> onSyncSuccess) {
        this.onSyncSuccess = onSyncSuccess;
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
}
