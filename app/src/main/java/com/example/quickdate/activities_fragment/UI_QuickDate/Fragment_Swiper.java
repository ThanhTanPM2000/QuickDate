package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.adapter.CardStackAdapter;
import com.example.quickdate.adapter.UserDiffCallBack;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.HashMap;


public class Fragment_Swiper extends Fragment implements CardStackListener {


    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    private CardStackAdapter cardStackAdapter;
    private ImageButton btn_skip, btn_love, btn_rewind;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private User user;
    private ArrayList<User> myOppositeUsers;
    private View root;
    private FirebaseUser firebaseUser;
    private Boolean flag = true;

    // position of card swipe to liked
    private int positionCard = 0;

    String uidUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swiper, container, false);
        init(view);
        doFunction();
        return view;
    }

    private void init(View view) {
        Activity_Home act = (Activity_Home) getActivity();
        user = act.getCurrentUser();
        myOppositeUsers = act.getAllOppositeUsers();
        act.tv_head_title.setText("Quick Date");

        cardStackView = view.findViewById(R.id.card_stack_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        cardStackAdapter = new CardStackAdapter(getActivity() ,myOppositeUsers);
        cardStackLayoutManager = new CardStackLayoutManager(getActivity(), this);
        btn_skip = (ImageButton) view.findViewById(R.id.skip_button);
        btn_love = (ImageButton) view.findViewById(R.id.love_button);
        btn_rewind = (ImageButton) view.findViewById(R.id.rewind_button);
    }

    private void doFunction() {
        setupCardStackView();
        setupButton();
    }

    private void setupButton() {
        PushDownAnim.setPushDownAnimTo(btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                cardStackLayoutManager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_rewind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                cardStackLayoutManager.setRewindAnimationSetting(setting);
                cardStackView.rewind();
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_love).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                cardStackLayoutManager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });
    }

    private void setupCardStackView() {
        cardStackLayoutManager.setStackFrom(StackFrom.None);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setTranslationInterval(8.0f);
        cardStackLayoutManager.setScaleInterval(0.95f);
        cardStackLayoutManager.setSwipeThreshold(0.3f);
        cardStackLayoutManager.setMaxDegree(20.0f);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollHorizontal(true);
        cardStackLayoutManager.setCanScrollVertical(true);
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        cardStackLayoutManager.setOverlayInterpolator(new LinearInterpolator());

        cardStackView.setLayoutManager(cardStackLayoutManager);
        cardStackView.setAdapter(cardStackAdapter);

        RecyclerView.ItemAnimator itemAnimator = cardStackView.getItemAnimator();
        if (itemAnimator instanceof DefaultItemAnimator) {
            DefaultItemAnimator di = (DefaultItemAnimator) itemAnimator;
            di.setSupportsChangeAnimations(false);
        }
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (cardStackLayoutManager.getTopPosition() == cardStackAdapter.getItemCount() - 5) {
            paginate();
        }
        if (direction == Direction.Right) {
            User test = myOppositeUsers.get(cardStackLayoutManager.getTopPosition() -1);
            addToHisNotifications( cardStackLayoutManager.getTopPosition() -1,
                    test.getInfo().getGender(),
                    test.getLookingFor().getLooking(),
                    test.getIdUser(),
                    "Liked",
                    "Want to match with you");
        }else if(direction == Direction.Left){
            
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {
    }

    private void paginate() {
        ArrayList<User> old = cardStackAdapter.getUsers();
        ArrayList<User> newer = old;
        newer.addAll(myOppositeUsers);
        UserDiffCallBack callback = new UserDiffCallBack(old, newer);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        cardStackAdapter.setUsers(newer);
        result.dispatchUpdatesTo(cardStackAdapter);
    }

    private void addToHisNotifications(int position ,String gender, String looking, String hisId, String type, String message) {
        String timeStamp = "" + System.currentTimeMillis();

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("senderId", user.getIdUser());
        hashMap.put("receiverId", myOppositeUsers.get(positionCard).getIdUser());
        hashMap.put("type", "Liked");
        hashMap.put("notification", "Want to match with you");
        hashMap.put("timeStamp", timeStamp);

        FirebaseDatabase.getInstance().getReference("Notifications")
                .push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your liked was send", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}