package com.example.filesharing.sharing_backend;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.filesharing.R;

public class MyActivity extends BaseActivity {

    private static final int CODE_REQ_PERMISSIONS = 665;

    private final ActivityResultLauncher<Intent> activityResultLauncherR =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()) {
                                    requestRequiredPermission();
                                } else {
                                    showToast("Lack of permission, please grant permission first");
                                }
                            }
                        }
                    });

    private void requestRequiredPermission() {
        ActivityCompat.requestPermissions(MyActivity.this,
                new String[]{Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findViewById(R.id.btnCheckPermission).setOnClickListener(v ->
                checkPermission());
        findViewById(R.id.btnSender).setOnClickListener(v ->
                startActivity(new Intent(MyActivity.this, SendFileActivity.class)));
        findViewById(R.id.btnReceiver).setOnClickListener(v ->
                startActivity(new Intent(MyActivity.this, ReceiveFileActivity.class)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQ_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    showToast("Lack of permission, please grant permission first");
                    return;
                }
            }
            showToast("Permission granted");
        }
    }

    private void openPermissionIntentForApi30() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
            activityResultLauncherR.launch(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activityResultLauncherR.launch(intent);
        }
    }

    public void checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()){
                requestRequiredPermission();
            }else {
                openPermissionIntentForApi30();
            }

        }else {
            ActivityCompat.requestPermissions(MyActivity.this,
                    new String[]{Manifest.permission.CHANGE_NETWORK_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
        }
    }
}