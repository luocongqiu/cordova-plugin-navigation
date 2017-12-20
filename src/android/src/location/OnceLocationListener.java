package com.cordova.plugins.location;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class OnceLocationListener implements AMapLocationListener {

    private CallbackContext callbackContext;
    private Callback callback;

    public OnceLocationListener(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onLocationChanged(AMapLocation location) {
        if (location == null) {
            return;
        }

        if (location.getErrorCode() != 0) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, location.getErrorInfo());
            this.sendResult(result);
            return;
        }

        try {

            JSONObject json = new JSONObject();
            json.put("latitude", location.getLatitude());
            json.put("longitude", location.getLongitude());
            PluginResult result = new PluginResult(PluginResult.Status.OK, json);
            this.sendResult(result);
        } catch (JSONException ignored) {
        }
    }

    void onCallback(Callback callback) {
        this.callback = callback;
    }

    private void sendResult(PluginResult result) {
        this.callbackContext.sendPluginResult(result);
        if (this.callback != null) {
            this.callback.handle();
        }
    }


    public interface Callback {
        void handle();
    }
}
