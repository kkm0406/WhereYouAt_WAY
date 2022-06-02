package com.example.koreantime;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.koreantime.DTO.DTO_group;
import com.example.koreantime.DTO.DTO_schecule;
import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mission10 extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    MapView mapView;
    Geocoder geocoder;
    String locationName = "";
    EditText locationText;
    Button locationBtn;
    ArrayList<markerGPS> markerList = new ArrayList<>();
    ArrayList<markerGPS> userLocations = new ArrayList<>();
    boolean isMarker = false;
    RelativeLayout relativeLayout;
    MaterialCalendarView calendarView;
    TimePicker timePicker;
    String date;
    String time;
    String meetingLocation;
    String[] groupmember;
    String groupname;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    ArrayList<String> memberAddress = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission10);
        calendarView = findViewById(R.id.calendar);
        locationText = findViewById(R.id.locationText);
        locationBtn = findViewById(R.id.locationBtn);
        relativeLayout = findViewById(R.id.kakaoMap);
        timePicker = findViewById(R.id.timePicker);

        OneDayDecorator oneDayDecorator = new OneDayDecorator();
//        EventDecorator eventDecorator = new EventDecorator();
        SaturdayDecorator saturdayDecorator = new SaturdayDecorator();
        SundayDecorator sundayDecorator = new SundayDecorator();
        calendarView.addDecorator(oneDayDecorator);
//        calendarView.addDecorator(eventDecorator);
        calendarView.addDecorator(saturdayDecorator);
        calendarView.addDecorator(sundayDecorator);



        Intent Intent = getIntent();
        groupname = Intent.getStringExtra("groupname");//그룹 이름 받아오기
        groupmember = Intent.getStringArrayExtra("groupmember");//그룹멤버 받아오기
        Log.d("meetingmake", groupmember[2]);
        for (String email:groupmember) {//그룹멤버의 첫번째 주소 정보 가져옴
            db.collection("user").document(email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                memberAddress.add(document.getData().get("addr1").toString());
                                Log.d("add_meeting", document.getData().get("addr1").toString());
                            } else {
                                Log.d("add_meeting", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        DTO_schecule new_meeting = new DTO_schecule("123456@naver.com", "청주시 개신동","진동", "20220602","1512","첨만드는 회의");
        db.collection("group").document(groupname).collection("schedule")//생성한 회의 DB에 쓰기
                .add(new_meeting)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("add_meeting", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("add_meeting", "Error adding document", e);
                    }
                });


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay tmpDate, boolean selected) {
                date = GetString(tmpDate);
                Log.d("date", date);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String str = "", str1 = "";
                if(i <= 9){
                    str = '0'+String.valueOf(i);
                }else{
                    str = String.valueOf(i);
                }
                if (i1 <= 9){
                    str1 = '0'+String.valueOf(i1);
                }else{
                    str1 = String.valueOf(i1);
                }
                time = str+"-"+str1;
            }
        });

        mapView = new MapView(mission10.this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord( 37.5418, 126.9818), true);
        mapView.setZoomLevel(4, true);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        relativeLayout.addView(mapView);

        userLocations.add(new markerGPS(37.5418, 126.9738));
        userLocations.add(new markerGPS(37.5437, 126.9627));
        userLocations.add(new markerGPS(37.5256, 126.9926));
        userLocations.add(new markerGPS(37.5525, 126.9875));
        userLocations.add(new markerGPS(37.5384, 126.9214));
        userLocations.add(new markerGPS(37.5203, 126.9343));
        userLocations.add(new markerGPS(37.5372, 126.9542));
        userLocations.add(new markerGPS(37.5481, 126.9422));

        for(int i=0;i<userLocations.size();i++){
            MapPOIItem marker = new MapPOIItem();
            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord
                    (userLocations.get(i).getLat(), userLocations.get(i).getLon());
            marker.setItemName("Default Marker");
            marker.setTag(0);
            marker.setMapPoint(MARKER_POINT);
            marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);
        }

        meetingLocation = GetCenter();

        geocoder = new Geocoder(this);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmpLocation = locationText.getText().toString();
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(tmpLocation, 10);
                } catch (IOException e) {
                    Log.d("geocoder error", String.valueOf(e));
                    e.printStackTrace();
                }
                if (list != null) {
                    isMarker = true;
                    double centerLat = list.get(0).getLatitude();
                    double centerLon = list.get(0).getLongitude();
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(centerLat, centerLon), true);
                    mapView.setZoomLevel(4, true);
                    Log.d("geocoder error", String.valueOf(list.size()));
                    for (int i = 0; i < list.size(); i++) {
                        Address address = list.get(i);
                        double lat = address.getLatitude();
                        double lon = address.getLongitude();
                        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(lat, lon);
                        MapPOIItem marker = new MapPOIItem();
                        marker.setItemName(list.get(i).getAddressLine(0));
                        marker.setMapPoint(MARKER_POINT);
                        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                        mapView.addPOIItem(marker);

                        markerGPS newMarker = new markerGPS(lat, lon);
                        markerList.add(newMarker);
                    }
                    //여기서부터 주소 입력하면 마커생기게
                } else {
                    Toast.makeText(mission10.this, "검색 결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String GetString(CalendarDay tmpDate){
        String strDate = String.valueOf(tmpDate).substring(12);
        strDate = strDate.substring(0, strDate.length()-1);
        String[] newDate = strDate.split("-");
        if(Integer.parseInt(newDate[1]) <= 9){
            int tmpNum = Integer.parseInt(newDate[1]);
            tmpNum += 1;
            newDate[1] = "0"+tmpNum;
        }else{
            int tmpNum =  Integer.parseInt(newDate[1]);
            tmpNum += 1;
            newDate[1] = String.valueOf(tmpNum);
        }
        return newDate[0]+"-"+newDate[1]+"-"+newDate[2];
    }

    public String GetCenter() {
        double area = 0;
        double centerX = 0, centerY = 0;
        double x1, x2, y1, y2;
        for (int i = 0; i < userLocations.size(); i++) {
            int j = (i + 1) % userLocations.size();
            x1 = userLocations.get(i).getLat();
            y1 = userLocations.get(i).getLon();
            x2 = userLocations.get(j).getLat();
            y2 = userLocations.get(j).getLon();

            area += x1 * y2;
            area -= y1 * x2;

            centerX += ((x1 + x2) * ((x1 * y2) - (x2 * y1)));
            centerY += ((y1 + y2) * ((x1 * y2) - (x2 * y1)));
        }

        area /= 2.0;
        area = Math.abs(area);

        double finalX = (centerX / (6.0 * area));
        double finalY = (centerY / (6.0 * area));



        finalX = Math.abs(finalX);
        finalY = Math.abs(finalY);

        float locateX = Float.parseFloat(String.valueOf(finalX));
        float locateY = Float.parseFloat(String.valueOf(finalY));

        Log.d("x, y", locateX + ", " + locateY);

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord( Math.abs(finalX), Math.abs(finalY));

        Log.d("GPS", MARKER_POINT.getMapPointGeoCoord().latitude+", "+MARKER_POINT.getMapPointGeoCoord().longitude);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Meeting Place");

        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    MARKER_POINT.getMapPointGeoCoord().latitude, // 위도
                    MARKER_POINT.getMapPointGeoCoord().longitude, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }
        if (list != null) {
            if (list.size() == 0) {
                marker.setItemName(" ");
            } else {
                marker.setItemName(list.get(0).getAddressLine(0));
            }

        }

        mapView.addPOIItem(marker);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord( Math.abs(finalX), Math.abs(finalY)), true);
        return list.get(0).getAddressLine(0);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
//        if (isMarker) {
//            mapView.removeAllPOIItems();
//            isMarker = false;
//        }
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord
                (mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    mapPoint.getMapPointGeoCoord().latitude, // 위도
                    mapPoint.getMapPointGeoCoord().longitude, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }
        if (list != null) {
            if (list.size() == 0) {
                marker.setItemName(" ");
            } else {
                marker.setItemName(list.get(0).getAddressLine(0));
            }

        }
        mapView.addPOIItem(marker);

        markerGPS markerGPS = new markerGPS(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
        markerList.add(markerGPS);
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}