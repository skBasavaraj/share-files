package com.example.filesharing.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.filesharing.R;
import com.example.filesharing.WIFIDirectBroadcastReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscoverList extends AppCompatActivity  {
  ListView listView;
  ArrayAdapter<String> arrayAdapter;
     public WifiP2pManager.PeerListListener peerListListener;
    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChanel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    AlertDialog.Builder builder;
    AlertDialog alert;
     List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_list);
        listView=findViewById(R.id.list);
         wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
         mChanel = mManager.initialize(this, getMainLooper(), null);
         mReceiver = new WIFIDirectBroadcastReceiver(mManager, mChanel, this);
         mIntentFilter = new IntentFilter();
         mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
         mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
         mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
         mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
         peerList();

         discover();

    }

    public void discover() {

        if (ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale( this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions( this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions( this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        mManager.discoverPeers(mChanel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.e("Discovry", "strat");
          /* Intent intent=new Intent(MainActivity.this,DiscoverList.class);
           startActivity(intent);*/
            }

            @Override
            public void onFailure(int i) {
                Log.e("Discovry", "fail");

            }
        });

    }

    public void peerList(){
        peerListListener=new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                if(!wifiP2pDeviceList.getDeviceList().equals(peers)){
                    peers.clear();
                    peers.addAll(wifiP2pDeviceList.getDeviceList());
                    deviceNameArray=new String[wifiP2pDeviceList.getDeviceList().size()];
                    deviceArray=new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                    int index=0;
                    for(WifiP2pDevice device:wifiP2pDeviceList.getDeviceList()){
                        deviceNameArray[index]=device.deviceName;
                        deviceArray[index]=device;
                        index++;
                    }

                    arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,deviceNameArray);
                    listView.setAdapter(arrayAdapter);
                    Log.e("Device","Device3"+ Arrays.toString(deviceNameArray));                 }
                if(peers.size()==0){
                    Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_SHORT).show();

                }
            }


        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

}