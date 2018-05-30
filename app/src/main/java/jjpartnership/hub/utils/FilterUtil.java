package jjpartnership.hub.utils;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;

/**
 * Created by Jonathan on 4/29/2018.
 */

public class FilterUtil {
    public static RealmList<GroupChatRealm> filterOutCustomerRequestGroups(RealmResults<GroupChatRealm> allGroups){
        RealmList<GroupChatRealm> filteredChats = new RealmList<>();
        for(GroupChatRealm chat : allGroups){
            if(chat.getCustomerRequestIds() == null || chat.getCustomerRequestIds().size() == 0) {
                filteredChats.add(chat);
            }
        }
        return filteredChats;
    }

    public static RealmList<GroupChatRealm> filterOutCustomerRequestAndAllAgentGroups(RealmResults<GroupChatRealm> allGroups){
        RealmList<GroupChatRealm> filteredChats = new RealmList<>();
        for(GroupChatRealm chat : allGroups){
            if(chat.getCustomerRequestIds() == null || chat.getCustomerRequestIds().size() == 0) {
                if(chat.getGroupName() != null) {
                    filteredChats.add(chat);
                }
            }
        }
        return filteredChats;
    }
}
