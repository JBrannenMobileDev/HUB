package jjpartnership.hub.data_layer.data_models;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class Request {
    private String requestId;
    private boolean requestFullfilled;
    private Message message;

    public Request() {
    }

    public Request(String requestId, boolean requestFullfilled, Message message) {
        this.requestId = requestId;
        this.requestFullfilled = requestFullfilled;
        this.message = message;
    }

    public Request(RequestRealm realm) {
        this.requestId = realm.getRequestId();
        this.requestFullfilled = realm.isRequestFullfilled();
        this.message = new Message(realm.getMessage());
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Boolean isRequestFullfilled() {
        return requestFullfilled;
    }

    public void setRequestFullfilled(boolean requestFullfilled) {
        this.requestFullfilled = requestFullfilled;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
