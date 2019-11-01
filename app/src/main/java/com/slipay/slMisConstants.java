package com.slipay;

public class slMisConstants {
    public static final int		MisCallbackRet_BufferLen		=	999999;

    public static final boolean		AppScence_DebugVersion			=	true;

    public static final int		Mis_MaxRecvBufferSize		=	2048;
    //字段数据格式
    public static final int		MisFieldFormat_Digit		=	1;//数民
    public static final int		MisFieldFormat_HexDigit		=	2;
    public static final int		MisFieldFormat_AN			=	3;
    public static final	int		MisFieldFormat_ANS			=	4;
    //深大这个回调比较简单，不需要返回消息
    public static final int		MisMsg_ResultNotify			=	1;
    public static final int		MisMsg_Callback				=	2;
}
