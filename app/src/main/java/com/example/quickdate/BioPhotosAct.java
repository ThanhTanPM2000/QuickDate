package com.example.quickdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quickdate.adapter.ImageRegisterAdapter;

import java.util.ArrayList;

public class BioPhotosAct extends AppCompatActivity {

    private ImageRegisterAdapter imageRegisterAdapter;
    private ArrayList<String> images = new ArrayList<String>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_photos);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        images.add("https://2.bp.blogspot.com/-Bzy0-Ljk8rs/TitZlwjFwXI/AAAAAAAABps/TGUfYSoSuAU/s1600/aragaki+yui+120.jpg");
        images.add("https://vignette.wikia.nocookie.net/drama/images/5/53/Aragaki_Yui29.jpg/revision/latest?cb=20180730190409&path-prefix=es");
        images.add("https://th.bing.com/th/id/OIP.4RBkLjzf3kbhAeIrmlO_xgAAAA?pid=Api&rs=1");
        images.add("https://th.bing.com/th/id/OIP.i1TdVXYUCdnAXZ4jZNS63QAAAA?pid=Api&w=300&h=300&rs=1");

        imageRegisterAdapter = new ImageRegisterAdapter(images, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageRegisterAdapter);
        recyclerView.setLayoutManager(linearLayout);
    }
}