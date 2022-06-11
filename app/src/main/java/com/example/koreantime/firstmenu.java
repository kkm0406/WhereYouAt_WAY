package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koreantime.DTO.DTO_group;
import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class firstmenu extends AppCompatActivity {

    DTO_user user_info;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    ArrayList<String> group_id=new ArrayList<String>(0);
    private double latitu;
    private double longti;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//그룹만들고서 그룹리스트 업데이트
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
            {
                if(resultCode==1){
                    String searchemail=user_info.getEmail();
                    db.collection("group").whereArrayContains("participation", searchemail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("added_group", Integer.toString(task.getResult().size()) );
                                        GridLayout grid = findViewById(R.id.grid);
//                                        for (int i=0;i<task.getResult().size()-2;i++) {//유저가 소속되어있는 그룹들 리턴
//
//                                        }
                                        grid.removeViewsInLayout(2,task.getResult().size()-1);
                                        int num=0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {//유저가 소속되어있는 그룹들 리턴
                                            createNew(num,document.getData().get("name").toString());
                                            Log.d("added_group", document.getId() + " => " + document.getData());
                                            num++;
                                        }
                                    } else {
                                        Log.d("added_group", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }case 1:
            {
                if(resultCode==1){
                    String searchemail=user_info.getEmail();
                    db.collection("user").document(searchemail)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            user_info = documentSnapshot.toObject(DTO_user.class);
                            Log.d("성공", user_info.getNickname());
                        }
                    });
                }
            }
            case 2:
            {
                if(resultCode==1){
                    String searchemail=user_info.getEmail();
                    db.collection("group").whereArrayContains("participation", searchemail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("added_group", Integer.toString(task.getResult().size()) );
                                        GridLayout grid = findViewById(R.id.grid);
//                                        for (int i=0;i<task.getResult().size()-2;i++) {//유저가 소속되어있는 그룹들 리턴
//
//                                        }
                                        grid.removeViewsInLayout(2,task.getResult().size());
                                        int num=0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {//유저가 소속되어있는 그룹들 리턴
                                            createNew(num,document.getData().get("name").toString());
                                            Log.d("added_group", document.getId() + " => " + document.getData());
                                            num++;
                                        }
                                    } else {
                                        Log.d("added_group", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu);

        Intent Intent = getIntent();
        user_info=(DTO_user) Intent.getSerializableExtra("user_info");

        LinearLayout edit = findViewById(R.id.edit);
        LinearLayout makeGroup = findViewById(R.id.makeGroup);
        TextView name = findViewById(R.id.name);
        GridLayout grid = findViewById(R.id.grid);
        Intent fcm = new Intent(getApplicationContext(), MyFirebaseMessaging.class);
        startService(fcm);

        name.setText(user_info.getNickname());

        db.collection("group").whereArrayContains("participation", user_info.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int num=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {//유저가 소속되어있는 그룹들 리턴
                                createNew(num,document.getData().get("name").toString());
                                Log.d("added_group", document.getId() + " => " + document.getData());
                                num++;
                                group_id.add(document.getId().toString());
                            }

                        } else {
                            Log.d("added_group", "Error getting documents: ", task.getException());
                        }
                    }
                });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//임시로 회의만들기 연결
                Intent intent = new Intent(firstmenu.this, edit.class);
                intent.putExtra("user_info", user_info);
                startActivityForResult(intent,1);
            }
        });

        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(firstmenu.this, GroupMaking.class);
                intent.putExtra("user_info", user_info);
                startActivityForResult(intent,0);
            }
        });
        start_real();
    }

    private void createNew(int e, String name){
        int num=e;
        GridLayout grid = findViewById(R.id.grid);

        LinearLayout group = new LinearLayout(this);
        group.setBackgroundResource(R.drawable.frame_shadow);
        group.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.FILL_HORIZONTAL;
//        params.setMargins(ConvertDPtoPX(this,10),0,ConvertDPtoPX(this,10),0);
        GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f));
        parem.setMargins(ConvertDPtoPX(this,10),0,ConvertDPtoPX(this,10),0);

//        GridLayout.LayoutParams params2 = new GridLayout.LayoutParams(grid.getLayoutParams());
//        params2.columnSpec = GridLayout.spec(e+2);
        group.setGravity(Gravity.CENTER);
        group.setLayoutParams(params);
        group.setLayoutParams(parem);
//        group.setLayoutParams(params2);
        group.setPadding(ConvertDPtoPX(this,25),ConvertDPtoPX(this,25),ConvertDPtoPX(this,25),ConvertDPtoPX(this,25));
        group.setId(e);

        TextView view1 = new TextView(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view1.setLayoutParams(params2);
        view1.setText(name);
        view1.setTextColor(Color.parseColor("#42C2FF"));
        view1.setGravity(Gravity.CENTER);
        view1.setPadding(0,ConvertDPtoPX(this,10),0,0);
        view1.setTextSize(18);

        ImageView view2 = new ImageView(this);
        LinearLayout.LayoutParams ImgParams = new LinearLayout.LayoutParams( ConvertDPtoPX(this,70), ConvertDPtoPX(this,70));
        ImgParams.setMargins(0,ConvertDPtoPX(this,15),0,0);
        view2.setLayoutParams(ImgParams);
        view2.setImageResource(R.drawable.groups);


        group.addView(view1);
        group.addView(view2);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("group").document(group_id.get(num))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DTO_group temp;
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    temp=document.toObject(DTO_group.class);
                                    Log.d("add_meeting", temp.getParticipation().toString());
                                    Log.d("add_meeting", temp.getName());
                                    Intent intent = new Intent(firstmenu.this, CarouselActivity.class);
                                    intent.putExtra("groupid",group_id.get(num));
                                    intent.putExtra("groupname",name);
                                    intent.putExtra("useremail",user_info.getEmail());

                                    intent.putExtra("membernick",(ArrayList)temp.getParticipation());
                                    startActivityForResult(intent,2);
                                } else {
                                    Log.d("add_meeting", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });


        grid.addView(group);

    }
    private void start_real(){
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
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                latitu = location.getLongitude();
                longti = location.getLatitude();
                Log.d("update location", String.valueOf(latitu));
                updateGPS();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000,
                5,
                gpsLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                5000,
                5,
                gpsLocationListener);
    }
    private void updateGPS(){
        db.collection("user").document(user_info.getEmail())
                .update("latitude", latitu)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("qq", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("qq", "Error updating document", e);
                    }
                });
        db.collection("user").document(user_info.getEmail())
                .update("longitude", longti)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("qq", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("qq", "Error updating document", e);
                    }
                });
    }
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}