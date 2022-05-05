package com.example.koreantime;

import static com.kakao.util.maps.helper.Utility.getKeyHash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MeetingMaking extends AppCompatActivity{

    private static final String LOG_TAG = "MeetingMakingKakaoMap";
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    public MapView mMapView;
    public MapPoint mapPoint;
    ViewGroup mapViewContainer;
    public LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_making);

        Button complete = findViewById(R.id.complete);
        MaterialCalendarView calendar = findViewById(R.id.calendar);

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

        //여기서부터 카카오맵 API
        getKeyHash(getApplicationContext());

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