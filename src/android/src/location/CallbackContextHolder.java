package com.cordova.plugins.location;

import org.apache.cordova.CallbackContext;

public class CallbackContextHolder {

    private boolean isOnceLocation;
    private CallbackContext callbackContext;

    public CallbackContextHolder(boolean isOnceLocation, CallbackContext callbackContext) {
        this.isOnceLocation = isOnceLocation;
        this.callbackContext = callbackContext;
    }

    public boolean isOnceLocation() {
        return isOnceLocation;
    }

    public void setOnceLocation(boolean onceLocation) {
        isOnceLocation = onceLocation;
    }

    public CallbackContext getCallbackContext() {
        return callbackContext;
    }

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }
}
