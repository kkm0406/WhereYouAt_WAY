package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.koreantime.DTO.DTO_user;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    mission10MainFragment mainFragment;
    Toolbar toolbar;
    DrawerLayout drawer;
    String[] orderList = {"생성순", "시간순"};
    ArrayList<ImageButton> meetingMaking = new ArrayList<>();
    DTO_user user_info;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//그룹만들고서 user_info 업데이트
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
            {
                if(resultCode==1){
                    user_info=(DTO_user) data.getSerializableExtra("result_user");
                    Log.d("group_making", "DocumentSnapshot data: " + user_info.getGroups_id());
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Intent Intent = getIntent();
        user_info=(DTO_user) Intent.getSerializableExtra("user_info");
        Log.d("DATABASE", "DocumentSnapshot data: " + user_info);

        ImageButton group_making_btn = findViewById(R.id.group_making_btn);
        LinearLayout showGroups = findViewById(R.id.showGroups);

        meetingMaking.add(findViewById(R.id.meeting_making1));
        //이미지 버튼 arraylist에 add하면서 인덱스 맞춰서 호출
        meetingMaking.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, MeetingMaking.class);
                startActivity(intent);
            }
        });

        group_making_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainPage.this, "Asdf", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPage.this, GroupMaking.class);
                intent.putExtra("user_info", user_info);
                startActivityForResult(intent,0);
            }
        });
        //그룹 만드는 페이지로 intent

        //임시버튼 만들어서 누르면 동적으로 그룹 보여지는 레이아웃 생성
        //나중에 그룹명, 인원 등을 db에서 읽어서 tmpClass로 보낸 후
        //MakeNewGroup에서 tmpClass를 담은 arraylist 인덱스 맞춰서
        //레이아웃 생성 -> tmpClass 이름 바꿔야함
        //tmpClass 안에 imageButton도 동적으로 바뀌게
        //동적으로 레이아웃 만들다 보면 group_making_btn를 가리는 거 해결

        Spinner orderSpinner = findViewById(R.id.order);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(MainPage.this,
                android.R.layout.simple_spinner_dropdown_item, orderList);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    Toast.makeText(MainPage.this, "생성순", Toast.LENGTH_SHORT).show();
                } else if (i == 2) {
                    Toast.makeText(MainPage.this, "시간순", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //미팅 보여주는 spinner(생성순, 시간순)




        mainFragment = new mission10MainFragment();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.menu1) {
//            toolbar.setTitle("첫 번째 화면 2018038065 김광모");
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
//        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

