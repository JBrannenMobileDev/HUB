package jjpartnership.hub.data_layer.data_models;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 2/23/18.
 */

public class UserRealm extends RealmObject implements Comparable<UserRealm>{
    public static final String TYPE_SALES = "sales_agent";
    public static final String TYPE_CUSTOMER = "account_rep";

    @PrimaryKey
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
    private RealmList<String> directChatIds;
    private RealmList<String> groupChatIds;
    private RealmList<String> accountIds;

    public UserRealm() {
    }

    public UserRealm(String uid, String email, String phoneNumber, String firstName, String lastName,
                     String companyId, String businessUnit, String role, String userType,
                     RealmList<String> directChatIds, RealmList<String> accountIds, int userColor, RealmList<String> groupChatIds) {
        this.uid = uid;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.businessUnit = businessUnit;
        this.role = role;
        this.userType = userType;
        this.directChatIds = directChatIds;
        this.accountIds = accountIds;
        this.userColor = userColor;
        this.groupChatIds = groupChatIds;
    }

    public UserRealm(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.companyId = user.getCompanyId();
        this.businessUnit = user.getBusinessUnit();
        this.role = user.getRole();
        this.userType = user.getUserType();
        if(user.getDirectChats() != null) this.directChatIds = createDirectChats(user.getDirectChats().values());
        this.accountIds = createAccountIdList(user.getAccountIds());
        this.userColor = user.getUserColor();
        if(user.getGroupChats() != null) this.groupChatIds = createGroupChats(user.getGroupChats().values());
    }

    public UserRealm(String uid) {
        this.uid = uid;
    }

    private RealmList<String> createGroupChats(Collection<String> groupChatIds) {
        if(groupChatIds != null) {
            RealmList<String> groupChatIdsRealm = new RealmList<>();
            for (String groupChatId : groupChatIds) {
                groupChatIdsRealm.add(groupChatId);
            }
            return groupChatIdsRealm;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createAccountIdList(List<String> accountIds) {
        if(accountIds != null) {
            RealmList<String> accountIdsRealm = new RealmList<>();
            for (String accountId : accountIds) {
                accountIdsRealm.add(accountId);
            }
            return accountIdsRealm;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createDirectChats(Collection<String> directChatIds) {
        if(directChatIds != null) {
            RealmList<String> directChatIdsRealm = new RealmList<>();
            for (String directChatId : directChatIds) {
                directChatIdsRealm.add(directChatId);
            }
            return directChatIdsRealm;
        }else{
            return new RealmList<>();
        }
    }

    public RealmList<String> getGroupChatIds() {
        return groupChatIds;
    }

    public void setGroupChatIds(RealmList<String> groupChatIds) {
        this.groupChatIds = groupChatIds;
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

    public RealmList<String> getDirectChatIds() {
        return directChatIds;
    }

    public void setDirectChatIds(RealmList<String> directChatIds) {
        this.directChatIds = directChatIds;
    }

    public RealmList<String> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(RealmList<String> accountIds) {
        this.accountIds = accountIds;
    }

    @Override
    public int compareTo(@NonNull UserRealm userToCompare) {
        return getFirstName().compareTo(userToCompare.getFirstName());
    }
}
