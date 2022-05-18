package com.example.koreantime;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class mission10 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    com.example.koreantime.mission10MainFragment mainFragment;
    com.example.koreantime.mission10Fragment1 fragment1;
    com.example.koreantime.mission10Fragment2 fragment2;
    Toolbar toolbar;
    DrawerLayout drawer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission10);

        mainFragment = new com.example.koreantime.mission10MainFragment();
        fragment2 = new com.example.koreantime.mission10Fragment2();
        drawer = findViewById(R.id.drawer_layout);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
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