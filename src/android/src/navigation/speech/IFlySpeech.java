package com.cordova.plugins.navigation.speech;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

public class IFlySpeech implements Speech, AudioManager.OnAudioFocusChangeListener {

    private Context context;
    private CallBack callBack;
    private AudioManager audioManager;

    private SpeechSynthesizer synthesizer;
    private SynthesizerListener listener;
    private boolean isPlaying;

    private static IFlySpeech instance;

    public static IFlySpeech getInstance(Context context) {
        if (instance == null) {
            instance = new IFlySpeech(context);
        }
        return instance;
    }

    private IFlySpeech(Context context) {
        this.context = context;
        this.init();
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.isPlaying = false;
    }

    private void init() {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=5a376b82");
        SpeechSynthesizer synthesizer = SpeechSynthesizer.createSynthesizer(this.context, null);
        //设置发音人
        synthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置语速,值范围：[0, 100],默认值：50
        synthesizer.setParameter(SpeechConstant.SPEED, "55");
        //设置音量
        synthesizer.setParameter(SpeechConstant.VOLUME, "tts_volume");
        //设置语调
        synthesizer.setParameter(SpeechConstant.PITCH, "tts_pitch");
        //设置与其他音频软件冲突的时候是否暂停其他音频
        synthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        //女生仅vixy支持多音字播报
        synthesizer.setParameter(SpeechConstant.VOICE_NAME, "vixy");
        this.synthesizer = synthesizer;
        this.listener = new SpeechSynthesizerListener();
    }

    public void playText(String playText) {
        if (playText == null || playText.length() == 0) {
            return;
        }

        int result = this.audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            this.synthesizer.startSpeaking(playText, this.listener);
            this.isPlaying = true;
        }
    }

    public void stopSpeak() {
        if (this.synthesizer != null) {
            this.synthesizer.stopSpeaking();
        }
        isPlaying = false;
    }

    public void destroy() {
        stopSpeak();
        if (this.synthesizer != null) {
            this.synthesizer.destroy();
        }
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setCallback(CallBack callback) {
        this.callBack = callback;
    }

    public void onAudioFocusChange(int focusChange) {

    }

    private class SpeechSynthesizerListener implements SynthesizerListener {

        public void onSpeakBegin() {
            isPlaying = true;
        }

        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        public void onSpeakPaused() {
            isPlaying = false;
        }

        public void onSpeakResumed() {

        }

        public void onSpeakProgress(int i, int i1, int i2) {

        }

        public void onCompleted(SpeechError speechError) {
            isPlaying = false;
            if (audioManager != null) {
                audioManager.abandonAudioFocus(IFlySpeech.this);
            }
            if (callBack != null) {
                if (speechError == null) {
                    callBack.onCompleted(0);
                }
            }
        }

        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }
}
