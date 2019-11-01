package utlis;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;

import com.geit.zzt.lang.io.ITInis;
import com.guoguang.ggbrowser.MainActivity;
import com.iflytek.cloud.SpeechUtility;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import logUtils.GGLog4j;
import webservice.Receiver;

import static com.slipay.slMisTran.CardTrans;
import static utlis.PayHttpUtils.GetSingleCabCollect;

/**
 * 描述：集中映射类，用于js的调用
 *
 * @author ZQH
 * @time 2019.10.25
 */
public class JsApi extends AppCompatActivity {


    // 用于接收Mainactivity对象
    private Context context;

    // 日志标签
    private final String TAG = "安卓---JsApi";

    //android生成日志的名称
    private GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");

    // web生成日志名称
    private GGLog4j weblogger = new GGLog4j("WEB网页.log");

    //自定义加载网页
    private String loadUrl = "file:///android_asset/build/index.html";

    // 实例网页（排队机）
    private String loadUrl2 = "http://www.ggzzrj.cn:8080/ued/test/android/index.html";

    // http交易结果
    private String httpResult;

    //新利集团动态库接口
    private int misRessult;

    // 创建JsApi实例，用于在其他activity调用
    public static utlis.JsApi instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //构造函数，传对象
    public JsApi(Context context1) {
        context = context1;
    }

    //================================ 映射函数类 ====================================//

    /**
     * 1、描述:映射文字转化为语音（科大讯飞sdk）
     *
     * @param text   需要转换成语音的字符串
     * @param pitch  声调（0-100）
     * @param volume 音量（0-100）
     */
    @JavascriptInterface
    public void speech(String text, String pitch, String volume) {
        SpeechUtility.createUtility(context.getApplicationContext(), "appid=5d355288"); // 讯飞注册码
        AudioUtils.getInstance().init(context, pitch, volume); // 初始化音调、音量
        AudioUtils.getInstance().speakText(text); // 播放语音
    }


    /**
     * 2、描述:映射文字转化为语音（android原生）
     *
     * @param text 需要转换成语音的字符串
     */
    @JavascriptInterface
    public void sysSpeech(final String text, int number) {

        //  延迟timem秒后，发送一个long类型数值
        Observable.range(3, number)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Integer value) {
                        ContextWrapper c = new ContextWrapper(context);
                        String cfg = c.getCacheDir().getAbsolutePath() + "//devconfig.ini";
                        float pitch = Float.parseFloat(ITInis.get(cfg, "语音", "pitch", "", "UTF-8"));
                        float speed = Float.parseFloat(ITInis.get(cfg, "语音", "speed", "", "UTF-8"));
                        // 设置音调
                        MainActivity.instance.tts.setPitch((pitch + 5) / 10);
                        // 设置音速
                        MainActivity.instance.tts.setSpeechRate((speed + 5) / 10);
                        // 文本内容
                        MainActivity.instance.tts.speak(text, TextToSpeech.QUEUE_ADD, null);

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        isspeak();
                    }
                });
    }

    /**
     * 3、描述:判断是有语音播放（用于js调用）
     */
    @JavascriptInterface
    public String Speaking() {
        if (MainActivity.instance.tts.isSpeaking()) {
            logger.i(TAG, "true：语音播放中");
            return "true";
        } else {
            logger.i(TAG, "flase：没有语音播放");
            return "false";
        }

    }

    /**
     * 4、描述:判断是有语音播放(返回字符串，用于浏览器)
     */
    @JavascriptInterface
    public Boolean isSpeaking() {
        if (MainActivity.instance.tts.isSpeaking()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 5、描述：映射获取手机的硬件信息（json格式）
     *
     * @return json格式的手机硬件信息
     */
    @JavascriptInterface
    public String DeviceInfo() {
        return new AppVersion().getDeviceInfo();

    }

    /**
     * 6、描述：映射获取app的版本信息（json格式）
     *
     * @return json格式的版本信息
     */
    @JavascriptInterface
    public String version() {
        return new AppVersion().getAppVersion(context);

    }

    /**
     * 7、描述:将web提供的日志写入特定的文件中
     */
    @JavascriptInterface
    public void webLog(String log) {
        weblogger.i("WEB网页日志", log);
    }

    /**
     * 8、获取科室id配置文件
     */
    @JavascriptInterface
    public String getConfig() {
        DataProcessingTool Dpt = new DataProcessingTool();
        ContextWrapper c = new ContextWrapper(context);
        String cfg = c.getCacheDir().getAbsolutePath() + "//devconfig.ini";
        logger.i(TAG, "文件地址" + cfg);
        String cache = Dpt.getCache(context, cfg, "配置文件", "devId" + "&" + "type" + "&" + "layout", "UTF-8");
        logger.i(TAG, "配置文件" + cache);
        return cache;
    }

    /**
     * 9、描述:开启轮询功能
     */
    @JavascriptInterface
    public void polling() {
        //轮询往缓存写入数据
        RXjava Pf = new RXjava();
        Pf.pollingFunctionReturnData(context);
    }

    /**
     * 10、描述:加载其他页面
     */
    @JavascriptInterface
    public void webView() {
        MainActivity.instance.mWebView.loadUrl(loadUrl);  // 加载页面
        //调用推送数据的轮询功能
        RXjava Pf = new RXjava();
        Pf.pollingFunctionReturnData2(MainActivity.instance.mWebView);
    }

    /**
     * 11、描述:加载自定义网页
     */
    @JavascriptInterface
    public void load() {
        MainActivity.instance.mWebView.loadUrl(loadUrl2);  // 加载页面
    }

    /**
     * 12、描述:映射将文件写入缓存
     *
     * @param ini         需要写入的ini格式的文件名称
     * @param sectionName 目录名称
     * @param keyValue    写入的数据（json格式：key1:value1&key2:value2）注意用英文下的冒号和&
     * @param encode      编码格式
     */
    @JavascriptInterface
    public void putCache(String ini, String sectionName, String keyValue, String encode) {
        DataProcessingTool Dpt = new DataProcessingTool();
        Dpt.putCache(context, ini, sectionName, keyValue, encode);
    }

    /**
     * 13、描述:映射将读取缓存文件
     * <p>
     * ini         需要写入的ini格式的文件名称
     *
     * @param sectionName 目录名称
     * @param key         需要取出的数据键值（json格式：key1&key2&··）,注意用英文下的&
     * @param encode      编码格式
     * @return 返回json格式的数据（json格式：key1：value1&key2：value2）
     */
    @JavascriptInterface
    public String getCache(String sectionName, String key, String encode) {
        //供js调用：ggie.getCache ("HTTPdata", "data", "UTF-8");
        DataProcessingTool Dpt = new DataProcessingTool();
        String cfg = getCacheDir().getAbsolutePath() + "//httpconfig.ini";
        String cache = Dpt.getCache(context, cfg, sectionName, key, encode);
        return cache;
    }

    /**
     * 14、描述:映射访问后台服务器的数据
     *
     * @param url  请求的网址
     * @param code 传入的请求数据（json格式：key1：value1&key2：value2）
     *             encode 编码格式
     * @return 返回json格式的数据
     */
    @JavascriptInterface
    public String data(final String url, final String code, final String encode) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
            }
        })
                .subscribe(new Observer<Integer>() {

                    // 2. 通过通过订阅（subscribe）连接观察者和被观察者
                    // 3. 创建观察者 & 定义响应事件的行为
                    @Override
                    public void onSubscribe(Disposable d) {
                        logger.d(TAG, "RXjava进行subscribe连接");
                    }

                    // 默认最先调用复写的 onSubscribe（）
                    @Override
                    public void onNext(Integer value) {
                        httpResult = GetSingleCabCollect(url, code, encode);
                        logger.i(TAG, "HTTP交易数据：" + httpResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.e(TAG, "用于http交易的RXjava出现异常：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }

                });


        return httpResult;
    }

    /**
     * 15、线程：用于接收rabbitmq的数据
     */
    @JavascriptInterface
    public void getMQThread() {
        // RxJava的流式操作
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
            }
        })
                .subscribe(new Observer<Integer>() {
                    // 2. 通过通过订阅（subscribe）连接观察者和被观察者
                    // 3. 创建观察者 & 定义响应事件的行为
                    @Override
                    public void onSubscribe(Disposable d) {
                        logger.d(TAG, "RXjava进行subscribe连接");
                    }

                    // 默认最先调用复写的 onSubscribe（）
                    @Override
                    public void onNext(Integer value) {
                        ContextWrapper c = new ContextWrapper(context);
                        String cfg = c.getCacheDir().getAbsolutePath() + "//devconfig.ini";
                        String getidev = ITInis.get(cfg, "设备终端号", "devId", "", "UTF-8");
                        logger.i(TAG, "------开启获取MQ数据线程------");
                        logger.i(TAG, "终端号devId：" + getidev);
                        logger.i(TAG, "路径：" + cfg);
                        Receiver.basicConsume(getidev);
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.e(TAG, "用于MQ的RXjava出现异常：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });


    }

    /**
     * 16、映射开启副屏1--显示Android的Activity界面
     */
    @JavascriptInterface
    public void setDisplayActivity() {
        GGDisplay.DislayActivity.display(context, MainActivity.instance.displayList, 1);
        GGDisplay.DislayActivity.instance.androidDisplay("qqq", "qqq", "qqq"); // 操作副屏的函数
    }


    /**
     * 17·映射开启副屏2--显示网页
     */
    @JavascriptInterface
    public void setDisplayH5() {
        //显示网页时，需要在主Activity中使用UI线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                H5Display.display((MainActivity) context, MainActivity.instance.displayList, 1);
            }
        });

    }

    /**
     * 18·映射操作副屏1--Activity传参显示功能
     */
    @JavascriptInterface
    public void exeDisplayActivity() {
        GGDisplay.DislayActivity.display(context, MainActivity.instance.displayList, 0);
        GGDisplay.DislayActivity.instance.androidDisplay("qqq", "qqq", "qqq"); // 操作副屏的函数

    }

    /**
     * 19·映射操作副屏2--显示网页
     */
    @JavascriptInterface
    public void exeDisplayH5() {
        //显示网页时，需要在主Activity中使用UI线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                H5Display.display((MainActivity) context, MainActivity.instance.displayList, 0);
            }
        });

    }

    /**
     * 20·新利集团动态库接口
     */
    @JavascriptInterface
    public int misTran(final byte[] byRequest, final int nReqLen, final byte[] byRecv, final int nRecvSize, final byte[] byInterface) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
            }
        })
                .subscribe(new Observer<Integer>() {

                    // 2. 通过通过订阅（subscribe）连接观察者和被观察者
                    // 3. 创建观察者 & 定义响应事件的行为
                    @Override
                    public void onSubscribe(Disposable d) {
                        logger.d(TAG, "动态库调用进行subscribe连接");
                    }

                    // 默认最先调用复写的 onSubscribe（）
                    @Override
                    public void onNext(Integer value) {
                        misRessult = CardTrans(byRequest,nReqLen,byRecv,nRecvSize,byInterface);
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.e(TAG, "调用动态库出现异常：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }

                });
        return misRessult;
    }

    //================================ 普通函数 ====================================//

    /**
     * 描述:TTS初始函数
     */
    public void ttsIni() {

        //  延迟timem秒后，发送一个long类型数值
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long value) {
                        MainActivity.instance.tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                // 如果装载TTS引擎成功
                                if (status == TextToSpeech.SUCCESS) {
                                    logger.i(TAG, "TTS引擎初始化成功");
                                } else {
                                    logger.e(TAG, "TTS引擎初始化失败");
                                }
                            }

                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 描述:用于语音播放完之后，调用web端的接口，用于系统语音播报
     */
    public void isspeak() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    public Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long value) {
                        if (!isSpeaking()) {
                            logger.i(TAG, "语音播放完毕，准备调web函数");
                            String message = "语音播放完毕";
                            MainActivity.instance.callJsSpeech("javascript:speachOver('" + message + "')");
                            mDisposable.dispose();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }});
                }



}
