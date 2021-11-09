package com.example.tmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {
    private RelativeLayout LoginButton;
    private long backkeypressedtime = 0;
    private TextView signupin;

    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backkeypressedtime + 2000){
            backkeypressedtime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로가기\' 버튼을 한번더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backkeypressedtime +2000){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        LoginButton = findViewById(R.id.login);
        signupin = findViewById(R.id.signupin);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        });

        signupin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, SignupPage1.class);
                startActivity(intent);
            }
        });

    }
}
