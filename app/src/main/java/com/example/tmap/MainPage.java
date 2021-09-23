package com.example.tmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Retrofit;

public class MainPage extends AppCompatActivity {

    static final int PERMISSIONS_REQQUEST = 0x0000001;
    private final static String appKey = "ceccf9c5a1d9e621829b7385d4e045b9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);



        Button Button = findViewById(R.id.btn1);
        Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("a","777777777777777777777777777777777777777");
                Intent intent = new Intent(getApplicationContext(), MapPage.class);
                if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Log.d("s","12315641984");
                    startActivity(intent);
                }else if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
                    Log.d("d","adfsdfasfas");
                    OnCheckPermission();
                }
            }

        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
             Log.d("f","퍼미션체크"+permissionCheck);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
        }else{
        }
    }

    private void OnCheckPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d("f","퍼미션체크1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Log.d("f","퍼미션체크2");
                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQQUEST);


            } else {
                Log.d("f","퍼미션체크3");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQQUEST);
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
                localBuilder.setTitle("권한 설정")
                        .setMessage("권한 거절로 인해 일부기능이 제한됩니다.\n(권한 설정 방법: 권한 -> 위치 -> 앱 사용중에만 허용)")
                        .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            .setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }
                            }})
                        .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                            }})
                        .create()
                        .show();
            }
        }
    }


}