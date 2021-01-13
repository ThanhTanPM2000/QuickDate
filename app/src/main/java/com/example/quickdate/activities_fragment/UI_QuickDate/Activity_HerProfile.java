package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quickdate.R;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.adapter.SliderAdapter;
import com.example.quickdate.model.Interest;
import com.example.quickdate.model.User;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_HerProfile extends AppCompatActivity {

    // Components in View
    private CircleImageView circleImageView;
    private TextView tv_info, tv_info2, tv_info3, tv_info4;
    private RecyclerView recyclerView;
    private ProgressDialog pd;

    // Model
    private User herUser;


    // Slider images
    private SliderView sliderView;
    private SliderAdapter adapter;

    // Array needs in View
    private ArrayList<Interest> interests;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_her_profile);
        init();
        doFunction();
    }

    private void init() {

        // Init User
        herUser = (User) getIntent().getSerializableExtra("User");

        // Init View
        circleImageView = findViewById(R.id.avatarIv_herProfile);
        tv_info = findViewById(R.id.tv_info_herProfile);
        tv_info2 = findViewById(R.id.tv_info2_herProfile);
        tv_info3 = findViewById(R.id.tv_info3_herProfile);
        tv_info4 = findViewById(R.id.tv_info4_herProfile);
        recyclerView = findViewById(R.id.recyclerView_herProfile);
        sliderView = findViewById(R.id.imageSlider_herProfile);

        // Init array interests
        interests = new ArrayList<Interest>();

    }

    private void doFunction() {
        showSliderImage();
        showInfo();
        showInterestRecyclerview();
    }

    private void showSliderImage() {
        // Init SliderAdapter
        adapter = new SliderAdapter(herUser.getInfo().getImages() , Activity_HerProfile.this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void showInfo() {
        //show avatar image
        try {
            // if image is received then set
            Picasso.get().load(herUser.getInfo().getImgAvt()).placeholder(R.drawable.ic_thumb).into(circleImageView);
        } catch (Exception e) {
            // if there is any exception while getting image then set default
            circleImageView.setImageResource(R.drawable.img_doneemoji);
        }

        String[] arr = herUser.getInfo().getNickname().split(" ");
        if (arr[arr.length - 1].length() > 8) {
            tv_info.setText(arr[arr.length - 1].substring(0, 8) + "...,");
        } else {
            tv_info.setText(arr[arr.length - 1] + ",");
        }
        tv_info2.setText(herUser.getInfo().getAge() + "");
        tv_info3.setText(herUser.getInfo().getProvincial() + ", " + herUser.getInfo().getHeight() + "cm - " + herUser.getInfo().getWeight() + "kg");
        tv_info4.setText(herUser.getInfo().getAboutMe());
    }

    private void showInterestRecyclerview() {
        for (Interest item : herUser.getInterests()) {
            if (item.getStatus()) {
                interests.add(item);
            }
        }

        InterestsAdapter interestsAdapter = new InterestsAdapter(interests, true);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_HerProfile.this, RecyclerView.HORIZONTAL, false));
    }
}