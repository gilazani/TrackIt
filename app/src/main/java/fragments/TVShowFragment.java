package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import adapters.MovieAdapter;
import adapters.TVShowAdapter;
import callbacks.OnMediaUnFavorite;
import callbacks.OnRemovedMedia;
import enums.LastMediaClickedOn;
import model.Media;
import model.User;
import model.UserMediaTracker;
import utils.FirebaseDataManagement;

public class TVShowFragment extends Fragment {
    private User user;

    private TVShowAdapter TVShowAdapter;

    private LastMediaClickedOn lastMediaClickedOn = LastMediaClickedOn.watched;// watched as default

    private MaterialButton TVShow_BTN_watched;
    private MaterialButton TVShow_BTN_favorite;
    private RecyclerView TVShow_LST_TVShowList;

    private boolean updateLater = false;//if data arrived before page loaded

    private OnMediaUnFavorite onMediaUnFavorite = new OnMediaUnFavorite() {
        @Override
        public void updateList(Media media) {
            user.changeMediaToOppositeFavorite(media.getId());//update favorite locally

            TVShowAdapter = new TVShowAdapter(getUserListByLastClicked(), onMediaUnFavorite, onRemovedMedia);
            TVShow_LST_TVShowList.setAdapter(TVShowAdapter);
            TVShow_LST_TVShowList.getAdapter().notifyDataSetChanged();
            FirebaseDataManagement.getInstance().addMedia(new UserMediaTracker(media,user.getMediaIDFavorite(media.getId())),user);//will override
        }
    } ;

    private OnRemovedMedia onRemovedMedia = new OnRemovedMedia() {
        @Override
        public void removeMedia(Media media) {
            user.removeMediaByID(media.getId());//remove media locally

            TVShowAdapter = new TVShowAdapter(getUserListByLastClicked(), onMediaUnFavorite, onRemovedMedia);
            TVShow_LST_TVShowList.setAdapter(TVShowAdapter);
            TVShow_LST_TVShowList.getAdapter().notifyDataSetChanged();
            FirebaseDataManagement.getInstance().deleteMedia(media.getId(),user);
        }
    };

    private ArrayList<UserMediaTracker> getUserListByLastClicked() {
        if (lastMediaClickedOn == LastMediaClickedOn.favorite) {
            return user.getFavoriteTVShowMediaList();
        }
        return user.getTVShowList();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show,container,false);
        initViews(view);

        if(updateLater){
            setAdapter();
            setViews();
        }

        return view;

    }

    public void initViews(View view){
        TVShow_BTN_watched = view.findViewById(R.id.TVShow_BTN_watched);
        TVShow_BTN_favorite = view.findViewById(R.id.TVShow_BTN_favorite);
        TVShow_LST_TVShowList = view.findViewById(R.id.TVShow_LST_TVShowList);

        TVShow_LST_TVShowList.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void setUser(User user) {
        this.user = user;
        setAdapter();// will change update later if not fit
        if(!updateLater) {
            setViews();
        }
    }

    private void setViews() {
        TVShow_BTN_watched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = LastMediaClickedOn.watched;
                showAllTVShowList();
            }
        });

        TVShow_BTN_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = LastMediaClickedOn.favorite;
                showAllFavoriteTVShowList();
            }
        });
    }

    private void showAllTVShowList() {
        TVShow_LST_TVShowList.setAdapter(new TVShowAdapter(user.getTVShowList(), onMediaUnFavorite, onRemovedMedia));
        TVShow_LST_TVShowList.getAdapter().notifyDataSetChanged();
    }

    private void showAllFavoriteTVShowList() {
        TVShow_LST_TVShowList.setAdapter(new MovieAdapter(user.getFavoriteTVShowMediaList(), onMediaUnFavorite, onRemovedMedia));
        TVShow_LST_TVShowList.getAdapter().notifyDataSetChanged();
    }

    private void setAdapter() {
        if (TVShow_LST_TVShowList == null) {
            updateLater = true;
        } else {
            TVShowAdapter = new TVShowAdapter(user.getTVShowList(), onMediaUnFavorite, onRemovedMedia);
            TVShow_LST_TVShowList.setAdapter(TVShowAdapter);
            updateLater = false;
        }
    }
}
