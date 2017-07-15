package cx.com.mvp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cx.com.mvp.adapter.GirlListAdapter;
import cx.com.mvp.bean.People;
import cx.com.mvp.present.PeoplePresenterImpl;
import cx.com.mvp.view.IPeopleView;

public class MainActivity extends BaseActivity<PeoplePresenterImpl> implements IPeopleView {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v1);
        listView = (ListView) findViewById(R.id.listview);
        mPresent.fetch();
    }

    @Override
    public PeoplePresenterImpl createPresent() {
        return new PeoplePresenterImpl(this);
    }

    @Override
    public void showloading() {
        Toast.makeText(this,"正在拼命加载",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPeople(List<People> girls) {
        listView.setAdapter(new GirlListAdapter(this,girls));
    }
}
