package cx.com.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cx.com.mvp.present.BasePresenter;

/**
 * Created by xinchang on 2017/7/14.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected  T mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = createPresent();
        mPresent.attachView(this);
    }

    @Override
    protected void onDestroy() {
        mPresent.detach();
        super.onDestroy();
    }

    public abstract T createPresent() ;
}
