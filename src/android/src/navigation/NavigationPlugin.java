package com.cordova.plugins.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigationPlugin extends CordovaPlugin {

    private static final String TAG = NavigationPlugin.class.getName();

    private static final int REQUEST_CODE = 1000;
    private CallbackContext callbackContext;

    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        if (!action.equals("navigation")) {
            return false;
        }

        double[] startPosition = this.parsePosition(args.getJSONObject(0));
        double[] endPosition = this.parsePosition(args.getJSONObject(1));
        int type = args.getInt(2);
        this.showNavigationView(startPosition, endPosition, type);

        this.callbackContext = callbackContext;
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
        return true;
    }

    private double[] parsePosition(JSONObject json) {
        try {
            return new double[]{json.getDouble("lat"), json.getDouble("lng")};
        } catch (JSONException ignored) {
        }
        return null;

    }

    private void showNavigationView(double[] startPosition, double[] endPosition, int type) {
        Context context = cordova.getActivity().getApplicationContext();
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.putExtra("start", startPosition);
        intent.putExtra("end", endPosition);
        intent.putExtra("type", type);
        cordova.startActivityForResult(this, intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_CODE) {
            return;
        }

        JSONObject json = new JSONObject();
        try {
            if (resultCode == Activity.RESULT_CANCELED) {
                json.put("status", -1);
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, json);
                callbackContext.sendPluginResult(pluginResult);
            } else {
                json.put("status", 0);
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, json);
                callbackContext.sendPluginResult(pluginResult);
            }
        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
