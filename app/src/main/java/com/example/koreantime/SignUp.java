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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String TAG = getClass().getSimpleName();
    Dialog imgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText name = findViewById(R.id.name);
        EditText id = findViewById(R.id.id);
        EditText pw = findViewById(R.id.pw);
        EditText address = findViewById(R.id.address);
        EditText tel = findViewById(R.id.tel);
        TextView getImg = findViewById(R.id.getImg);
        TextView signUp = findViewById(R.id.signUp);

        imgDialog = new Dialog(SignUp.this);
        imgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imgDialog.setContentView(R.layout.get_img_dialog);

        getImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImgDialog();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean signFlag = true;
                if (name.getText().toString().length() == 0) {
                    name.setHint("이름을 입력하세요");
                    name.requestFocus();
                    signFlag = false;
                }
                if (id.getText().toString().length() == 0) {
                    id.setHint("ID를 입력하세요");
                    id.requestFocus();
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
                if (tel.getText().toString().length() == 0) {
                    tel.setHint("전화번호를 입력하세요");
                    tel.requestFocus();
                    signFlag = false;
                }
                if (signFlag) {
                    Toast.makeText(SignUp.this, "db에 저장해야됨", Toast.LENGTH_SHORT).show();
                    Log.d("hi dhksfy", "createUserWithEmail:success");
                    mAuth= FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(tel.getText().toString(), pw.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d("tag", "createUserWithEmail:success"+user);
                                Intent intent = new Intent(SignUp.this, StartPage.class);
                                startActivity(intent);
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


    public void showImgDialog() {
            imgDialog.show();

            Button cancel = imgDialog.findViewById(R.id.cancle);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgDialog.dismiss();
                }
            });

            Button fromCamera = imgDialog.findViewById(R.id.fromCamera);
            fromCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SignUp.this, "카메라 연결", Toast.LENGTH_SHORT).show();
                }
            });

            Button fromFile = imgDialog.findViewById(R.id.fromFile);
            fromFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SignUp.this, "파일 저장소 연결", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }