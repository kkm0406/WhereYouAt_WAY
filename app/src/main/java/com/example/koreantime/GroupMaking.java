package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.koreantime.DTO.DTO_group;
import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupMaking extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_making);
        FirebaseFirestore db= FirebaseFirestore.getInstance();

        Intent Intent = getIntent();
        DTO_user user_info=(DTO_user) Intent.getSerializableExtra("user_info");
        DTO_group new_group=new DTO_group("name",1,new ArrayList<>(1));

        db.collection("group")
                .add(new_group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("add_group", "DocumentSnapshot written with ID: " + documentReference.getId());
                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        String email=user_info.getEmail();
                        ArrayList<String> update_group=(ArrayList<String>) user_info.getGroups_id();
                        update_group.add(documentReference.getId());
                        Log.d("add_group", update_group+"");
                        DocumentReference washingtonRef = db.collection("user").document(email);
// Set the "isCapital" field of the city 'DC'
                        washingtonRef
                                .update("groups_id",(List<String>)update_group )
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("add_group", "DocumentSnapshot successfully updated!");
                                        Intent result_intent=new Intent();
                                        result_intent.putExtra("result_user",user_info);
                                        setResult(1,result_intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("add_group", "Error updating document", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("add_group", "Error adding document", e);
                    }
                });
        //                for (String group_id:user.getGroups_id()) {
//                    db.collection("user").document(group_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
//                                    groups[0] =(document.toObject(DTO_group.class));
//                                } else {
//                                    Log.d("DATABASE", "No such document");
//                                }
//                            } else {
//                                Log.d("DATABASE", "get failed with ", task.getException());
//                            }
//                        }
//                    });
//                }

//        Button back = findViewById(R.id.back);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GroupMaking.this, MainPage.class);
//                startActivity(intent);
//            }
//        });


    }
}