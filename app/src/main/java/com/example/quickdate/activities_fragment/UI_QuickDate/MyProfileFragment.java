package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.BioPhotosActivity;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.InterestsActivity;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.adapter.SliderAdapter;
import com.example.quickdate.listener.UserListener;
import com.example.quickdate.model.Interest;
import com.example.quickdate.model.User;
import com.example.quickdate.utility.UploadImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import okhttp3.internal.Internal;

import static android.app.Activity.RESULT_OK;


public class MyProfileFragment extends Fragment {

    // Components in View
    private ImageSlider imageSlider;
    private CircularImageView avatarIv;
    private TextView tv_info, tv_info2, tv_info3, tv_info4;
    private Button btn_edit;
    private RecyclerView recyclerView;
    private View root;
    private User myUser;
    private ProgressDialog pd;

    // Slider images
    private SliderView sliderView;
    private SliderAdapter adapter;

    // Firebase Database Ref
    DatabaseReference databaseReference;

    // Storage
    StorageReference storageReference;
    // Path where images of user avatar will be stored
    String storagePath = "images";

    // Uri of Picked Image
    private Uri image_Uri;

    // Array needs in View
    private ArrayList<Interest> interests;

    // Permissions Contants
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_REQUEST_CODE = 200;
    public static final int IMAGE_PICK_GALLERY_CODE = 300;
    public static final int IMAGE_PICK_CAMERA_CODE = 400;

    // Array òf permissions to be requested
    private String[] cameraPermissions;
    private String[] storagePermissions;

    public MyProfileFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        findUserInfo();
    }

    private void init() {

        // Init View
        avatarIv = (CircularImageView) root.findViewById(R.id.avatarIv_myProfile);
        tv_info = root.findViewById(R.id.tv_info_myProfile);
        tv_info2 = root.findViewById(R.id.tv_info2_myProfile);
        tv_info3 = root.findViewById(R.id.tv_info3_myProfile);
        tv_info4 = root.findViewById(R.id.tv_info4_myProfile);
        btn_edit = root.findViewById(R.id.edit_myProfile);
        recyclerView = root.findViewById(R.id.recyclerView_myProfile);
        sliderView = root.findViewById(R.id.imageSlider_Profile);

        // Init Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Init Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Init array interests
        interests = new ArrayList<Interest>();

        // Init ProgressDialog
        pd = new ProgressDialog(getActivity());


    }

    private void findUserInfo() {
        String[] genders = new String[]{"Male", "Female"};
        String[] lookingFor = new String[]{"OneNight", "LongTerm", "Settlement"};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                FirebaseDatabase.getInstance()
                        .getReference("Users/" + genders[i] + "/" + lookingFor[j] + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    myUser = snapshot.getValue(User.class);
                                    doFunction();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }
        }
    }

    private void doFunction() {
        showSliderImage();
        showInfo();
        showInterestRecyclerview();

        PushDownAnim.setPushDownAnimTo(btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
    }

    private void showEditProfileDialog() {
        String options[] = {"Edit Avatar", "Edit Cover And Info Profile", "Edit Interest"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Choose Action");
        // set item
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                switch (which) {
                    case 0:
                        // Edit Avatar
                        pd.setMessage("Updating Avatar Image");
                        showImagePicDialog();
                        break;
                    case 1:
                        // Edit Cover Images And Info Profile
                        pd.setMessage("Updating Cover Images And Info Profile");
                       // Call BioPhotosFragment
                        editProfile();
                        break;
                    case 2:
                        // Edit Interests
                        pd.setMessage("Updating Interests");
                        // Call InterestsFragment to load Users Interests Database
                        editInterest();
                        break;
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void editProfile() {
        Intent intent = new Intent(getActivity(), BioPhotosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", myUser);
        intent.putExtra("isRegisterInfo", true);
        startActivity(intent);
        getActivity().finish();
    }

    private void editInterest() {
        Intent intent = new Intent(getActivity(), InterestsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", myUser);
        intent.putExtra("isRegisterInfo", true);
        startActivity(intent);
        getActivity().finish();
    }

    private void showImagePicDialog() {
        // Show dialog containing options Camera and Gallary to pick the image

        String options[] = {"Camera", "Gallary"};

        // Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set title
        builder.setTitle("Pick Image From");
        // Set item
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle dialog item clicks
                if (which == 0) {
                    // Camera click
                    if (checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    // Gallary click
                    if (checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showSliderImage() {
        // Init SliderAdapter
        adapter = new SliderAdapter(myUser.getInfo().getImages() ,getActivity());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void showInfo() {
        //show avatar image
        try {
            // if image is received then set
            Picasso.get().load(myUser.getInfo().getImgAvt()).placeholder(R.drawable.img_doneemoji).into(avatarIv);
        } catch (Exception e) {
            // if there is any exception while getting image then set default
            avatarIv.setImageResource(R.drawable.img_doneemoji);
        }

        String[] arr = myUser.getInfo().getNickname().split(" ");
        if (arr[arr.length - 1].length() > 8) {
            tv_info.setText(arr[arr.length - 1].substring(0, 8) + "...,");
        } else {
            tv_info.setText(arr[arr.length - 1] + ",");
        }
        tv_info2.setText(myUser.getInfo().getAge() + "");
        tv_info3.setText(myUser.getInfo().getProvincial() + ", " + myUser.getInfo().getHeight() + "cm - " + myUser.getInfo().getWeight() + "kg");
        tv_info4.setText(myUser.getInfo().getAboutMe());
    }

    private void showInterestRecyclerview() {
        for (Interest item : myUser.getInterests()) {
            if (item.getStatus()) {
                interests.add(item);
            }
        }
        InterestsAdapter interestsAdapter = new InterestsAdapter(interests, myUser.getStatus() == 1);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                // Picking from camera, first check if camera and storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccept && writeStorageAccepted) {
                        // Permissions allowed
                        pickFromCamera();
                    } else {
                        // Permissions denied
                        Toast.makeText(getActivity(), "Please enable camera & storage permission", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                // Picking from gallery, first check if storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        // Permissions allowed
                        pickFromGallery();
                    } else {
                        // Permissions denied
                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // This method will be called after picking image from camera or gallery
        if (resultCode == RESULT_OK) {
            assert data != null : "Data null";
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // Image is picked from gallery, get uri of image
                image_Uri = data.getData();
                uploadAvatarImage(image_Uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // Image is picked from camera, get uri of image
                uploadAvatarImage(image_Uri);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadAvatarImage(Uri uri) {
        // Show progressDialog
        pd.show();

        // Path and name of Image to be stored in firebase storage
        // e.g: images/avatar_187pm20569
        String filePathAndName = storagePath + "/" + "avatar" + "_" + myUser.getIdUser();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image is uploaded to storage, now get it's url and store in user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        // Check if image is uploaded or not and url is received
                        if (uriTask.isSuccessful()) {
                            // Image uploaded
                            // add/upload url in User's database
                            assert downloadUri != null;
                            databaseReference.child(myUser.getInfo().getGender())
                                    .child(myUser.getLookingFor().getLooking())
                                    .child(myUser.getIdUser())
                                    .child("info")
                                    .child("imgAvt").setValue(downloadUri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image Updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Error Updating Image...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There were some error(s), get and show error message, dismiss progress dialog
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkStoragePermission() {
        // Check if storage permission is enable or not
        // return true if enabled
        // return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return !result;
    }

    private void requestStoragePermission() {
        // Request runtime storage permission
        getActivity().requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        // Check if storage permission is enable or not
        // return true if enabled
        // return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return !result || !result1;
    }

    private void requestCameraPermission() {
        // Request runtime storage permission
        getActivity().requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        // Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        // Put image Uri
        image_Uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        // Pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }
}