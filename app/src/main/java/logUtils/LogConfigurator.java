package logUtils;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

import java.io.IOException;

/**
 * Configures the Log4j logging framework. See <a href=
 * "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html"
 * >Patterns</a> for pattern layout.
 * 
 * Add by King.zi
 * @author Rolf Kulemann
 */
public class 	LogConfigurator
{
	private Level rootLevel = Level.DEBUG;
	private String filePattern = "%d - [%p::%c::%C] - %m%n";
	private String logCatPattern = "%m%n";
	private String fileName = "android-log4j.log";
	private int maxBackupSize = 5;
	private long maxFileSize = 512 * 1024;
	private boolean immediateFlush = true;
	private boolean useLogCatAppender = true;
	private boolean useFileAppender = true;
	private boolean resetConfiguration = true;
	private boolean internalDebugging = false;
	private boolean useDailyRollingFileAppender = false;
	private String datePattern = "'.'yyyyMMdd";


	public LogConfigurator()
	{
		
	}


	/**
	 * @param fileName Name of the log file
	 */
	public LogConfigurator(final String fileName)
	{
		setFileName(fileName);
	}


	/**
	 * @param fileName Name of the log file
	 * @param rootLevel Log level for the root logger
	 */
	public LogConfigurator(final String fileName, final Level rootLevel)
	{
		this(fileName);
		setRootLevel(rootLevel);
	}


	/**
	 * @param fileName Name of the log file
	 * @param rootLevel Log level for the root logger
	 * @param filePattern Log pattern for the file appender
	 */
	public LogConfigurator(final String fileName, final Level rootLevel, final String filePattern)
	{
		this(fileName);
		setRootLevel(rootLevel);
		setFilePattern(filePattern);
	}


	/**
	 * @param fileName Name of the log file
	 * @param maxBackupSize Maximum number of backed up log files
	 * @param maxFileSize Maximum size of log file until rolling
	 * @param filePattern Log pattern for the file appender
	 * @param rootLevel Log level for the root logger
	 */
	public LogConfigurator(final String fileName, final int maxBackupSize, final long maxFileSize, final String filePattern, final Level rootLevel)
	{
		this(fileName, rootLevel, filePattern);
		setMaxBackupSize(maxBackupSize);
		setMaxFileSize(maxFileSize);
	}


	public void configure()
	{
		final Logger root = Logger.getRootLogger();

		if (isResetConfiguration())
		{
			LogManager.getLoggerRepository().resetConfiguration();
		}

		LogLog.setInternalDebugging(isInternalDebugging());

		if (isUseFileAppender())
		{
			configureFileAppender();
		}

		if (isUseLogCatAppender())
		{
			configureLogCatAppender();
		}

		root.setLevel(getRootLevel());
	}


	/**
	 * Sets the level of logger with name <code>loggerName</code>. Corresponds
	 * to log4j.properties <code>log4j.logger.org.apache.what.ever=ERROR</code>
	 * 
	 * @param loggerName
	 * @param level
	 */
	public void setLevel(final String loggerName, final Level level)
	{
		Logger.getLogger(loggerName).setLevel(level);
	}


	private void configureFileAppender()
	{
		if (isUseDailyRollingFileAppender())
		{
			setDailyRollingFileAppender();
		}
		else
		{
			setRollingFileAppender();
		}
	}


	/**
	 * 文件大小到达指定尺寸时产生一个新的文件
	 */
	private void setRollingFileAppender()
	{
		final Logger root = Logger.getRootLogger();
		final RollingFileAppender rollingFileAppender;
		final Layout fileLayout = new PatternLayout(getFilePattern());

		try
		{
			rollingFileAppender = new RollingFileAppender(fileLayout, getFileName());
		}
		catch (final IOException e)
		{
			throw new RuntimeException("Exception configuring log system", e);
		}

		rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());
		// 在日志文件到达设置的最大值后，将会自动滚动，即将原来的内容移到mylog.log.1文件
		rollingFileAppender.setMaximumFileSize(getMaxFileSize());
		rollingFileAppender.setImmediateFlush(isImmediateFlush());
		root.addAppender(rollingFileAppender);
	}


	/**
	 * 每天生成一个新的日志文件
	 */
	private void setDailyRollingFileAppender()
	{
		final Logger root = Logger.getRootLogger();
		final DailyRollingFileAppender rollingFileAppender;
		final Layout fileLayout = new PatternLayout(getFilePattern());
		
		try
		{
			rollingFileAppender = new DailyRollingFileAppender(fileLayout, getFileName(), datePattern);
		}
		catch (final IOException e)
		{
			throw new RuntimeException("Exception configuring log system", e);
		}
		rollingFileAppender.setImmediateFlush(isImmediateFlush());

		root.addAppender(rollingFileAppender);
	}


	private void configureLogCatAppender()
	{
		final Logger root = Logger.getRootLogger();
		final Layout logCatLayout = new PatternLayout(getLogCatPattern());
		final de.mindpipe.android.logging.log4j.LogCatAppender logCatAppender = new de.mindpipe.android.logging.log4j.LogCatAppender(logCatLayout);

		root.addAppender(logCatAppender);
	}


	/**
	 * Return the log level of the root logger
	 * 
	 * @return Log level of the root logger
	 */
	public Level getRootLevel()
	{
		return rootLevel;
	}


	/**
	 * Sets log level for the root logger
	 * 
	 * @param level Log level for the root logger
	 */
	public void setRootLevel(final Level level)
	{
		this.rootLevel = level;
	}


	public String getFilePattern()
	{
		return filePattern;
	}


	public void setFilePattern(final String filePattern)
	{
		this.filePattern = filePattern;
	}


	public String getLogCatPattern()
	{
		return logCatPattern;
	}


	public void setLogCatPattern(final String logCatPattern)
	{
		this.logCatPattern = logCatPattern;
	}


	/**
	 * Returns the name of the log file
	 * 
	 * @return the name of the log file
	 */
	public String getFileName()
	{
		return fileName;
	}


	/**
	 * Sets the name of the log file
	 * 
	 * @param fileName Name of the log file
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}


	/**
	 * Returns the maximum number of backed up log files
	 * 
	 * @return Maximum number of backed up log files
	 */
	public int getMaxBackupSize()
	{
		return maxBackupSize;
	}


	/**
	 * Sets the maximum number of backed up log files
	 * 
	 * @param maxBackupSize Maximum number of backed up log files
	 */
	public void setMaxBackupSize(final int maxBackupSize)
	{
		this.maxBackupSize = maxBackupSize;
	}


	/**
	 * Returns the maximum size of log file until rolling
	 * 
	 * @return Maximum size of log file until rolling
	 */
	public long getMaxFileSize()
	{
		return maxFileSize;
	}


	/**
	 * Sets the maximum size of log file until rolling
	 * 
	 * @param maxFileSize Maximum size of log file until rolling
	 */
	public void setMaxFileSize(final long maxFileSize)
	{
		this.maxFileSize = maxFileSize;
	}


	public boolean isImmediateFlush()
	{
		return immediateFlush;
	}


	public void setImmediateFlush(final boolean immediateFlush)
	{
		this.immediateFlush = immediateFlush;
	}


	/**
	 * Returns true, if FileAppender is used for logging
	 * 
	 * @return True, if FileAppender is used for logging
	 */
	public boolean isUseFileAppender()
	{
		return useFileAppender;
	}


	/**
	 * @param useFileAppender the useFileAppender to configmq
	 */
	public void setUseFileAppender(final boolean useFileAppender)
	{
		this.useFileAppender = useFileAppender;
	}


	/**
	 * Returns true, if LogcatAppender should be used
	 * 
	 * @return True, if LogcatAppender should be used
	 */
	public boolean isUseLogCatAppender()
	{
		return useLogCatAppender;
	}


	/**
	 * If configmq to true, LogCatAppender will be used for logging
	 * 
	 * @param useLogCatAppender If true, LogCatAppender will be used for logging
	 */
	public void setUseLogCatAppender(final boolean useLogCatAppender)
	{
		this.useLogCatAppender = useLogCatAppender;
	}


	public void setResetConfiguration(boolean resetConfiguration)
	{
		this.resetConfiguration = resetConfiguration;
	}


	/**
	 * Resets the log4j configuration before applying this configuration.
	 * Default is true.
	 * 
	 * @return True, if the log4j configuration should be reset before applying
	 *         this configuration.
	 */
	public boolean isResetConfiguration()
	{
		return resetConfiguration;
	}


	public void setInternalDebugging(boolean internalDebugging)
	{
		this.internalDebugging = internalDebugging;
	}


	public boolean isInternalDebugging()
	{
		return internalDebugging;
	}


	public boolean isUseDailyRollingFileAppender()
	{
		return useDailyRollingFileAppender;
	}


	public void setUseDailyRollingFileAppender(boolean useDailyRollingFileAppender)
	{
		this.useDailyRollingFileAppender = useDailyRollingFileAppender;
	}
	
}
