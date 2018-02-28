package jjpartnership.hub.view_layer.activities.main_activity;

/**
 * Created by jbrannen on 2/25/18.
 */

public class MainPresenterImp implements MainPresenter {
    private MainView activity;

    public MainPresenterImp(MainView activity){
        this.activity = activity;
    }

    @Override
    public void onSearchQuery(String newText) {

    }
}
