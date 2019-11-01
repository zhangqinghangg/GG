package com.slipay;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Lizq on 2015/12/29.
 */
public class slMisTran {
    public static final boolean		AppScence_DebugVersion			=	true;


    private static	Handler 		m_hMainHandler = null;	//UI 线程设置用于响应业务处理线程回调消息使用

    private slCallbackInfo m_hCallbackInfo = null;	//每笔交易单独一个，用于处理回调参数

    static {
        System.loadLibrary("Device");
        System.loadLibrary("slTools");
        System.loadLibrary("slMis");
    }

    public static	void SetMainHandle(Handler handler)
    {
        m_hMainHandler = handler;
    }


    private class slCallbackInfoImp  implements slCallbackInfo
    {
        private String 	m_strId;
        private byte[] 	m_byRequest;
        private int		m_nReqLen;
        private byte[] 	m_byResponse;
        private int		m_nRespSize;

        private int		m_nResult = -99999;

        private boolean m_bLock = false;

        public slCallbackInfoImp(final String strId, byte[] byRequest, int nReqLen, byte[] byResponse, int nRespSize)
        {
            m_strId = strId;

            m_byResponse = byResponse;
            m_nRespSize = nRespSize;
        }
        @Override
        public String	GetId()
        {
            return m_strId;
        }
        @Override
        public int GetIntTag(int nTag, int nDefault)
        {
            return 0;
        }
        @Override
        public byte[] GetTagValue(int nTag)
        {
            return null;
        }
        @Override
        public void SetTagValue(int nTag, int nValue)
        {

        }
        @Override
        public void SetTagValue(int nTag, byte[] byValue)
        {

        }
        @Override
        public void SetLastResult(int nReult)
        {
            if(true == m_strId.startsWith("s"))
                m_nResult = 0;
            else
                m_nResult = nReult;

            unLock();
        }

        public synchronized void Lock()
        {
            while (m_bLock == false)
            {
                try {
                    wait();
                } catch (Exception e) { }
            }

            m_bLock = false;
            notify();

        }
        public synchronized void unLock()
        {
            notify();
            m_bLock = false;
        }

        public int StartCallback()
        {

            m_bLock = false;
            Message message = new Message();
            message.what = slMisConstants.MisMsg_Callback;
            message.obj = this;
            m_hMainHandler.sendMessage(message);

            //	if(true != m_strId.startsWith("s"))
            {
                try {
                    while(m_nResult == -99999) Thread.sleep(100);
                } catch (Exception e) { }

                if(true != m_strId.equalsIgnoreCase("bcDetectPosition"))
                {
                    assert(m_nResult > 0);
                }
                return m_nResult;
            }
            //	else
            //		return 0;
        }
    }

    public boolean CardTransEx(final String strRequest)
    {
        Log.i("CardTrans", "begin....");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] byRequestInGBK = strRequest.getBytes("GBK");

                    int nReqBytesCount = byRequestInGBK.length;
                    byte[] byRecv = new byte[slMisConstants.Mis_MaxRecvBufferSize];
                    int nRet = -1;

                    if(slMisConstants.AppScence_DebugVersion == true)
                    {
                        Log.i("CardTrans", "begin");
                    }
                    byte[] byInterface = new byte[2];
                    System.arraycopy("QR".getBytes(), 0, byInterface, 0, 2);

                    nRet = CardTrans(byRequestInGBK, nReqBytesCount, byRecv, slMisConstants.Mis_MaxRecvBufferSize, byInterface);
                    if(slMisConstants.AppScence_DebugVersion == true)
                    {
                        Log.i("CardTrans:", "CardTrans=[" + nRet + "]");
                    }

                    if(m_hMainHandler != null)
                    {
                        Message message = new Message();
                        message.arg1 = nRet;
                        message.what = slMisConstants.MisMsg_ResultNotify;
                        if(nRet < 0)
                            message.obj = null;
                        else
                            message.obj = byRecv;
                        m_hMainHandler.sendMessage(message);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
        return true;
    }


    //提供给 NDK 回调的函数，回调的时候，
    //1 : byId, byTequest, byResponse 如果有内容，全部都是 GBK字符集的
    //2:  byId 不可为空，表示回调的内容， byRequest, nReqLen 是回调函数提供的参数， byResponse, 是NDK要求JAVA返回数据的缓冲区，nRespSize是返回缓冲区的大小
    public int onCallback(final byte[] byId, byte[] byRequest, int nReqLen, byte[] byResponse, int nRespSize)
    {
        try {
            if(byId == null || byId.length < 1)
                return -99992;
            String strId = new String(byId, "GBK").trim();
            String strReq = null;
            if(byRequest != null && nReqLen > 0)
            {
                strReq = new String(byRequest, "GBK").trim();
                if(slMisConstants.AppScence_DebugVersion == true)
                {
                    Log.i("onCallback:", "id=[" + strId + "] strReq=[" + strReq + "] nReqLen=[" + nReqLen + "]");
                }
            }
            else if(slMisConstants.AppScence_DebugVersion == true)
            {
                Log.i("onCallback:", "id=[" + strId + "]");
            }
            if(m_hMainHandler == null)
                return -99991;





            //	m_hCallbackInfo = null;
            slCallbackInfoImp hCallbackInfo = new slCallbackInfoImp(strId, byRequest, nReqLen, byResponse, nRespSize);
            //		m_hCallbackInfo = hCallbackInfo;
            int nRet = hCallbackInfo.StartCallback();
            Log.i("AppNotify", "id=[" + strId + "] nRet=[" + nRet + "]");
            //	m_hCallbackInfo = null;
            return nRet;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


//以下参数，byRequest - 请求串，定长固定结构体，GBK字符集，无内容时填充空格
//    nReqLen - 请求串长度
//  byRecv 返回缓冲区，调用者需要申请空间，512字节，返回内容，定长固定结构体，GBK字符集，无内容时填充空格
//  nRecvSize --调用者为  byRecv 申请的实际空间大小

    public static native int CardTrans(byte[] byRequest, int nReqLen, byte[] byRecv, int nRecvSize, byte[] byInterface);
}
