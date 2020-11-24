package com.example.quickdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TabLayOutActivity extends AppCompatActivity {

    TabLayout myTab;
    TabItem myProfile, mySwiper, myMatches;
    ViewPager viewFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_lay_out);

        myTab = findViewById(R.id.tlMenu);
        myProfile = findViewById(R.id.tlProfile);
        mySwiper = findViewById(R.id.tlSwiper);
        myMatches = findViewById(R.id.tlMatches);
        viewFrag = findViewById(R.id.viewPager1);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), myTab.getTabCount());
        viewFrag.setAdapter(pagerAdapter);

        myTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewFrag.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}