package jjpartnership.hub.data_layer;

import java.util.List;

import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.data_layer.firebase_db.FirebaseManager;
import jjpartnership.hub.data_layer.realm_db.RealmManager;
import jjpartnership.hub.utils.BaseCallback;

/**
 * Created by jbrannen on 2/24/18.
 */

public class DataManager {
    private static final DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private FirebaseManager fbManager;
    private RealmManager realmManager;

    private DataManager() {
        fbManager = new FirebaseManager();
        realmManager = new RealmManager();
    }

    public void writeEmailPassword(String password){
        fbManager.writeEmailPassword(password);
    }

    public void initializeDbData(){
        fbManager.loadCompaniesToFirebase();
    }

    public void createNewUser(String uid, String email, String typeId){
        fbManager.writeNewUser(new User(uid, email, typeId));
    }

    public void updateRealmUser(UserRealm user) {
        realmManager.insertOrUpdateUser(user);
    }

    public void updateRealmCompanies(List<CompanyRealm> companies) {
        realmManager.insertOrUpdateCompanies(companies);
    }

    public void updateUser(String firstName, String lastName, String phoneNumber, String businessUnit, String role) {
        fbManager.updateUser(firstName, lastName, phoneNumber, businessUnit, role);
    }

    public void getCompany(String emailDomain, BaseCallback<Boolean> companyNameCallback) {
        fbManager.getMatchingCompanyBoolean(emailDomain, companyNameCallback);
    }

    public void saveCompanyName(String name) {
        realmManager.saveCompanyName(name);
    }
}
