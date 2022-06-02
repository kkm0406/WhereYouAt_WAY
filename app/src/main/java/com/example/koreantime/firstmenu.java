package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
                                        for (QueryDocumentSnapshot document : task.getResult()) {//유저가 소속되어있는 그룹들 리턴
                                            Log.d("added_group", document.getId() + " => " + document.getData());
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
        Intent fcm = new Intent(getApplicationContext(), MyFirebaseMessaging.class);
        startService(fcm);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(firstmenu.this, GroupMaking.class);
                startActivity(intent);
            }
        });

        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(firstmenu.this, "Asdf", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(firstmenu.this, GroupMaking.class);
                intent.putExtra("user_info", user_info);
                startActivityForResult(intent,0);
            }
        });
    }
}