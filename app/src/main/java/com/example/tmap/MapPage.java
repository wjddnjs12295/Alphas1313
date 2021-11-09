package com.example.tmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapPage extends AppCompatActivity {

    // 포인트 == 위경도 나타내는 클래스
    // 아이템 == 지도에 마커를 표시하기 위한 클래스

    //T맵키 작성
    private static final String mApiKey = "l7xxa1a4006f04c0457490f37c791bcf534e";
    //POI검색, 경로검색 등의 지도데이터를 관리하는 클래스
    private TMapData tmapdata = null;
    //맵의 기능저장
    private TMapView mMapView = null;

    private Context mContext = null;

    //맵퍼미션관리
    PermissionManager mPermissionManager = null;
    //기기 위치정보 퍼미션관련
    private static final int GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE = 101;
    public static final int DEFAULT_LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    public static final long DEFAULT_LOCATION_REQUEST_INTERVAL = 20000L;
    public static final long DEFAULT_LOCATION_REQUEST_FAST_INTERVAL = 10000L;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    //기기 맵위경도 값 저장
    private double toplon, toplat;
    //메인메뉴에서 받아온 위경도값 저장
    private Double latitude = null;
    private Double longitude = null;



    //뒤로가기버튼
    private Button mapback;


    //티맵 마커 아이디 저장
    String strID;

    //맵중심 반경2km내 가게ID정보 저장
    private final ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    //맵중심 반경2km내 가게포인트 저장
    private final ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    //선택된 가게포인트 저장
    private final ArrayList<MapPoint> selectedPoint = new ArrayList<MapPoint>();

    //총 거리 및 시간 저장
    private String totalDistance = null;
    private String totalTime = null;
    //총 거리 및 도착예정시간 알림
    private TextView routeDistance;
    private TextView routeTime;

    //맵 중심포인트 저장
    private TMapPoint centerPoint = null;
    //마커핀 위경도 저장
    private Double maplat = null;
    private Double maplon = null;

    //가게 클릭시 나오는 가게이미지
    Bitmap storeimagebitmap;
    //가게이름 저장
    private String store_name = null;
    //가게위도 저장
    private Double store_lat;
    //가게경도 저장
    private Double store_lon;
    //가게 이미지url저장
    private String store_url;
    //가게 아이디저장
    private String store_id;
    //가게이미지 저장HashMap
    private Map<String, String> urlMap;
    //가게아이디 저장HashMap
    private Map<String, String> urlMap1;
    //맵중심 모든 가게정보 저장HashMap
    private Map<String, String> urlMap2;
    //서버데이터 배열저장
    JSONArray jsonArray;
    JSONObject jsonObject;
    //Json 가게이름 저장용
    private String storeName;
    //가게이미지 뷰
    private ImageView store_image;
    //가게 메뉴정보 저장 후 Order페이지에 넣기
    String result;
    //가게 가제정보 저장 후 Order페이지에 넣기
    String result1;
    //선택된 가게 클릭시 나오는 가게정보 이름저장
    private TextView mapstorename;
    //가게 이름 저장
    private String storename;


    //현재위치 이동 버튼
    private ImageView locationBtn;
    //내위치로 이동하는 버튼 클릭여부
    private boolean locationClik = false;



    //맵에 표시되는 가게아이템
    private TMapMarkerItem storeItem = new TMapMarkerItem();
    //내위치 마커이미지저장
    private Bitmap locationbit = null;
    //내위치 아이템
    private TMapMarkerItem locationItem = new TMapMarkerItem();
    //내위치에서 선택된 가게까지 거리 및 시간계산하기 위해 도착지점 포인트 저장
    private TMapPoint endPoint = null;
    private final TMapMarkerItem endItem = new TMapMarkerItem();
    //맵 중심 위경도 저장 포인트
    private TMapPoint tpoint = null;

    //선택된 가게 중심 위경도 저장 포인트
    private TMapPoint selpoint = null;
    //마지막에 눌렸던 선택되어있는 마커 아이디
    private String selectedMarkerID = "";
    private TMapMarkerItem selectedMarkerItem;
    //눌려지는 마커 아이디
    private String willSelectPointID = "";
    //선택안된 마커아이템 정보
    customView custom;
    Bitmap bi;
    //선택된 마커아이템 정보
    clickcustomView clickcustom;
    Bitmap cbi;
    //선택된 마커 중 맵 이동시 남아있는 마커아이템정보
    clickcustomView clickcustom1;
    Bitmap cbi1;


    //맵페이지 시작시 3초동안 나오는 이미지
    LinearLayout timeimage;
    //커스텀 토스트메세지
    Toast toast = null;

    //줌레벨 터치전 후 비교값
    int getzoomLevel = 0;
    int willzoomLevel = 0;

    /**
     * 내위치 활성화 버튼
     */
    public void onClickLocationBtn(View v) {
        locationClik = !locationClik;
        checkLocationPermission();
        if (locationClik) {
            mMapView.setIconVisibility(locationClik);
            locationBtn.setBackgroundResource(R.drawable.location_btn);
        }
        locationBtn.setBackgroundResource(R.drawable.location_btn_sel);
        locationbit = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot1);
        mMapView.setIcon(locationbit);
        locationItem.setIcon(locationbit);
        mMapView.bringMarkerToFront(locationItem);
        tpoint.setLongitude(toplon);
        tpoint.setLatitude(toplat);
        mMapView.setCenterPoint(toplon, toplat);/**  서버 URL설정   */
        mMapView.removeAllMarkerItem();
        String url = "http://3.34.43.28:5000/api/map/getNearStores";
        maplat = mMapView.getCenterPoint().getLatitude();
        maplon = mMapView.getCenterPoint().getLongitude();
        // AsyncTask를 통해 HttpURLConnection 수행.
        m_mapPoint.clear();
        NetworkTask networkTask = new NetworkTask(url, maplat, maplon);
        networkTask.execute();
    }
    /**
     * 내위치 활성화 버튼
     */


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_page);
        timeimage = findViewById(R.id.timeimage);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                // 시간 지난 후 실행할 코딩
                timeimage.setVisibility(View.GONE);
            }
        }, 1700);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        urlMap = new HashMap<String, String>();
        urlMap1 = new HashMap<String, String>();
        urlMap2 = new HashMap<String, String>();

        totalDistance = null;
        totalTime = null;
        mContext = this;

        mapback = findViewById(R.id.mapback);

        mapback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mMapView = new TMapView(this);
        mMapView.setHttpsMode(true);
        mMapView.setCenterPoint(longitude, latitude);


        locationBtn = findViewById(R.id.location_btn);

        routeDistance = findViewById(R.id.route_distance);
        routeTime = findViewById(R.id.route_time);
        mapstorename = findViewById(R.id.map_storename);


        //Tmap 각종 객체 선언
        tmapdata = new TMapData(); //POI검색, 경로검색 등의 지도데이터를 관리하는 클래스
        RelativeLayout relativeLayout = findViewById(R.id.mapview);
        RelativeLayout relativeLayout1 = findViewById(R.id.mapui);
        RelativeLayout relativeLayout2 = findViewById(R.id.mapui1);


        relativeLayout.addView(mMapView);
        relativeLayout1.bringToFront();
        relativeLayout2.bringToFront();
        mMapView.setSKTMapApiKey(mApiKey);
        store_image = findViewById(R.id.store_image);
        GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.background_rounding);
        store_image.setBackground(drawable);
        store_image.setClipToOutline(true);
        /** 줌레벨 */
        mMapView.setZoomLevel(16);
        /** 지도 타입 */
        mMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        /** 언어 설정 */
        mMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        mPermissionManager = new PermissionManager();
        locationBtn.setBackgroundResource(R.drawable.location_btn_sel);

        String url = "http://3.34.43.28:5000/api/map/getNearStores";
        maplat = mMapView.getCenterPoint().getLatitude();
        maplon = mMapView.getCenterPoint().getLongitude();
        // AsyncTask를 통해 HttpURLConnection 수행.
        m_mapPoint.clear();
        NetworkTask networkTask = new NetworkTask(url, maplat, maplon);
        networkTask.execute();



        mContext = this;

        getzoomLevel = mMapView.getZoomLevel();


        /**  맵 클릭시   */
        mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                mMapView.removeAllTMapPolyLine();
                locationBtn.setBackgroundResource(R.drawable.location_btn);
                willzoomLevel = mMapView.getZoomLevel();
                /**  서버 URL설정   */
                if (getzoomLevel == willzoomLevel) {
                    /** 마커 클릭시  */
                    if (markerlist.size() != 0 && centerPoint.equals(mMapView.getCenterPoint())) {
                        willSelectPointID = markerlist.get(0).getID();

                        if (!selectedMarkerID.equals((markerlist.get(0).getID()))) {
                            Thread mThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL(urlMap.get(markerlist.get(0).getID()));
                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setDoInput(true);
                                        conn.connect();
                                        InputStream is = conn.getInputStream();
                                        storeimagebitmap = BitmapFactory.decodeStream(is);
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
                                store_image.setImageBitmap(storeimagebitmap);
                                mapstorename.setText(markerlist.get(0).getName());
                                clickcustom = new clickcustomView(MapPage.this, markerlist.get(0).getName());
                                relativeLayout1.setVisibility(View.VISIBLE);

                                TMapPoint point4 = markerlist.get(0).getTMapPoint();
                                endPoint = point4;

                                View cv = clickcustom.getView();
                                cbi = createBitmapFromLayout(cv);
                                locationBtn.setBackgroundResource(R.drawable.location_btn);
                                drawPedestrianPath();

                                selectedMarkerItem = markerlist.get(0);
                                showSelectedMarker(markerlist.get(0), willSelectPointID, selectedMarkerID);//, willSelectPointID, selectedMarkerID);

                                relativeLayout1.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        new Thread() {
                                            @Override
                                            public void run() {
                                                try {
                                                    URL url = new URL("http://3.34.43.28:5000/api/store/menus/" + urlMap1.get(selectedMarkerID));
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
                                                    result1 = urlMap2.get(selectedMarkerID);
                                                    Intent intent = new Intent(MapPage.this, OrderPage.class);
                                                    intent.putExtra("menus", result);
                                                    intent.putExtra("store", result1);
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

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    /** 마커 클릭시  */
                    else {

                        if (!centerPoint.equals(mMapView.getCenterPoint())) {
                            if (clickcustom != null) {
                                relativeLayout1.setVisibility(View.VISIBLE);
                                mMapView.removeAllMarkerItem();
                                m_mapPoint.clear();
                                String url = "http://3.34.43.28:5000/api/map/getNearStores";
                                maplat = mMapView.getCenterPoint().getLatitude();
                                maplon = mMapView.getCenterPoint().getLongitude();
                                // AsyncTask를 통해 HttpURLConnection 수행.
                                NetworkTask networkTask = new NetworkTask(url, maplat, maplon);
                                networkTask.execute();
                            } else {
                                /**  서버 URL설정   */
                                mMapView.removeAllMarkerItem();
                                m_mapPoint.clear();
                                String url = "http://3.34.43.28:5000/api/map/getNearStores";
                                maplat = mMapView.getCenterPoint().getLatitude();
                                maplon = mMapView.getCenterPoint().getLongitude();
                                // AsyncTask를 통해 HttpURLConnection 수행.
                                NetworkTask networkTask = new NetworkTask(url, maplat, maplon);
                                networkTask.execute();

                            }
                        } else {
                            if (selectedMarkerItem != null) {
                                selectedPoint.clear();
                                mMapView.removeMarkerItem(selectedMarkerItem.getID());
                                relativeLayout1.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (selectedMarkerItem.getName().equals(m_mapPoint.get(i).getName())) {
                                        showSelectedMarkerPoint(0);
                                        break;
                                    } else {
                                        mMapView.removeMarkerItem(selectedMarkerItem.getID());
                                    }
                                }
                                //여기
                                custom = new customView(MapPage.this, selectedMarkerItem.getName());
                                View v = custom.getView();
                                bi = createBitmapFromLayout(v);
                                selectedMarkerItem.setIcon(bi);
                                selectedMarkerID = "";
                                clickcustom = null;
                                selectedMarkerItem = null;
                            }
                        }
                    }
                }
                return false;
            }


            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                centerPoint = mMapView.getCenterPoint();
                getzoomLevel = mMapView.getZoomLevel();
                return false;
            }
        });
        /**  맵 클릭시   */
        mMapView.setHttpsMode(true);
    }

    /**
     * 뒤로가기 이벤트
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapPage.this, MainPage.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
        startActivity(intent);  //인텐트 이동
        finish();
    }

    /**
     * 뒤로가기 이벤트
     */

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        int accessLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessLocation == PackageManager.PERMISSION_GRANTED) {
            checkLocationSetting();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkLocationSetting();
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        builder.setTitle("위치 권한이 꺼져있습니다.");
                        builder.setMessage("[권한] 설정에서 위치 권한을 허용해야 합니다.");
                        builder.setPositiveButton("설정으로 가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.getWindow().setGravity(Gravity.CENTER);
                        alert.show();
                    }
                    break;
                }
            }
        }
    }

    private void checkLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(DEFAULT_LOCATION_REQUEST_PRIORITY);
        locationRequest.setInterval(DEFAULT_LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_LOCATION_REQUEST_FAST_INTERVAL);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapPage.this);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                })
                .addOnFailureListener(MapPage.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MapPage.this, GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sendIntentException) {
                                    sendIntentException.printStackTrace();
                                }
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkLocationSetting();
            } else {
                finish();
            }
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            toplon = locationResult.getLastLocation().getLongitude();
            toplat = locationResult.getLastLocation().getLatitude();
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }
    };

    /**
     * 서버데이터
     */
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String NTurl;
        private Double NTlat;
        private Double NTlon;

        public NetworkTask(String url, Double lat, Double lon) {

            this.NTurl = url;
            this.NTlat = lat;
            this.NTlon = lon;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            RequestHttpConnection requestHttpURLConnection = new RequestHttpConnection();
            result = requestHttpURLConnection.request(NTurl, NTlat, NTlon); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            String temp = JSONParse(s);
        }

        @RequiresApi(api = Build.VERSION_CODES.R)
        String JSONParse(String jsonStr) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                jsonArray = new JSONArray(jsonStr);

                /**
                 * 근처 가게 없을때 나오는 토스트 메세지
                 */
                if (jsonArray.length() == 0) {
                    LayoutInflater inflater = getLayoutInflater();
                    View toastDesign = inflater.inflate(R.layout.map_dialog, (ViewGroup) findViewById(R.id.toastbox)); //toast_design.xml 파일의 toast_design_root 속성을 로드

                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastDesign);
                    toastDesign.setMinimumHeight(70);
                    toastDesign.setMinimumWidth(580);
                    toast.show();
                }
                /**
                 * 근처 가게 없을때 나오는 토스트 메세지
                 */

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    store_name = jsonObject.getString("store_name");
                    store_lat = jsonObject.getDouble("store_lat");
                    store_lon = jsonObject.getDouble("store_lon");
                    store_url = jsonObject.getString("store_thumb_url");
                    store_id = jsonObject.getString("_id");
                    Log.d("루트확인", "" + jsonObject);


//                    addPoint();
                    showMarkerPoint(i);
                }
                showSelectedMarkerPoint(0);
                Log.d("json", "" + stringBuilder);

                return storeName;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    /**
     * 서버데이터
     */


    /**
     * 비트맵 생성
     */
    private Bitmap createBitmapFromLayout(View tv) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(spec, spec);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(tv.getMeasuredWidth(), tv.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate((-tv.getScrollX()), (-tv.getScrollY()));
        tv.draw(c);
        return b;
    }
    /**  비트맵 생성  */


    /**
     * 선택된 마커처리
     */

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showSelectedMarker(TMapMarkerItem item, String willSelectPointID, String selectedID) {
        storeItem = new TMapMarkerItem();
        if (selectedID != "") {
            View v = new customView(this, mMapView.getMarkerItemFromID(selectedID).getName()).getView();
            bi = createBitmapFromLayout(v);
            mMapView.getMarkerItemFromID(selectedID).setIcon(bi);
        }
        item.setIcon(cbi);
        selectedMarkerID = willSelectPointID;
    }

    /**
     * 선택된 마커처리
     */

//    /**
//     * 핀 데이터
//     */

//    public void addPoint() {
//        m_mapPoint.add(new MapPoint(store_name, store_lat, store_lon));
//        Log.d("뤁확인", "" + store_name);
//    }
//    /**   핀 데이터  */

    /**
     * 핀 생성
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showMarkerPoint(int index) {
        m_mapPoint.add(new MapPoint(store_name, store_lat, store_lon));

        tpoint = new TMapPoint(m_mapPoint.get(index).getLatitude(),
                m_mapPoint.get(index).getLongitude());

        storename = m_mapPoint.get(index).getName();

        ///////////////// 기본
        custom = new customView(this, storename);
        View v = custom.getView();
        String t = (String) ((TextView) v.findViewById(R.id.bubble_title)).getText();
        bi = createBitmapFromLayout(v);


        MapPage mContext = this;
        storeItem = new TMapMarkerItem();
        locationItem = new TMapMarkerItem();

        strID = store_id;

        storeItem.setTMapPoint(tpoint);
        storeItem.setName(m_mapPoint.get(index).getName());
        storeItem.setVisible(TMapMarkerItem.VISIBLE);
        storename = m_mapPoint.get(index).getName();
        storeItem.setIcon(bi);
        storeItem.setCalloutTitle(m_mapPoint.get(index).getName());
        mMapView.addMarkerItem(strID, storeItem);
        mArrayMarkerID.add(strID);
        urlMap.put(strID, store_url);
        urlMap1.put(strID, store_id);
        urlMap2.put(strID, jsonObject.toString());
    }
    /**
     * 핀 생성
     */


    /**
     * 선택된 핀 생성
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showSelectedMarkerPoint(int index) {
        if (selectedMarkerItem != null) {
            selectedPoint.clear();
            String sename = selectedMarkerItem.getName();
            Double selat = selectedMarkerItem.latitude;
            Double selon = selectedMarkerItem.longitude;
            selectedPoint.add(new MapPoint(sename, selat, selon));
            selpoint = new TMapPoint(selectedPoint.get(0).getLatitude(),
                    selectedPoint.get(0).getLongitude());
            clickcustom1 = new clickcustomView(this, sename);
            View cv = clickcustom1.getView();
            String ct = (String) ((TextView) cv.findViewById(R.id.bubble_title)).getText();
            cbi1 = createBitmapFromLayout(cv);

            selectedMarkerItem.setTMapPoint(selpoint);
            selectedMarkerItem.setName(selectedPoint.get(0).getName());
            selectedMarkerItem.setVisible(TMapMarkerItem.VISIBLE);
            selectedMarkerItem.setIcon(cbi1);
            selectedMarkerItem.setCalloutTitle(selectedPoint.get(0).getName());
            mMapView.addMarkerItem(selectedMarkerID, selectedMarkerItem);
            mMapView.bringMarkerToFront(selectedMarkerItem);
        }
    }
    /**
     * 선택된 핀 생성
     */


    /**
     * 길찾기실행
     */
    public void drawPedestrianPath() {
        findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH);
    }
    /**   길찾기실행   */

    /**
     * 보행자경로
     */
    private String getContentFromNode(Element item, String tagName) {
        NodeList list = item.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            if (list.item(0).getFirstChild() != null) {
                return list.item(0).getFirstChild().getNodeValue();
            }
        }
        return null;
    }
    /**   보행자경로   */


    /**
     * 타입에 따른 길찾기(현재 도보코드만 적용)
     */
    private void findPathDataAllType(final TMapData.TMapPathType type) {
        totalDistance = null;
        totalTime = null;

//        TMapPoint point1 = mMapView.getCenterPoint();

        TMapPoint point1 = new TMapPoint(latitude, longitude);
        TMapPoint point2 = endPoint;
        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAllType(type, point1, point2, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                TMapPolyLine polyline = new TMapPolyLine();
                if (doc != null) {
                    NodeList list = doc.getElementsByTagName("Document");
                    Element storeinfo = (Element) list.item(0);
                    totalDistance = getContentFromNode(storeinfo, "tmap:totalDistance");
                    totalTime = getContentFromNode(storeinfo, "tmap:totalTime");

                    NodeList list2 = doc.getElementsByTagName("LineString");

                    for (int i = 0; i < list2.getLength(); i++) {
                        Element item = (Element) list2.item(i);
                        String str = getContentFromNode(item, "coordinates");
                        if (str == null) {
                            continue;
                        }

                        String[] str2 = str.split(" ");
                        for (int k = 0; k < str2.length; k++) {
                            try {
                                String[] str3 = str2[k].split(",");
                                TMapPoint point = new TMapPoint(Double.parseDouble(str3[1]), Double.parseDouble(str3[0]));
                                polyline.addLinePoint(point);
                            } catch (Exception e) {
                            }
                        }
                    }
                    TMapInfo info = mMapView.getDisplayTMapInfo(polyline.getLinePoint());

                    int totalSec = Integer.parseInt(totalTime);
                    int day = totalSec / (60 * 60 * 24);
                    int hour = (totalSec - day * 60 * 60 * 24) / (60 * 60);
                    int minute = (totalSec - day * 60 * 60 * 24 - hour * 3600) / 60;
                    String time = null;
                    if (hour > 0) {
                        time = hour + "시간 " + minute + "분";
                    } else {
                        time = minute + "분 ";
                    }

                    if (Double.parseDouble(totalDistance) < 1000) {
                        double km = Double.parseDouble(totalDistance) / 1000;
                        String km1 = String.format("%.1f", km);
                        routeDistance.setText(km1 + "km");
                    } else {
                        double km = Double.parseDouble(totalDistance) / 1000;
                        String km1 = String.format("%.1f", km);
                        routeDistance.setText(km1 + "km");
                    }

                    if (Double.parseDouble(totalDistance) < 500) {
                        double km = Double.parseDouble(totalDistance) / 1000;
                        String km1 = String.format("%.1f", km);
                        routeDistance.setText(km1 + "km");
                    }
                    routeTime.setText(time);

                    endItem.setTMapPoint(point2);
                    endItem.setVisible(TMapMarkerItem.VISIBLE);
                    endItem.setIcon(cbi);
                }
            }
        });
    }
    /**  타입에 따른 길찾기(현재 도보코드만 적용)*/

}
