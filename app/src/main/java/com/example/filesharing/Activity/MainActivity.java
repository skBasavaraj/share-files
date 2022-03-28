 package com.example.filesharing.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.filesharing.ApManger;
import com.example.filesharing.R;
import com.example.filesharing.WIFIDirectBroadcastReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public WifiP2pManager.PeerListListener peerListListener;
    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChanel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    CardView cardViewRcv, cardSend;
    AlertDialog.Builder builder;
    AlertDialog alert;
    ListView listView;
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
      DiscoverList discoverList=new DiscoverList();
     @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardViewRcv = findViewById(R.id.RcvBtn);
        cardSend = findViewById(R.id.Send);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.background_gradient_bottom));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (checkPermission()) {

        } else {
            requestPermission();

        }

        Wenable();
        sendActity();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        builder = new AlertDialog.Builder(this);

//        discover();
//      peerList();
        //openDialog(view);
    }


    private void sendActity() {

        cardSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             OpenSend();
            }
        });
    }

   /* public void discover() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        mManager.discoverPeers(mChanel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.e("Discovry", "strat");
          *//* Intent intent=new Intent(MainActivity.this,DiscoverList.class);
           startActivity(intent);*//*

            }

            @Override
            public void onFailure(int i) {
                Log.e("Discovry", "fail");

            }
        });

}*/
    public void OpenSend() {


        ApManger ap = new ApManger(getApplicationContext());
        if (ap.isApOn()) {
            hotspot();
        } else if (!wifiManager.isWifiEnabled()) {
            dialog();
        } else {
            Intent intent = new Intent(this, ViewPagers.class);
            startActivity(intent);

            /*Intent intent = new Intent(this, DiscoverList.class);
            startActivity(intent);*/
        }


     }





    public void Wenable() {

        cardViewRcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 ApManger ap = new ApManger( getApplicationContext());
                if (ap.isApOn()){
                    hotspot();
                }else if(!wifiManager.isWifiEnabled()){
                    dialog();
                }else {

                    send();

                }


            }
        });


    }
public void send(){
        Intent intent=new Intent(this,ReceiveScreen.class);
        startActivity(intent);
}
    public void hotspot(){
        builder.setMessage("you need to turn OFF HotSpot")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                        intent.setComponent(cn);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
    AlertDialog    alert = builder.create();
        alert.setTitle("Turn OFF HotSpot");
        alert.show();

     }

    public void dialog(){


         builder.setMessage("you need to turn on WiFI")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                        startActivityForResult(panelIntent, 1);
                             alert.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         dialog.cancel();

                    }
                });
           alert = builder.create();
         alert.setTitle("Turn On WIFI");
        alert.show();

    }






    @RequiresApi(api = Build.VERSION_CODES.R)
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 111);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
             ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
    }

}