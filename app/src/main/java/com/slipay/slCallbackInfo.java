package com.slipay;

public abstract interface slCallbackInfo {
	
	public abstract String	GetId();
	public abstract int GetIntTag(int nTag, int nDefault);
	public abstract byte[] GetTagValue(int nTag);
	public abstract void SetTagValue(int nTag, int nValue);
	public abstract void SetTagValue(int nTag, byte[] byValue);
	
	public abstract void SetLastResult(int nReult);
}
