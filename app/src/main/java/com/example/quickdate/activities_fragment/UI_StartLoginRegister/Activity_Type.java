package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickdate.R;
import com.example.quickdate.model.User;
import com.thekhaeng.pushdownanim.PushDownAnim;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class Activity_Type extends AppCompatActivity {

    private RangeSeekBar rangeSeekBar_age, rangeSeekBar_height, rangeSeekBar_weight;
    private ImageButton btn_longTerm, btn_oneNight, btn_Settlement;
    private TextView tv_longTerm, tv_oneNight, tv_Settlement;
    private TextView tv_age, tv_height, tv_weight;
    private ImageView iv_backAct, iv_submit;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        init();
        doFunctionInAct();
    }

    private void init() {
        rangeSeekBar_age = findViewById(R.id.rangeSeekBar_age_typeAct);
        rangeSeekBar_height = findViewById(R.id.rangeSeekBar_height_typeAct);
        rangeSeekBar_weight = findViewById(R.id.rangeSeekBar_weight_typeAct);
        btn_oneNight = findViewById(R.id.btn_oneNight_typeAct);
        btn_longTerm = findViewById(R.id.btn_longTerm_typeAct);
        btn_Settlement = findViewById(R.id.btn_settlement_typeAct);
        tv_age = findViewById(R.id.tv_value_age);
        tv_height = findViewById(R.id.tv_value_height);
        tv_weight = findViewById(R.id.tv_value_weight);
        iv_backAct = findViewById(R.id.iv_backAct_typeAct);
        iv_submit = findViewById(R.id.iv_submit_typeAct);
        tv_longTerm = findViewById(R.id.tv_long_term);
        tv_oneNight = findViewById(R.id.tv_one_night);
        tv_Settlement = findViewById(R.id.tv_settlement);

        user = (User) getIntent().getSerializableExtra("User");
    }

    private void doFunctionInAct() {
        rangeSeekBarAgeFunction();
        rangeSeekBarHeightFunction();
        rangeSeekBarWeightFunction();
        lookingForFunction();

        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(v -> callBackAct());

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(v -> callSubmitAct());
    }

    private void rangeSeekBarAgeFunction() {
        rangeSeekBar_age.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                user.getLookingFor().setMin_age(18 + rangeSeekBar.getProgressStart());
                user.getLookingFor().setMax_age(18 + rangeSeekBar.getProgressEnd());
                String strValue = user.getLookingFor().getMin_age() + "-" + user.getLookingFor().getMax_age();
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

    private void rangeSeekBarHeightFunction() {
        rangeSeekBar_height.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                user.getLookingFor().setMin_height(100 + rangeSeekBar.getProgressStart());
                user.getLookingFor().setMax_height(100 + rangeSeekBar.getProgressEnd());
                String strValue = user.getLookingFor().getMin_height() + "-" + user.getLookingFor().getMax_height() + "cm";
                tv_height.setText(strValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar) {

            }
        });
    }

    private void rangeSeekBarWeightFunction() {
        rangeSeekBar_weight.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                user.getLookingFor().setMin_weight(40 + rangeSeekBar.getProgressStart());
                user.getLookingFor().setMax_weight(40 + rangeSeekBar.getProgressEnd());
                String strValue = user.getLookingFor().getMin_weight() + "-" + user.getLookingFor().getMax_weight() + "kg";
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

    private void lookingForFunction() {
        PushDownAnim.setPushDownAnimTo(btn_oneNight).setOnClickListener(v -> {
            user.getLookingFor().setLooking("OneNight");
            btn_oneNight.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_rectangle_white));
            btn_oneNight.setColorFilter(Activity_Type.this.getResources().getColor(R.color.red, null));
            tv_oneNight.setTextColor(getResources().getColor(R.color.black, null));

            btn_longTerm.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_retangle_clarity));
            btn_longTerm.setColorFilter(Activity_Type.this.getResources().getColor(R.color.white, null));
            tv_longTerm.setTextColor(getResources().getColor(R.color.white, null));

            btn_Settlement.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_retangle_clarity));
            btn_Settlement.setColorFilter(Activity_Type.this.getResources().getColor(R.color.white, null));
            tv_Settlement.setTextColor(getResources().getColor(R.color.white, null));
        });

        PushDownAnim.setPushDownAnimTo(btn_longTerm).setOnClickListener(v -> {
            user.getLookingFor().setLooking("LongTerm");
            btn_longTerm.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_rectangle_white));
            btn_longTerm.setColorFilter(Activity_Type.this.getResources().getColor(R.color.red, null));
            tv_longTerm.setTextColor(getResources().getColor(R.color.black, null));

            btn_oneNight.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_retangle_clarity));
            btn_oneNight.setColorFilter(Activity_Type.this.getResources().getColor(R.color.white, null));
            tv_oneNight.setTextColor(getResources().getColor(R.color.white, null));

            btn_Settlement.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_retangle_clarity));
            btn_Settlement.setColorFilter(Activity_Type.this.getResources().getColor(R.color.white, null));
            tv_Settlement.setTextColor(getResources().getColor(R.color.white, null));
        });

        PushDownAnim.setPushDownAnimTo(btn_Settlement).setOnClickListener(v -> {
            user.getLookingFor().setLooking("Settlement");
            btn_Settlement.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_rectangle_white));
            btn_Settlement.setColorFilter(Activity_Type.this.getResources().getColor(R.color.red, null));
            tv_Settlement.setTextColor(getResources().getColor(R.color.black, null));

            btn_longTerm.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_retangle_clarity));
            btn_longTerm.setColorFilter(Activity_Type.this.getResources().getColor(R.color.white, null));
            tv_longTerm.setTextColor(getResources().getColor(R.color.white, null));

            btn_oneNight.setBackground(ContextCompat.getDrawable(Activity_Type.this, R.drawable.shape_retangle_clarity));
            btn_oneNight.setColorFilter(Activity_Type.this.getResources().getColor(R.color.white, null));
            tv_oneNight.setTextColor(getResources().getColor(R.color.white, null));
        });
    }

    private void callBackAct() {
        Intent intent = new Intent(Activity_Type.this, Activity_BioPhotos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", user);
        startActivity(intent);
        finish();
    }

    private void callSubmitAct() {
        Intent intent = new Intent(Activity_Type.this, Activity_Interests.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", user);
        startActivity(intent);
        finish();
    }
}