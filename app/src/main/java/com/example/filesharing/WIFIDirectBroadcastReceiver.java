package com.example.filesharing;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.filesharing.activity.DiscoverList;
import com.example.filesharing.activity.MainActivity;

public class WIFIDirectBroadcastReceiver extends BroadcastReceiver {
    public WifiP2pManager wifiP2pManager;
    public WifiP2pManager.Channel mchanel;
    public MainActivity mainActivity;
    public DiscoverList discoverList;

    public WIFIDirectBroadcastReceiver(WifiP2pManager wifiP2pManager1, WifiP2pManager.Channel mchanel, MainActivity mainActivity) {
        this.wifiP2pManager = wifiP2pManager1;
        this.mchanel = mchanel;
        this.mainActivity = mainActivity;
    }

    public WIFIDirectBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel mchanel, DiscoverList discoverList) {
        this.wifiP2pManager = wifiP2pManager;
        this.mchanel = mchanel;
        this.discoverList = discoverList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "WIFI IS ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI IS OFF ", Toast.LENGTH_SHORT).show();

            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (ActivityCompat.checkSelfPermission(discoverList, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            wifiP2pManager.requestPeers(mchanel, discoverList.peerListListener);


        }else  if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){

        }else  if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

        }
    }
}













