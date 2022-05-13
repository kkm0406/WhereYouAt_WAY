package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koreantime.DTO.DTO_group;
import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        EditText id = findViewById(R.id.id);
        EditText pw = findViewById(R.id.pw);
        TextView findPw = findViewById(R.id.findPw);
        TextView signUp = findViewById(R.id.signUp);
        TextView logIn = findViewById(R.id.login);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPage.this, SignUp.class);
                startActivity(intent);
            }
        });

        findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPage.this, findPW.class);
                startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db= FirebaseFirestore.getInstance();

                db.collection("user").document("users").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DTO_user user;
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
                                user =document.toObject(DTO_user.class);
                                Intent intent = new Intent(StartPage.this, MainPage.class);
                                intent.putExtra("user_info", user);
                                startActivity(intent);
                            } else {
                                Log.d("DATABASE", "No such document");
                            }
                        } else {
                            Log.d("DATABASE", "get failed with ", task.getException());
                        }
                    }
                });
//                for (String group_id:user.getGroups_id()) {
//                    db.collection("user").document(group_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
//                                    groups[0] =(document.toObject(DTO_group.class));
//                                } else {
//                                    Log.d("DATABASE", "No such document");
//                                }
//                            } else {
//                                Log.d("DATABASE", "get failed with ", task.getException());
//                            }
//                        }
//                    });
//                }

            }
        });



    }
}