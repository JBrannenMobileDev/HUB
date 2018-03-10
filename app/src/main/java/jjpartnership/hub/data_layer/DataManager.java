package jjpartnership.hub.data_layer;

import java.util.List;

import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.GroupChat;
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

    public void createNewUser(String uid, String email){

    }

    public void updateRealmUser(UserRealm user) {
        realmManager.insertOrUpdateUser(user);
    }

    public void updateRealmCompanies(List<CompanyRealm> companies) {
        realmManager.insertOrUpdateCompanies(companies);
    }

    public void updateUser(String firstName, String lastName, String phoneNumber, String businessUnit, String role) {

    }

    public void getCompany(String emailDomain, BaseCallback<Boolean> companyNameCallback) {
        fbManager.getMatchingCompanyBoolean(emailDomain, companyNameCallback);
    }

    public void saveCompanyName(String name) {
        realmManager.saveCompanyName(name);
    }

    public void populateDataBaseFakeData() {
        fbManager.onBoardNewSalesCompany();
    }

    public void verifyUserAccountExists(String email, BaseCallback<String> userAccountExistsCallback) {
        fbManager.verifyUserAccountExists(email, userAccountExistsCallback);
    }

    public void updateRealmAccount(Account account) {
        realmManager.updateAccount(account);
    }

    public void updateRealmDirectChat(DirectChat chat) {
        realmManager.updateDirectChat(chat);
    }

    public void updateRealmGroupChat(GroupChat gChat) {
        realmManager.updateGroupChat(gChat);
    }

    public void updateRealmCompany(Company company) {
        realmManager.updateCompany(company);
    }
}
