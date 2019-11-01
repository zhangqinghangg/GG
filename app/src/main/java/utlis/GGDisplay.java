package utlis;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geit.zzt.lang.io.ITInis;
import com.guoguang.ggbrowser.MainActivity;
import com.guoguang.ggbrowser.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static android.widget.Toast.LENGTH_SHORT;

public class GGDisplay extends Activity {

    public static GGDisplay instance;

    /*
     * 类： DislayActivity类，用于副屏的显示，可以单独成类，有的情况需要继承Activity类
     * 描述：定义副屏1，用来显示普通Android界面
     */


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static class DislayActivity extends Presentation {

        public static DislayActivity instance;
        TextView tos;
        EditText edc;
        EditText edr;
        private Disposable subscribe;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public DislayActivity(Context outerContext, Display display) {
            super(outerContext, display);

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fuping);
            instance = this;
        }

        /**
         * 描述：用于副屏显示Android系统的Activity，传参显示
         *
         * @param getclient 需要显示的特定字符串
         * @param getroom   需要显示的特定字符串
         * @param result    需要显示的特定字符串
         */
        public void androidDisplay(String getclient, String getroom, String result) {
            tos = (TextView) findViewById(R.id.text1);
            edc = (EditText) findViewById(R.id.editclient);
            edr = (EditText) findViewById(R.id.editroom);
            tos.setText(result);
            edc.setText(getclient);
            edr.setText(getroom);

        }

        /**
         * 描述：副屏的初始化函数，直接调用可显示副屏
         *
         * @param mcontext 需要显示的特定字符串
         * @param displays 存放副屏的数组，需要在要使用的Activity中定义，例：//在MainActivity中定义
         *                 DisplayManager manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
         *                 displayList = manager.getDisplays();
         * @param i        displays[0] 主屏，displays[1] 副屏
         */
        public static void display(Context mcontext, Display[] displays, int i) {
            DislayActivity dislayActivity = new DislayActivity(mcontext, displays[i]);
            dislayActivity.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dislayActivity.show();

        }

    }

    /*
     * 类： DislayH5类，用于副屏的显示，可以单独成类，有的情况需要继承Activity类
     * 描述：定义副屏2，用来显示普通WebView网页界面
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static class DislayH5 extends Presentation {
        public static DislayH5 instance;

        WebView xwv;
        String url_jiaohao = "file:///android_asset/build/index.html"; // 实例网页

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public DislayH5(Context outerContext, Display display) {
            super(outerContext, display);

        }

        /**
         * 描述：副屏的初始化函数，直接调用可显示副屏
         *
         * @param mcontext 需要显示的特定字符串
         * @param displays 存放副屏的数组，需要在要使用的Activity中定义，例：//在MainActivity中定义
         *                 DisplayManager manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
         *                 displayList = manager.getDisplays();
         * @param i        displays[0] 主屏，displays[1] 副屏
         */
        public static void display(MainActivity mcontext, Display[] displays, int i) {
            DislayH5 dislayH5 = new DislayH5(mcontext, displays[i]);
            dislayH5.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dislayH5.show();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_webview);
            xwv = findViewById(R.id.webshow);
            instance = this;
            initWebView();
            xwv.loadUrl(url_jiaohao);

        }

        /*
         * 描述：webview初始化
         */
        public void initWebView() {
            WebSettings webSettings = xwv.getSettings();// WebSettings类作用：对WebView进行配置和管理
            webSettings.setJavaScriptEnabled(true); // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setAllowFileAccess(true);// 允许启用或禁用WebView访问文件数据
            webSettings.setSupportZoom(true); // 支持缩放，默认为true。是下面那个的前提。
            webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
            webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
            webSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式
        }
    }

}