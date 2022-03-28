package com.example.filesharing.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.filesharing.Adapter.sendReceive;
import com.example.filesharing.Model.ItemList;
import com.example.filesharing.R;
import com.example.filesharing.sharing_backend.BaseActivity;
import com.example.filesharing.sharing_backend.DirectActionListener;
import com.example.filesharing.sharing_backend.FileTransfer;
import com.example.filesharing.sharing_backend.ReceiveFileActivity;
import com.example.filesharing.sharing_backend.WifiServerService;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.utils.EventHookUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ReceiveScreen extends BaseActivity {
    private TextView tv_log;

    private ProgressDialog progressDialog;

    private WifiP2pManager wifiP2pManager;

    private WifiP2pManager.Channel channel;

    private boolean connectionInfoAvailable;

    private BroadcastReceiver broadcastReceiver;

    private WifiServerService wifiServerService;

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WifiServerService.WifiServerBinder binder = (WifiServerService.WifiServerBinder) service;
            wifiServerService = binder.getService();
            wifiServerService.setProgressChangListener(progressChangListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (wifiServerService != null) {
                wifiServerService.setProgressChangListener(null);
                wifiServerService = null;
            }
            bindService();
        }
    };

    private final DirectActionListener directActionListener = new DirectActionListener() {
        @Override
        public void wifiP2pEnabled(boolean enabled) {
            log("wifiP2pEnabled: " + enabled);
        }

        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            log("onConnectionInfoAvailable");
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                connectionInfoAvailable = true;
                if (wifiServerService != null) {
                    startService(WifiServerService.class);
                }
            }
        }

        @Override
        public void onDisconnection() {
            connectionInfoAvailable = false;
            log("onDisconnection");
        }

        @Override
        public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
            log("onSelfDeviceAvailable");
           // log(wifiP2pDevice.toString());
        }

        @Override
        public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
         //   log("onPeersAvailable,size:" + wifiP2pDeviceList.size());
            for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList) {
                //log(wifiP2pDevice.toString());
            }
        }

        @Override
        public void onChannelDisconnected() {
            log("onChannelDisconnected");
        }
    };

    private final WifiServerService.OnProgressChangListener progressChangListener = new WifiServerService.OnProgressChangListener() {
        @Override
        public void onProgressChanged(final FileTransfer fileTransfer, final int progress) {
            runOnUiThread(() -> {
                progressDialog.setMessage("file name： " + fileTransfer.getFileName());
                progressDialog.setProgress(progress);
                progressDialog.show();
            });
        }

        @Override
        public void onTransferFinished(final File file) {
            runOnUiThread(() -> {
                progressDialog.cancel();
//                if (file != null && file.exists()) {
//                    Glide.with(ReceiveFileActivity.this).load(file.getPath()).into(iv_image);
//                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_screen);

        wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        if (wifiP2pManager == null) {
            finish();
            return;
        }
        channel = wifiP2pManager.initialize(this, getMainLooper(), directActionListener);
        broadcastReceiver = new DirectBroadcastReceiver(wifiP2pManager, channel, directActionListener);
        registerReceiver(broadcastReceiver, DirectBroadcastReceiver.getIntentFilter());
        initView();
        bindService();
    }

    private void initView() {
        setTitle("Receive files");
          tv_log = findViewById(R.id.log);
             if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                  //  log("createGroup onSuccess");
                    dismissLoadingDialog();
                    showToast("onSuccess");
                }

                @Override
                public void onFailure(int reason) {
                   // log("createGroup onFailure: " + reason);
                    dismissLoadingDialog();
                    showToast("onFailure");
                }
            });

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("receiving file");
        progressDialog.setMax(100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiServerService != null) {
            wifiServerService.setProgressChangListener(null);
            unbindService(serviceConnection);
        }
        unregisterReceiver(broadcastReceiver);
        stopService(new Intent(this, WifiServerService.class));
        if (connectionInfoAvailable) {
            removeGroup();
        }
    }

    private void removeGroup() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                log("removeGroup onSuccess");
                showToast("onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                log("removeGroup onFailure");
                showToast("onFailure");
            }
        });
    }

    private void log(String log) {
        tv_log.append(log + "\n");
        tv_log.append("----------" + "\n");
    }

    private void bindService() {
        Intent intent = new Intent( this, WifiServerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

}

