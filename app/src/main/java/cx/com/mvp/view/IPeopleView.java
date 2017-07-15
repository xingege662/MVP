package cx.com.mvp.view;

import java.util.List;

import cx.com.mvp.bean.People;

/**
 * Created by xinchang on 2017/7/14.
 */

public interface IPeopleView {

    void  showloading();
    void showPeople(List<People> girls);
}
