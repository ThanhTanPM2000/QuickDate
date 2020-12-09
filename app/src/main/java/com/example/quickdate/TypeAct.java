package com.example.quickdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class TypeAct extends AppCompatActivity {

    RangeSeekBar rangeSeekBar_age, rangeSeekBar_height, rangeSeekBar_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);



        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    final RangeSeekBar seekBar, final int progressStart, final int progressEnd, final boolean fromUser) { }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar seekBar) { }
        });
    }

    private void initialization(){
        rangeSeekBar_age = (RangeSeekBar) findViewById(R.id.rangeSeekBar_age_typeAct) ;
        rangeSeekBar_height = (RangeSeekBar) findViewById(R.id.rangeSeekBar_height_typeAct);
        rangeSeekBar_weight = (RangeSeekBar) findViewById(R.id.rangeSeekBar_weight_typeAct);
    }
}