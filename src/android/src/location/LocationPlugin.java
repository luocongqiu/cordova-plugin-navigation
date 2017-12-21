package com.cordova.plugins.location;

import android.Manifest;
import android.content.pm.PackageManager;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocationPlugin extends CordovaPlugin {

    private AMapLocationClient client;
    private LocationListener locationListener;
    private Map<Integer, CallbackContextHolder> permissionCallback = new HashMap<Integer, CallbackContextHolder>();

    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (!action.equals("getCurrentPosition") && !action.equals("watchPosition")) {
            return false;
        }

        if (!hasPermisssion()) {
            int requestCode = UUID.randomUUID().hashCode();
            PermissionHelper.requestPermissions(this, requestCode, permissions);
            permissionCallback.put(requestCode, new CallbackContextHolder(action.equals("getCurrentPosition"), callbackContext));
        } else {
            this.initClient(action.equals("getCurrentPosition"), callbackContext);
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
        return true;
    }

    private void initClient(boolean isOnceLocation, CallbackContext callbackContext) {
        if (!isOnceLocation && this.client != null) {
            this.locationListener.addCallback(callbackContext);
            return;
        }

        final AMapLocationClient client = new AMapLocationClient(this.cordova.getActivity().getApplicationContext());
        AMapLocationClientOption option = this.getDefaultOption();
        option.setOnceLocation(isOnceLocation);
        if (isOnceLocation) {
            OnceLocationListener locationListener = new OnceLocationListener(callbackContext);
            client.setLocationListener(locationListener);
            locationListener.onCallback(new OnceLocationListener.Callback() {
                @Override
                public void handle() {
                    destroyLocation(client);
                }
            });
        } else {
            LocationListener locationListener = new LocationListener();
            locationListener.addCallback(callbackContext);
            client.setLocationListener(locationListener);
            this.locationListener = locationListener;
            this.client = client;
        }
        client.startLocation();
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        option.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        option.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setInterval(2000);//可选，设置定位间隔。默认为2秒
        option.setNeedAddress(false);//可选，设置是否返回逆地理地址信息。默认是true
        option.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        option.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        option.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        option.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        option.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return option;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.destroyLocation(this.client);
        this.client = null;
    }

    private void destroyLocation(AMapLocationClient client) {
        if (client == null) {
            return;
        }
        client.onDestroy();
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        PluginResult result;

        CallbackContextHolder contextHolder = permissionCallback.get(requestCode);
        if (contextHolder == null) {
            return;
        }

        CallbackContext context = contextHolder.getCallbackContext();

        permissionCallback.remove(requestCode);

        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                context.sendPluginResult(result);
                return;
            }

        }
        this.initClient(contextHolder.isOnceLocation(), context);
    }


    public void requestPermissions(int requestCode) {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }

    public boolean hasPermisssion() {
        for (String p : permissions) {
            if (!PermissionHelper.hasPermission(this, p)) {
                return false;
            }
        }
        return true;
    }
}
