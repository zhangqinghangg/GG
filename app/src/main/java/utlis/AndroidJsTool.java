package utlis;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.geit.zzt.lang.ITStrings;
import com.guoguang.ggbrowser.MainActivity;

import org.nutz.lang.Strings;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import logUtils.GGLog4j;

public class AndroidJsTool extends Activity {

    // 日志标签
    private static final String TAG = "安卓---AndroidJsTool";

    //日志名称
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");
    private WebView xwv;
    public static AndroidJsTool instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @JavascriptInterface
    public void jsCallAndroid(String params) {
        Log.i("yirong610", "jsCallAndroid 参数 = " + params);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void androidCallJs(String methodName, String params) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        if (Strings.isEmpty(params)) {
            params = "";
        } else {
            params = "\"" + params + "\"";
        }
        // 因为evaluateJavascript在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            // 用loadURL调用js函数时，不能有返回值
            xwv.loadUrl(ITStrings.format("javascript:{}({})", methodName, params));
        } else {
            xwv.evaluateJavascript(ITStrings.format("javascript:{}({})", methodName, params), new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("yirong610", "js的返回值 = " + value);
                }
            });
        }
    }

    /**
     * 描述: android调用js，向js推送数据,在Receiver类中进行了调用
     *
     * @param str str的格式为："javascript:javaCallJS('" + data + "')"，data为与js约定的格式
     */
    public void callJs(final String str) {
        logger.i(TAG, "准备调用JS函数" + str);
        // RxJava的流式操作
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
            }
        })
                .subscribe(new Observer<Integer>() {
                    // 2. 通过通过订阅（subscribe）连接观察者和被观察者
                    // 3. 创建观察者 & 定义响应事件的行为
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe（）

                    @Override
                    public void onNext(Integer value) {
                        try {
                            logger.i(TAG, "UI线程执行,准备加载js函数");
                            MainActivity.instance.mWebView.loadUrl(str);
                        } catch (Exception e) {
                            logger.e(TAG, "UI线程更新异常" + e.getMessage());
                        }
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
