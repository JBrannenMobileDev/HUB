package jjpartnership.hub.data_layer.realm_db;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by jbrannen on 2/24/18.
 */

public class RealmManager {

    public RealmManager() {
    }

    public void insertOrUpdateUser(final User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new UserRealm(user));
            }
        });
        realm.close();
    }

    public void updateAccount(final List<Account> accounts) {
        final List<AccountRealm> realmAccounts = new ArrayList<>();
        for(Account account : accounts){
            realmAccounts.add(new AccountRealm(account));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmAccounts);
            }
        });
        realm.close();
    }

    public void updateDirectChat(final List<DirectChat> chats) {
        final List<DirectChatRealm> realmChats = new ArrayList<>();
        for(DirectChat chat : chats){
            realmChats.add(new DirectChatRealm(chat));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmChats);
            }
        });
        realm.close();
    }

    public void updateGroupChat(final List<GroupChat> gChats) {
        final List<GroupChatRealm> realmChats = new ArrayList<>();
        for(GroupChat chat : gChats){
            realmChats.add(new GroupChatRealm(chat));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmChats);
            }
        });
        realm.close();
    }

    public void updateCompany(final List<Company> companies) {
        final List<CompanyRealm> realmCompanies = new ArrayList<>();
        for(Company company : companies){
            realmCompanies.add(new CompanyRealm(company));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmCompanies);
            }
        });
        realm.close();
    }

    public void updateRealmMessages(List<Message> messages) {
        final List<MessageRealm> realmMessages = new ArrayList<>();
        for(Message message : messages){
            realmMessages.add(new MessageRealm(message));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmMessages);
            }
        });
        realm.close();
    }

    public void updateMainAccountsModel(MainAccountsModel accountsModel) {

    }
}
