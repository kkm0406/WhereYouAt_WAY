package com.example.koreantime;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class mission10MainFragment extends Fragment {

    mission10MainFragment mainFragment;
    com.example.koreantime.mission10Fragment1 fragment1;
    com.example.koreantime.mission10Fragment2 fragment2;

    ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_mission10_main_fragment,container,false);

        pager = (ViewPager)view.findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(3);

        MypaperAdapter adapter = new MypaperAdapter(getChildFragmentManager());
        fragment1 = new com.example.koreantime.mission10Fragment1();
        adapter.addItem(fragment1);
        fragment2 = new com.example.koreantime.mission10Fragment2();
        adapter.addItem(fragment2);


        pager.setAdapter(adapter);

        return view;
    }

    class MypaperAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MypaperAdapter(FragmentManager fa) {
            super(fa);
        }

        public void addItem(Fragment f){
            items.add(f);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return  items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}