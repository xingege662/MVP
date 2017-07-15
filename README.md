# MVP
[TOC]
## 序言
一年前，接触了自己的第一个MVP的Android项目，当时仔细研究过一段时间这种架构，但是后来一直负责的项目都是基于MVC，现在都要忘记了，现在把他记录下来。
## 背景
### 什么叫MVP,为什么要用MVP？
15年实习的时候，很幸运开始从0开始做第一个项目，老大把框架搭建起来，后来开始写业务逻辑代码，当时所在的公司是做电商的，需求那就是3个字，改，改，改，Activity的代码上千行已经习以为常，这样的代码变得非常臃肿，难以维护。

那么什么叫MVP？就是Modle——View——Presenter，看到这个概念很懵逼，先甩一张图看一下和普通MVC的区别：
![image](http://opy4iwqsf.bkt.clouddn.com/WX20170715-163924@2x.png)
从图中可以很明显的看到MVC和MVP的区别，MVP消除了View和Model之间的相互依赖，中间通过Presenter来通讯，解耦合。

## MVC和MVP各个部分在干什么？
MVC和MVP两个单词只差了一个字母，但是两种处理方式上改变得太多。
### MVC三个部分分别在干什么
- View：对应于布局文件，但是细细的想一想这个View对应于布局文件，其实能做的事情特别少，实际上关于布局文件中的数据绑定的操作，事件处理的操作都在Activity中，造成了Activity既像View又像Controller。
- Model:业务逻辑和实体模型
- Controller:对应于Activity
### MVP三个部分分别在干什么
- View:对应于Activity，负责View的绘制以及用户交互
- Model:业务逻辑和实体逻辑
- Presenter：负责完成View与Model之间的交互

## 写一个MVP的demo
- 定义Modle
1.定义bean类：

```
public class People {
    public int icon;
    public String like;
    public String style;

    public int getIcon() {
        return icon;
    }

    public String getLike() {
        return like;
    }

    public String getStyle() {
        return style;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "Girl{" +
                "icon=" + icon +
                ", like='" + like + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
```
2.定义Model接口：

```
public interface IPeopleModel {
    void loadPeople(PeolpleOnLoadListener girlOnLoadListener);
    interface PeolpleOnLoadListener{
        void onComplete(List<People> girls);
    }
}
```
model处理数据逻辑：

```
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
```
- 定义Presenter
从上面的图中可以看到，Presenter依赖了Model，也依赖了View，所以应该持有他们的引用。因为项目中会有很多的Presenter需要去编写，也有很多的代码会重复，所以把这些共有的东西进行封装，放到BasePresenter中。

1.编写基类Presenter

```
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

```
2.编写具体的Presenter

```
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

```
- 定义View。

1.先确定我们的View中干什么，定义功能接口

```
public interface IPeopleView {

    void  showloading();
    void showPeople(List<People> girls);
}
```
2.因为View要依赖Presenter，所以我们每个Activity肯定会持有一份Presenter的引用，所以在Activity销毁的时候要释放，这些操作放到BaseActivity中。

```
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
```
3.编写具体的Activity

```
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

```
OK，MVP架构的练手项目就已经写完了。可以看到MVP模式中，Activity的代码会少很多。只是在展示，数据的处理和业务放在了model和presenter中。
## 效果图：
![image](http://opy4iwqsf.bkt.clouddn.com/2017-07-15%2018.30.27.gif)
## 归纳(套路）
现在，每个人应该都听过一个词，叫套路，没错，做任何事情都有套路，把套路搞清楚了，再复杂的东西变得简单一些，总结一下编写MVP模式的套路：
- View：负责绘制UI元素，与用户进行交互，也就是我们的Activity。他可以从模型中读取数据，但是不能修改或更新模型。view提供提供UI交互，在presenter的控制下修改UI，将业务事件交由presenter处理，注意: View层不存储数据，不与Model层交互，在Android中View层一般是Activity、Fragment、View（控件）、ViewGroup（布局等）等
- Activity interface:需要View实现的接口，View通过View interface与Presenter进行交互，降低耦合，方便进行单元测试
- Model:负责存储、检索、操纵数据(有时也实现一个Model interface用来降低耦合);表示数据模型和业务逻辑(business logic)。模型并不总是DataSet，DataTable之类的东西，它代表着一类组件(components)或类(class)，这些组件或类可以向外部提供数据，同时也能从外部获取数据并将这些数据存储在某个地方。简单的理解，可以把模型想象成“外观类(facade class)。职责就是从网络，数据库，文件，传感器，第三方等数据源读写数据， 对外部的数据类型进行解析转换为APP内部数据交由上层处理，对数据的临时存储,管理，协调上层数据请求。
- Presenter：作为View与Model交互的中间纽带，处理与用户交互的负责逻辑。

## MVP的好处
- 减少了Activity的职责，简化了Activity中的代码，将复杂的逻辑代码提取到了Presenter中进行处理。与之对应的好处就是，耦合度更低
- Activity代码变得更加整洁，使用MVP之后，Activity就能瘦身许多了，基本上只有FindView、SetListener以及Init的代码。其他的就是对Presenter的调用，还有对View接口的实现。这种情形下阅读代码就容易多了，而且你只要看Presenter的接口，就能明白这个模块都有哪些业务，很快就能定位到具体代码。Activity变得容易看懂，容易维护，以后要调整业务、删减功能也就变得简单许多。
- 方便进行单元测试，般单元测试都是用来测试某些新加的业务逻辑有没有问题，如果采用传统的代码风格（习惯性上叫做MV模式，少了P），我们可能要先在Activity里写一段测试代码，测试完了再把测试代码删掉换成正式代码，这时如果发现业务有问题又得换回测试代码，咦，测试代码已经删掉了！好吧重新写吧……

MVP中，由于业务逻辑都在Presenter里，我们完全可以写一个PresenterTest的实现类继承Presenter的接口，现在只要在Activity里把Presenter的创建换成PresenterTest，就能进行单元测试了，测试完再换回来即可。万一发现还得进行测试，那就再换成PresenterTest吧。
## MVP的坏处
接手的MVP的大项目中，有时候层层回调让我不知所云，虽然代码的耦合降低，但是让人一看，会有很懵逼的感觉，代码写太多了，没有MVC好上手。不知道大家有没有这种感觉。哈哈！！
## 总结
没有最好的架构，只有最合适的架构，虽然MVP架构很好，但是在小项目中，MVC就已经很好的支持项目的开发，没必要用MVP。在大项目中可以考虑MVP的架构。搞定，收工。

[源码点击下载](https://github.com/xingege662/MVP)
