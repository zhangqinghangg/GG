package logUtils;

import android.text.TextUtils;


import org.apache.log4j.Logger;

import utlis.TimeUtils;

public class GGLog4j
{

	// log开关
	public static final boolean SWITCH_LOG = true;
	private static boolean isConfigured = false;


	public GGLog4j(String fileName)
	{
		String logName = fileName + "." + TimeUtils.getTime(0);
		Log4jConfigure.configure(logName);
	}


	public static void d(String tag, String message)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.debug(message);
		}
	}


	public static void d(String tag, String message, Throwable exception)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.debug(message, exception);
		}
	}


	public static void i(String tag, String message)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.info(message);
		}
	}


	public static void i(String tag, String message, Throwable exception)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.info(message, exception);
		}
	}


	public static void w(String tag, String message)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.warn(message);
		}
	}


	public static void w(String tag, String message, Throwable exception)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.warn(message, exception);
		}
	}


	public static void e(String tag, String message)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.error(message);
		}
	}


	public static void e(String tag, String message, Throwable exception)
	{
		if (SWITCH_LOG)
		{
			Logger LOGGER = getLogger(tag);
			LOGGER.error(message, exception);
		}
	}


	private static Logger getLogger(String tag)
	{
		Logger logger;
		if (TextUtils.isEmpty(tag))
		{
			logger = Logger.getRootLogger();
		}
		else
		{
			logger = Logger.getLogger(tag);
		}
		return logger;
	}
}
