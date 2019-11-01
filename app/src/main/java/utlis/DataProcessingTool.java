package utlis;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;

import com.geit.zzt.lang.io.ITInis;
import com.guoguang.ggbrowser.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.json.Json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import logUtils.GGLog4j;

public class DataProcessingTool extends Activity {

    private static final String TAG = "安卓---DataProcessingTool";
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    public static utlis.DataProcessingTool instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configdev);
        instance = this;
    }

    /**
     * 描述:用于存储访问后台返回的json格式的数据
     *
     * @param data 后台返回的json格式的数据
     */
    public void putReturnData(Context context, String dataName, String data) {
        ContextWrapper c = new ContextWrapper(context);
        String cfg = c.getCacheDir().getAbsolutePath() + "//itbrower.ini";
        ITInis.set(cfg, "返回数据", dataName, data, "UTF-8");
    }

    /**
     * 描述:用于取出后台返回的json格式的数据
     */
    public String getReturnData(Context context, String dataName) {
        ContextWrapper c = new ContextWrapper(context);
        String cfg = c.getCacheDir().getAbsolutePath() + "//itbrower.ini";
        String str = ITInis.get(cfg, "返回数据", dataName, "", "UTF-8");
        return str;
    }

    /**
     * 12、描述:映射将文件写入缓存
     *
     * @param ini         需要写入的ini格式的文件名称
     * @param sectionName 目录名称
     * @param keyValue    写入的数据（json格式：key1:value1&key2:value2）注意用英文下的冒号和&
     * @param encode      编码格式
     */
    public void putCache(Context context, String ini, String sectionName, String keyValue, String encode) {
        ContextWrapper c = new ContextWrapper(context);
        String cfg = c.getCacheDir().getAbsolutePath() + "//" + ini + ".ini";
        Log.i(TAG, cfg);
        String[] strarray = keyValue.split("&");
        for (int i = 0; i < strarray.length; i++) {
            String[] strarray2 = strarray[i].split(":");
            ITInis.set(cfg, sectionName, strarray2[0], strarray2[1], encode);
        }
    }

    /**
     * 13、描述:映射将读取缓存文件
     *
     * @param ini         需要写入的ini格式的文件名称
     * @param sectionName 目录名称
     * @param key         需要取出的数据键值（json格式：key1&key2&··）,注意用英文下的&
     * @param encode      编码格式
     * @return 返回json格式的数据（json格式：key1：value1&key2：value2）
     */
    public String getCache(Context context, String ini, String sectionName, String key, String encode) {
        JSONObject jsonParam = new JSONObject();

        ContextWrapper c = new ContextWrapper(context);
        String cfg = c.getCacheDir().getAbsolutePath() + "//" + ini + ".ini";
        Log.i(TAG, cfg);
        String[] strarray = key.split("&");
        for (int i = 0; i < strarray.length; i++) {
            ITInis.get(cfg, sectionName, strarray[i], "", encode);
            try {
                jsonParam.put(strarray[i], ITInis.get(cfg, sectionName, strarray[i], "", encode));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(jsonParam);
    }


    /**
     * 用于获取返回map中的不同键值的value
     *
     * @param map 由json转化的map
     * @param key 键值
     * @param def null
     * @return 不同键值的value
     */
    public String getValue(Map<String, String> map, String key, String def) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return "";
    }

    /**
     * 将asset文件写入缓存
     */
    public boolean copyAsset(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void clearWebViewCache1() {

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        logger.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        logger.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        logger.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            logger.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    //用于MQ配置数据的提取，将json字符串转化成map，然后在根据key值取出value值
    public String getMQConfigData(Context context,String key ) {

            ContextWrapper c = new ContextWrapper(context);
            String cfg = getCacheDir().getAbsolutePath() + "//mqconfig.ini";
            DataProcessingTool Dpt = new DataProcessingTool();
            String getidev = Dpt.getCache(context, cfg, "配置文件", "URL" + "&" + "IP" + "&" + "Port" + "&" + "MQname" + "&" + "MQpassword" + "&" + "Exchange", "UTF-8");
            String value ="";
        if (getidev == null) {
                logger.e(TAG, "未提取到MQ配置数据，请检查......");
            } else {
                Map<String, String> map = (HashMap<String, String>) Json.fromJson(getidev);
                value = getValue(map, key, "");
            }
        return value;
    }


}
