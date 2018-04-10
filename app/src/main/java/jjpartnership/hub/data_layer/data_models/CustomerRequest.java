package jjpartnership.hub.data_layer.data_models;

/**
 * Created by Jonathan on 4/2/2018.
 */

public class CustomerRequest {
    public static final String TYPE_RESOLVED = "resolved";
    public static final String TYPE_CANCELLED = "cancelled";

    private String requestId;
    private String accountId;
    private String customerUid;
    private String requestMessage;
    private String requestSubject;
    private String groupChatId;
    private String customerName;
    private Message mostRecentGroupMessage;
    private String mostRecentMessageUid;
    private long mostRecentMessageTime;
    private boolean isOpen;
    private long openDate;
    private long closeDate;
    private String resolutionType;

    public CustomerRequest() {
    }

    public CustomerRequest(CustomerRequestRealm realm){
        this.requestId = realm.getRequestId();
        this.accountId = realm.getAccountId();
        this.customerUid = realm.getCustomerUid();
        this.requestMessage = realm.getRequestMessage();
        this.groupChatId = realm.getGroupChatId();
        this.mostRecentGroupMessage = new Message(realm.getMostRecentGroupMessage());
        this.mostRecentMessageUid = realm.getMostRecentMessageUid();
        this.mostRecentMessageTime = realm.getMostRecentMessageTime();
        this.isOpen = realm.isOpen();
        this.openDate = realm.getOpenDate();
        this.closeDate = realm.getCloseDate();
        this.resolutionType = realm.getResolutionType();
        this.requestSubject = realm.getRequestSubject();
        this.customerName = realm.getCustomerName();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRequestSubject() {
        return requestSubject;
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

    public Message getMostRecentGroupMessage() {
        return mostRecentGroupMessage;
    }

    public void setMostRecentGroupMessage(Message mostRecentGroupMessage) {
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
