package webservice;

import com.geit.zzt.lang.io.ITInis;

public class GGQueueConstants {

    // MQ配置缓存地址
    String cfg = "/data/data/com.guoguang.ggbrowser/cache//mqconfig.ini";

    /**
     * 描述：公网测试环境
     */

    //队列服务URL地址
    public String URL = ITInis.get(cfg, "MQ配置", "URL", "", "UTF-8"); //队列服务URL地址

    //MQ服务器的IP地址
    public String MQServerIP = ITInis.get(cfg, "MQ配置", "IP", "", "UTF-8"); //MQ服务器的IP地址

    // MQ服务器的串口地址
    public int MQServerPort = Integer.parseInt(ITInis.get(cfg, "MQ配置", "Port", "", "UTF-8")); // MQ服务器的串口地址

    // MQ服务器的登录账号
    public String MQUsername = ITInis.get(cfg, "MQ配置", "MQname", "", "UTF-8"); // MQ服务器的登录账号

    // MQ服务器的登录密码
    public String MQPassword = ITInis.get(cfg, "MQ配置", "MQpassword", "", "UTF-8"); // 医院MQ服务器的登录密码

    // 转发器
    public String EXCHANGE_NAME = ITInis.get(cfg, "MQ配置", "Exchange", "", "UTF-8"); // 转发器


    /**
     * 描述：公网测试环境(固定板)
     */
//    	public  String URL = "http://jeremyda.cn:8019"; //队列服务URL地址
//    	public  String MQServerIP = "106.12.24.238"; //MQ服务器的IP地址
//    	public  int MQServerPort = 5672; // MQ服务器的串口地址
//    	public  String MQUsername = "guest"; // MQ服务器的登录账号
//    	public  String MQPassword = "yangsu"; // 医院MQ服务器的登录密码
//        public  String EXCHANGE_NAME = "GG_EXCHANGE_NAME"; // 转发器

    /**
     * 描述：医院测试环境(固定板)
     */

    //	public static String MQServerIP = "192.168.16.200"; // 医院MQ服务器的IP地址
    //	public static int MQServerPort = 5672; // 医院MQ服务器的串口地址
    //	public static String MQUsername = "admin"; // 医院MQ服务器的登录账号
    //	public static String MQPassword = "86970000"; // 医院MQ服务器的登录密码
    //	public static String QUEUE_NAME = "GG_Queue_NAME";
    //	public static String EXCHANGE_NAME = "GG_EXCHANGE_NAME";// 转发器


}
