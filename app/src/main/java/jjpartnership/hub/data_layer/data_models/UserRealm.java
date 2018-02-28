package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 2/23/18.
 */

public class UserRealm extends RealmObject{
    @PrimaryKey private String uid;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private CompanyRealm company;
    private String businessUnit;
    private String role;
    private String type;
    private RealmList<MessageRealm> directChats;
    private RealmList<MessageRealm> groupChats;

    public UserRealm() {
    }

    public UserRealm(String uid, String email, String type) {
        this.uid = uid;
        this.email = email;
        this.type = type;
    }

    public UserRealm(User user) {
        this.uid = user.getUid();
        this.phoneNumber = user.getPhoneNumber();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.company = new CompanyRealm(user.getCompany());
        this.businessUnit = user.getBusinessUnit();
        this.role = user.getRole();
        this.type = user.getType();
        this.directChats = createDirectChats(user.getDirectChat());
        this.groupChats = createGroupChats(user.getGroupChat());
    }

    private RealmList<MessageRealm> createGroupChats(List<Message> groupChat) {
        RealmList<MessageRealm> groupChatsRealm = new RealmList<>();
        for(Message message : groupChat){
            groupChatsRealm.add(new MessageRealm(message));
        }
        return groupChatsRealm;
    }

    private RealmList<MessageRealm> createDirectChats(List<Message> directChat) {
        RealmList<MessageRealm> directChatsRealm = new RealmList<>();
        for(Message message : directChat){
            directChatsRealm.add(new MessageRealm(message));
        }
        return directChatsRealm;
    }

    public String getType() {
        return type != null ? type : "";
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MessageRealm> getDirectChat() {
        return directChats != null ? directChats : new ArrayList<MessageRealm>();
    }

    public void setDirectChatIds(RealmList<MessageRealm> directChatIds) {
        this.directChats = directChatIds;
    }

    public List<MessageRealm> getGroupChat() {
        return groupChats != null ? groupChats : new ArrayList<MessageRealm>();
    }

    public void setGroupChatIds(RealmList<MessageRealm> groupChatIds) {
        this.groupChats = groupChatIds;
    }

    public String getUid() {
        return uid != null ? uid : "";
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : "";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CompanyRealm getCompany() {
        return company != null ? company : new CompanyRealm();
    }

    public void setCompany(CompanyRealm company) {
        this.company = company;
    }

    public String getBusinessUnit() {
        return businessUnit != null ? businessUnit : "";
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getRole() {
        return role != null ? role : "";
    }

    public void setRole(String role) {
        this.role = role;
    }
}
