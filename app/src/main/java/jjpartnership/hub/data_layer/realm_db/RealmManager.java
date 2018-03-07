package jjpartnership.hub.data_layer.realm_db;

import java.util.List;

import io.realm.Realm;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
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
}
