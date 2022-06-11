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
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupMaking extends AppCompatActivity {

    ArrayList<String> searchList = new ArrayList<>();
    ArrayList<String> memberemail = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    ListView searchListView;
    ImageButton searchBtn;
    HorizontalScrollView memberScroll;
    LinearLayout memberList;
    ArrayList<String> joinedMember = new ArrayList<String>();
    LayoutInflater layoutInflater;
    EditText enterName;
    Button complete;
    DTO_group new_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_making);
        searchListView = findViewById(R.id.searchList);
        searchBtn = findViewById(R.id.searchBtn);
        memberScroll = findViewById(R.id.memberScroll);
        memberList = findViewById(R.id.memberList);
        enterName = findViewById((R.id.enterName));
        listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, searchList);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        complete = findViewById(R.id.complete);

        Intent Intent = getIntent();
        DTO_user user_info = (DTO_user) Intent.getSerializableExtra("user_info");
        joinedMember.add(user_info.getEmail());
        MakeMemberView(user_info.getNickname());


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_nick = enterName.getText().toString();
                listAdapter.clear();
                db.collection("user").whereEqualTo("nickname", search_nick)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        searchList.add(document.getId());
                                        memberemail.add(document.getData().get("nickname").toString());
                                    }
                                    searchListView.setAdapter(listAdapter);
                                } else {
                                    Log.d("add_group", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                joinedMember.add(searchList.get(i));
                MakeMemberView(memberemail.get(i));
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.group_name);
                new_group = new DTO_group(text.getText().toString(), joinedMember.size(), joinedMember);
                db.collection("group")
                        .add(new_group)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("add_group", "DocumentSnapshot successfully updated!");
                                Intent result_intent = new Intent();
                                setResult(1, result_intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("add_group", "Error adding document", e);
                            }
                        });
            }
        });



    }

    public void MakeMemberView(String name) {
        layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_add_member_view, null, false);

        LinearLayout linearLayout = view.findViewById(R.id.member);
        TextView textView = (TextView) linearLayout.getChildAt(0);
        textView.setText(name);
//        linearLayout.addView(textView);
        memberList.addView(view);
    }

}