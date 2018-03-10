package jjpartnership.hub.data_layer.realm_db;

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
import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by jbrannen on 2/24/18.
 */

public class RealmManager {

    public RealmManager() {
    }

    public void insertOrUpdateUser(final UserRealm user) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(user);
            }
        });
        realm.close();
    }


    public void insertOrUpdateCompanies(final List<CompanyRealm> companies) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(companies);
            }
        });
        realm.close();
    }

    public void saveCompanyName(final String companyName) {
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                bgRealm.copyToRealmOrUpdate(new UserCompanyName(companyName));
//            }
//        });
    }

    public void updateAccount(final Account account) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new AccountRealm(account));
            }
        });
        realm.close();
    }

    public void updateDirectChat(final DirectChat chat) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new DirectChatRealm(chat));
            }
        });
        realm.close();
    }

    public void updateGroupChat(final GroupChat gChat) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new GroupChatRealm(gChat));
            }
        });
        realm.close();
    }

    public void updateCompany(final Company company) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(new CompanyRealm(company));
            }
        });
        realm.close();
    }
}
