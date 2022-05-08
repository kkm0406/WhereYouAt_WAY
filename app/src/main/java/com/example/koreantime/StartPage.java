package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        Button btn = findViewById(R.id.DB_practice);

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
                Intent intent = new Intent(StartPage.this, MainPage.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("user")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(StartPage.this, document.getData().values().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(StartPage.this, "에러", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }
}