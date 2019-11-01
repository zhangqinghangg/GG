package com.slipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guoguang.ggbrowser.R;

//import android.app.Activity;
//import android.app.AlertDialog;
//
//import android.os.Bundle;
//import android.os.Handler;


public class Test extends Activity implements OnClickListener {
    public static final String mTAG = "slMisTest.MainActivity";
    public static final String m_strXMLHead = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
    public static final String m_strQryPayURL = m_strXMLHead + "<Singlee><TransId>76</TransId><Tramt>000000000001</Tramt><TranSerial>AA-20191028125228</TranSerial><PayFlag>AL</PayFlag></Singlee>";
    public static final String m_strQryPayResult = m_strXMLHead + "<Singlee><TransId>77</TransId><TranSerial>AA-20191028103556</TranSerial></Singlee>";
    public static final String m_strCloseOrder = m_strXMLHead + "<Singlee><TransId>82</TransId><TranSerial>AA-20191028125112</TranSerial></Singlee>";

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private TextView m_tvResponse;
    private Button m_btnQryURL = null;
    private Button m_btnQryPayResult = null;
    private Button m_btnCloseOrder = null;

    private MainThreadHandler handler = null;


    private static final String m_strDlgMsg[] = {
            "snLogin", "正在签到", null,
            "snDownloadInfo", "下载银行卡参数", null,
            "snReadCard", "正在读卡", null,
            "snWaitCard", "请插卡，挥卡或刷卡", null,


            "bpGetEncodePin", "请输入密码", null,

            null, null, null
    };

    private static AlertDialog m_pDlgHandler = null;
    private static String m_strActCallbackId = null;

    private class MainThreadHandler extends Handler {

        private MainThreadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProcessThreadMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        m_btnQryURL = (Button) findViewById(R.id.btnQryURL);
        m_btnQryPayResult = (Button) findViewById(R.id.btnQryPayResult);
        m_btnCloseOrder = (Button) findViewById(R.id.btnCloseOrder);
        m_btnQryPayResult.setOnClickListener(this);
        m_btnCloseOrder.setOnClickListener(this);
        m_btnQryURL.setOnClickListener(this);

        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);

        m_tvResponse = (TextView) findViewById(R.id.idResponse);

        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);


        handler = new MainThreadHandler(getMainLooper());
        slMisTran.SetMainHandle(handler);

        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQryURL: {
                m_tvResponse.setText("");
                slMisTran mis = new slMisTran();
                mis.CardTransEx(m_strQryPayURL);
            }
            break;
            case R.id.btnQryPayResult: {
                m_tvResponse.setText("");
                slMisTran mis = new slMisTran();
                mis.CardTransEx(m_strQryPayResult);
            }
            break;
            case R.id.btnCloseOrder: {
                m_tvResponse.setText("");
                slMisTran mis = new slMisTran();
                mis.CardTransEx(m_strCloseOrder);
            }
            break;
            case R.id.btnExit: {
                Log.i(mTAG, "begin exit");
                OnExitDialog();
            }
            break;
        }
    }

    private void HandleCallback(String strId, slCallbackInfo info) {
        m_strActCallbackId = strId;
        int nCallbackResult = -9999;

        // 输入密码，是一个特殊的回调，会在设备对象中阻塞。
        if (strId.equalsIgnoreCase("bzPinEcho")) {
            String strTitle = "请输入密码";

            try {
                byte[] byInfo = info.GetTagValue(0);
                String strInfo = new String(byInfo, "GBK").trim();

                if (strInfo.equalsIgnoreCase("S")) {
                    if (m_pDlgHandler != null) {
                        m_pDlgHandler.dismiss();
                        m_pDlgHandler = null;
                    }

                    m_pDlgHandler = new AlertDialog.Builder(this).setTitle(strTitle).setMessage("").create();
                    m_pDlgHandler.show();
                    return;
                } else if (strInfo.equalsIgnoreCase("E") || strInfo.equalsIgnoreCase("O") || strInfo.equalsIgnoreCase("C")) {
                    if (m_pDlgHandler != null) {
                        m_pDlgHandler.dismiss();
                        m_pDlgHandler = null;
                    }
                    return;
                } else {
                    m_pDlgHandler.setMessage(strInfo);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (m_pDlgHandler != null) {
            m_pDlgHandler.dismiss();
            m_pDlgHandler = null;
        }

        info.SetLastResult(nCallbackResult);

        int nIndex = 0;
        String strTitle = null;
        String strMsg = null;

        for (nIndex = 0; m_strDlgMsg[nIndex] != null; nIndex += 3) {
            if (m_strDlgMsg[nIndex].equalsIgnoreCase(strId)) {
                strTitle = m_strDlgMsg[nIndex + 1];
                strMsg = m_strDlgMsg[nIndex + 2];
                break;
            }
        }
        if (strTitle == null) {
            strTitle = "Callback id=[" + strId + "]";
        }

        if (strMsg == null)
            strMsg = "";

        m_pDlgHandler = new AlertDialog.Builder(this).setTitle(strTitle).setMessage(strMsg).create();
        m_pDlgHandler.show();
        return;

    }

    private void ProcessThreadMessage(Message msg) {
        switch (msg.what) {
            case slMisConstants.MisMsg_ResultNotify:
                byte[] byRecv = (byte[]) msg.obj;

                int nRecvLen = msg.arg1;
                if (nRecvLen < 1)
                    nRecvLen = 0;
                String strResponse = "";
//			String strOutput = "";
                if (byRecv == null || nRecvLen < 1) {
                    strResponse = "Code: C99991\r\n Msg:ERROR\r\n";
                } else {
                    try {
                        strResponse = new String(byRecv, "GBK").trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                m_tvResponse.setText(strResponse);

                if (m_pDlgHandler != null) {
                    m_pDlgHandler.dismiss();
                    m_pDlgHandler = null;
                }
                break;
            case slMisConstants.MisMsg_Callback:
                slCallbackInfo info = (slCallbackInfo) msg.obj;
                if (info == null)
                    return;

                HandleCallback(info.GetId(), info);
                //	  CallbackHandler.OnCallback(info.GetId(), info);
                break;
            default: {

            }
            break;
        }

        return;
    }

    private void OnExitDialog() {
        AlertDialog dlgExit = new AlertDialog.Builder(this)
                .setTitle("退出程序")
                .setMessage("确定要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Test.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK
                                || keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_HOME) {
                            return true;
                        }
                        return false;
                    }
                })
                .create();

        dlgExit.show();
        //	dlgExit.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            OnExitDialog();
            return true;
        }
//  	else if(keyCode == KeyEvent.KEYCODE_MENU)
//  		return true;
        else if (keyCode == KeyEvent.KEYCODE_HOME)
            return true;

        return super.onKeyDown(keyCode, event);

    }

    // 拦截/屏蔽系统Home键
    public void onAttachedToWindow() {
        // 	this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//  	super.onAttachedToWindow();
    }


}

