package utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import callbacks.MediaListChanged;
import model.Image;
import model.User;
import model.UserMediaTracker;

public class FirebaseDataManagement {
    private static FirebaseDatabase database;

    private static FirebaseDataManagement firebaseDataManagement = null;


    private FirebaseDataManagement() {

    }

    public static void init() {
        if (firebaseDataManagement == null) {
            firebaseDataManagement = new FirebaseDataManagement();
        }
    }

    public static FirebaseDataManagement getInstance() {
        return firebaseDataManagement;
    }

    public void addMedia(UserMediaTracker mediaAndPref, User user) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference listRef = database.getReference("mediaList");
        listRef.child(user.getUserID()).child(mediaAndPref.getMedia().getId()).setValue(mediaAndPref);
    }

    public void deleteMedia(String mediaID, User user) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference listRef = database.getReference("mediaList");
        listRef.child(user.getUserID()).child(mediaID).removeValue();
    }

    public void getMediaList(User user, MediaListChanged mediaListChanged) {
        ArrayList<UserMediaTracker> userMediaTrackers = new ArrayList<UserMediaTracker>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference listRef = database.getReference("mediaList").child(user.getUserID());
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot media : snapshot.getChildren()) {
                    Image image = new Image(media.child("media").child("image").getValue(Image.class));
                    UserMediaTracker userMediaTracker = media.getValue(UserMediaTracker.class);
                    userMediaTracker.getMedia().setImage(image);
                    userMediaTrackers.add(userMediaTracker);
                }
                mediaListChanged.setMediaList(userMediaTrackers);
                mediaListChanged.notifyMediaListChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
