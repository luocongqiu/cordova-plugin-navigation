package com.cordova.plugins.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigationPlugin extends CordovaPlugin {

    private static final String TAG = NavigationPlugin.class.getName();

    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int REQUEST_CODE = 1000;
    private NavigationOption option;
    private CallbackContext callbackContext;

    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        if (!action.equals("navigation")) {
            return false;
        }

        this.option = NavigationOption.parseArgs(args);
        this.callbackContext = callbackContext;

        if (!hasPermisssion()) {
            PermissionHelper.requestPermissions(this, 0, permissions);
        } else {
            this.showNavigationView();
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
        return true;
    }


    private void showNavigationView() {
        if (this.option == null) {
            return;
        }
        Context context = cordova.getActivity().getApplicationContext();
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.putExtra("start", this.option.getStartPosition());
        intent.putExtra("end", this.option.getEndPosition());
        intent.putExtra("type", this.option.getType());
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

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {

        if (this.callbackContext == null) {
            return;
        }

        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                PluginResult result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                this.callbackContext.sendPluginResult(result);
                return;
            }
        }
        this.showNavigationView();
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
