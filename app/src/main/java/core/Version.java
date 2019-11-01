package core;

/**
 * 浏览器版本信息
 * @author yisu
 */
public class Version
{
	// 换行符
	private static final String lineSepartor = System.getProperty("line.separator");
	
	// 当前版本号（在MainActivity中获取当前版本，然后更新此值）
	public static String version = "v1.0.0";
	
	// 版本信息
	public static final String description = new StringBuilder("版本信息：")
	                                                .append(lineSepartor)
	                                                
			                                        .append("v1.0.0").append(lineSepartor)
			                                        .append("   实现双屏异显").append(lineSepartor)
			                                        .append(lineSepartor)
			                                        
			                                        .append("v1.0.1").append(lineSepartor)
			                                        .append("   实现手势登录和大量的配置功能").append(lineSepartor)
			                                        .append(lineSepartor)
			                                        
			                                        .append("v1.0.2").append(lineSepartor)
			                                        .append("   增加JSAPI函数：调用安卓原生的显示；本地TCP监听服务的启动/停止；TCP发送数据；日志查看；JS回调等").append(lineSepartor)
			                                        .append(lineSepartor)
			                                        
			                                        .append("v1.0.3").append(lineSepartor)
			                                        .append("   增加JSAPI函数：读写ini配置文件；供C端调用的日志写入函数;播放音频文件;增加重启/关机功能").append(lineSepartor)
			                                        .append(lineSepartor)
			                                        
			                                        .append("v1.0.4").append(lineSepartor)
			                                        .append("   增加JSAPI函数：增加发送广播、拍照和截屏功能，并提供相应的JSAPI函数").append(lineSepartor)
			                                        .append(lineSepartor)
			                                        
			                                        .toString()
			                                        ;
	
}
