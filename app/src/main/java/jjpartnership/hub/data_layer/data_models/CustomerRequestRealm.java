package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 4/2/2018.
 */

public class CustomerRequestRealm extends RealmObject{
    public static final String TYPE_RESOLVED = "resolved";
    public static final String TYPE_CANCELLED = "cancelled";

    @PrimaryKey
    private String requestId;
    private String accountId;
    private String customerUid;
    private String requestMessage;
    private String requestSubject;
    private String groupChatId;
    private String customerName;
    private MessageRealm mostRecentGroupMessage;
    private String mostRecentMessageUid;
    private long mostRecentMessageTime;
    private boolean isOpen;
    private long openDate;
    private long closeDate;
    private String resolutionType;

    public CustomerRequestRealm() {
    }

    public CustomerRequestRealm(CustomerRequest data){
        this.requestId = data.getRequestId();
        this.accountId = data.getAccountId();
        this.customerUid = data.getCustomerUid();
        this.requestMessage = data.getRequestMessage();
        this.groupChatId = data.getGroupChatId();
        this.mostRecentGroupMessage = new MessageRealm(data.getMostRecentGroupMessage());
        this.mostRecentMessageUid = data.getMostRecentMessageUid();
        this.mostRecentMessageTime = data.getMostRecentMessageTime();
        this.isOpen = data.isOpen();
        this.openDate = data.getOpenDate();
        this.closeDate = data.getCloseDate();
        this.resolutionType = data.getResolutionType();
        this.requestSubject = data.getRequestSubject();
        this.customerName = data.getCustomerName();
    }

    public String getRequestSubject() {
        return requestSubject;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setRequestSubject(String requestSubject) {
        this.requestSubject = requestSubject;
    }

    public static String getTypeResolved() {
        return TYPE_RESOLVED;
    }

    public static String getTypeCancelled() {
        return TYPE_CANCELLED;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public MessageRealm getMostRecentGroupMessage() {
        return mostRecentGroupMessage;
    }

    public void setMostRecentGroupMessage(MessageRealm mostRecentGroupMessage) {
        this.mostRecentGroupMessage = mostRecentGroupMessage;
    }

    public String getMostRecentMessageUid() {
        return mostRecentMessageUid;
    }

    public void setMostRecentMessageUid(String mostRecentMessageUid) {
        this.mostRecentMessageUid = mostRecentMessageUid;
    }

    public long getMostRecentMessageTime() {
        return mostRecentMessageTime;
    }

    public void setMostRecentMessageTime(long mostRecentMessageTime) {
        this.mostRecentMessageTime = mostRecentMessageTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public long getOpenDate() {
        return openDate;
    }

    public void setOpenDate(long openDate) {
        this.openDate = openDate;
    }

    public long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(long closeDate) {
        this.closeDate = closeDate;
    }

    public String getResolutionType() {
        return resolutionType;
    }

    public void setResolutionType(String resolutionType) {
        this.resolutionType = resolutionType;
    }
}
