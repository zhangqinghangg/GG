package utlis;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.guoguang.ggbrowser.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class RXjava extends Activity {

    private Disposable subscribe;
    public RXjava instance = null;
    DataProcessingTool Dpt = new DataProcessingTool();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

    }

    /**
     * 描述:用于轮询将数据写入缓存
     */
    public void pollingFunctionReturnData(final Context context) {

        //interval对应参数 ：首次执行延时时间 、 每次轮询间隔时间 、 时间类型
        subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String returnData = "第" + aLong + "次轮询#结果：" + new AppVersion().getDeviceInfo();
                        String returnData2 = "第" + aLong + "次轮询#结果：" + new AppVersion().getAppVersion(context);
                        Dpt.putCache(context,"browser", "医院", "终端号:" + aLong + "&科室号:" + aLong + "&病人:" + aLong, "utf-8");
                        Dpt.putReturnData(context,"数据1",returnData);
                        Dpt.putReturnData(context,"数据2",returnData2);
                        MainActivity.instance.tv.setText("写入缓存：终端号:" + aLong + "&科室号:" + aLong + "&病人:" + aLong);
                    }
                });
    }

    /**
     * 描述:用于向web网页轮询插入数据
     */
    public void pollingFunctionReturnData(final WebView xwv) {
        subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("zqh", "写入数据成功");
                        String data = "Trade9" + "|" + String.valueOf(aLong) +"骨科"+ "|" + "张三";
                        xwv.loadUrl("javascript:javaCallJS('" + data + "')");
                    }
                });
    }

    /**
     * 描述:用于向js轮询插入数据，用于MainActivity
     */
    public void pollingFunctionReturnData2(final X5WebView xwv) {
        subscribe =   Observable.intervalRange(3,10,2, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("zqh", "写入数据成功");
                        String data = "Trade9" + "|" + String.valueOf(aLong) + "|" + "张三";
                        xwv.loadUrl("javascript:javaCallJS('" + data + "')");
                    }
                });


    }

    /**
     * 描述:用于异步操作，RXjava
     */
    public void RXjava(final X5WebView xwv , final TextView tv) {
    // RxJava的流式操作
        Observable.create(new ObservableOnSubscribe<String>() {
        // 1. 创建被观察者 & 生产事件
        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            emitter.onNext("1234567i");
            emitter.onComplete();
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<String>() {
        // 2. 通过通过订阅（subscribe）连接观察者和被观察者
        // 3. 创建观察者 & 定义响应事件的行为
        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "开始采用subscribe连接");
        }
        // 默认最先调用复写的 onSubscribe（）

        @Override
        public void onNext(String value) {
            tv.setText(value);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "对Error事件作出响应");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "对Complete事件作出响应");
        }

    });


    }


}
