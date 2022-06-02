package com.example.koreantime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class edit extends AppCompatActivity {

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
                //바뀐 이름 + 주소 db에 저장
//                DocumentReference washingtonRef = db.collection("user").document(email);
//// Set the "isCapital" field of the city 'DC'
//                                washingtonRef
//                                        .update("groups_id", (List<String>) update_group)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w("add_group", "Error updating document", e);
//                                            }
//                                        });
                Intent intent = new Intent(edit.this, firstmenu.class);
                startActivity(intent);
            }
        });
    }
}