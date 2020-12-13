package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class TypeAct extends AppCompatActivity {

    private RangeSeekBar rangeSeekBar_age, rangeSeekBar_height, rangeSeekBar_weight;
    private Button btn_longTerm, btn_oneNight, btn_Settlement;
    private TextView tv_age, tv_height, tv_weight;
    private ImageView iv_backAct, iv_submit;
    private HashMap<String, Object> map;
    private FirebaseAuth firebaseAuth;
    private int lookingFor;
    private int minAge;
    private int maxAge;
    private float minHeight;
    private float maxHeight;
    private int minWeight;
    private int maxWeight;

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
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_typeAct);
        iv_submit = (ImageView) findViewById(R.id.iv_submit_typeAct);

        minAge = 18;
        maxAge = 38;
        minHeight = 1.6f;
        maxHeight = 1.75f;
        minWeight = 55;
        maxWeight = 65;

        firebaseAuth = FirebaseAuth.getInstance();
        map = new HashMap<>();
    }

    private void doFunctionInAct(){
        rangeSeekBarAgeFunction();
        rangeSeekBarHeightFunction();
        rangeSeekBarWeightFunction();
        lookingForFunction();
        callBackAct();
        callSubmitAct();
    }

    private void rangeSeekBarAgeFunction(){
        rangeSeekBar_age.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                minAge = 18 + rangeSeekBar.getProgressStart();
                maxAge = 18 + rangeSeekBar.getProgressEnd();
                String strValue = minAge + "-" + maxAge;
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
                minHeight = (float) (100 + rangeSeekBar.getProgressStart()) /100;
                maxHeight = (float) (100 + rangeSeekBar.getProgressEnd()) /100;
                String strValue = minHeight + "-" + maxHeight + "m";
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

    private void rangeSeekBarWeightFunction(){
        rangeSeekBar_weight.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                minWeight = 40 + rangeSeekBar.getProgressStart();
                maxWeight = 40 + rangeSeekBar.getProgressEnd();
                String strValue = minWeight + "-" + maxWeight + "kg";
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

    private void callBackAct(){
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelectGenderAct.class));
                finish();
            }
        });
    }

    private void callSubmitAct(){
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("min_Age", minAge);
                map.put("max_Age", maxAge);
                map.put("min_height", minHeight);
                map.put("max_height", maxHeight);
                map.put("min_weight", minWeight);
                map.put("max_weight", maxWeight);
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getCurrentUser().getUid());
                db.child("type").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), SwipeAct.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}