package com.example.koreantime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.koreantime.DTO.DTO_group;
import com.example.koreantime.DTO.DTO_schecule;
import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class mission10 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    com.example.koreantime.mission10MainFragment mainFragment;
    com.example.koreantime.mission10Fragment1 fragment1;
    com.example.koreantime.mission10Fragment2 fragment2;
    Toolbar toolbar;
    DrawerLayout drawer;
    String[] groupmember;
    String groupname;
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission10);

        Intent Intent = getIntent();
        groupname = Intent.getStringExtra("groupname");//그룹 이름 받아오기
        groupmember = Intent.getStringArrayExtra("groupmember");//그룹멤버 받아오기
        Log.d("meetingmake", groupmember[2]);
        for (String email:groupmember) {//그룹멤버의 첫번째 주소 정보 가져옴
            db.collection("user").document(email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Log.d("add_meeting", document.getData().get("addr1").toString());
                            } else {
                                Log.d("add_meeting", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        DTO_schecule new_meeting = new DTO_schecule("123456@naver.com", "청주시 개신동","진동", "20220602","1512","첨만드는 회의");
        db.collection("group").document(groupname).collection("schedule")//생성한 회의 DB에 쓰기
                .add(new_meeting)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("add_meeting", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("add_meeting", "Error adding document", e);
                    }
                });
//        mainFragment = new com.example.koreantime.mission10MainFragment();
//        fragment2 = new com.example.koreantime.mission10Fragment2();
//        drawer = findViewById(R.id.drawer_layout);
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu1){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
        }
        else if(id == R.id.menu2){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment2).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}