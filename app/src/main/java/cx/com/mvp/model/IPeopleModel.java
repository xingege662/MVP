package cx.com.mvp.model;

import java.util.List;

import cx.com.mvp.bean.People;

/**
 * Created by xinchang on 2017/7/14.
 */

public interface IPeopleModel {
    void loadPeople(PeolpleOnLoadListener girlOnLoadListener);
    interface PeolpleOnLoadListener{
        void onComplete(List<People> girls);
    }
}
