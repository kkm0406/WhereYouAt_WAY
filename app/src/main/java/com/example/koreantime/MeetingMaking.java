package com.example.koreantime;

import static com.kakao.util.maps.helper.Utility.getKeyHash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.kakao.util.maps.helper.Utility;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MeetingMaking extends AppCompatActivity {

    public String KakaoMapAPI = "23587b07ad9cde9c7fe9ea1e5d430685";
    public MapView mMapView;
    public MapPoint mapPoint;
    public LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_making);

        Button complete = findViewById(R.id.complete);
        MaterialCalendarView calendar = findViewById(R.id.calendar);

        getKeyHash(getApplicationContext());

        mMapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.kakaoMap);
        mapViewContainer.addView(mMapView);
        mMapView.removeAllPOIItems();


//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            public void run() {
//                // 3초 후에 현재위치를 받아오도록 설정 , 바로 시작 시 에러납니다.
//                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
//            }
//        }, 4000); // 1000 = 1초
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        calendar.addDecorators(
                new MySelectorDecorator(MeetingMaking.this),
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator()
        );


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeetingMaking.this, GroupMeetings.class);
                startActivity(intent);
            }
        });
    }

    public void Marker(String MakerName, double startX, double startY) {
        mapPoint = MapPoint.mapPointWithGeoCoord(startY, startX);
        mMapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
        mMapView.addPOIItem(marker);
    }

    public void MapMarker(String MakerName, String detail, double startX, double startY) {
        mapPoint = MapPoint.mapPointWithGeoCoord(startY, startX);
        mMapView.setMapCenterPoint(mapPoint, true); //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName + "(" + detail + ")"); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
        mMapView.addPOIItem(marker);
    }

    public String getKeyHash(final Context context) {
        PackageInfo packageInfo = Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null) return null;
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.v("디버그 keyHash", String.valueOf(signature));
                Toast.makeText(MeetingMaking.this, (CharSequence) signature, Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }


}