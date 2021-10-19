package com.example.tmap;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amar.library.ui.StickyScrollView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderPage extends AppCompatActivity {


    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView menuRecycleView;
    private RecyclerView.Adapter menuAdapter;
    private RecyclerView.Adapter menuAdapter1;
    private RecyclerView.LayoutManager menuLayoutManager;

    private View menuLayout;
    private LinearLayout cell;

    RequestQueue queue;

    private Button backbtn;
    private Button storeinfobtn;
    private LinearLayout orderbox;
    private StickyScrollView scrollView;
    private TextView storename;
    private TextView storehour;
    private TextView storetel;
    private TextView storeaddress;
    private ImageView storeimage;
    private String store_name = null;
    private String store_tel = null;
    private String store_image = null;
    private String store_hour = null;
    private String store_address = null;
    private String store_id = null;
    Bitmap bitmap;
    private String result = null;
    private String categoryid = null;
    private String categoryitem = null;
    private String categoryitem1[];
    private String option = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);

        mRecycleView = findViewById(R.id.categorytitle);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        scrollView = findViewById(R.id.scrollView);
        menuRecycleView = findViewById(R.id.menutitle);
        menuRecycleView.setHasFixedSize(true);
        menuLayoutManager = new LinearLayoutManager(this);
        menuRecycleView.setLayoutManager(menuLayoutManager);
        backbtn = findViewById(R.id.backbtn);
        orderbox= findViewById(R.id.OrderBox);

        orderbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderPage.this, ShoppingPage.class);
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        queue = Volley.newRequestQueue(this);

        Intent intent1 = getIntent();

        Bundle bundle1 = intent1.getExtras();
        String store = bundle1.getString("store");
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(store);
            Log.d("스토어",jsonObject1.toString());
            store_id = jsonObject1.getString("_id");
            store_name = jsonObject1.getString("store_name");
            store_tel = jsonObject1.getString("store_tel");
            store_image = jsonObject1.getString("store_thumb_url");
            store_hour = jsonObject1.getString("store_business_hour");
            store_address = jsonObject1.getString("store_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        storename = findViewById(R.id.storename);
        storeimage = findViewById(R.id.storeimage);
        storehour = findViewById(R.id.storehour);
        storetel = findViewById(R.id.storetel);
        storeaddress = findViewById(R.id.storeaddress);

        storeinfobtn = findViewById(R.id.storeinfo);

        storeinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://3.34.43.28:5000/api/store/businessinfo/" + store_id);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setDoInput(true); //데이터를 읽어올지 설정
                            InputStream is = connection.getInputStream();
                            StringBuilder sb = new StringBuilder();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            result = null;
                            while ((result = br.readLine()) != null) {
                                sb.append(result + "\n");
                            }
                            result = sb.toString();
                            Intent intent = new Intent(OrderPage.this, StoreinfoPage.class);
                            intent.putExtra("info", result);
                            intent.putExtra("name", store_name);
                            startActivity(intent);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


        storename.setText(store_name);
        storehour.setText(store_hour);
        storetel.setText(store_tel);
        storeaddress.setText(store_address);


        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(store_image);
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

            storeimage.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String menus = bundle.getString("menus");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(menus);
//            Log.d("Object확인", String.valueOf(jsonObject));

            JSONArray arrayCategory = jsonObject.getJSONArray("store_categories");
            List<OrderTitleData> categoryData = new ArrayList<>();
            for (int i = 0, j = arrayCategory.length(); i < j; i++) {
                JSONObject categoriesobj = arrayCategory.getJSONObject(i);

                OrderTitleData orderTitleData = new OrderTitleData();
                orderTitleData.setTitle(categoriesobj.getString("category_name"));
                categoryitem = categoriesobj.getString("category_name");
                categoryitem1 = new String[arrayCategory.length()];
                categoryitem1[i] = categoryitem;
                categoryData.add(orderTitleData);
            }
            mAdapter = new CategoryAdapter(categoryData, OrderPage.this);
            mRecycleView.setAdapter(mAdapter);


            List<OrderMenuData> menuData = new ArrayList<>();
//
//            JSONArray arrayoption = jsonObject.getJSONArray("store_options");
//            Log.d("무야호 : ", ""+arrayoption);
//            for (int i = 0, j = arrayoption.length(); i < j; i++) {
//                JSONObject optionobj = arrayoption.getJSONObject(i);
//                OrderMenuData orderMenuData = new OrderMenuData();
//                Log.d("옵션!",optionobj.toString());
//                option = optionobj.toString();
//            }
//
//            JSONArray arraymenu = jsonObject.getJSONArray("store_menus");
//
//            for (int i = 0, j = arraymenu.length(); i < j; i++) {
//                JSONObject menuobj = arraymenu.getJSONObject(i);
//                Log.d("메뉴!!", menuobj.toString());
//                OrderMenuData orderMenuData = new OrderMenuData();
//                orderMenuData.setName(menuobj.getString("menu_name"));
//                orderMenuData.setPrice(menuobj.getString("menu_price"));
//                orderMenuData.setUrlToImage(menuobj.getString("menu_thumb_url"));
////                orderMenuData.setOption(option);
//                orderMenuData.setMenuid(store_id);
//
//                menuData.add(orderMenuData);
//            }
//
//            menuAdapter = new MenuAdapter(menuData, OrderPage.this);
//            menuRecycleView.setAdapter(menuAdapter);

            JSONArray arraymenu = jsonObject.getJSONArray("store_menus");
            JSONArray arrayoption = jsonObject.getJSONArray("store_options");
            for (int i = 0, j = arraymenu.length(); i < j; i++) {
                JSONObject menuobj = arraymenu.getJSONObject(i);
                OrderMenuData orderMenuData = new OrderMenuData();
                orderMenuData.setName(menuobj.getString("menu_name"));
                orderMenuData.setPrice(menuobj.getString("menu_price"));
                orderMenuData.setUrlToImage(menuobj.getString("menu_thumb_url"));
                String menuid = menuobj.getString("_id");
                String categoryid = menuobj.getString("category_id");
                for (int a = 0, b = arrayoption.length(); a < b; a++) {
                    JSONObject optionobj = arrayoption.getJSONObject(a);
                    String optionid = optionobj.getString("menu_id");
                    if (menuid.equals(optionid)) {
                        orderMenuData.setOption(arrayoption.toString());
                    }
                }
                menuData.add(orderMenuData);
            }

            menuAdapter = new MenuAdapter(menuData, OrderPage.this);
            menuRecycleView.setAdapter(menuAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}