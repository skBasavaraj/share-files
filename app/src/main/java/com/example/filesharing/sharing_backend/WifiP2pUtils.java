package com.example.filesharing.sharing_backend;

import android.net.wifi.p2p.WifiP2pDevice;


public class WifiP2pUtils {

    public static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "usable";
            case WifiP2pDevice.INVITED:
                return "Inviting";
            case WifiP2pDevice.CONNECTED:
                return "connected";
            case WifiP2pDevice.FAILED:
                return "lose";
            case WifiP2pDevice.UNAVAILABLE:
                return "unavailable";
            default:
                return "unknown";
        }
    }

}
