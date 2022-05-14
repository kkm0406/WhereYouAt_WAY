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

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText pw = findViewById(R.id.pw);
        EditText address = findViewById(R.id.address);
        TextView signUp = findViewById(R.id.signUp);


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
                    Toast.makeText(SignUp.this, "db에 저장해야됨", Toast.LENGTH_SHORT).show();
                    Log.d("hi dhksfy", "createUserWithEmail:success");
                    mAuth= FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(tel.getText().toString().trim(), pw.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseFirestore db= FirebaseFirestore.getInstance();
                                DTO_user new_user = new DTO_user(name.getText().toString(),tel.toString(),address.toString(),"","");
                                Log.d("add_user", ""+new_user);
                                String email=tel.toString();

                                db.collection("user").document(email)
                                        .set(new_user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("add user", "DocumentSnapshot successfully written!");
//                                                Intent intent = new Intent(SignUp.this, StartPage.class);
//                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("add user", "Error writing document", e);
                                            }
                                        });
//                                Map<String, Object> city = new HashMap<>();
//                                city.put("name", "Los Angeles");
//                                city.put("state", "CA");
//                                city.put("country", "USA");
//
//                                db.collection("user").document("LA")
//                                        .set(city)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d(TAG, "DocumentSnapshot successfully written!");
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w(TAG, "Error writing document", e);
//                                            }
//                                        });


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUp.this, tel.getText().toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUp.this, pw.getText().toString(), Toast.LENGTH_SHORT).show();
                                Log.w("tag", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });

                }

            }
        });
    }
}