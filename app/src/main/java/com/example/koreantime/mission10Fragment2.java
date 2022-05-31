package com.example.koreantime;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


public class mission10Fragment2 extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    MapView mapView;
    Geocoder geocoder;
    String locationName = "";
    EditText locationText;
    Button locationBtn;
    ArrayList<markerGPS> markerList = new ArrayList<>();
    ArrayList<markerGPS> userLocations = new ArrayList<>();
    boolean isMarker = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_mission10_fragment2, container, false);

        locationText = v.findViewById(R.id.locationText);
        locationBtn = v.findViewById(R.id.locationBtn);

        mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.kakaoMap);
        mapViewContainer.addView(mapView);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.541, 126.986), true);
        mapView.setZoomLevel(4, true);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);


//        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.541, 126.986);
//        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName("Default Marker");
//        marker.setTag(0);
//        marker.setMapPoint(MARKER_POINT);
//        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        mapView.addPOIItem(marker);

        userLocations.add(new markerGPS(37.541, 126.986));
        userLocations.add(new markerGPS(37.545, 126.986));
        userLocations.add(new markerGPS(37.551, 126.986));
        userLocations.add(new markerGPS(37.553, 126.986));
        userLocations.add(new markerGPS(37.537, 126.986));
        userLocations.add(new markerGPS(37.535, 126.986));
        //https://jayprogram.tistory.com/82 -> 다각형 무게중심


        ArrayList<markerGPS> centerGPS = new ArrayList<>();
        double[] tmpLocation = GetCenter();
        centerGPS.add(new markerGPS(tmpLocation[0], tmpLocation[1]));

        Log.d("Qwerqwer", String.valueOf(centerGPS.get(0).getLat()));


        geocoder = new Geocoder(this.getContext());

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
                    Toast.makeText(getActivity(), "검색 결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public double[] GetCenter() {
        double area = 0, cx = 0, cy = 0;
        double finalX = 0, finalY = 0;
        for (int i = 0; i < userLocations.size(); i++) {
            int j = (i + 1) % userLocations.size();
            float x1 = Float.parseFloat(String.format("%.3f", userLocations.get(i).getLat()));
            float y1 = Float.parseFloat(String.format("%.3f", userLocations.get(i).getLon()));
            float x2 = Float.parseFloat(String.format("%.3f", userLocations.get(j).getLat()));
            float y2 = Float.parseFloat(String.format("%.3f", userLocations.get(j).getLon()));

            area += x1 * y2;
            area -= y1 * x2;

            float vx = (x1 + x2) * ((x1 * y2) - (x2 * y1));
            float tmpCx =  Float.parseFloat(String.format("%.4f", vx));
            cx += tmpCx;

            float vy = (y1 + y2) * ((x1 * y2) - (x2 * y1));
            float tmpCy = Float.parseFloat(String.format("%.4f", vy));
            cy += tmpCy;
            Log.d("tmpCx : : : : ", String.valueOf(tmpCx));
            Log.d("tmpCy : : : : ", String.valueOf(tmpCy));


            finalX = Double.parseDouble(String.format("%.4f", cx));
            finalY = Double.parseDouble(String.format("%.4f", cy));
            Log.d("cx : : : : ", String.valueOf(finalX));
            Log.d("cy : : : : ", String.valueOf(finalY));
        }

        area /= 2;
        area = Math.abs(area);

        Log.d("StringX : : : : ", String.valueOf(cx));
        Log.d("StringY : : : : ", String.valueOf(cy));

        float tmpCx = Float.parseFloat(String.format("%.6f", (cx)));
        float tmpCy = Float.parseFloat(String.format("%.6f", (cy)));

        Log.d("tmpCx : : : : ", String.valueOf(tmpCx));
        Log.d("tmpCy : : : : ", String.valueOf(tmpCy));

        cx =  Float.parseFloat(String.format("%.6f", (tmpCx / (6.0 * area))));
        cy =  Float.parseFloat(String.format("%.6f", (tmpCy / (6.0 * area))));

        Log.d("finalcx : : : : ", String.valueOf(cx));
        Log.d("finalcy : : : : ", String.valueOf(cy));

        double[] tmpArr = {Math.abs(cx), Math.abs(cy)};

        return tmpArr;

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
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord
                (mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MARKER_POINT);
        mapView.removePOIItem(marker);
        Log.d("double tapped", "double tapped");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        if (isMarker) {
            mapView.removeAllPOIItems();
            isMarker = false;
        }

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord
                (mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
        MapPOIItem marker = new MapPOIItem();

        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);

        markerGPS markerGPS = new markerGPS(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
        markerList.add(markerGPS);

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
        double lat = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
        double lon = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    lat, // 위도
                    lon, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }
        if (list != null) {
            if (list.size() == 0) {
                Log.d("address", "해당되는 주소 정보는 없습니다");
            } else {
                Log.d("address", list.get(0).getAddressLine(0));
                locationName = list.get(0).getAddressLine(0);
                mapPOIItem.setItemName(list.get(0).getAddressLine(0));
            }
        }
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