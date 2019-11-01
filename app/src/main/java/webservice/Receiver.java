package webservice;

import android.app.Activity;

import com.guoguang.ggbrowser.MainActivity;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import logUtils.GGLog4j;

public class Receiver extends Activity {

    //转发器，固定值
    private static String EXCHANGE_NAME = "GG_EXCHANGE_NAME";

    // 接收到的MQ数据
    private static String message;

    // 日志标签
    private static final String TAG = "安卓---Receiver";

    //日志名称
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");

    public static void basicConsume(String idDev) {
        Connection connection = null;
        final Channel channel;
        GGMQRequest mqrequest = new GGMQRequest();
        try {
            // 获取连接和通道
            connection = mqrequest.getConnection();
            channel = connection.createChannel();
            final String queueName = idDev;
            String BindingKey = idDev;

            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, BindingKey);

            // 空闲时才取
            channel.basicQos(1);

            logger.i(TAG, "--------开始接收MQ数据-------");
            final Consumer consumer = new DefaultConsumer(channel) {


                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    message = new String(body, "UTF-8");
                    logger.i("=====================", "=================================");
                    logger.i("MQ返回数据",  message);
                    logger.i("=====================", "=================================");
                    if (message == null) {
                        logger.e(TAG, "接收到的MQ数据为空，请检查......");
                    } else {
                        MainActivity.instance.callJs("javascript:getQueue('" + message + "')");
//                        AndroidJsTool.instance.callJs("javascript:getQueue('" + message + "')");

                        // 确认消费
                        channel.basicAck(envelope.getDeliveryTag(), true);
                    }
                }
            };
            // 第二个参数为true表示自动回送ack确认消息,这里采用手动
            channel.basicConsume(queueName, false, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(TAG, "接收MQ数据异常，重新启动获取MQ数据的线程：" + e.getMessage());
        }
    }


}