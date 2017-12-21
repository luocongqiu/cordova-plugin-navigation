package com.cordova.plugins.navigation;

import org.apache.cordova.CordovaArgs;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigationOption {

    private double[] startPosition;
    private double[] endPosition;
    private int type;

    public NavigationOption(double[] startPosition, double[] endPosition, int type) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.type = type;
    }

    public static NavigationOption parseArgs(CordovaArgs args) {
        try {
            double[] startPosition = parsePosition(args.getJSONObject(0));
            double[] endPosition = parsePosition(args.getJSONObject(1));
            int type = args.getInt(2);
            return new NavigationOption(startPosition, endPosition, type);
        } catch (JSONException ignored) {
        }
        return null;
    }

    private static double[] parsePosition(JSONObject json) throws JSONException {
        return new double[]{json.getDouble("lat"), json.getDouble("lng")};
    }

    public double[] getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(double[] startPosition) {
        this.startPosition = startPosition;
    }

    public double[] getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(double[] endPosition) {
        this.endPosition = endPosition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
