package com.example.quickdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thekhaeng.pushdownanim.PushDownAnim;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class TypeAct extends AppCompatActivity {

    RangeSeekBar rangeSeekBar_age, rangeSeekBar_height, rangeSeekBar_weight;
    Button btn_longTerm, btn_oneNight, btn_Settlement;
    TextView tv_age, tv_height, tv_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        initialization();
        doFunctionInAct();
    }

    private void initialization(){
        rangeSeekBar_age = (RangeSeekBar) findViewById(R.id.rangeSeekBar_age_typeAct) ;
        rangeSeekBar_height = (RangeSeekBar) findViewById(R.id.rangeSeekBar_height_typeAct);
        rangeSeekBar_weight = (RangeSeekBar) findViewById(R.id.rangeSeekBar_weight_typeAct);
        btn_oneNight = (Button) findViewById(R.id.btn_oneNight_typeAct);
        btn_longTerm = (Button) findViewById(R.id.btn_longTerm_typeAct);
        btn_Settlement = (Button) findViewById(R.id.btn_settlement_typeAct);
        tv_age = (TextView) findViewById(R.id.tv_value_age);
        tv_height = (TextView) findViewById(R.id.tv_value_height);
        tv_weight = (TextView) findViewById(R.id.tv_value_weight);
    }

    private void doFunctionInAct(){
        rangeSeekBarAgeFunction();
        rangeSeekBarHeightFunction();
        rangeSeekBarWeightFunction();
        lookingForFunction();
    }

    private void rangeSeekBarAgeFunction(){
        rangeSeekBar_age.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                int startValue = 18 + rangeSeekBar.getProgressStart();
                int endValue = 18 + rangeSeekBar.getProgressEnd();
                String strValue = startValue + "-" + endValue;
                tv_age.setText(strValue);
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar rangeSeekBar) {

            }
        });
    }

    private void rangeSeekBarHeightFunction(){
        rangeSeekBar_height.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                float startValue = (float) (100 + rangeSeekBar.getProgressStart()) /100;
                float endValue = (float) (100 + rangeSeekBar.getProgressEnd()) /100;
                String strValue = startValue + "-" + endValue + "m";
                tv_height.setText(strValue);
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar rangeSeekBar) {

            }
        });
    }

    private void rangeSeekBarWeightFunction(){
        rangeSeekBar_weight.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                int startValue = 40 + rangeSeekBar.getProgressStart();
                int endValue = 40 + rangeSeekBar.getProgressEnd();
                String strValue = startValue + "-" + endValue + "kg";
                tv_weight.setText(strValue);
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar rangeSeekBar) {

            }
        });
    }

    private void lookingForFunction(){
        PushDownAnim.setPushDownAnimTo(btn_oneNight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_oneNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_one_night_select));
                btn_longTerm.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_long_term));
                btn_Settlement.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_settlement));
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_longTerm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_longTerm.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_long_term_select));
                btn_oneNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_one_night));
                btn_Settlement.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_settlement));
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_Settlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Settlement.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_settlement_select));
                btn_oneNight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_one_night));
                btn_longTerm.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_long_term));
            }
        });
    }
}