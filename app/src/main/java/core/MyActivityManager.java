package core;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity管理器
 * @author yisu
 */
public class MyActivityManager
{
	// 活动集合
	private List<Activity> mList = new LinkedList<Activity>();
	
	// 单例
	private static MyActivityManager instance;
	
	/**
	 * 构造函数
	 */
	public MyActivityManager()
	{
		System.out.println("========================== 初始化MyActivityManager ==========================");
	}
	

	/**
	 * 获得实例
	 * @return
	 */
	public synchronized static MyActivityManager getInstance()
	{
		if (null == instance)
		{
			instance = new MyActivityManager();
		}
		return instance;
	}


	/**
	 * 增加Activity
	 * @param activity
	 */
	public void addActivity(Activity activity)
	{
		mList.add(activity);
	}


	/**
	 * 结束指定的Activity
	 * @param activity
	 */
	public void finishActivity(Activity activity)
	{
		if (activity != null)
		{
			this.mList.remove(activity);
			activity.finish();
			activity = null;
		}
	}


	/**
	 * 退出应用 
	 * @param isExitApp - 是否彻底退出应用
	 */
	public void exit(boolean isExitApp)
	{
		try
		{
			// 销毁所有活动
			for (Activity activity : mList)
			{
				if (activity != null)
					activity.finish();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(isExitApp) 
				System.exit(0);
		}
	}
	
}
