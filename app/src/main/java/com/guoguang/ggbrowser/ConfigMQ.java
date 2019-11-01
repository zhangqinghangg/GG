package com.guoguang.ggbrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geit.zzt.lang.io.ITInis;

import core.MyActivityManager;
import logUtils.GGLog4j;
import utlis.DataProcessingTool;


/**
 * @author zhangqinghang
 * 创建于 2019/8/7.
 * 描述：用于配置MQ的界面
 */
public class ConfigMQ extends Activity {

    // URL地址编辑框，IP地址编辑框，串口编辑框，MQ登录账号编辑框，MQ登录密码编辑框，转发器编辑框
    private EditText editURL, editIP, editPort, editMQname, editMQpassword, editExchange;

    // URL地址，IP地址，串口，MQ登录账号，MQ登录密码，转发器
    private String eURL, eIP, ePort, eMQname, eMQpassword, eExchange;

    //用于专门显示填入的配置信息
    private TextView tos;

    // 日志标签
    private static final String TAG = "安卓---ConfigMQ";

    // 日志名称
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configmq);
        MyActivityManager.getInstance().addActivity(this);
        tos = (TextView) findViewById(R.id.text1);
        editURL = (EditText) findViewById(R.id.editURL);
        editIP = (EditText) findViewById(R.id.editIP);
        editPort = (EditText) findViewById(R.id.editPort);
        editMQname = (EditText) findViewById(R.id.editMQname);
        editMQpassword = (EditText) findViewById(R.id.editMQpassword);
        editExchange = (EditText) findViewById(R.id.editexchange);
        get();
        // 如果缓存的内容为空，进行编辑框初始化
        if (String.valueOf(editURL.getText()).equals("")) {
            init();
        } else {
            set();
        }
    }


    //================================ 按键函数 ====================================//

    // 确认按键，用于保存编辑的配置信息
    public void btnHandler_ok(View v) {
        set();
    }

    // 重置按键
    public void btnHandler_reset(View v) {
        editURL.setText("");
        editIP.setText("");
        editPort.setText("");
        editMQname.setText("");
        editMQpassword.setText("");
        editExchange.setText("");
    }


    // 返回按键，返回主界面
    public void btn_back(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    //按键触发举动
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_UP://键盘松开ACTION_UP
            case KeyEvent.ACTION_DOWN: //键盘按下KeyEvent.ACTION_DOWN
                if (keyCode == KeyEvent.KEYCODE_BACK) {//获取物理键盘返回键
                    try {
                        Intent newWindow = new Intent(this, MainActivity.class);//跳转界面
                        startActivity(newWindow);
                    } catch (Exception ex) {
                        logger.e(TAG, "返回按键异常" + ex.getMessage());
                    }
                }
        }
        return false;
    }

    //================================ 调用函数 ====================================//

    //初始化MQ配置文件
    public void init() {
        editURL.setText("http://jeremyda.cn:8019");
        editIP.setText("106.12.24.238");
        editPort.setText("5672");
        editMQname.setText("guest");
        editMQpassword.setText("yangsu");
        editExchange.setText("GG_EXCHANGE_NAME");
    }

    // 获取缓存的数据，并在编辑框进行相应的显示
    public void get() {
        String cfg = getCacheDir().getAbsolutePath() + "//mqconfig.ini";
        editURL.setText(ITInis.get(cfg, "MQ配置", "URL", "", "UTF-8"));
        editIP.setText(ITInis.get(cfg, "MQ配置", "IP", "", "UTF-8"));
        editPort.setText(ITInis.get(cfg, "MQ配置", "Port", "", "UTF-8"));
        editMQname.setText(ITInis.get(cfg, "MQ配置", "MQname", "", "UTF-8"));
        editMQpassword.setText(ITInis.get(cfg, "MQ配置", "MQpassword", "", "UTF-8"));
        editExchange.setText(ITInis.get(cfg, "MQ配置", "Exchange", "", "UTF-8"));

    }

    // 将编辑框的内容保存到缓存
    public void set() {
        eURL = String.valueOf(editURL.getText());
        eIP = String.valueOf(editIP.getText());
        ePort = String.valueOf(editPort.getText());
        eMQname = String.valueOf(editMQname.getText());
        eMQpassword = String.valueOf(editMQpassword.getText());
        eExchange = String.valueOf(editExchange.getText());
        if (eURL.equals("") || eIP.equals("") || ePort.equals("") || eMQname.equals("") || eMQpassword.equals("") || eExchange.equals("")) {
            tos.setText("配置文件不能为空");
        } else {
            String cfg = getCacheDir().getAbsolutePath() + "//mqconfig.ini";
            DataProcessingTool Dpt = new DataProcessingTool();
            ITInis.set(cfg, "MQ配置", "URL", eURL, "UTF-8");
            ITInis.set(cfg, "MQ配置", "IP", eIP, "UTF-8");
            ITInis.set(cfg, "MQ配置", "Port", ePort, "UTF-8");
            ITInis.set(cfg, "MQ配置", "MQname", eMQname, "UTF-8");
            ITInis.set(cfg, "MQ配置", "MQpassword", eMQpassword, "UTF-8");
            ITInis.set(cfg, "MQ配置", "Exchange", eExchange, "UTF-8");
            Dpt.putCache(ConfigMQ.this, cfg, "配置文件", "URL" + ":" + eURL + "&" + "IP" + ":" + eIP + "&" + "Port" + ":" + ePort + "&" + "MQname" + ":" + eMQname + "&" + "MQpassword" + ":" + eMQpassword + "&" + "Exchange" + ":" + eExchange, "UTF-8");
            tos.setText("配置文件:" + "\n" + "URL" + ":" + eURL + "\n" + "IP" + ":" + eIP + "\n" + "Port" + ":" + ePort + "\n" + "MQname" + ":" + eMQname + "\n" + "MQpassword" + ":" + eMQpassword + "\n" + "Exchange" + ":" + eExchange);
            logger.i(TAG, "你的文件保存地址为：" + cfg + "\n" + "配置文件:URL" + ":" + eURL + "&" + "IP" + ":" + eIP + "&" + "Port" + ":" + ePort + "&" + "MQname" + ":" + eMQname + "&" + "MQpassword" + ":" + eMQpassword + "&" + "Exchange" + ":" + eExchange);
        }
    }
}
