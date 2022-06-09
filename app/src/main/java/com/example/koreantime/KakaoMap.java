package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;



class markerGPS {
    double lat;
    double lon;

    markerGPS(double newLat, double newLon) {
        this.lat = newLat;
        this.lon = newLon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}

public class KakaoMap extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    MapView mapView;
    Geocoder geocoder;
    LinearLayout setTime;
    TextView month;
    TextView day;
    TextView Meetingtime;
    EditText locationText;
    ImageView locationBtn;
    ArrayList<markerGPS> markerList = new ArrayList<>();
    ArrayList<markerGPS> userLocations = new ArrayList<>();
    boolean isMarker = false;
    RelativeLayout relativeLayout;
    MaterialCalendarView calendarView;
    TimePicker timePicker;

    String meetingLocation;
    double finalX = 0.0;
    double finalY = 0.0;
    ArrayList<MapPOIItem> mapPOIItemArrayList = new ArrayList<>();
    SeekBar seekBarVibrate;
    SeekBar seekBarAlarm;
    Button complete;

    String date = "";
    String time = "";
    int vibrate = 50;
    int alarm;

    CheckBox checkAlarm;
    String[] groupmember;
    String groupname;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> memberAddress = new ArrayList<>();
    DTO_user user_info;
    int is_end=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_map);
        calendarView = findViewById(R.id.calendar);
        relativeLayout = findViewById(R.id.kakaoMap);
        seekBarVibrate = findViewById(R.id.vibrate);
        seekBarAlarm = findViewById(R.id.alarm);
        complete = findViewById(R.id.complete);
        checkAlarm = findViewById(R.id.checkAlarm);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);

        TextView Meetingtime = findViewById(R.id.Meetingtime);

        OneDayDecorator oneDayDecorator = new OneDayDecorator();
//        EventDecorator eventDecorator = new EventDecorator();
        SaturdayDecorator saturdayDecorator = new SaturdayDecorator();
        SundayDecorator sundayDecorator = new SundayDecorator();
        calendarView.addDecorator(oneDayDecorator);
//        calendarView.addDecorator(eventDecorator);
        calendarView.addDecorator(saturdayDecorator);
        calendarView.addDecorator(sundayDecorator);

        geocoder = new Geocoder(this);

        Intent Intent = getIntent();
        user_info=(DTO_user) Intent.getSerializableExtra("user_info");
        groupname = Intent.getStringExtra("groupname");//그룹 이름 받아오기
        groupmember = Intent.getStringArrayExtra("groupmember");//그룹멤버 받아오기

//        Log.d("meetingmake", groupmember[1]);
        for (String email : groupmember) {//그룹멤버의 첫번째 주소 정보 가져옴
            db.collection("user").document(email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                memberAddress.add(document.getData().get("addr1").toString());
                                AddressToGPS(document.getData().get("addr1").toString());
                                Log.d("add_meeting", document.getData().get("addr1").toString());
                                is_end++;
                            } else {
                                Log.d("add_meeting", "Error getting documents: ", task.getException());
                            }
                            if(is_end==groupmember.length) {
                                makelocation();
                            }
                        }
                    });
        }


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay tmpDate, boolean selected) {
                date = GetString(tmpDate);
                Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR);
                int mMin = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        KakaoMap.this, android.R.style.Theme_Holo, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time = i+"-"+i1;
                        LinearLayout setTime = findViewById(R.id.setTime);
                        setTime.setVisibility(View.VISIBLE);
                        Meetingtime.setText(i+" : "+i1);
                    }
                }, mHour, mMin, false);
                timePickerDialog.show();
            }
        });

        mapView = new MapView(KakaoMap.this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5418, 126.9818), true);
        mapView.setZoomLevel(4, true);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        relativeLayout.addView(mapView);


        seekBarVibrate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vibrate = seekBar.getProgress();
            }
        });


        seekBarAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alarm = seekBar.getProgress();
            }
        });


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAlarm.isChecked()){
                    DTO_schecule new_meeting = new DTO_schecule(user_info.getEmail(), meetingLocation,Integer.toString(alarm), Integer.toString(vibrate) , time,date, "첨만드는 회의");

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
                }else{
                    DTO_schecule new_meeting = new DTO_schecule(user_info.getEmail(), meetingLocation, "0" ,Integer.toString(vibrate) , time,date, "첨만드는 회의");

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
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        relativeLayout.removeAllViews();
    }

    private void makelocation(){
        for (int i = 0; i < userLocations.size(); i++) {
            MapPOIItem marker = new MapPOIItem();
            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(userLocations.get(i).getLat(), userLocations.get(i).getLon());
            marker.setItemName("Default Marker");
            marker.setTag(0);
            marker.setMapPoint(MARKER_POINT);
            marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);
        }


        meetingLocation = GetCenter();
        MakeLine();
    }

    private void AddressToGPS(String tmpLocation) {
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(tmpLocation, 10);
        } catch (IOException e) {
            Log.d("init geocoder error", String.valueOf(e));
            e.printStackTrace();
        }
        if (list != null) {
            double centerLat = list.get(0).getLatitude();
            double centerLon = list.get(0).getLongitude();
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(centerLat, centerLon), true);
            mapView.setZoomLevel(5, true);
            Log.d("geocoder error", String.valueOf(list.size()));
            Address address = list.get(0);
            double lat = address.getLatitude();
            double lon = address.getLongitude();
            userLocations.add(new markerGPS(lat, lon));
//            for (int i = 0; i < list.size(); i++) {
//                Address address = list.get(i);
//                double lat = address.getLatitude();
//                double lon = address.getLongitude();
//                userLocations.add(new markerGPS(lat, lon));
//                    MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(lat, lon);
//                    MapPOIItem marker = new MapPOIItem();
//                    marker.setItemName(list.get(i).getAddressLine(0));
//                    marker.setMapPoint(MARKER_POINT);
//                    marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
//                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//                    mapView.addPOIItem(marker);
//
//                    markerGPS newMarker = new markerGPS(lat, lon);
//                    markerList.add(newMarker);
//            }
        }
    }

    private void MakeLine() {
        for (markerGPS item : userLocations) {
            double lat = item.getLat();
            double lon = item.getLon();
            MapPolyline polyline = new MapPolyline();
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(finalX, finalY));
            polyline.setLineColor(Color.argb(100, 255, 0, 0));
            mapView.addPolyline(polyline);
        }
    }

    public String GetMonth(String num){
        switch (num){
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return  "MAY";
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
                return  "NOV";
            case "12":
                return  "DEC";
        }
        return "";
    }

    public String GetString(CalendarDay tmpDate) {
        String strDate = String.valueOf(tmpDate).substring(12);
        strDate = strDate.substring(0, strDate.length() - 1);
        String[] newDate = strDate.split("-");
        if (Integer.parseInt(newDate[1]) <= 9) {
            int tmpNum = Integer.parseInt(newDate[1]);
            tmpNum += 1;
            newDate[1] = "0" + tmpNum;
        } else {
            int tmpNum = Integer.parseInt(newDate[1]);
            tmpNum += 1;
            newDate[1] = String.valueOf(tmpNum);
        }

        month.setText(GetMonth(newDate[1]));
        day.setText(newDate[2]);
        return newDate[0] + "-" + newDate[1] + "-" + newDate[2];
    }

    public String GetCenter() {
        double area = 0, centerX = 0, centerY = 0;
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

        finalX = (centerX / (6.0 * area));
        finalY = (centerY / (6.0 * area));

        finalX = Math.abs(finalX);
        finalY = Math.abs(finalY);

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(Math.abs(finalX), Math.abs(finalY));

        MapPOIItem marker = new MapPOIItem();

        mapView.setZoomLevel(5, true);
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

        mapPOIItemArrayList.add(marker);
        mapView.addPOIItem(marker);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Math.abs(finalX), Math.abs(finalY)), true);
        return list.get(0).getAddressLine(0);
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        for(int i=0;i<mapPOIItemArrayList.size();i++){
            mapView.removePOIItem(mapPOIItemArrayList.get(i));
        }

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
                meetingLocation = list.get(0).getAddressLine(0);
            }

        }

        mapPOIItemArrayList.add(marker);
        mapView.addPOIItem(marker);

        markerGPS markerGPS = new markerGPS(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
        markerList.add(markerGPS);
        mapView.removeAllPolylines();
        for (markerGPS item : userLocations) {
            double lat = item.getLat();
            double lon = item.getLon();
            MapPolyline polyline = new MapPolyline();
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord
                    (mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude));
            polyline.setLineColor(Color.argb(100, 255, 0, 0));
            mapView.addPolyline(polyline);
        }
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