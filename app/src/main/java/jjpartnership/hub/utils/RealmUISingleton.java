package jjpartnership.hub.utils;

import io.realm.Realm;

/**
 * Created by Jonathan on 3/16/2018.
 */

public class RealmUISingleton {
    private static final RealmUISingleton ourInstance = new RealmUISingleton();

    public static RealmUISingleton getInstance() {
        return ourInstance;
    }

    private Realm realm;

    private RealmUISingleton() {
    }

    public void initRealmInstance(){
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealmInstance(){
        return realm;
    }

    public void closeRealmInstance(){
        if(!realm.isClosed()) realm.close();
    }
}
