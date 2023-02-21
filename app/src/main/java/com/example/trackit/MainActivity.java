package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import callbacks.MediaListChanged;
import callbacks.NotifySearchResultLocked;
import callbacks.OnAddToListPressed;
import callbacks.OnSearchResultReturn;
import enums.MediaType;
import fragments.MovieFragment;
import fragments.ProfileFragment;
import fragments.SearchFragment;
import fragments.TVShowFragment;
import model.Image;
import model.Media;
import model.User;
import model.UserMediaTracker;
import utils.FirebaseDataManagement;
import utils.MediaDataCollector;

public class MainActivity extends AppCompatActivity{

    public static final String KEY_USER = "KEY_USER";
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;
    private TVShowFragment tvShowFragment;
    private MovieFragment movieFragment;

    private BottomNavigationView main_BNV_toolbar;
    private ImageView main_BTN_logout;

    private User user;

    private OnSearchResultReturn onSearchResultReturn = new OnSearchResultReturn() {
        @Override
        public void updateSearchResult(Media[] media) {
            searchFragment.updateAdapter(media);
        }
    };

    private NotifySearchResultLocked notifySearchResultLocked = new NotifySearchResultLocked() {
        @Override
        public void updateSearchAdapter() {
            searchFragment.updateFlag();
        }
    };

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();

        setAndActivateLogout();
        
        setFragments();

        setUser();
        
        setBNavigationBar();

        setCallback();

        setMediaTracker();
        
    }


    private void setAndActivateLogout() {
        main_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    private void setMediaTracker(){
            MediaListChanged mediaListChanged = new MediaListChanged() {
                @Override
                public void setMediaList(ArrayList<UserMediaTracker> mediaList) {
                    user.setMediaListFromDB(mediaList);
                }

                @Override
                public void notifyMediaListChange() {
                    profileFragment.setUser(user);
                    searchFragment.setUser(user);
                    tvShowFragment.setUser(user);
                    movieFragment.setUser(user);

                }
            };
            FirebaseDataManagement.getInstance().getMediaList(user, mediaListChanged);
        }


    private void setUser() {
        Intent previousIntent = getIntent();
        user = (User) previousIntent.getSerializableExtra(KEY_USER);

//        searchFragment.setUser(user);
//        profileFragment.setUser(user);
//        tvShowFragment.setUser(user);
//        movieFragment.setUser(user);
    }

    private void setCallback() {
        MediaDataCollector.getInstance().setCallback(onSearchResultReturn, notifySearchResultLocked);
        //searchFragment.setCallbacks(onAddToListPressed);
    }

    private void setBNavigationBar() {
        main_BNV_toolbar.setOnItemSelectedListener(item -> {
            return onNavigationItemSelected(item);
        });
        main_BNV_toolbar.setSelectedItemId(R.id.menu_ITM_profile);
    }

    private void setFragments() {
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        tvShowFragment = new TVShowFragment();
        movieFragment = new MovieFragment();
    }

    private void initViews() {

        main_BNV_toolbar = findViewById(R.id.main_BNV_toolbar);
        main_BTN_logout = findViewById(R.id.main_BTN_logout);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.menu_ITM_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_FRAME_page, profileFragment).commit();
            setMediaTracker();
            return true;
        }
        if (id == R.id.menu_ITM_search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_FRAME_page, searchFragment).commit();
            setMediaTracker();
            return true;
        }

        if (id == R.id.menu_ITM_TVShow) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_FRAME_page, tvShowFragment).commit();
            setMediaTracker();
            return true;
        }
        if (id == R.id.menu_ITM_movie) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_FRAME_page, movieFragment).commit();
            setMediaTracker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}