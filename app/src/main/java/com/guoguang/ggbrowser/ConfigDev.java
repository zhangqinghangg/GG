package com.guoguang.ggbrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geit.zzt.lang.io.ITInis;

import core.MyActivityManager;
import logUtils.GGLog4j;
import utlis.DataProcessingTool;


/**
 * @author zhangqinghang
 * 创建于 2019/8/7.
 * 描述：用于配置终端的界面
 */
public class ConfigDev extends Activity {

    //用于专门显示填入的配置信息
    private TextView tos;

    // 设备终端编辑框，屏幕类型编辑框，布局类型编辑框，声调编辑框，语速编辑框
    private EditText editdev, editscreen, editlayout, editpit, editspeed;

    // 设备终端，屏幕类型，布局类型，声调，语速
    private String editd, edits, editl, editp, editsp;

    //声调
    private int Pit;

    //语速
    private int speed;

    // 日志标签
    private static final String TAG = "安卓---ConfigDev";

    // 日志名称
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configdev);
        MyActivityManager.getInstance().addActivity(this);

        tos = (TextView) findViewById(R.id.text1);
        editdev = (EditText) findViewById(R.id.editclient);
        editscreen = (EditText) findViewById(R.id.editscreen);
        editlayout = (EditText) findViewById(R.id.editlayout);
        editpit = (EditText) findViewById(R.id.editpit);
        editspeed = (EditText) findViewById(R.id.editspeed);
        get();
        // 如果缓存的内容为空，进行编辑框初始化
        if (String.valueOf(editdev.getText()).equals("")) {
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
        editdev.setText("");
        editscreen.setText("");
        editlayout.setText("");
        editpit.setText("");
        editspeed.setText("");

    }

    // 返回按键，返回主界面
    public void btn_back(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    // 按键调节声调、语速的大小
    public void click(View v) {
        switch (v.getId()) {
            // 声调减
            case R.id.jian1:
                Pit = Pit - 1;
                ifmethod(editpit, Pit, 1, 10, 1);
                break;
            // 语速减
            case R.id.jian2:
                speed = speed - 1;
                ifmethod(editspeed, speed, 1, 15, 1);
                break;
            // 声调加
            case R.id.jia1:
                Pit = Pit + 1;
                ifmethod(editpit, Pit, 1, 10, 2);
                break;
            // 语速加
            case R.id.jia2:
                speed = speed + 1;
                ifmethod(editspeed, speed, 1, 15, 2);
                break;
        }

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

    //初始化编辑框的内容
    public void init() {
        editdev.setText("dev10.1.2");
        editscreen.setText("01");
        editlayout.setText("A");
        editpit.setText("5");
        editspeed.setText("5");

    }

    // 将编辑框的内容保存到缓存
    public void set() {
        editp = String.valueOf(editpit.getText());
        editsp = String.valueOf(editspeed.getText());
        editd = String.valueOf(editdev.getText());
        edits = String.valueOf(editscreen.getText());
        editl = String.valueOf(editlayout.getText());
        if (editd.equals("") || edits.equals("") || editl.equals("")) {
            tos.setText("提醒：配置文件不能为空！！");
        } else {
            String cfg = getCacheDir().getAbsolutePath() + "//devconfig.ini";
            DataProcessingTool Dpt = new DataProcessingTool();
            ITInis.set(cfg, "设备终端号", "devId", editd, "UTF-8");
            ITInis.set(cfg, "设备终端号", "screen", edits, "UTF-8");
            ITInis.set(cfg, "设备终端号", "layout", editl, "UTF-8");
            ITInis.set(cfg, "语音", "pitch", editp, "UTF-8");
            ITInis.set(cfg, "语音", "speed", editsp, "UTF-8");
            Dpt.putCache(ConfigDev.this, cfg, "配置文件", "devId" + ":" + editd + "&" + "type" + ":" + edits + "&" + "layout" + ":" + editl, "UTF-8");
            tos.setText("保存地址：" + cfg + "\n" + "设备终端：" + editd + "\n" + "屏幕类型：" + edits + "\n" + "布局类型：" + editl);
        }
    }

    // 获取缓存的数据，并在编辑框进行相应的显示
    public void get() {
        String cfg = getCacheDir().getAbsolutePath() + "//devconfig.ini";
        editdev.setText(ITInis.get(cfg, "设备终端号", "devId", "", "UTF-8"));
        editscreen.setText(ITInis.get(cfg, "设备终端号", "screen", "", "UTF-8"));
        editlayout.setText(ITInis.get(cfg, "设备终端号", "layout", "", "UTF-8"));
        editpit.setText(ITInis.get(cfg, "语音", "pitch", "", "UTF-8"));
        editspeed.setText(ITInis.get(cfg, "语音", "speed", "", "UTF-8"));
    }

    // 对声调、语速的设置范围进行控制
    public void ifmethod(EditText edit, int ps, int i, int j, int flag) {
        if (ps > j || ps < i) {
            if (flag == 1) {
                edit.setText(i + "");
                Toast.makeText(this, "请输入" + i + "-" + j + "的数字", Toast.LENGTH_LONG).show();
            } else {
                edit.setText(j + "");
                Toast.makeText(this, "请输入" + i + "-" + j + "的数字", Toast.LENGTH_LONG).show();
            }
        } else {
            edit.setText(ps + "");
        }
    }


}
