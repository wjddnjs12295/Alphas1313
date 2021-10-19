package com.example.tmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class splash extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    //    static final int PERMISSIONS_REQQUEST = 0x0000001;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceStare) {
//        super.onCreate(savedInstanceStare);
//        setContentView(R.layout.activity_splash);
//
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                Intent intent = new Intent(getApplicationContext(), MainPage.class);
////                startActivity(intent);
////                finish();
//                OnCheckPermission();
//            }
//        },3000);
//    }
//    @Override
//    protected void  onResume() {
//        super.onResume();
//
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }
//
//    private void OnCheckPermission() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            Log.d("f","퍼미션체크1");
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
//                Log.d("f","퍼미션체크2");
//                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다.", Toast.LENGTH_SHORT).show();
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        PERMISSIONS_REQQUEST);
//                Intent intent = new Intent(getApplicationContext(), MainPage.class);
//                startActivity(intent);
//
//            } else {
//                Log.d("f","퍼미션체크3");
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        PERMISSIONS_REQQUEST);
////                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
////                localBuilder.setTitle("권한 설정")
////                        .setMessage("권한 거절로 인해 일부기능이 제한됩니다.")
////                        .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener(){
////                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
////                                try {
////                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
////                                            .setData(Uri.parse("package:" + getPackageName()));
////                                    startActivity(intent);
////                                } catch (ActivityNotFoundException e) {
////                                    e.printStackTrace();
////                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
////                                    startActivity(intent);
////                                }
////                            }})
////                        .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
////                            }})
////                        .create()
////                        .show();
//            }
//        }
//
//    }
//}
    private final boolean permissionCheck = false;
    static final int PERMISSIONS_REQQUEST = 0x0000001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private void requestSapPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

        } catch (Exception ignored) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        final Intent intent = new Intent(getApplicationContext(), MainPage.class);
        Log.d("tq", "tq");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("adfsasdfsda", "asfsadfasdfsdaf");
                startActivity(intent);
                splash.this.finish();
            }
        }, 3000);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermission()) {
            requestSapPermissions();
        }
        final Intent intent = new Intent(getApplicationContext(), MainPage.class);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("s", "12315641984");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("adfsasdfsda", "asfsadfasdfsdaf");
                    startActivity(intent);
                    splash.this.finish();
                }
            }, 3000);
        }
    }


    @Override
    public void onResume() {
        super.onResume();


//        if(checkPermission()){
        if (permissionCheck) {
            Log.d("afasfsa", "가나다라 : " + permissionCheck);

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    final Intent intent = new Intent(getApplicationContext(), MainPage.class);
//                    startActivity(intent);
//                    splash.this.finish();
//                }
//            }, 3000);
//
//        }
        }
    }
}