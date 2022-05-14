package com.example.koreantime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class findPW extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        EditText email = findViewById(R.id.email);
        TextView btn_find = findViewById(R.id.btn_find);
        TextView Exit = findViewById(R.id.Exit);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInfo = email.getText().toString();

                Intent intent = new Intent(findPW.this, StartPage.class);
                startActivity(intent);
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(findPW.this, StartPage.class);
                startActivity(intent);
            }
        });
    }
}