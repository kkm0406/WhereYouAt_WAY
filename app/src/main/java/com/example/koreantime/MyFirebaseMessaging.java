package com.example.koreantime;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public MyFirebaseMessaging() {
        super();
        Log.d("FCM Token", "FCM start");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM Token", "notification");
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {

            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"),remoteMessage.getData().get("vibrate"),remoteMessage.getData().get("alarm"));
        }
    }

//    @Override
//    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
//    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.popup);
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_message, message);
        return remoteViews;
    }
    public void showNotification(String title, String message,String vibrate,String alarm) {
        //팝업 터치시 이동할 액티비티를 지정합니다.
        Intent intent = new Intent(this, firstmenu.class);
        //알림 채널 아이디 : 본인 하고싶으신대로...
        String channel_id = "CHN_IDdf";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //기본 사운드로 알림음 설정. 커스텀하려면 소리 파일의 uri 입력
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.way)
                .setSound(uri)
                .setAutoCancel(true)
                .setOnlyAlertOnce(false) //동일한 알림은 한번만.. : 확인 하면 다시 울림
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //안드로이드 버전이 커스텀 알림을 불러올 수 있는 버전이면
            //커스텀 레이아웃 호출
            builder = builder.setContent(getCustomDesign(title, "빨리 와!!!!!"));
        } else { //아니면 기본 레이아웃 호출
            builder = builder.setContentTitle(title)
                    .setContentText("빨리 와!!!!!")
                    .setSmallIcon(R.drawable.way); //커스텀 레이아웃에 사용된 로고 파일과 동일하게..
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //알림 채널이 필요한 안드로이드 버전을 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "CHN_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationChannel.setVibrationPattern(new long[]{100, 1000,100,5000,100,10000});
            notificationManager.createNotificationChannel(notificationChannel);

            Log.d("FCM Token", "씨발");
        }
        //알림 표시 !
        notificationManager.notify(0, builder.build());
    }


}
