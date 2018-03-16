package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class RequestRealm extends RealmObject{
    @PrimaryKey
    private String requestId;
    private boolean requestFullfilled;
    private MessageRealm message;

    public RequestRealm() {
    }

    public RequestRealm(String requestId, boolean requestFullfilled, MessageRealm message) {
        this.requestId = requestId;
        this.requestFullfilled = requestFullfilled;
        this.message = message;
    }

    public RequestRealm(Request request) {
        this.requestId = request.getRequestId();
        this.requestFullfilled = request.isRequestFullfilled();
        this.message = new MessageRealm(request.getMessage());
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

    public MessageRealm getMessage() {
        return message;
    }

    public void setMessage(MessageRealm message) {
        this.message = message;
    }
}
