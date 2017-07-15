package cx.com.mvp.present;

import java.util.List;

import cx.com.mvp.bean.People;
import cx.com.mvp.model.IPeopleModel;
import cx.com.mvp.model.PeopleModelImpl;
import cx.com.mvp.view.IPeopleView;


/**
 * Created by xinchang on 2017/7/14.
 */

public class PeoplePresenterImpl extends BasePresenter<IPeopleView> {

    IPeopleView  iGirlView;
    public PeoplePresenterImpl(IPeopleView iGirlView){
        this.iGirlView = iGirlView;
    }
    IPeopleModel peopleModel = new PeopleModelImpl();
    @Override
    public void fetch() {
        iGirlView.showloading();
        if(peopleModel!=null){
            peopleModel.loadPeople(new IPeopleModel.PeolpleOnLoadListener() {
                @Override
                public void onComplete(List<People> peoples) {
                    iGirlView.showPeople(peoples);
                }
            });
        }
    }
}
