package com.cordova.plugins.navigation.speech;

public interface Speech {

    void playText(String playText);

    void stopSpeak();

    void destroy();

    boolean isPlaying();

    void setCallback(CallBack callback);
}
