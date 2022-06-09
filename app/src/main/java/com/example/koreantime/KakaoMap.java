package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

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
    String locationName = "";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_map);
        calendarView = findViewById(R.id.calendar);
        locationText = findViewById(R.id.locationText);
        locationBtn = findViewById(R.id.locationBtn);
        relativeLayout = findViewById(R.id.kakaoMap);
        timePicker = findViewById(R.id.timePicker);
        seekBarVibrate = findViewById(R.id.vibrate);
        seekBarAlarm = findViewById(R.id.alarm);
        complete = findViewById(R.id.complete);
        checkAlarm = findViewById(R.id.checkAlarm);

        OneDayDecorator oneDayDecorator = new OneDayDecorator();
//        EventDecorator eventDecorator = new EventDecorator();
        SaturdayDecorator saturdayDecorator = new SaturdayDecorator();
        SundayDecorator sundayDecorator = new SundayDecorator();
        calendarView.addDecorator(oneDayDecorator);
//        calendarView.addDecorator(eventDecorator);
        calendarView.addDecorator(saturdayDecorator);
        calendarView.addDecorator(sundayDecorator);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay tmpDate, boolean selected) {
                date = GetString(tmpDate);
                if (!date.equals("")) {
                    timePicker.setVisibility(View.VISIBLE);
                } else {
                    timePicker.setVisibility(View.GONE);
                }
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String str = "", str1 = "";
                if (i <= 9) {
                    str = '0' + String.valueOf(i);
                } else {
                    str = String.valueOf(i);
                }
                if (i1 <= 9) {
                    str1 = '0' + String.valueOf(i1);
                } else {
                    str1 = String.valueOf(i1);
                }
                time = str + "-" + str1;
            }
        });

        mapView = new MapView(KakaoMap.this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5418, 126.9818), true);
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

        for (int i = 0; i < userLocations.size(); i++) {
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

        geocoder = new Geocoder(this);
        meetingLocation = GetCenter();
        MakeLine();

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
                    if (list.size() != 0) {
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
                    } else {
                        Toast.makeText(KakaoMap.this, "정확한 지명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KakaoMap.this, "검색 결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }

                MapPoint mapPoint = null;
                MapPoint.PlainCoordinate map = mapPoint.getMapPointScreenLocation();
            }
        });

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
                    Log.d("go to db", date+", "+time+", "+vibrate+", "+alarm+", "+meetingLocation);
                }else{
                    Log.d("go to db", date+", "+time+", "+vibrate+", "+meetingLocation);
                }
            }
        });
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