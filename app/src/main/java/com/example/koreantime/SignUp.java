package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String TAG = getClass().getSimpleName();
    String pushtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText pw = findViewById(R.id.pw);
        EditText address = findViewById(R.id.address);
        TextView signUp = findViewById(R.id.signUp);
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



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean signFlag = true;
                if (name.getText().toString().length() == 0) {
                    name.setHint("닉네임을 입력하세요");
                    name.requestFocus();
                    signFlag = false;
                }
                if (email.getText().toString().length() == 0) {
                    email.setHint("이메일을 입력하세요");
                    email.requestFocus();
                    signFlag = false;
                }
                if (pw.getText().toString().length() == 0) {
                    pw.setHint("비밀번호를 입력하세요");
                    pw.requestFocus();
                    signFlag = false;
                }
                if (address.getText().toString().length() == 0) {
                    address.setHint("주소를 입력하세요");
                    address.requestFocus();
                    signFlag = false;
                }
                if (signFlag) {
                    mAuth= FirebaseAuth.getInstance();
                    Log.d("signup", "다적음");

                    mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pw.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseFirestore db= FirebaseFirestore.getInstance();
                                DTO_user new_user = new DTO_user(email.getText().toString(),name.getText().toString(),address.getText().toString(),"","",pushtoken);
                                Log.d("signup", ""+new_user);

                                db.collection("user").document(email.getText().toString())
                                        .set(new_user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("signup", "DocumentSnapshot successfully written!");
                                                Toast.makeText(SignUp.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("signup", "Error writing document", e);
                                            }
                                        });
                            } else {
                                Log.w("signup", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });

                }

            }
        });
    }
}