package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.adapter.ImageRegisterAdapter;
import com.example.quickdate.listener.ImagesListener;
import com.example.quickdate.model.Info;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BioPhotosFragment extends Fragment implements ImagesListener {

    private ConstraintLayout contraintLayout_snakebar;
    private final HashMap<String, String> imagesURI = new HashMap<>();

    private RecyclerView recyclerView;
    private String localFileUri;
    private ImageView iv_submit, iv_backAct;
    private ImageButton btn_uploadImage;
    private EditText et_nickName, et_aboutMe;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private ArrayList<String> index;
    private User user;
    private View root;
    private UploadTask uploadTask;
    private it.sephiroth.android.library.numberpicker.NumberPicker numberPicker_age, numberPicker_height, numberPicker_weight;

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
        return inflater.inflate(R.layout.fragment_bio_photos, container, false);
    }

    private void initialization(View view) {
        root = view;
        btn_uploadImage = (ImageButton) view.findViewById(R.id.btn_uploadImage);
        et_nickName = (EditText) view.findViewById(R.id.et_nickName);
        et_aboutMe = (EditText) view.findViewById(R.id.et_aboutMe);
        numberPicker_age = view.findViewById(R.id.numberPicker_age);
        numberPicker_height = view.findViewById(R.id.numberPicker_height);
        numberPicker_weight = view.findViewById(R.id.numberPicker_weight);
        contraintLayout_snakebar = (ConstraintLayout) view.findViewById(R.id.contraintLayout_snakebar);
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_bioPhotosAct);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_bioPhotosAct);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());

        user = (User) getArguments().getSerializable("User");
    }

    private void doFunctionInAct() {
        uploadImageFunction();
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackAct();
            }
        });
        callSubmitAct();
    }

    private void uploadImageFunction() {
        PushDownAnim.setPushDownAnimTo(btn_uploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void callBackAct() {
        if (NavHostFragment.findNavController(BioPhotosFragment.this).getCurrentDestination().getId() == R.id.bioPhotosFragment) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("User", user);
            NavHostFragment.findNavController(BioPhotosFragment.this)
                    .navigate(R.id.action_bioPhotosFragment_to_selectGenderFragment, bundle);
        }
    }

    private void callSubmitAct() {
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDataInput(et_nickName.getText().toString(), et_aboutMe.getText().toString())) {
                    String guestImage;
                    index = new ArrayList<>(imagesURI.keySet());
                    if (imagesURI == null) {
                        guestImage = user.getInfo().getImgAvt();
                    } else {
                        guestImage = imagesURI.get(index.get(0));
                    }
                    user.getInfo().setImages(imagesURI);
                    user.getInfo().setImgAvt(guestImage);
                    user.getInfo().setNickname(et_nickName.getText().toString());
                    user.getInfo().setAboutMe(et_aboutMe.getText().toString());
                    user.getInfo().setAge(numberPicker_age.getProgress());
                    user.getInfo().setHeight(numberPicker_height.getProgress());
                    user.getInfo().setWeight(numberPicker_weight.getProgress());

                    if (NavHostFragment.findNavController(BioPhotosFragment.this).getCurrentDestination().getId() == R.id.bioPhotosFragment) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("User", user);
                        NavHostFragment.findNavController(BioPhotosFragment.this)
                                .navigate(R.id.action_bioPhotosFragment_to_typeFragment, bundle);
                    }
                }
            }
        });
    }

    private void saveDataToRealTimeDatabaseFunc() {
        StorageReference ref = firebaseStorage.getReference("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + localFileUri);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (imagesURI.get(ref.getName()) == null) {
                            imagesURI.put(ref.getName(), uri.toString());
                            addDataToRecyclerViewFunc();
                        } else {
                            Snackbar.make(contraintLayout_snakebar, "This Image has already Upload", BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    private void addDataToRecyclerViewFunc() {
        ImageRegisterAdapter imageRegisterAdapter = new ImageRegisterAdapter(imagesURI, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        recyclerView.setAdapter(imageRegisterAdapter);
        recyclerView.setLayoutManager(linearLayout);
    }

    private boolean checkDataInput(String nickName, String aboutMe) {
        {
            if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(aboutMe)) {
                Snackbar.make(contraintLayout_snakebar, "All fields should not empty", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (nickName.length() < 5) {
                Snackbar.make(contraintLayout_snakebar, "Field Nickname at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (aboutMe.length() <= 5) {
                Snackbar.make(contraintLayout_snakebar, "Field about me at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (imagesURI.size() < 3 || imagesURI.size() > 5) {
                Snackbar.make(contraintLayout_snakebar, "You only Upload between 3 to 5 Images", BaseTransientBottomBar.LENGTH_LONG).show();
            } else {
                return true;
            }
            return false;
        }
    }

    @Override
    public void onImageClicked(String imageKey) {
        imagesURI.remove(imageKey);
        addDataToRecyclerViewFunc();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            localFileUri = data.getData().getLastPathSegment();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

            uploadTask = storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + data.getData().getLastPathSegment()).putFile(data.getData(), metadata);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    long progress = (long)(100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    Snackbar.make(contraintLayout_snakebar, "Upload " + progress + "%", 5000).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadTask.cancel();
                        }
                    }).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(contraintLayout_snakebar, "Failed to upload", BaseTransientBottomBar.LENGTH_LONG).show();
                    Snackbar.make(contraintLayout_snakebar, "Failed", 2000).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    saveDataToRealTimeDatabaseFunc();
                    Snackbar.make(contraintLayout_snakebar, "Successfully", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    Snackbar.make(contraintLayout_snakebar, "Paused", 2000).show();
                }
            });
        }
    }
}