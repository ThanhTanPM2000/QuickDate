package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickdate.R;
import com.example.quickdate.model.LookingFor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class TypeFragment extends Fragment {

    private RangeSeekBar rangeSeekBar_age, rangeSeekBar_height, rangeSeekBar_weight;
    private Button btn_longTerm, btn_oneNight, btn_Settlement;
    private TextView tv_age, tv_height, tv_weight;
    private ImageView iv_backAct, iv_submit;
    private FirebaseAuth firebaseAuth;
    private LookingFor lookingFor;
    private View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        doFunctionInAct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_type, container, false);
    }
    private void initialization(View view) {
        this.view = view;
        rangeSeekBar_age = (RangeSeekBar) view.findViewById(R.id.rangeSeekBar_age_typeAct);
        rangeSeekBar_height = (RangeSeekBar) view.findViewById(R.id.rangeSeekBar_height_typeAct);
        rangeSeekBar_weight = (RangeSeekBar) view.findViewById(R.id.rangeSeekBar_weight_typeAct);
        btn_oneNight = (Button) view.findViewById(R.id.btn_oneNight_typeAct);
        btn_longTerm = (Button) view.findViewById(R.id.btn_longTerm_typeAct);
        btn_Settlement = (Button) view.findViewById(R.id.btn_settlement_typeAct);
        tv_age = (TextView) view.findViewById(R.id.tv_value_age);
        tv_height = (TextView) view.findViewById(R.id.tv_value_height);
        tv_weight = (TextView) view.findViewById(R.id.tv_value_weight);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_typeAct);
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_typeAct);

        lookingFor = new LookingFor();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void doFunctionInAct() {
        rangeSeekBarAgeFunction();
        rangeSeekBarHeightFunction();
        rangeSeekBarWeightFunction();
        lookingForFunction();

        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackAct();
            }
        });

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubmitAct();
            }
        });
    }

    private void rangeSeekBarAgeFunction() {
        rangeSeekBar_age.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                lookingFor.setMin_age(18 + rangeSeekBar.getProgressStart());
                lookingFor.setMax_age(18 + rangeSeekBar.getProgressEnd());
                String strValue = lookingFor.getMin_age() + "-" + lookingFor.getMax_age();
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
                lookingFor.setMin_height(100 + rangeSeekBar.getProgressStart());
                lookingFor.setMax_height(100 + rangeSeekBar.getProgressEnd());
                String strValue = lookingFor.getMin_height() + "-" + lookingFor.getMax_height() + "cm";
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
                lookingFor.setMin_weight(40 + rangeSeekBar.getProgressStart());
                lookingFor.setMax_weight(40 + rangeSeekBar.getProgressEnd());
                String strValue = lookingFor.getMin_weight() + "-" + lookingFor.getMax_weight() + "kg";
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
        PushDownAnim.setPushDownAnimTo(btn_oneNight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookingFor.setLooking(0);
                btn_oneNight.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.img_one_night_select));
                btn_longTerm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_long_term));
                btn_Settlement.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_settlement));
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_longTerm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookingFor.setLooking(1);
                btn_longTerm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_long_term_select));
                btn_oneNight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_one_night));
                btn_Settlement.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_settlement));
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_Settlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookingFor.setLooking(2);
                btn_Settlement.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_settlement_select));
                btn_oneNight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_one_night));
                btn_longTerm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_long_term));
            }
        });
    }

    private void callBackAct() {
        NavHostFragment.findNavController(TypeFragment.this)
                .navigate(R.id.action_typeFragment_to_bioPhotosFragment);
    }

    private void callSubmitAct() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getCurrentUser().getUid());
        db.child("lookingFor").setValue(lookingFor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    NavHostFragment.findNavController(TypeFragment.this)
                            .navigate(R.id.action_typeFragment_to_interestsFragment);
                } else {
                    Snackbar.make(view, task.getException().getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });
    }
}