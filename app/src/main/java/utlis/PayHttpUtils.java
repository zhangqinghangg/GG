package utlis;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import logUtils.GGLog4j;

/**
 * 描述：Httppost请求类（json数据传输）
 * 创建时间：2019.10.12
 *
 * @author zqh
 */
public class PayHttpUtils {

    private static final String TAG = "安卓--- PayHttpUtils";
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");
    /**
     * @param url 请求的网址
     * @param str 传入的请求数据（json格式：key1：value1&key2：value2）
     * @param encode 编码格式
     * @return 返回json格式的数据
     */
    public static String GetSingleCabCollect(String url, String str,String encode) {
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonParam = new JSONObject();

        try {
            String[] strarray = str.split("&");
            for (int i = 0; i < strarray.length; i++) {
                String[] strarray2 = strarray[i].split(":");
                jsonParam.put(strarray2[0], strarray2[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            logger.e(TAG, "json组包异常：" + e.getMessage());
        }
        // 解决中文乱码问题
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParam.toString(), encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.e(TAG, "解决中文乱码异常：" + e.getMessage());
        }
        if (entity != null) {
            entity.setContentEncoding(encode);
            entity.setContentType("application/json");
        }
        httpPost.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();
        // 获取HttpResponse实例
        HttpResponse httpResp = null;
        try {
            httpResp = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            logger.e(TAG, "Http响应异常：" + e.getMessage());
        }
        // 判断是够请求成功
        if (httpResp != null) {
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                String result = null;
                logger.i(TAG, "HttpPost请求成功：" + httpResp.getStatusLine().getStatusCode());
                try {
                    result = EntityUtils
                            .toString(httpResp.getEntity(), encode);
                    logger.i(TAG, "HttpPost方式请求成功，返回数据如下：" + result);
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.e(TAG, "接收数据异常：" + e.getMessage());
                }
            } else {
                logger.e(TAG, "HttpPost方式请求失败"
                        + httpResp.getStatusLine().getStatusCode());
            }
        }
        return null;
    }

}
