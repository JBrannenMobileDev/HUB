package jjpartnership.hub.data_layer;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;

/**
 * Created by Jonathan on 5/28/2018.
 */

public class RealmMessageBatchUtil {
    private List<Message> messagesToUpdate;
    private Runnable batchUpdate;
    private Handler handler;

    public RealmMessageBatchUtil(){
        messagesToUpdate = new ArrayList<>();
        batchUpdate = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().updateRealmMessages(messagesToUpdate);
                Log.d(RealmMessageBatchUtil.class.getSimpleName(), "Batch messages update sent - count: " + String.valueOf(messagesToUpdate.size()));
                messagesToUpdate = new ArrayList<>();
                handler = null;
            }
        };
    }

    public void updateMessageRealm(Message message){
        messagesToUpdate.add(message);
        if(handler == null){
            handler = new Handler();
            handler.postDelayed(batchUpdate, 50);
        }
    }
}
