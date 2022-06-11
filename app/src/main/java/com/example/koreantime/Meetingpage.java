package com.example.koreantime;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koreantime.DTO.DTO_schecule;
import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meetingpage extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DTO_user user_info;
    String[] members;
    ArrayList<String> late_member = new ArrayList<String>();
    RelativeLayout kakaoMap;
    MapView mapView;
    Button arrive;
    Button punish;
    Geocoder geocoder;
    double initLat = 36.6259;
    double initLon = 127.4526;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    Messaging temp1 = new Messaging();
    Messaging temp2 = new Messaging();
    Messaging temp3 = new Messaging();
    Messaging temp4 = new Messaging();
    TextView nowAddress;
    MapPOIItem initMarker;
    boolean arriveFlag = false;
    boolean punishFlag = false;
    DTO_schecule meetingclass;
    String tttkk;
    String[] member_id;

    ArrayList<MapPOIItem> mapPOIItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingpage);
        TextView date = findViewById(R.id.month);
        TextView time = findViewById(R.id.time);

        nowAddress = findViewById(R.id.nowAddress);
        geocoder = new Geocoder(this);
        kakaoMap = findViewById(R.id.kakaoMap);
        mapView = new MapView(Meetingpage.this);
        kakaoMap.addView(mapView);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        Intent Intent = getIntent();
        String m_id = Intent.getStringExtra("id");
        String g_id = Intent.getStringExtra("gid");
        String email = Intent.getStringExtra("email");
        String[] members_token = Intent.getStringArrayExtra("tokens");
        Log.d("plz", email);
        member_id = Intent.getStringArrayExtra("member_id");
        ArrayList<String> other_id=new ArrayList<String>();
        for ( int i=0; i < member_id.length; i++ ) {
            if(member_id[i]!=email){
                other_id.add(member_id[i]);
            }
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("group").document(g_id).collection("schedule").document(m_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        meetingclass = document.toObject(DTO_schecule.class);
                        date.setText(SplitDate(meetingclass.getDate()));
                        time.setText(meetingclass.getTime());
                        GeoCoding(meetingclass.getLocation());
                        GPSToAddress(initLat, initLon);
                    } else {
                        Log.d("inter meeing", "No such document");
                    }
                } else {
                    Log.d("inter meeing", "get failed with ", task.getException());
                }
            }
        });

        Task<String> token = FirebaseMessaging.getInstance().getToken();

        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("FCM Token", task.getResult().toString());
                    tttkk=task.getResult().toString();
                }
            }
        });

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        MapPOIItem marker = new MapPOIItem();
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                for(String id:other_id){
                    RemoveAllMarkers();
                    db.collection("user").document(id)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("ssivl", document.getData().get("latitude").toString());

                                    Log.d("ssivl", document.getData().get("longitude").toString());

                                    MakeMarker(Double.parseDouble(document.getData().get("longitude").toString()) , Double.parseDouble(document.getData().get("latitude").toString()) );
                                } else {
                                    Log.d("inter meeing", "No such document");
                                }
                            } else {
                                Log.d("inter meeing", "get failed with ", task.getException());
                            }
                        }
                    });

                }
                mapView.removePOIItem(marker);
                MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                marker.setItemName("It's Me!!");
                marker.setTag(0);
                marker.setMapPoint(MARKER_POINT);
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setCustomImageResourceId(R.drawable.redpin);
                mapView.addPOIItem(marker);

                if(!arriveFlag){
                    Check10M(latitude, longitude);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                gpsLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                gpsLocationListener);

        arrive = findViewById(R.id.arrive);
        punish = findViewById(R.id.punish);

        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arriveFlag) {
                    punish.setVisibility(View.VISIBLE);
                }else{
                    punish.setVisibility(View.INVISIBLE);
                }
            }
        });

        punish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                while (true) {
//                    punish.setBackgroundColor(Color.parseColor("#FF2C2C"));
//                    try {
//                        Thread.sleep(16000);
//                        break;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                punish.setBackgroundColor(Color.parseColor("#FFF"));
                try {
                    if (temp1.getStatus() == AsyncTask.Status.RUNNING) {
                        temp1.cancel(true);
                    }
                    if (temp2.getStatus() == AsyncTask.Status.RUNNING) {
                        temp1.cancel(true);
                    }
                    if (temp3.getStatus() == AsyncTask.Status.RUNNING) {
                        temp1.cancel(true);
                    }
                    if (temp4.getStatus() == AsyncTask.Status.RUNNING) {
                        temp1.cancel(true);
                    }
                } catch (Exception e) {
                }
                if(members_token.length>0){
                    if(tttkk!=members_token[0]){
                        temp1.setToken(members_token[0]);
                        temp1.execute();
                    }
                }
                if(members_token.length>1){
                    if(tttkk!=members_token[1]) {
                        temp2.setToken(members_token[1]);
                        temp2.execute();
                    }
                }
                if(members_token.length>2){
                    if(tttkk!=members_token[2]) {
                        temp3.setToken(members_token[2]);
                        temp3.execute();
                    }
                }
                if(members_token.length>3){
                    if(tttkk!=members_token[3]) {
                        temp4.setToken(members_token[3]);
                        temp4.execute();
                    }
                }
            }
        });
    }

    private void MakeMarker(double latitude, double longitude) {
        Log.d("MakeMarker", latitude+"< "+longitude);
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setCustomImageResourceId(R.drawable.bluepin);
        mapView.addPOIItem(marker);
        mapPOIItems.add(marker);
    }

    private void RemoveAllMarkers() {
        for(int i=0;i<mapPOIItems.size();i++){
            mapView.removePOIItem(mapPOIItems.get(i));
        }
    }

    private String SplitDate(String date) {
        String tmpDate = date.substring(5);
        String[] newDate = tmpDate.split("-");
        return GetMonth(newDate[0])+"."+newDate[1];
    }

    public String GetMonth(String num) {
        switch (num) {
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAY";
            case "06":
                return "JUN";
            case "07":
                return "JUL";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OCT";
            case "11":
                return "NOV";
            case "12":
                return "DEC";
        }
        return "";
    }

    private void GeoCoding(String location) {
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(location, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
                initLat = 36.6287;
                initLon = 127.4605;
            } else {
                initLat = list.get(0).getLatitude();
                initLon = list.get(0).getLongitude();
            }
        }
        initMarker = new MapPOIItem();
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(initLat, initLon);
        initMarker.setItemName("여기서 봐");
        initMarker.setTag(0);
        initMarker.setMapPoint(MARKER_POINT);
        initMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
        initMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        initMarker.setCustomImageResourceId(R.drawable.mymarker);// 기본으로 제공하는 BluePin 마커 모양.
        mapView.addPOIItem(initMarker);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(initLat, initLon), true);
        mapView.setZoomLevel(5, true);
    }

    private String SplitAddress(String addressLine) {
        return addressLine.substring(5);
    }

    private void GPSToAddress(double latitude, double longitude) {
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    latitude, // 위도
                    longitude, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }
        if (list != null) {
            if (list.size() == 0) {
//                marker.setItemName(" ");
            } else {
                nowAddress.setText(SplitAddress(list.get(0).getAddressLine(0)));
            }
        }
    }

    void send_penalty(String token, String vibrate, String alarm) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kakaoMap.removeAllViews();
    }

    private void Check10M(double latitude, double longitude) {
        double theta = initLon - longitude;
        double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(initLat)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(initLat)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;
        if (dist <= 30) {
            arrive.setBackgroundResource(R.drawable.get_img_btn1);
            punish.setVisibility(View.INVISIBLE);
            arriveFlag = true;
        } else {
            Log.d("distance", "아직 멀음");
            arriveFlag = false;
            arrive.setBackgroundResource(R.drawable.get_img_btn);
        }
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    private void onFinishReverseGeoCoding(String result) {
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(Meetingpage.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(Meetingpage.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Meetingpage.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Meetingpage.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Meetingpage.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Meetingpage.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Meetingpage.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Meetingpage.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE) {//사용자가 GPS 활성 시켰는지 검사
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {
                    Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                    checkRunTimePermission();
                }
            }
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}