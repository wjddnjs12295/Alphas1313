package com.example.tmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amar.library.ui.StickyScrollView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.skt.Tmap.TMapPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdercheckPage extends AppCompatActivity {

    private LinearLayout backbtn;
    private TextView name;
    private TextView price;
    private TextView totalprice;
    private String essprice;
    private String sleprice;
    private Integer intorginalprice;
    private Integer intessprice;
    private Integer intselprice;
    private Integer total;
    private ImageView image;
    private Bitmap bitmap;
    private RecyclerView eRecycleView;
    private RecyclerView.Adapter eAdapter;
    private RecyclerView.LayoutManager eLayoutManager;
    private RecyclerView sRecycleView;
    private RecyclerView.Adapter sAdapter;
    private RecyclerView.LayoutManager sLayoutManager;
    RequestQueue queue;
    private JSONArray JSONArray = null;
    private boolean Click = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordercheck_page);
        name = findViewById(R.id.itemname);
        price = findViewById(R.id.itemprice);
        image = findViewById(R.id.itemimage);
        backbtn = findViewById(R.id.checkback);
        totalprice = findViewById(R.id.totalprice);

        eRecycleView = findViewById(R.id.essential);
        eRecycleView.setHasFixedSize(true);
        eLayoutManager = new LinearLayoutManager(this);
        eRecycleView.setLayoutManager(eLayoutManager);


        sRecycleView = findViewById(R.id.selection);
        sRecycleView.setHasFixedSize(true);
        sLayoutManager = new LinearLayoutManager(this);
        sRecycleView.setLayoutManager(sLayoutManager);
        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String menus = bundle.getString("titlekey");
        String menus1 = bundle.getString("contentkey");
        String menus2 = bundle.getString("imagekey");
        String option = bundle.getString("optionkey");
        Log.d("key확인용", menus);
        Log.d("key확인용", menus1);
        Log.d("key확인용", menus2);
        Log.d("옵션이지!!!!", "" + option);


        List<EssentialData> essentialData = new ArrayList<>();
        List<SelectionData> selectionData = new ArrayList<>();
        try {
            JSONArray = new JSONArray(option);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(JSONArray == null){
            return;
        }
        for (int index = 0 ; index < JSONArray.length(); index++) {
            JSONObject eachOption = null;
            try {
                eachOption = JSONArray.getJSONObject(index);
                //필수 선택으로 나눠서 저장
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Log.d("옵션 : " + index, ""+eachOption);
            EssentialData etData = new EssentialData();
            SelectionData stData = new SelectionData();
            try {
                String a = eachOption.getString("option_required");
//                Log.d("옵션?", ""+a);
                if (a == "true") {
                    Log.d("필수"+index, "" + eachOption);
                    etData.setETitle(eachOption.getString("option_name"));
                    etData.setEPrice(eachOption.getString("option_price"));
                    Log.d("s","1231231");
                    essentialData.add(etData);
                }
                if (a == "false"){
                    Log.d("선택"+index,""+eachOption);
                    stData.setSTitle(eachOption.getString("option_name"));
                    stData.setSPrice(eachOption.getString("option_price"));
                    selectionData.add(stData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        eAdapter = new EssentialAdapter(essentialData, OrdercheckPage.this);
        eRecycleView.setAdapter(eAdapter);
        sAdapter = new SelectionAdapter(selectionData, OrdercheckPage.this);
        sRecycleView.setAdapter(sAdapter);

//        EssentialAdapter essentialAdapter = new EssentialAdapter(essentialData, OrdercheckPage.this);
//        eRecycleView.setAdapter(essentialAdapter);
//        SelectionAdapter selectionAdapter = new SelectionAdapter(selectionData, OrdercheckPage.this);
//        sRecycleView.setAdapter(selectionAdapter);

        intorginalprice = Integer.parseInt(menus1);
        totalprice.setText(menus1);
        total = intorginalprice;

//        essentialAdapter.setOnItemClickListener(new EssentialAdapter.OnItemClickEventListener() {
//            @Override
//            public void onItemClick(View a_view, int a_position) {
//                Click = !Click;
//                if (Click) {
//                    Log.d("클릭", "" + essentialData.get(a_position).getEPrice());
//                    essprice = essentialData.get(a_position).getEPrice();
//                    intessprice = Integer.parseInt(essprice);
//                        total += intessprice;
//                        totalprice.setText(total.toString());
//                }
//            }
//        });
//        selectionAdapter.setOnItemClickListener(new SelectionAdapter.OnItemClickEventListener() {
//            @Override
//            public void onItemClick(View b_view, int a_position) {
//                Click = !Click;
//                if (Click) {
//                    Log.d("클릭", "" + selectionData.get(a_position).getSPrice());
//                    sleprice = selectionData.get(a_position).getSPrice();
//                    intselprice = Integer.parseInt(sleprice);
//                    total += intselprice;
//                    String a = String.format("%,d", total);
//                    totalprice.setText(a);
//                }
//                else {
//                    sleprice = selectionData.get(a_position).getSPrice();
//                    intselprice = Integer.parseInt(sleprice);
//                    total -= intselprice;
//                    String a = String.format("%,d", total);
//                    totalprice.setText(a);
//                }
//            }
//        });



        name.setText(menus);
        price.setText(menus1);
        Uri uri = Uri.parse(menus2);
        image.setImageURI(uri);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(menus2);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        mThread.start();
        try {
            //join -> 쓰레드 종료시까지
            mThread.join();
            image.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        backbtn.bringToFront();
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}