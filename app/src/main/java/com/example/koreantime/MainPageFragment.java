package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainPageFragment extends Fragment {

    ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_main_page_fragment,container,false);

        pager = (ViewPager)view.findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(3);

        MypaperAdapter adapter = new MypaperAdapter(getChildFragmentManager());

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