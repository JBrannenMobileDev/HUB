package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

/**
 * Created by jbrannen on 2/23/18.
 */

public class User{
    private String uid;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String companyId;
    private String businessUnit;
    private String role;
    private String userType;
    private int userColor;
    private Map<String,String> directChats;
    private List<String> accountIds;

    public User() {
    }

    public User(String uid, String email, String phoneNumber, String firstName, String lastName,
                String companyId, String businessUnit, String role, String userType,
                Map<String,String> directChatIds, List<String> accountIds, int userColor) {
        this.uid = uid;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.businessUnit = businessUnit;
        this.role = role;
        this.userType = userType;
        this.directChats = directChatIds;
        this.accountIds = accountIds;
        this.userColor = userColor;
    }

    public User(UserRealm realmUser){
        if(realmUser != null) {
            this.uid = realmUser.getUid();
            this.email = realmUser.getEmail();
            this.phoneNumber = realmUser.getPhoneNumber();
            this.firstName = realmUser.getFirstName();
            this.lastName = realmUser.getLastName();
            this.companyId = realmUser.getCompanyId();
            this.businessUnit = realmUser.getBusinessUnit();
            this.role = realmUser.getRole();
            this.userType = realmUser.getUserType();
            this.directChats = createMap(realmUser.getDirectChatIds());
            this.accountIds = realmUser.getAccountIds();
            this.userColor = realmUser.getUserColor();
        }
    }

    private Map<String, String> createMap(RealmList<String> directChatIds) {
        Map<String, String> dChatIdsMap = new HashMap<>();
        for(String chatId : directChatIds){
            dChatIdsMap.put(chatId, chatId);
        }
        return dChatIdsMap;
    }

    public void addAccount(String accountId) {
        if(accountIds == null){
            accountIds = new ArrayList<>();
        }
        accountIds.add(accountId);
    }

    public int getUserColor() {
        return userColor;
    }

    public void setUserColor(int userColor) {
        this.userColor = userColor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Map<String,String> getDirectChats() {
        return directChats;
    }

    public void setDirectChats(Map<String,String> directChats) {
        this.directChats = directChats;
    }

    public List<String> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<String> accountIds) {
        this.accountIds = accountIds;
    }
}
