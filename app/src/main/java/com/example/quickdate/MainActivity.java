package com.example.quickdate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.Models.Users;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference reference;
    private static final String TAG = "FacebookAuthentication";
    private Button btnLogin, btnFacebook, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallbackManager = CallbackManager.Factory.create();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btn_login);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnSignup = findViewById(R.id.btn_signup);

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "onSuccess" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Cancle");
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "Cancle");
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    firebaseAuth.signOut();
                }
            }
        };
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            /*FirebaseUser user = firebaseAuth.getCurrentUser();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", user.getUid());
                            hashMap.put("username", user.getDisplayName());
                            hashMap.put("imageUrl", user.getPhotoUrl().toString());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent a = new Intent(getBaseContext(), Profile_User.class);
                                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(a);
                                        finish();
                                    }
                                }
                            });*/
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            /*final DocumentReference doc = db.collection("cities").document("test");

                            db.runTransaction(new Transaction.Function<Double>()
                            {
                                @Nullable
                                @Override
                                public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(doc);

                                    double newvalue = snapshot.getDouble(("listExample"))+2;
                                    if(newvalue>=100){
                                        transaction.update(doc, "listExample", newvalue);
                                        return newvalue;
                                    }
                                    else
                                    {
                                        throw new FirebaseFirestoreException("listExample is too low than 100", FirebaseFirestoreException.Code.ABORTED);
                                    }
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Double>() {
                                @Override
                                public void onSuccess(Double aDouble) {

                                }
                            });*/
                            /*WriteBatch wb = db.batch();

                            DocumentReference doc = db.collection("cities").document("Ho Chi Minh");
                            wb.set(doc, new Users());

                            DocumentReference doc2 = db.collection("cities").document("test");
                            wb.delete(doc2);

                            wb.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });*/
                            Map<String, Object> map = new HashMap<>();
                            map.put("banh", FieldValue.delete());
                            db.collection("cities").document("Ho Chi Minh").update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStart() {
        super.onStart();
        /*firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        if(firebaseUser != null){
            Intent intent = new Intent(getBaseContext(), Profile_User.class);
            startActivity(intent);
            finish();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}