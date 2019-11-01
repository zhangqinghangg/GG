package core;

import android.app.Application;
import android.content.Context;

/**
 * 自定义的Application
 * @author yisu
 */
public class MyApplication extends Application
{

	// 全局Context（可供非Activity，且需Context入参的时候用）
	private static Context context = null;
	
	/**
	 * 创建
	 */
	@Override
	public void onCreate()
	{
	    super.onCreate();
		System.out.println("========================== 初始化全局Context ===========================");
		try
		{
			context = getApplicationContext();
		}
		catch(Exception ex)
		{
			System.out.println("======>> 初始化全局Context发生异常：" + ex.toString());
		}
	}


	/**
	 * 获取Context
	 * @return
	 */
	public static Context getContext()
	{
		return context;
	}
	
	
	/*
	 * 当低内存时
	 * @see android.app.Application#onLowMemory()
	 */
	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		System.gc();
	}
	
}
