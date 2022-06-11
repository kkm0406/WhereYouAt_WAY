package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class edit extends AppCompatActivity {

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DTO_user user_info;
    String pushtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText name = findViewById(R.id.name);
        EditText address1 = findViewById(R.id.address1);
        EditText address2 = findViewById(R.id.address2);
        EditText address3 = findViewById(R.id.address3);
        ImageView chAd1 = findViewById(R.id.chAd1);
        ImageView chAd2 = findViewById(R.id.chAd2);
        ImageView chAd3 = findViewById(R.id.chAd3);
        Button confirm = findViewById(R.id.confirm);
        Intent Intent = getIntent();

        user_info=(DTO_user) Intent.getSerializableExtra("user_info");
        name.setText(user_info.getNickname());
        address1.setText(user_info.getAddr1());
        address2.setText(user_info.getAddr2());
        address3.setText(user_info.getAddr3());
        Task<String> token = FirebaseMessaging.getInstance().getToken();

        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("FCM Token", task.getResult().toString());
                    pushtoken=task.getResult().toString();
                }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText(null);
            }
        });

        chAd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address1.setText(null);
            }
        });

        chAd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address2.setText(null);
            }
        });

        chAd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address3.setText(null);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DTO_user new_user=new DTO_user(user_info.getEmail(),name.getText().toString(),address1.getText().toString(),address2.getText().toString(),address3.getText().toString(),pushtoken);
                db.collection("user").document(user_info.getEmail())
                        .set(new_user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent result_intent = new Intent();
                                setResult(1, result_intent);
                                Log.d("edit_profile", "DocumentSnapshot successfully written!");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("edit_profile", "Error writing document", e);
                            }
                        });
// Set the "isCapital" field of the city 'DC'

            }
        });
    }
}