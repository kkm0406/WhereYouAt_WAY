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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        Intent fcm = new Intent(StartPage.this, MyFirebaseMessaging.class);
        startService(fcm);
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
                Toast.makeText(StartPage.this, "로그인시도", Toast.LENGTH_SHORT).show();
                FirebaseFirestore db= FirebaseFirestore.getInstance();
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                if(id.getText().toString().length() == 0 || pw.getText().toString().length() == 0){
                    Toast.makeText(StartPage.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(id.getText().toString().trim(), pw.getText().toString())
                            .addOnCompleteListener(StartPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("login", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        db.collection("user").document(id.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DTO_user user;
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Log.d("login", "DocumentSnapshot data: " + document.getData());
                                                        user =document.toObject(DTO_user.class);
                                                        Intent intent = new Intent(StartPage.this, firstmenu.class);
                                                        intent.putExtra("user_info", user);
                                                        startActivity(intent);
                                                    } else {
                                                        Log.d("login", "No such document");
                                                    }
                                                } else {
                                                    Log.d("login", "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(StartPage.this, "로그인실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });



    }
}