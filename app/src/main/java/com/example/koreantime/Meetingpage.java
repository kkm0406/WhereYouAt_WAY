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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class Meetingpage extends AppCompatActivity {

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DTO_user user_info;
    String[] members;
    ArrayList<String> late_member=new ArrayList<String>();
    Button punish;
    Messaging temp=new Messaging();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        punish=findViewById(R.id.punish);
        punish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp.setToken("cZF6ykcGTaC2hRT-qBO5KM:APA91bGDEgSHsXKnxqZ0IvviBdXqyMf0RdZhaRDKPLNxwacaSkQn7QnhRr_JqpL-a2UNBO_OUhHqSXyuPLzefwrRkJVAYvz-IlcehtS5GjExkuXc0ViZa-KIiwJPyV9wr3LFVaT8zuux");
                temp.execute();
            }
        });
        setContentView(R.layout.activity_meetingpage);
    }
    void send_penalty(String token, String vibrate, String alarm){
        Messaging temp=new Messaging(token,vibrate,alarm);
        temp.execute();
    }
}