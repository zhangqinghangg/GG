package com.guoguang.ggbrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebSettings;

import core.GlobalFunction;
import core.MyActivityManager;
import logUtils.GGLog4j;
import utlis.JsApi;
import utlis.X5WebView;


public class MainActivity extends Activity {

    //系统文字转语音
    public TextToSpeech tts;

    // 显示屏（开启副屏时使用）
    public Display[] displayList;

    //X5浏览器内核
    public X5WebView mWebView;

    // 计算退出时间
    private long exitTime = 0;

    //显示倒计时和轮询效果
    public TextView tv;
    private TextView tv2;

    //日志标签
    private static final String TAG = "安卓---MainActivity";

    //日志保存文件名
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");

    //APP缓存文件名
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    //Mainactivity实例
    public static com.guoguang.ggbrowser.MainActivity instance = null;

    //数据存储
    private SharedPreferences.Editor editor;

    // android浏览器页面
    private String url_html = "file:///android_asset/demo2.html";

    // 黑龙江北安农垦医院排队机实例网页
//     private String url_html = "http://www.ggzzrj.cn:8080/ued/test/android/index.html";

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager.getInstance().addActivity(this);
        // 去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉Activity上面的状态栏，要加在setContentView()之前才有效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        instance = this;
        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);

        //用于双屏异显，获取屏幕的个数
        DisplayManager manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayList = manager.getDisplays();

        //加载X5WebView
        mWebView = (X5WebView) findViewById(R.id.webview_a);
        X5Initwebview();

    }


    /**
     * 描述：X5WebView进行初始化配置和管理
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void X5Initwebview() {

        //清除缓存
        mWebView.clearCache(true);
        // DataProcessingTool Dpt = new DataProcessingTool();
        // Dpt.clearWebViewCache1();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAppCacheEnabled(false);
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        // 映射JS接口（专门的类）
        JsApi jsapi = new JsApi(MainActivity.this);

        //tts初始化
        jsapi.ttsIni();

        // 通过WebView的addJavascriptInterface方法去注入一个我们自己写的interface（接口）
        mWebView.addJavascriptInterface(jsapi, "ggie");

        // 加载页面
        mWebView.loadUrl(url_html);
    }



    //================================ 普通函数 ====================================//


    /**
     * 描述: android调用js，向js推送数据,在Receiver类中进行了调用
     *
     * @param str str的格式为："javascript:javaCallJS('" + data + "')"，data为与js约定的格式
     */
    public void callJs(final String str) {

        // 使用UI线程更新UI显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.i(TAG, "======Web传MQ数据函数======");
                    mWebView.loadUrl(str);
                    logger.i(TAG, "getQueue（）函数调用成功");
                } catch (Exception e) {
                    logger.e(TAG, "调用Web传MQ数据函数异常" + e.getMessage());
                }
            }
        });
    }

    /**
     * 描述: android调用js，向js推送数据,在Receiver类中进行了调用
     *
     * @param str str的格式为："javascript:javaCallJS('" + data + "')"，data为与js约定的格式
     */
    public void callJsSpeech(final String str) {

        // 使用UI线程更新UI显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.i(TAG, "=======Web语音结束函数=====");
                    mWebView.loadUrl(str);
                    logger.i(TAG, "speachOver()函数调用成功");
                } catch (Exception e) {
                    logger.e(TAG, "调Web语音结束函数异常" + e.getMessage());
                }
            }
        });
    }

    /**
     * 描述：右击鼠标1次，弹出参数配置对话框，连续右击鼠标2次，退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (event.getAction() == KeyEvent.ACTION_DOWN) // 按下状态
            {
                if (keyCode == KeyEvent.KEYCODE_BACK) // 回退键
                {
                    if (System.currentTimeMillis() - this.exitTime > 2000) {
                        AlertDialog();
                    } else {
                        GlobalFunction.ExitApp(true);
                    }
                    return true;
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, "发生异常：" + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }


    //================================ Android界面操作函数 ====================================//

    /**
     * 描述：用于配置管理的弹出窗口
     */
    public void AlertDialog() {
        Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
        this.exitTime = System.currentTimeMillis();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dailog, null);
        //  将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题
        Button btn_zhuye = (Button) view.findViewById(R.id.btn_zhuye);
        Button btn_tuichu = (Button) view.findViewById(R.id.btn_tuichu);
        Button btn_MQconfig = (Button) view.findViewById(R.id.btn_MQconfig);
        Button btn_devconfig = (Button) view.findViewById(R.id.btn_devconfig);
        Button btn_update = (Button) view.findViewById(R.id.btn_update);
        //跳转主页界面
        btn_zhuye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        //退出应用程序
        btn_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
                GlobalFunction.ShowPrompt(MainActivity.this, "系统提示", "\n确实要关闭应用么？\n", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GlobalFunction.ExitApp(true);
                    }
                }, "取消", null);
                dialog.dismiss();// 隐藏dialog
            }
        });
        //跳转MQ配置界面
        btn_MQconfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
                final View inputDialog = getLayoutInflater().inflate(R.layout.self_input_password, null);
                GlobalFunction.ShowPrompt(MainActivity.this, inputDialog, "管理员登录", null, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        login(MainActivity.this, inputDialog, 2);
                    }
                }, "取消", null, "注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();// 隐藏dialog
                        final View inputDialog = getLayoutInflater().inflate(R.layout.self_input_password, null);
                        GlobalFunction.ShowPrompt(MainActivity.this, inputDialog, "注册新密码", null, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                regist(inputDialog);
                            }
                        }, "取消", null, "", null);
                    }
                });
            }
        });
        //跳转设备终端号配置界面
        btn_devconfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
                final View inputDialog = getLayoutInflater().inflate(R.layout.self_input_password, null);
                GlobalFunction.ShowPrompt(MainActivity.this, inputDialog, "管理员登录", null, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        login(MainActivity.this, inputDialog, 1);
                    }
                }, "取消", null, "注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();// 隐藏dialog
                        final View inputDialog = getLayoutInflater().inflate(R.layout.self_input_password, null);
                        GlobalFunction.ShowPrompt(MainActivity.this, inputDialog, "注册新密码", null, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                regist(inputDialog);
                            }
                        }, "取消", null, "", null);
                    }
                });
            }
        });
        //跳转帮助界面
        btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    /**
     * 描述：注册密码函数
     */
    public void regist(View inputDialog) {
        EditText et = (EditText) inputDialog.findViewById(R.id.et_input_password);
        String editpwd = et.getText().toString();
        if (editpwd.length() < 6) {
            Toast.makeText(MainActivity.this, "请输入六位正确的数字或字母", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            editor.putString("password", et.getText().toString());
            editor.apply();
            editor = pref.edit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 描述：登录函数
     */
    public void login(Context context, View inputDialog, int flag) {
        EditText et = (EditText) inputDialog.findViewById(R.id.et_input_password);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String password = pref.getString("password", "");
        editor = pref.edit();
        if (et.getText().toString().equals(password)) {
            Intent intent = new Intent();
            if (flag == 1) {
                intent.setClass(context, ConfigDev.class);
            }
            if (flag == 2) {
                intent.setClass(context, ConfigMQ.class);
            }
            startActivity(intent);

        } else {
            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    //================================ Activity周期操作函数 ====================================//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.i(TAG, "---------- 准备退出 ----------");
        //清除缓存
//        mWebView.clearCache(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger.i(TAG, "---------- onStart -----------");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logger.i(TAG, "---------- onRestart ----------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.i(TAG, "---------- onResume -----------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger.i(TAG, "----------- onPause -----------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger.i(TAG, "---------- onStop -----------");
    }

}

