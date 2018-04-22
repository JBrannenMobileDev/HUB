package jjpartnership.hub.utils;

/**
 * Created by jbrannen on 5/19/17.
 */

public interface TwoResponseCallback<T, S> {
    void onResponse(T object1, S object2);
    void onFailure(Exception e);
}
