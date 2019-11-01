package logUtils;

import android.os.Environment;


import org.apache.log4j.Level;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import utlis.StringUtils;
import utlis.TimeUtils;

public class Log4jConfigure {

    // 最大的文件大小
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 50;

    // 日志默认的保存地址
    private static final String DEFAULT_LOG_DIR = "//guoguang//log//";

    // 标签
    private static final String TAG = "Log4jConfigure";
    private static final String PACKAGE_NAME = "com.example.myprinter";  // com.ccb.dev.Interface.guoguang.logUtils

    // 日志打印级别
    public static String level = "DEBUG";

    // 日志内容打印模式
    public static String pattern = "%d\t%p/%c:\t%m%n";

    // 日志保存天数
    public static int savedays = 5;

    public static void configure(String fileName) {

        // 如果有配置文件
        final LogConfigurator logConfigurator = new LogConfigurator();
        try {
            if (isSdcardMounted()) {
                if (savedays > 0) {
                    try {
                        File f = new File(Environment.getExternalStorageDirectory() + DEFAULT_LOG_DIR);
                        File[] files = f.listFiles();// 列出所有文件
                        if (files != null) {
                            int count = files.length;// 文件个数
                            for (int i = 0; i < count; i++) {
                                File file = files[i];
                                if (delLog(file.getName()))
                                    file.delete();
                            }
                        }
                    } catch (Exception e) {
                        android.util.Log.e(TAG, "Log4j delete error" + e);
                    }
                }
                logConfigurator.setFileName(Environment.getExternalStorageDirectory() + DEFAULT_LOG_DIR + fileName);


            } else {
                logConfigurator.setFileName("//data//data//" + PACKAGE_NAME + "//files" + File.separator + fileName);
            }
            // 以下设置是按指定大小来生成新的文件
            logConfigurator.setMaxBackupSize(4);
            logConfigurator.setMaxFileSize(MAX_FILE_SIZE);

            // 以下设置是按天生成新的日志文件,与以上两句互斥,MAX_FILE_SIZE将不在起作用
            // 文件名形如 MyApp.log.2016-06-02,MyApp.log.2016-06-03
            //logConfigurator.setUseDailyRollingFileAppender(true);
            // 以下为通用配置

            logConfigurator.setImmediateFlush(true);
            logConfigurator.setRootLevel(Level.toLevel(level));
            logConfigurator.setFilePattern(pattern);
            logConfigurator.configure();
            android.util.Log.e(TAG, "Log4j config finish");
        } catch (Throwable throwable) {
            logConfigurator.setResetConfiguration(true);
            android.util.Log.e(TAG, "Log4j config error, use default config. Error:" + throwable);
        }
    }

    /**
     * 删除日志文件
     *
     */
    private static boolean delLog(String fileName) throws Exception {
        System.out.println("fileName=" + fileName);
        if (StringUtils.appearNumber(fileName, "\\.") > 1) {
            String logDate = fileName.split("\\.")[2];
            System.out.println("date=" + TimeUtils.getTime(2) + "====logDate" + logDate);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(logDate);
            String days = TimeUtils.DateSpace(TimeUtils.getTime(2), new SimpleDateFormat("yyyyMMdd").format(date));
            System.out.println("days=" + days);
            if (Integer.parseInt(days) > savedays)
                return true;

        }
        return false;

    }


    /**
     * SD卡是否正常挂载
     *
     * @return
     */
    private static boolean isSdcardMounted() {
        // System.out.println("Environment.getExternalStorageState() = " + Environment.getExternalStorageState());  // mounted
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
