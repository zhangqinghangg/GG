package webservice;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Service
public class GGMQRequest {

	private final static Logger logger = LoggerFactory.getLogger(GGMQRequest.class);
	public static Connection connection;

	public Connection getConnection() throws Exception {
		GGQueueConstants ggQueueConstants =new GGQueueConstants();
		if(connection == null) 
		{
			//定义连接工厂
	        ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost(ggQueueConstants.MQServerIP);
	        factory.setPort(ggQueueConstants.MQServerPort);
	        //设置vhost
//	        factory.setVirtualHost("QueueServer");
			factory.setVirtualHost("/");
	        factory.setUsername(ggQueueConstants.MQUsername);
	        factory.setPassword(ggQueueConstants.MQPassword);
	        //factory.setAutomaticRecoveryEnabled(true);
	        
	        //通过工厂获取连接
	        connection = factory.newConnection();
		}
        
        return connection;
	}
	
}
