package utlis;

import android.app.Presentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.guoguang.ggbrowser.MainActivity;
import com.guoguang.ggbrowser.R;

import io.reactivex.disposables.Disposable;

public class H5Display extends Presentation {

    WebView xwv;
    String url_jiaohao = "file:///android_asset/build/index.html"; // 实例网页
    String URL = "http://192.168.8.118:33127/api/queue/sign"; // 后台数据接口
    private static final String TAG = "SystemWebview";
    public static H5Display instance = null;
    private Disposable subscribe;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public H5Display(Context outerContext, Display display) {
        super(outerContext, display);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 去掉Activity上面的状态栏，要加在setContentView()之前才有效果
        setContentView(R.layout.activity_webview);
        xwv = findViewById(R.id.webshow);
        instance = this;
        initWebView();
        xwv.loadUrl(url_jiaohao);

        //调用推送数据的轮询功能
        RXjava Pf =new RXjava();
        Pf.pollingFunctionReturnData(xwv);
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
        H5Display h5Display = new H5Display(mcontext, displays[i]);
        h5Display.getWindow().setType(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        h5Display.show();
    }
    /**
     * 描述: webview初始化
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


        //webview设置WebViewClient
        xwv.setWebViewClient(new WebViewClient() {
            @Override
            // 加载网页需要重定向的时候回调
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  // 点击超链接的时候重新在原来进程上加载URL
                return true;
            }

            // 网页加载完之后的运行操作
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("yirong610", "onPageFinished完成网页加载");
                // 线程1：用于接收科室id数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("yirong610", "接收科室id数据线程1执行成功");
                        //String roomid = PayHttp.GetSingleCabCollect(URL, "idDev", "dev6.1.3");
                        //Log.i(TAG, roomid);
                    }
                }).start();

                // 线程2：用于接收rabbitmq的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "线程2执行成功");
                        // Receiver.basicConsume("dev6.1.3");
                    }
                }).start();
            }
        });
        xwv.loadUrl(url_jiaohao);

    }
}
