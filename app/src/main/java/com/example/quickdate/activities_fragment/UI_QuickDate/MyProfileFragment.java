package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.quickdate.R;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.listener.UserListener;
import com.example.quickdate.model.Info;
import com.example.quickdate.model.Interest;
import com.example.quickdate.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;


public class MyProfileFragment extends Fragment{
    private ImageSlider imageSlider;
    private ArrayList<SlideModel> slideModels;
    private ArrayList<String> index;
    private TextView tv_info, tv_info2, tv_info3, tv_info4;
    private Button btn_edit;
    private RecyclerView recyclerView;
    private View root;
    private User myUser;
    private ArrayList<Interest> interestsTrue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization();
    }

    private void initialization() {
        imageSlider = root.findViewById(R.id.imageSlider_Profile);
        slideModels = new ArrayList<SlideModel>();
        tv_info = root.findViewById(R.id.tv_info_myProfile);
        tv_info2 = root.findViewById(R.id.tv_info2_myProfile);
        tv_info3 = root.findViewById(R.id.tv_info3_myProfile);
        tv_info4 = root.findViewById(R.id.tv_info4_myProfile);
        btn_edit = root.findViewById(R.id.edit_myProfile);
        recyclerView = root.findViewById(R.id.recyclerView_myProfile);
        interestsTrue = new ArrayList<Interest>();

        ((SwipeAct) getActivity()).passVal(new UserListener() {
            @Override
            public void getUser(User user) {
                myUser = user;
                index = new ArrayList<>(myUser.getInfo().getImages().keySet());
                doFunctionInAct();
            }
        });
    }

    private void doFunctionInAct() {
        showSliderImage();
        showInfo();
        showInterestRecyclerview();

        PushDownAnim.setPushDownAnimTo(btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMyProfile();
            }
        });
    }

    private void showSliderImage() {
        for (int i = 0; i < myUser.getInfo().getImages().size(); i++) {
            slideModels.add(new SlideModel(myUser.getInfo().getImages().get(index.get(i))));
        }
        imageSlider.setImageList(slideModels, false);
    }

    private void showInfo() {
        String[] arr = myUser.getInfo().getNickname().split(" ");
        if(arr[arr.length -1].length() > 8){
            tv_info.setText(arr[arr.length -1].substring(0, 8) + "...,");
        }else{
            tv_info.setText(arr[arr.length-1] + ",");
        }
        tv_info2.setText(myUser.getInfo().getAge()+"");
        tv_info3.setText(myUser.getInfo().getProvincial() + ", " + myUser.getInfo().getHeight() + "cm - " + myUser.getInfo().getWeight() + "kg");
        tv_info4.setText(myUser.getInfo().getAboutMe());
    }

    private void showInterestRecyclerview() {
        for(Interest item : myUser.getInterests()){
            if(item.getStatus()){
                interestsTrue.add(item);
            }
        }
        InterestsAdapter interestsAdapter = new InterestsAdapter(interestsTrue, myUser.getStatus() == 1);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
    }

    private void editMyProfile(){

    }
}