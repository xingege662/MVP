package cx.com.mvp.model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import cx.com.mvp.R;
import cx.com.mvp.bean.People;

/**
 * Created by xinchang on 2017/7/14.
 */

public class PeopleModelImpl implements IPeopleModel {
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void loadPeople(final PeolpleOnLoadListener peopleOnLoadListener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final List<People> data = new ArrayList<People>();
                data.add(new People(R.drawable.f1, "五颗星", "美女1"));
                data.add(new People(R.drawable.f2, "四颗星", "美女2"));
                data.add(new People(R.drawable.f3, "五颗星", "美女3"));
                data.add(new People(R.drawable.f4, "三颗星", "美女4"));
                data.add(new People(R.drawable.f5, "五颗星", "美女5"));
                data.add(new People(R.drawable.f6, "三颗星", "美女6"));
                data.add(new People(R.drawable.f7, "四颗星", "美女7"));
                data.add(new People(R.drawable.f8, "五颗星", "美女8"));
                data.add(new People(R.drawable.f9, "四颗星", "美女9"));
                data.add(new People(R.drawable.f10, "三颗星", "美女10"));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //回调
                        peopleOnLoadListener.onComplete(data);
                    }
                });
            }
        }.start();
    }
}
