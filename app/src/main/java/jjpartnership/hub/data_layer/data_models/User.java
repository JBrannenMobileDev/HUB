package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbrannen on 2/23/18.
 */

public class User{
    private String uid;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Company company;
    private String businessUnit;
    private String role;
    private String type;
    private List<Message> directChats;
    private List<Message> groupChats;

    public User() {
    }

    public User(String uid, String email, String type) {
        this.uid = uid;
        this.email = email;
        this.type = type;
    }

    public User(UserRealm realmUser){
        this.uid = realmUser.getUid();
        this.email = realmUser.getEmail();
        this.phoneNumber = realmUser.getPhoneNumber();
        this.firstName = realmUser.getFirstName();
        this.lastName = realmUser.getLastName();
        this.company = new Company(realmUser.getCompany());
        this.businessUnit = realmUser.getBusinessUnit();
        this.role = realmUser.getRole();
        this.type = realmUser.getType();
        this.directChats = createDirectChatsList(realmUser.getDirectChat());
        this.groupChats = createGroupChatsList(realmUser.getGroupChat());
    }

    private List<Message> createGroupChatsList(List<MessageRealm> groupChatRealm) {
        List<Message> groupChat = new ArrayList<>();
        for(MessageRealm realmMessage : groupChatRealm){
            groupChat.add(new Message(realmMessage));
        }
        return groupChat;
    }

    private List<Message> createDirectChatsList(List<MessageRealm> directChatRealm) {
        List<Message> directChat = new ArrayList<>();
        for(MessageRealm realmMessage : directChatRealm){
            directChat.add(new Message(realmMessage));
        }
        return directChat;
    }

    public String getType() {
        return type != null ? type : "";
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Message> getDirectChat() {
        return directChats != null ? directChats : new ArrayList<Message>();
    }

    public void setDirectChat(List<Message> directChatIds) {
        this.directChats = directChatIds;
    }

    public List<Message> getGroupChat() {
        return groupChats != null ? groupChats : new ArrayList<Message>();
    }

    public void setGroupChatIds(List<Message> groupChatIds) {
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

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public Company getCompany() {
        return company != null ? company : new Company();
    }

    public void setCompany(Company company) {
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
