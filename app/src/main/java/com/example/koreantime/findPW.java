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
import com.google.firebase.auth.FirebaseAuth;

public class findPW extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        EditText email = findViewById(R.id.email);
        Button btn_find = findViewById(R.id.find);
        Button Exit = findViewById(R.id.exit);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInfo = email.getText().toString();

                auth.sendPasswordResetEmail(userInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(findPW.this, "비밀번호 재설정 이메일 발송 성공", Toast.LENGTH_SHORT).show();
                                    Log.d("find fw", "Email sent.");
                                    finish();
                                }
                                else{
                                    Toast.makeText(findPW.this, "비밀번호 재설정 이메일 발송 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}