package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
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

import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class firstmenu extends AppCompatActivity {

    DTO_user user_info;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
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
                            }
                        } else {
                            Log.d("added_group", "Error getting documents: ", task.getException());
                        }
                    }
                });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//임시로 회의만들기 연결
                Intent intent = new Intent(firstmenu.this, mission10.class);
                intent.putExtra("groupname","vx8wmq6udyECfB6woP3O");//임시로 그룹이름 전달
                String[] tempgroupmember=new String[] {"1@naver.com", "123456@naver.com","123@naver.com","email@naver.com"};
                intent.putExtra("groupmember",tempgroupmember);
                startActivity(intent);
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
    }

    private void createNew(int e, String name){
        GridLayout grid = findViewById(R.id.grid);

        LinearLayout group = new LinearLayout(this);
        group.setBackgroundResource(R.drawable.frame_shadow);
        group.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(ConvertDPtoPX(this,160), ConvertDPtoPX(this,160),Gravity.CENTER);
        groupParams.setMargins(ConvertDPtoPX(this,10),ConvertDPtoPX(this,10),ConvertDPtoPX(this,10),ConvertDPtoPX(this,10));
        group.setGravity(Gravity.CENTER);
        group.setLayoutParams(groupParams);
        group.setId(e);

        TextView view1 = new TextView(this);
        view1.setText(name);
        view1.setTextColor(Color.parseColor("#42C2FF"));
        view1.setGravity(Gravity.CENTER);
        view1.setPadding(0,ConvertDPtoPX(this,10),0,0);
        view1.setTextSize(18);
//        Typeface typeface = getResources().getFont(R.font.cafe);
//        view1.setTypeface(typeface);

        ImageView view2 = new ImageView(this);
        LinearLayout.LayoutParams ImgParams = new LinearLayout.LayoutParams(ConvertDPtoPX(this,70), ConvertDPtoPX(this,70));
        ImgParams.setMargins(0,ConvertDPtoPX(this,15),0,0);
        view2.setLayoutParams(ImgParams);
        view2.setImageResource(R.drawable.groups);


        group.addView(view1);
        group.addView(view2);

        grid.addView(group);
    }
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}