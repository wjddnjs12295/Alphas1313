package com.example.tmap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreinfoPage extends AppCompatActivity {

    private String store_reg_no = null;
    private String store_ceo_name = null;
    private String store_name = null;
    private String store_origin_info = null;
    private TextView name = null;
    private TextView regno = null;
    private TextView ceoname = null;
    private TextView origin_info = null;
    private TextView name1 = null;
    private Button backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storeinfo_page);

        backbtn = findViewById(R.id.infoback);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String info = bundle.getString("info");
        String storename1 = bundle.getString("name");

        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(info);
            store_reg_no = jsonObject1.getString("store_reg_no");
            store_name = jsonObject1.getString("store_name");
            store_ceo_name = jsonObject1.getString("store_ceo_name");
            store_origin_info = jsonObject1.getString("store_origin_info");
            Log.d("regno", store_reg_no);
            Log.d("2", store_name);
            Log.d("3", store_ceo_name);
            Log.d("4", store_origin_info);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        name1 = findViewById(R.id.storename1);
        name = findViewById(R.id.store_name2);
        ceoname = findViewById(R.id.store_ceo_name);
        regno = findViewById(R.id.store_reg_no);
        origin_info = findViewById(R.id.store_origin_info);

        name.setText(store_name);
        name1.setText(storename1);
        ceoname.setText(store_ceo_name);
        regno.setText(store_reg_no);
        origin_info.setText(store_origin_info);
    }
}
