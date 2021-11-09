package com.example.tmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
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

import java.util.ArrayList;


public class RoadPage extends AppCompatActivity
        implements TMapGpsManager.onLocationChangedCallback {


    private String storeName;
    private Context mContext = null;
    private boolean m_bTrackingMode = false;

    private LinearLayout routeLayout;

    TMapGpsManager gps = null;
    PermissionManager mPermissionManager = null;

    private TMapData tmapdata = null;
    private TMapView mMapView = null;
    private static final String mApiKey = "l7xxa1a4006f04c0457490f37c791bcf534e";
    private static int mMarkerID;
    private final ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private final ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    private String totalDistance = null;
    private String totalTime = null;


    //마커핀 주소
    private String address;
    //마커핀 위경도
    private final Double lat = null;
    private final Double lon = null;

    // 보행자 전체 경로 버튼
    private ImageButton road;

    //현재위치 트래킹모드 버튼
    private ImageView locationBtn;

    //총 거리 및 도착예정시간 알림
    private TextView routeDistance;
    private TextView routeTime;
    private TextView mapstorename;

    private Double latitude = null;
    private Double longitude = null;

    private TMapPoint endPoint = null;

    private TMapMarkerItem storeItem = new TMapMarkerItem();
    private Bitmap storebit = null;

    private Bitmap locationbit = null;
    private final TMapMarkerItem locationItem = new TMapMarkerItem();

    private Bitmap startbit = null;
    private TMapMarkerItem startItem = new TMapMarkerItem();

    private Bitmap endbit = null;
    private final TMapMarkerItem endItem = new TMapMarkerItem();

    private TMapPoint tpoint = null;
    private String storename;
    customView custom;
    clickcustomView clickcustom;
    Bitmap bi;
    Bitmap cbi;
    String ct;

    private String store_name;
    private Double store_lat;
    private Double store_lon;

    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                mMapView.setLocationPoint(longitude, latitude);
                mMapView.setCenterPoint(longitude, latitude);
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /**
     * 내위치 활성화 버튼
     */
    public void onClickLocationBtn(View v) {
        m_bTrackingMode = !m_bTrackingMode;
        if (m_bTrackingMode) {
            mMapView.setIconVisibility(m_bTrackingMode);
            setTrackingMode(false);
        }
        setTrackingMode(m_bTrackingMode);
        locationbit = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.dot);
        locationItem.setIcon(locationbit);
        mMapView.bringMarkerToFront(locationItem);
    }

    /**
     * setTrackingMode 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */
    public void setTrackingMode(boolean isShow) {
        mMapView.setTrackingMode(isShow);
        if (isShow) {
            mPermissionManager.request(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
                @Override
                public void granted() {
                    if (gps != null) {
                        gps.setMinTime(500);
                        gps.setMinDistance((float) 1);
//                        gps.setProvider(gps.GPS_PROVIDER);
//                        gps.OpenGps();
                        gps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
                        gps.OpenGps();
                        locationbit = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.dot);
                        mMapView.setIcon(locationbit);
                        locationItem.setIcon(locationbit);
                        mMapView.bringMarkerToFront(locationItem);
                        mMapView.setZoomLevel(16);
                    } else {
                    }
                }

                @Override
                public void denied() {
                    Toast.makeText(RoadPage.this, "위치정보 수신에 동의하지 않으시면 현재위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            });
            locationBtn.setBackgroundResource(R.drawable.location_btn_sel);
            mMapView.setCenterPoint(mMapView.getLocationPoint().getLongitude(), mMapView.getLocationPoint().getLatitude());
        } else {
            if (gps != null) {
                gps.CloseGps();
                locationBtn.setBackgroundResource(R.drawable.location_btn);
            }
            locationBtn.setBackgroundResource(R.drawable.location_btn);
        }
    }

    /**
     * setTrackingMode 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road_page);


        totalDistance = null;
        totalTime = null;
        mContext = this;

        mMapView = new TMapView(this);
        mMapView.setHttpsMode(true);

        /**  서버 URL설정   */
        String url = "http://3.34.43.28:5000/api/map/getNearStores";
        Double lat = 35.1723882891497;
        Double lon = 129.12663084721376;
        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, lat, lon);
        networkTask.execute();
        /**  서버 URL설정   */

        //버튼 선언
//        buttonZoomIn = (Button)findViewById(R.id.buttonZoomIn);
//        buttonZoomOut = (Button)findViewById(R.id.buttonZoomOut);
        road = findViewById(R.id.road);
        locationBtn = findViewById(R.id.location_btn);

        routeDistance = findViewById(R.id.route_distance);
        routeTime = findViewById(R.id.route_time);
        mapstorename = findViewById(R.id.map_storename);

        // "확대" 버튼 클릭
//        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMapView.MapZoomIn();
//            }
//        });
//
//        // "축소" 버튼 클릭
//        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMapView.MapZoomOut();
//            }
//        });

        Button imageButton = findViewById(R.id.order);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderPage.class);
                startActivity(intent);
            }
        });

        /**  지도 보기   */
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setTrackingMode(false);
                mMapView.setSightVisible(false);
                setTrackingMode(false);
//                item2.setVisible(TMapMarkerItem.HIDDEN);
                locationBtn.setBackgroundResource(R.drawable.location_btn);
                if (endPoint != null) {
                    drawPedestrianPath();
                    setTrackingMode(false);
                    mMapView.setTrackingMode(false);
                    mMapView.setSightVisible(false);
                }
            }
        });
        /**  지도 보기   */

        //Tmap 각종 객체 선언
        tmapdata = new TMapData(); //POI검색, 경로검색 등의 지도데이터를 관리하는 클래스
        RelativeLayout relativeLayout = findViewById(R.id.mapview);
        RelativeLayout relativeLayout1 = findViewById(R.id.mapui);
        RelativeLayout relativeLayout2 = findViewById(R.id.mapui1);

        relativeLayout.addView(mMapView);
        relativeLayout1.bringToFront();
        relativeLayout2.bringToFront();
        mMapView.setSKTMapApiKey(mApiKey);
        mMapView.setTrackingMode(true);

        gps = new TMapGpsManager(RoadPage.this);
        setGps();

        addPoint();
        showMarkerPoint();

        /** 현재 보는 방향 */
//        mMapView .setCompassMode(true);
//        mMapView .setPOIRotate(true);
        /** 시야 표출 */
        /** 현위치 아이콘표시 */
//        mMapView .setIconVisibility(true);
        /** 줌레벨 */
        mMapView.setZoomLevel(16);
        /** 지도 타입 */
        mMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        /** 언어 설정 */
        mMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
//        setTrackingMode(false);

        mPermissionManager = new PermissionManager();

        /**  맵 클릭시   */
        mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                setTrackingMode(false);
                relativeLayout1.setVisibility(View.GONE);
                mMapView.removeAllTMapPolyLine();
                locationBtn.setBackgroundResource(R.drawable.location_btn);
                if (markerlist.size() != 0) {
                    TMapPoint point4 = markerlist.get(0).getTMapPoint();
                    endPoint = point4;
                    mapstorename.setText(markerlist.get(0).getName());
                    clickcustom = new clickcustomView(RoadPage.this, markerlist.get(0).getName());
                    View cv = clickcustom.getView();
                    cbi = createBitmapFromLayout(cv);
                    drawPedestrianPath();
                    routeLayout.setVisibility(View.VISIBLE);
                    locationBtn.setBackgroundResource(R.drawable.location_btn);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    mMapView.setCenterPoint(point4.getLongitude(), point4.getLatitude());
                }
                return false;
            }

        });
        /**  맵 클릭시   */

        mMapView.setHttpsMode(true);
        locationBtn.performClick();
    }


    /**
     * 서버데이터
     */
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private Double lat;
        private Double lon;

        public NetworkTask(String url, Double lat, Double lon) {

            this.url = url;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            RequestHttpConnection requestHttpURLConnection = new RequestHttpConnection();
            result = requestHttpURLConnection.request(url, lat, lon); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            String temp = JSONParse(s);
//            tv_outPut.setText(temp);
        }

        String JSONParse(String jsonStr) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    store_name = jsonObject.getString("store_name");
                    store_lat = jsonObject.getDouble("store_lat");
                    store_lon = jsonObject.getDouble("store_lon");
//                    Log.d("123123123",""+jsonObject);
                    stringBuilder.append("store_name : ").append(store_name + "\n").append("store_lat : ").append(store_lat + "\n").append("store_lon : ").append(store_lon).append("\n");
                    Log.d("lat", "" + stringBuilder);
                }
                return storeName;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    /**   서버데이터   */


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
     * 현재 내위치 바로가기
     */
    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1000, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }
    /**   현재 내위치 바로가기  */

    /**
     * 핀 데이터
     */
    public void addPoint() {
        m_mapPoint.add(new MapPoint("센텀시티", 35.168827, 129.131745));
        m_mapPoint.add(new MapPoint("센텀수영강변2차 e편한세상아파트", 35.18230966959019, 129.11610345559023));
        m_mapPoint.add(new MapPoint("빽다방 샌텀SH밸리점", 35.17280698833681, 129.13104780722543));
        m_mapPoint.add(new MapPoint("커피피커 coffee picker", 35.17331564371104, 129.13001783904735));
        m_mapPoint.add(new MapPoint("거리계산용 핀", 35.17303730130183, 129.12769067717878));

    }
    /**   핀 데이터  */


    /**
     * 핀 생성
     */
    // 마커(핀) 찍는함수
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showMarkerPoint() {
        for (int i = 0; i < m_mapPoint.size(); i++) {
            tpoint = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());

            storename = m_mapPoint.get(i).getName();
            custom = new customView(this, storename);
            Log.d("storename", "" + storename);
            View v = custom.getView();
            String t = (String) ((TextView) v.findViewById(R.id.bubble_title)).getText();
            Log.d("@@@@", "" + t);
            bi = createBitmapFromLayout(v);


            RoadPage mContext = this;
            storeItem = new TMapMarkerItem();

            String strID = String.format("pmarker%d", mMarkerID++);

            storeItem.setTMapPoint(tpoint);
            storeItem.setName(m_mapPoint.get(i).getName());
            storeItem.setVisible(TMapMarkerItem.VISIBLE);
            storename = m_mapPoint.get(i).getName();
            storeItem.setIcon(bi);
            storeItem.setCalloutTitle(m_mapPoint.get(i).getName());
            mMapView.addMarkerItem(strID, storeItem);
            mArrayMarkerID.add(strID);

        }
    }
    /**   핀 생성 */

    /**
     * 길찾기실행
     */
    public void drawPedestrianPath() {
        findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH);
        setTrackingMode(false);
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
        Log.d("88888888888888888888888888888888888888", "" + endPoint);
        TMapPoint point2 = endPoint;
        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAllType(type, point1, point2, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                TMapPolyLine polyline = new TMapPolyLine();
                polyline.setLineColor(Color.rgb(138, 43, 226));
                polyline.setLineWidth(20);
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
                    int zoom = info.getTMapZoomLevel();
                    if (zoom > 0) {
                        zoom = 16;
                    }


//                    mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());

//                    mMapView.addTMapPolyLine("t",polyline);

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
//                        routeDistance.setText("총 거리 : " + Double.parseDouble(totalDistance) + "m");
                        routeDistance.setText(km1 + "km");
                        Log.d("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ", "거리" + Double.parseDouble(totalDistance));
                    } else {
                        double km = Double.parseDouble(totalDistance) / 1000;
                        String km1 = String.format("%.1f", km);
//                        routeDistance.setText("총 거리 : " + km + "km");
                        routeDistance.setText(km1 + "km");
                        Log.d("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ", "거리" + km);
                    }

                    if (Double.parseDouble(totalDistance) < 500) {
                        double km = Double.parseDouble(totalDistance) / 1000;
                        String km1 = String.format("%.1f", km);
//                        routeDistance.setText("총 거리 : " + Double.parseDouble(totalDistance) + "m");
                        routeDistance.setText(km1 + "km");
                        Log.d("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ", "거리" + Double.parseDouble(totalDistance));
                    }

                    mMapView.setZoomLevel(zoom);
//                    routeTime.setText("예상 시간 : 약 " + time);
                    routeTime.setText("" + time);
//                    routeTime.setText(time);
                    Log.d("시간", "" + time);


                    startItem = new TMapMarkerItem();
                    startItem.setTMapPoint(point1);
                    startItem.setVisible(TMapMarkerItem.VISIBLE);
                    startItem.setIcon(startbit);
//                    mMapView.addMarkerItem("t1",startItem);

                    if (mMapView.getMarkerItemFromID("t") != null) {
                        mMapView.removeMarkerItem("t");
                    }

                    endItem.setTMapPoint(point2);
                    endItem.setVisible(TMapMarkerItem.VISIBLE);
                    endItem.setIcon(cbi);
                    mMapView.addMarkerItem("t", endItem);
                }
                mMapView.sendMarkerToBack(storeItem);
                mMapView.removeAllTMapPolyLine();

            }
        });
    }
    /**  타입에 따른 길찾기(현재 도보코드만 적용)*/
}