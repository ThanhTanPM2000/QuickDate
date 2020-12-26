package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.res.Resources;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickdate.R;
import com.example.quickdate.model.LookingFor;
import com.example.quickdate.model.User;
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
    private ImageButton btn_longTerm, btn_oneNight, btn_Settlement;
    private TextView tv_longTerm, tv_oneNight, tv_Settlement;
    private TextView tv_age, tv_height, tv_weight;
    private ImageView iv_backAct, iv_submit;
    private FirebaseAuth firebaseAuth;
    private View view;
    private User user;

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
        btn_oneNight = (ImageButton) view.findViewById(R.id.btn_oneNight_typeAct);
        btn_longTerm = (ImageButton) view.findViewById(R.id.btn_longTerm_typeAct);
        btn_Settlement = (ImageButton) view.findViewById(R.id.btn_settlement_typeAct);
        tv_age = (TextView) view.findViewById(R.id.tv_value_age);
        tv_height = (TextView) view.findViewById(R.id.tv_value_height);
        tv_weight = (TextView) view.findViewById(R.id.tv_value_weight);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_typeAct);
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_typeAct);
        tv_longTerm = (TextView) view.findViewById(R.id.tv_long_term);
        tv_oneNight = (TextView) view.findViewById(R.id.tv_one_night);
        tv_Settlement = (TextView) view.findViewById(R.id.tv_settlement);
        firebaseAuth = FirebaseAuth.getInstance();

        user = (User) getArguments().getSerializable("User");
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
        PushDownAnim.setPushDownAnimTo(btn_oneNight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getLookingFor().setLooking("OneNight");
                btn_oneNight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_rectangle_white));
                btn_oneNight.setColorFilter(getContext().getResources().getColor(R.color.red, null));
                tv_oneNight.setTextColor(getResources().getColor(R.color.black, null));

                btn_longTerm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_retangle_clarity));
                btn_longTerm.setColorFilter(getContext().getResources().getColor(R.color.white, null));
                tv_longTerm.setTextColor(getResources().getColor(R.color.white, null));

                btn_Settlement.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_retangle_clarity));
                btn_Settlement.setColorFilter(getContext().getResources().getColor(R.color.white, null));
                tv_Settlement.setTextColor(getResources().getColor(R.color.white, null));
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_longTerm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getLookingFor().setLooking("LongTerm");
                btn_longTerm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_rectangle_white));
                btn_longTerm.setColorFilter(getContext().getResources().getColor(R.color.red, null));
                tv_longTerm.setTextColor(getResources().getColor(R.color.black, null));

                btn_oneNight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_retangle_clarity));
                btn_oneNight.setColorFilter(getContext().getResources().getColor(R.color.white, null));
                tv_oneNight.setTextColor(getResources().getColor(R.color.white, null));

                btn_Settlement.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_retangle_clarity));
                btn_Settlement.setColorFilter(getContext().getResources().getColor(R.color.white, null));
                tv_Settlement.setTextColor(getResources().getColor(R.color.white, null));
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_Settlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getLookingFor().setLooking("Settlement");
                btn_Settlement.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_rectangle_white));
                btn_Settlement.setColorFilter(getContext().getResources().getColor(R.color.red, null));
                tv_Settlement.setTextColor(getResources().getColor(R.color.black, null));

                btn_longTerm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_retangle_clarity));
                btn_longTerm.setColorFilter(getContext().getResources().getColor(R.color.white, null));
                tv_longTerm.setTextColor(getResources().getColor(R.color.white, null));

                btn_oneNight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_retangle_clarity));
                btn_oneNight.setColorFilter(getContext().getResources().getColor(R.color.white, null));
                tv_oneNight.setTextColor(getResources().getColor(R.color.white, null));
            }
        });
    }

    private void callBackAct() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user);
        NavHostFragment.findNavController(TypeFragment.this)
                .navigate(R.id.action_typeFragment_to_bioPhotosFragment, bundle);
    }

    private void callSubmitAct() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user);
        NavHostFragment.findNavController(TypeFragment.this)
                .navigate(R.id.action_typeFragment_to_interestsFragment, bundle);
    }
}