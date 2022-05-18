package com.example.koreantime;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class mission10Fragment1 extends Fragment implements SeekBar.OnSeekBarChangeListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.activity_mission10_fragment1, container, false);
        SeekBar vibrate  = (SeekBar) rootView.findViewById(R.id.vibrate);
        SeekBar alarm = (SeekBar) rootView.findViewById(R.id.alarm);

        vibrate.setOnSeekBarChangeListener(this);
        alarm.setOnSeekBarChangeListener(this);

        return rootView;


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.d("seekbar", String.valueOf(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}