package com.example.tmap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SignupPage1 extends AppCompatActivity {

    private LinearLayout SignupBtn;
    private CheckBox AllCheck;
    private CheckBox Sign1;
    private CheckBox Sign2;
    private CheckBox Sign3;
    private CheckBox Sign4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        SignupBtn = findViewById(R.id.signupback);


        SignupBtn.bringToFront();

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AllCheck = findViewById(R.id.allcheck);
        Sign1 = findViewById(R.id.sign1);
        Sign2 = findViewById(R.id.sign2);
        Sign3 = findViewById(R.id.sign3);
        Sign4 = findViewById(R.id.sign4);

        AllCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllCheck.isChecked()){
                    Sign1.setChecked(true);
                    Sign2.setChecked(true);
                    Sign3.setChecked(true);
                    Sign4.setChecked(true);
                }
                else {
                    Sign1.setChecked(false);
                    Sign2.setChecked(false);
                    Sign3.setChecked(false);
                    Sign4.setChecked(false);
                }
            }
        });

        Sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllCheck.isChecked()){
                    AllCheck.setChecked(false);
                }else if(Sign1.isChecked() && Sign2.isChecked() && Sign3.isChecked() && Sign4.isChecked()){
                    AllCheck.setChecked(true);
                }
            }
        });

        Sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllCheck.isChecked()){
                    AllCheck.setChecked(false);
                }else if(Sign1.isChecked() && Sign2.isChecked() && Sign3.isChecked() && Sign4.isChecked()){
                    AllCheck.setChecked(true);
                }
            }
        });

        Sign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllCheck.isChecked()){
                    AllCheck.setChecked(false);
                }else if(Sign1.isChecked() && Sign2.isChecked() && Sign3.isChecked() && Sign4.isChecked()){
                    AllCheck.setChecked(true);
                }
            }
        });

        Sign4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AllCheck.isChecked()){
                    AllCheck.setChecked(false);
                }else if(Sign1.isChecked() && Sign2.isChecked() && Sign3.isChecked() && Sign4.isChecked()){
                    AllCheck.setChecked(true);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
