package utlis;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import logUtils.GGLog4j;

/**
 * 描述：语音播放工具类
 * 2019.10.29
 */
public class AudioUtils {
    private static AudioUtils audioUtils;
    private SpeechSynthesizer mySynthesizer;
    private TextToSpeech tts;

    //日志标签
    private static final String TAG = "安卓---AudioUtils";

    // 日志名称
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");

    public AudioUtils() {

    }

    public static AudioUtils getInstance() {
        if (audioUtils == null) {
            synchronized (AudioUtils.class) {
                if (audioUtils == null) {
                    audioUtils = new AudioUtils();
                }
            }
        }
        return audioUtils;
    }

    private InitListener myInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
        }
    };

    /**
     * 描述:用于语音的初始化,需要调用
     *
     * @param context 所在Activity，例：MainActivity.this
     * @param pitch   声调（0-100）
     * @param volume  音量（0-100）
     */
    public void init(Context context, String pitch, String volume) {
        mySynthesizer = SpeechSynthesizer.createSynthesizer(context,
                myInitListener);
        mySynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); // 设置发音人
        mySynthesizer.setParameter(SpeechConstant.PITCH, pitch); // 设置音调
        mySynthesizer.setParameter(SpeechConstant.VOLUME, volume); // 设置音量
    }

    /**
     * 描述:根据传入的文本转换音频并播放，需要调用
     *
     * @param content 需要转换成语音的字符串
     */
    public void speakText(String content) {
        int code = mySynthesizer.startSpeaking(content,
                new SynthesizerListener() {

                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onBufferProgress(int i, int i1, int i2, String s) {

                    }

                    @Override
                    public void onSpeakPaused() {

                    }

                    @Override
                    public void onSpeakResumed() {

                    }

                    @Override
                    public void onSpeakProgress(int i, int i1, int i2) {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {

                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                });
    }


}