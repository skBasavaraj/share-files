package com.example.filesharing.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filesharing.adapters.listAdapter;
import com.example.filesharing.model.ItemList;
import com.example.filesharing.model.ListItem;
import com.example.filesharing.R;
import com.example.filesharing.sharing_backend.BaseActivity;
import com.example.filesharing.sharing_backend.DeviceAdapter;
import com.example.filesharing.sharing_backend.DirectActionListener;
import com.example.filesharing.sharing_backend.LoadingDialog;
import com.example.filesharing.sharing_backend.WifiClientTask;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiscoverList extends BaseActivity {
    public WifiP2pManager.PeerListListener peerListListener;
    public static final String TAG = "SendFileActivity";
    public WifiP2pManager wifiP2pManager;
    public WifiP2pManager.Channel channel;
    public WifiP2pInfo wifiP2pInfo;
    public boolean wifiP2pEnabled = false;
    public  RecyclerView rv_deviceList,rvList;

    public List<WifiP2pDevice> wifiP2pDeviceList;
    public DeviceAdapter deviceAdapter;
    public WifiP2pDevice mWifiP2pDevice;
    public LoadingDialog loadingDialog;
    public BroadcastReceiver broadcastReceiver;
    public static DiscoverList instance;
    public static List<ItemList> list=new ArrayList<>();
     Uri  imageUri;
     FastAdapter<listAdapter> fastAdapter;
    ItemAdapter<listAdapter> itemAdapter;
     public final DirectActionListener directActionListener = new DirectActionListener() {

        @Override
        public void wifiP2pEnabled(boolean enabled) {
            wifiP2pEnabled = enabled;
        }

        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            dismissLoadingDialog();
            wifiP2pDeviceList.clear();
            deviceAdapter.notifyDataSetChanged();
            
            Log.e(TAG, "onConnectionInfoAvailable");
            Log.e(TAG, "onConnectionInfoAvailable groupFormed: " + wifiP2pInfo.groupFormed);
            Log.e(TAG, "onConnectionInfoAvailable isGroupOwner: " + wifiP2pInfo.isGroupOwner);
            Log.e(TAG, "onConnectionInfoAvailable getHostAddress: " + wifiP2pInfo.groupOwnerAddress.getHostAddress());
            StringBuilder stringBuilder = new StringBuilder();
            if (mWifiP2pDevice != null) {
                stringBuilder.append("connected device name：");
                stringBuilder.append(mWifiP2pDevice.deviceName);
                stringBuilder.append("\n");
                stringBuilder.append("The address of the connected device：");
                stringBuilder.append(mWifiP2pDevice.deviceAddress);

            }
            stringBuilder.append("\n");
            stringBuilder.append("Is the group owner：");
            stringBuilder.append(wifiP2pInfo.isGroupOwner ? "is the group owner" : "non-group owner");
            stringBuilder.append("\n");
            stringBuilder.append("Group owner IP address：");
            stringBuilder.append(wifiP2pInfo.groupOwnerAddress.getHostAddress());
             if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner) {
                 DiscoverList.this.wifiP2pInfo = wifiP2pInfo;
                  hold();

             }
        }

        @Override
        public void onDisconnection() {
            Log.e(TAG, "onDisconnection");
            
            showToast("in a disconnected state");
            wifiP2pDeviceList.clear();
            deviceAdapter.notifyDataSetChanged();
             wifiP2pInfo = null;
        }

        @Override
        public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
            Log.e(TAG, "onSelfDeviceAvailable");
            Log.e(TAG, "DeviceName: " + wifiP2pDevice.deviceName);
            Log.e(TAG, "DeviceAddress: " + wifiP2pDevice.deviceAddress);
            Log.e(TAG, "Status: " + wifiP2pDevice.status);

            showToast("Device Connected ");
          }

        @Override
        public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
            Log.e(TAG, "onPeersAvailable :" + wifiP2pDeviceList.size());
            DiscoverList.this. wifiP2pDeviceList.clear();
            DiscoverList.this.wifiP2pDeviceList.addAll(wifiP2pDeviceList);
            deviceAdapter.notifyDataSetChanged();
            loadingDialog.cancel();
        }

        @Override
        public void onChannelDisconnected() {
            Log.e(TAG, "onChannelDisconnected");
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_list);
        rv_deviceList = findViewById(R.id.rv_deviceList);
        rvList=findViewById(R.id.rvlist);

       rvList.setLayoutManager(new LinearLayoutManager(this) );

        rvList.setHasFixedSize(true);

        itemAdapter = itemAdapter.items();

        itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        rvList.setAdapter(fastAdapter);

        wifiP2pDeviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(wifiP2pDeviceList);
         deviceAdapter.setClickListener(position -> {
            mWifiP2pDevice = wifiP2pDeviceList.get(position);
            showToast(mWifiP2pDevice.deviceName);
            connect();

        });
      //  itemAdapter.add(new listAdapter().withAdapter( new ListItem(imageUri.toString(), imageUri.toString())));
     //   fastAdapter.notifyAdapterItemInserted(0);
        loadingDialog = new LoadingDialog(this);

        rv_deviceList.setAdapter(deviceAdapter);
        rv_deviceList.setLayoutManager(new LinearLayoutManager(this));
         initEvent();
         discover();
    }

    private void connect() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showToast("Please grant location permission first");
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        if (config.deviceAddress != null && mWifiP2pDevice != null) {
            config.deviceAddress = mWifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            showLoadingDialog("connecting " + mWifiP2pDevice.deviceName);
            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    rv_deviceList.setVisibility(View.GONE);
                    setTitle(mWifiP2pDevice.deviceName);
                    Log.e(TAG, "connect onSuccess");

                }

                 @Override
                public void onFailure(int reason) {
                    showToast("Connection failed " + reason);
                    dismissLoadingDialog();
                }
            });
        }
    }

    public static DiscoverList PasItem(List<ItemList> select) {
        list=select;
        return instance;

    }

    private   void hold() {
        Handler hdlr = new Handler();
        new Thread(new Runnable() {
            public void run() {
                for(int i=0;i<list.size();i++) {
                    int finalI = i;
                    hdlr.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            imageUri = Uri.parse("file:///"+list.get(finalI).getPath());
                            new WifiClientTask(DiscoverList.this).execute(wifiP2pInfo.groupOwnerAddress.getHostAddress(), imageUri);
                            if(list.size()-1==finalI) {
                                Toast.makeText(DiscoverList.this, finalI+1+""+"Transfer Completed", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DiscoverList.this,  finalI+1+""+"Item Sending...", Toast.LENGTH_SHORT).show();

                            }
                  itemAdapter.add(new listAdapter().withadapter(new ListItem(imageUri.toString(),imageUri.toString())));
                 fastAdapter.notifyAdapterDataSetChanged();
                          }
                    });

                    try {
                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        ).start();



    }

    public void discover() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        loadingDialog.show("Searching for nearby devices", true, false);
        wifiP2pDeviceList.clear();
        deviceAdapter.notifyDataSetChanged();
         wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                showToast("Success");
            }

            @Override
            public void onFailure(int reasonCode) {
                showToast("Failure");
                loadingDialog.cancel();
            }
        });


    }

   private void initEvent() {
       wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
       if (wifiP2pManager == null) {
           finish();
           return;
       }
       channel = wifiP2pManager.initialize(this, getMainLooper(), directActionListener);
       broadcastReceiver = new DirectBroadcastReceiver(wifiP2pManager, channel, directActionListener);
       registerReceiver(broadcastReceiver, DirectBroadcastReceiver.getIntentFilter());
   }
   @Override
   protected void onDestroy() {
       super.onDestroy();
       unregisterReceiver(broadcastReceiver);
       disconnect();
   }
    private void disconnect() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onFailure(int reasonCode) {
                Log.e(TAG, "disconnect onFailure:" + reasonCode);
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "disconnect onSuccess");

            }
        });
    }
}