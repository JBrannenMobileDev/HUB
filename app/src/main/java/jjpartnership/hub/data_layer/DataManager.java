package jjpartnership.hub.data_layer;

import java.util.List;

import jjpartnership.hub.data_layer.data_models.Account;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.DirectChat;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.MessageThread;
import jjpartnership.hub.data_layer.data_models.User;
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

    public void syncFirebaseToRealmDb(){
        fbManager.initDataListeners();
    }

    public void createNewUser(String uid, String email){

    }

    public void updateRealmUser(User user) {
        realmManager.insertOrUpdateUser(user);
    }

    public void updateUser(String firstName, String lastName, String phoneNumber, String businessUnit, String role) {

    }

    public void getCompany(String emailDomain, BaseCallback<Boolean> companyNameCallback) {
        fbManager.getMatchingCompanyBoolean(emailDomain, companyNameCallback);
    }

    public void populateDataBaseFakeData() {
        fbManager.onBoardNewSalesCompany();
    }

    public void verifyUserAccountExists(String email, BaseCallback<String> userAccountExistsCallback) {
        fbManager.verifyUserAccountExists(email, userAccountExistsCallback);
    }

    public void updateRealmAccounts(List<Account> account) {
        realmManager.updateAccount(account);
    }

    public void updateRealmDirectChats(List<DirectChat> chat) {
        realmManager.updateDirectChat(chat);
    }

    public void updateRealmGroupChats(List<GroupChat> gChat) {
        realmManager.updateGroupChat(gChat);
    }

    public void updateRealmCompanys(List<Company> company) {
        realmManager.updateCompany(company);
    }

    public void updateRealmMessages(List<Message> messages) {
        realmManager.updateRealmMessages(messages);
    }

    public void updateRealmMessage(Message message) {
        realmManager.updateRealmMessage(message);
    }

    public void updateRealmMainModels(MainAccountsModel accountsModel, MainRecentModel recentModel) {
        realmManager.updateMainAccountsModel(accountsModel, recentModel);
    }

    public void createNewMessage(Message newMessage) {
        realmManager.insertOrUpdateMessage(new MessageRealm(newMessage));
        fbManager.createNewMesage(newMessage);
    }

    public void clearRealmData() {
        realmManager.nukeDb();
    }

    public void setFreshInstallCallback(BaseCallback<Boolean> freshInstallDataLoadedToRealmCallback) {
        realmManager.setFreshInstallCallback(freshInstallDataLoadedToRealmCallback);
    }

    public void updateRealmUserColor(long color, String uid) {
        realmManager.insertOrUpdateUserColor(color, uid);
    }

    public void insertOrUpdateMessageThread(MessageThread thread) {
        realmManager.insertOrUpdateMessageThread(thread);
    }

    public void updateFirebaseMessageThreadTyping(String chatId, String messageThreadId, String userName, boolean isTyping) {
        fbManager.updateFirebaseMessageThreadTyping(chatId, messageThreadId, userName, isTyping);
    }
}
