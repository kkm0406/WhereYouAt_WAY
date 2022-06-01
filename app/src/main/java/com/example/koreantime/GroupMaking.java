package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    ArrayList<String> searchList = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    ListView searchListView;
    ImageButton searchBtn;
    HorizontalScrollView memberScroll;
    LinearLayout memberList;
    ArrayList<LinearLayout> members;
    LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_making);
        searchListView = findViewById(R.id.searchList);
        searchBtn = findViewById(R.id.searchBtn);
        memberScroll = findViewById(R.id.memberScroll);
        memberList = findViewById(R.id.memberList);

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
//                                        finish();
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




        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchList.add("member1");
                searchList.add("member2");
                searchList.add("member3");
                searchList.add("member4");
                searchList.add("member5");
                searchList.add("member6");

                listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, searchList);
                searchListView.setAdapter(listAdapter);
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(GroupMaking.this, searchList.get(i)+"asdfasdf", Toast.LENGTH_SHORT).show();
//                members.add(MakeMemberView(searchList.get((i))));
                MakeMemberView(searchList.get((i)));
            }
        });


    }

    public void MakeMemberView (String name){
        layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_add_member_view, null, false);

        LinearLayout linearLayout = view.findViewById(R.id.member);
        TextView textView = (TextView) linearLayout.getChildAt(0);
        textView.setText(name);
//        linearLayout.addView(textView);
        memberList.addView(view);
    }

}