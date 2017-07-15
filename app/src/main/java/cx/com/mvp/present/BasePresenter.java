package cx.com.mvp.present;

import java.lang.ref.WeakReference;

/**
 * Created by xinchang on 2017/7/14.
 */

public abstract class BasePresenter<T> {
    protected WeakReference<T> mViewRef;
    public abstract void fetch();
    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }

    public void detach(){
        if(mViewRef!=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
