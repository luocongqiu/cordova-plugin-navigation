package com.cordova.plugins.navigation.speech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.Deque;
import java.util.LinkedList;

public class NavigationBroadcast implements AMapNaviListener, CallBack {


    private Speech speech;
    private Deque<String> words = new LinkedList<String>();
    private Handler handler;

    private final int PLAY = 1;
    private final int CHECK = 2;

    private static NavigationBroadcast instance;

    public static NavigationBroadcast getInstance(Context context) {
        if (instance == null) {
            instance = new NavigationBroadcast(context);
        }
        return instance;
    }

    public NavigationBroadcast(Context context) {
        this.speech = IFlySpeech.getInstance(context);
        this.speech.setCallback(this);
        this.initHandler();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case PLAY:
                        if (speech != null && words.size() > 0) {
                            speech.playText(words.removeFirst());
                        }
                        break;
                    case CHECK:
                        if (!speech.isPlaying()) {
                            handler.obtainMessage(PLAY).sendToTarget();
                        }
                        break;
                }
            }
        };
    }

    public void stopSpeaking() {
        if (speech != null) {
            speech.stopSpeak();
        }
        words.clear();
    }

    public void destroy() {
        if (speech != null) {
            speech.destroy();
        }
        instance = null;
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {
        this.words.addLast(s);
        handler.obtainMessage(CHECK).sendToTarget();
    }

    @Override
    public void onEndEmulatorNavi() {
        words.addLast("导航结束");
    }

    @Override
    public void onArriveDestination() {
        words.addLast("到达目的地");
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        words.addLast("路线规划失败");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        words.addLast("路线重新规划");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        words.addLast("前方路线拥堵，路线重新规划");
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCompleted(int code) {
        handler.obtainMessage(PLAY).sendToTarget();
    }
}
