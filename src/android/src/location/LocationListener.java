package com.cordova.plugins.location;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Deque;
import java.util.LinkedList;

public class LocationListener implements AMapLocationListener {

    private Deque<CallbackContext> callbackContexts = new LinkedList<CallbackContext>();

    private double latitude;
    private double longitude;
    private boolean isInit = false;
    private String error;

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location == null) {
            return;
        }

        if (location.getErrorCode() != 0) {
            String error = location.getErrorInfo();
            this.sendResult(this.getErrorResult(error));
            if (!isInit) {
                this.isInit = true;
                this.error = error;
            }
            return;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (latitude == this.latitude || longitude == this.longitude) {
            return;
        }

        this.isInit = true;
        this.error = null;

        this.latitude = latitude;
        this.longitude = longitude;

        this.sendResult(this.getPositionResult(latitude, longitude));
    }

    private PluginResult getErrorResult(String error) {
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, error);
        result.setKeepCallback(true);
        return result;
    }

    private PluginResult getPositionResult(double latitude, double longitude) {
        try {
            JSONObject json = new JSONObject();
            json.put("latitude", latitude);
            json.put("longitude", longitude);

            PluginResult result = new PluginResult(PluginResult.Status.OK, json);
            result.setKeepCallback(true);
            return result;
        } catch (JSONException ignored) {
        }
        return null;
    }

    private void sendResult(PluginResult result) {
        if (result == null) {
            return;
        }
        for (CallbackContext callbackContext : callbackContexts) {
            callbackContext.sendPluginResult(result);
        }
    }

    void addCallback(CallbackContext callbackContext) {
        this.callbackContexts.add(callbackContext);

        if (this.callbackContexts.size() > 10) {
            this.callbackContexts.removeFirst();
        }

        if (!isInit) {
            return;
        }

        if (this.error != null) {
            callbackContext.sendPluginResult(this.getErrorResult(error));
        } else {
            callbackContext.sendPluginResult(this.getPositionResult(this.latitude, this.longitude));
        }
    }
}
