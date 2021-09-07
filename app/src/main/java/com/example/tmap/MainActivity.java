package com.example.tmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Elizabeth on 2016-10-04.
 */
public class MainActivity extends AppCompatActivity
        implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = false;

    private LinearLayout routeLayout;

    TMapGpsManager gps = null;
    PermissionManager mPermissionManager = null;

    private TMapData tmapdata = null;
    private TMapView mMapView  = null;
    private static String mApiKey = "l7xxa1a4006f04c0457490f37c791bcf534e";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    private String totalDistance = null;
    private String totalTime = null;
    private String totalFare = null;

    private String address;
    private Double lat = null;
    private Double lon = null;

    private Button buttonZoomIn;
    private Button buttonZoomOut;
    private Button road;
    private ImageView locationBtn;

    private TextView routeDistance;
    private TextView routeTime;
    private TextView routeFare;



    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView .setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        totalDistance = null;
        totalTime = null;
        totalFare = null;

        mContext = this;
        gps = new TMapGpsManager(MainActivity.this);

        //버튼 선언
        buttonZoomIn = (Button)findViewById(R.id.buttonZoomIn);
        buttonZoomOut = (Button)findViewById(R.id.buttonZoomOut);
        road = (Button)findViewById(R.id.road);
        locationBtn = (ImageView) findViewById(R.id.location_btn);

        routeLayout = (LinearLayout) findViewById(R.id.route_layout);
        routeDistance = (TextView) findViewById(R.id.route_distance);
        routeTime = (TextView) findViewById(R.id.route_time);
        routeFare = (TextView) findViewById(R.id.route_fare);

        // "확대" 버튼 클릭
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.MapZoomIn();
            }
        });

        // "축소" 버튼 클릭
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.MapZoomOut();
            }
        });
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawPedestrianPath();
                mMapView.setTrackingMode(false);
                locationBtn.setBackgroundResource(R.drawable.location_btn);
                mMapView .setSightVisible(false);
                mMapView.setIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_pause));
            }
        });

        //Tmap 각종 객체 선언
        tmapdata = new TMapData(); //POI검색, 경로검색 등의 지도데이터를 관리하는 클래스
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        mMapView  = new TMapView(this);

        linearLayout.addView(mMapView );
        mMapView .setSKTMapApiKey(mApiKey);

        addPoint();
        showMarkerPoint();

        /* 현재 보는 방향 */
//        mMapView .setCompassMode(true);
//        mMapView .setPOIRotate(true);
        /* 시야 표출 */

        /* 현위치 아이콘표시 */
//        mMapView .setIconVisibility(true);

        /* 줌레벨 */
        mMapView .setZoomLevel(15);

        /* 지도 타입 */
        mMapView .setMapType(TMapView.MAPTYPE_STANDARD);

        /* 언어 설정 */
        mMapView .setLanguage(TMapView.LANGUAGE_KOREAN);

        setTrackingMode(false);



        mPermissionManager = new PermissionManager();


//        /*  화면중심을 단말의 현재위치로 이동 */
//        mMapView .setTrackingMode(true);
//        mMapView .setSightVisible(true);




        //풍선 클릭시
        mMapView .setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {

                lat = markerItem.latitude;
                lon = markerItem.longitude;

                //1. 위도, 경도로 주소 검색하기
                tmapdata.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String strAddress) {
                        address = strAddress;
                    }
                });

                Toast.makeText(MainActivity.this, "주소 : " + address, Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void onClickLocationBtn(View v) {
        m_bTrackingMode = !m_bTrackingMode;
        if (m_bTrackingMode) {
            mMapView.setIconVisibility(m_bTrackingMode);
        }
        setTrackingMode(m_bTrackingMode);
        mMapView .setSightVisible(true);
        mMapView.setIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_login));
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
                        gps.setMinTime(30);
                        gps.setMinDistance((float) 0.1);
                        gps.setProvider(gps.GPS_PROVIDER);
                        gps.OpenGps();
                        gps.setProvider(gps.NETWORK_PROVIDER);
                        gps.OpenGps();
                        Log.d("adfsasfasdf","위도"+gps.getLocation().getLatitude()+"경도"+gps.getLocation().getLongitude());
                    }
                }

                @Override
                public void denied() {
                    Toast.makeText(MainActivity.this, "위치정보 수신에 동의하지 않으시면 현재위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            });

            locationBtn.setBackgroundResource(R.drawable.location_btn_sel);
            mMapView.setCenterPoint(mMapView.getLocationPoint().getLongitude(), mMapView.getLocationPoint().getLatitude());
        } else {
            if (gps != null) {
                gps.CloseGps();
            }
            locationBtn.setBackgroundResource(R.drawable.location_btn);
        }
    }

    //핀 찍을 data
    public void addPoint() {
        // 강남 //
        m_mapPoint.add(new MapPoint("센텀시티", 35.168827, 129.131745));
    }

    // 마커(핀) 찍는함수
    public void showMarkerPoint() {
        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            /* 핀 이미지 */
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);
            /* 핀 이미지 */
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);

            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("부산");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            /* 풍선 안 우측버튼 */
            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.i_go);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strID = String.format("pmarker%d", mMarkerID++);

            mMapView .addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
        }
    }

    public void drawPedestrianPath() {
        findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH);
    }


    public TMapPoint randomTMapPoint() {

        /*
            센텀시티역
        */
        double latitude = 35.168827;
        double longitude = 129.131745;
        /*
            수영강변e편한세상
        */
//        double latitude = 35.18230966959019;
//        double longitude = 129.11610345559023;

//        latitude = Math.min(37.575113, latitude);
//        latitude = Math.max(37.483086, latitude);
//
//        longitude = Math.min(127.027359, longitude);
//        longitude = Math.max(126.878357, longitude);

        TMapPoint point = new TMapPoint(latitude, longitude);


        return point;
    }

    public TMapPoint randomTMapPoint2() {
        double latitude = ((double) Math.random()) * (37.770555 - 37.404194) + 37.483086;
        double longitude = ((double) Math.random()) * (127.426043 - 126.770296) + 126.878357;

        latitude = Math.min(37.770555, latitude);
        latitude = Math.max(37.404194, latitude);

        longitude = Math.min(127.426043, longitude);
        longitude = Math.max(126.770296, longitude);


        TMapPoint point = new TMapPoint(latitude, longitude);

        return point;
    }

    private String getContentFromNode(Element item, String tagName) {
        NodeList list = item.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            if (list.item(0).getFirstChild() != null) {
                return list.item(0).getFirstChild().getNodeValue();
            }
        }
        return null;
    }

    private void findPathDataAllType(final TMapData.TMapPathType type) {
        totalDistance = null;
        totalTime = null;
        totalFare = null;

        //TMapPoint point1 = mMapView.getCenterPoint();

        TMapPoint point1 =new TMapPoint(gps.getLocation().getLatitude(), gps.getLocation().getLongitude());
        Log.d("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ",""+point1.getLongitude());
        TMapPoint point2 = null;
        if (type == TMapData.TMapPathType.CAR_PATH) {
            point2 = randomTMapPoint2();
        } else {
            point2 = randomTMapPoint();
        }
        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAllType(type, point1, point2, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                TMapPolyLine polyline = new TMapPolyLine();
                polyline.setLineColor(Color.rgb(138,43,226));
                polyline.setLineWidth(20);
                if (doc != null) {
                    NodeList list = doc.getElementsByTagName("Document");
                    Element item2 = (Element) list.item(0);
                    totalDistance = getContentFromNode(item2, "tmap:totalDistance");
                    totalTime = getContentFromNode(item2, "tmap:totalTime");
                    if (type == TMapData.TMapPathType.CAR_PATH) {
                        totalFare = getContentFromNode(item2, "tmap:totalFare");
                    }

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
                    if (zoom > 18) {
                        zoom = 18;
                    }

                    routeLayout.bringToFront();
                    routeLayout.setVisibility(View.VISIBLE);

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

                    if(Double.parseDouble(totalDistance) < 1000){
                        routeDistance.setText("총 거리 : " + Double.parseDouble(totalDistance) + "m");
                        Log.d("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ","거리"+ Double.parseDouble(totalDistance));
                    }else {
                        double km = Double.parseDouble(totalDistance) / 1000;
                        routeDistance.setText("총 거리 : " + km + "km");
                        Log.d("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ","거리"+km);

                    }
                    routeTime.setText("예상 시간 : 약 " + time);
                    Log.d("ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ","시간"+time);
                    if (totalFare != null) {
                        routeFare.setVisibility(View.VISIBLE);
                        routeFare.setText("유료도로 요금 : " + totalFare + "원");
                    } else {
                        routeFare.setVisibility(View.GONE);
                    }

                    mMapView.setZoomLevel(zoom);
                    mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());

                    mMapView.addTMapPath(polyline);
                }
            }
        });
    }
}