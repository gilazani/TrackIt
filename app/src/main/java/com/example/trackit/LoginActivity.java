package com.example.trackit;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;
import java.util.List;

import model.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        connectUser();


//        user.updateProfile(new UserProfileChangeRequest.Builder()
//                .setDisplayName("Tom Cohen")
//                .build());

//        String uid = user.getUid();
//        String phone = user.getPhoneNumber();
//        String name = user.getDisplayName();
//        String email = user.getEmail();
//        int x= 0;
    }

    private void goToMain() {
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.putExtra(MainActivity.KEY_USER,user);
        startActivity(startIntent);
        finish();
    }


    private void connectUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            login();
        }else{
            loadUserData(user);
            goToMain();
        }
    }

    private void loadUserData(FirebaseUser user) {
        this.user = new User(user.getDisplayName(), user.getUid(), user.getPhotoUrl().toString());
    }


    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        loadUserData(mAuth.getCurrentUser());
        goToMain();
    }


    private void login() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
//                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
               // .setLogo(R.mipmap.ic_launcher_round)
                .build();
        signInLauncher.launch(signInIntent);
    }
}