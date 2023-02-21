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
import callbacks.OnMediaUnFavorite;
import callbacks.OnRemovedMedia;
import enums.LastMediaClickedOn;
import model.Media;
import model.User;
import model.UserMediaTracker;
import utils.FirebaseDataManagement;

public class MovieFragment extends Fragment {
    private User user;

    private MovieAdapter movieAdapter;

    private LastMediaClickedOn lastMediaClickedOn = LastMediaClickedOn.watched;// watched as default

    private MaterialButton movie_BTN_watched;
    private MaterialButton movie_BTN_favorite;
    private RecyclerView movie_LST_movieList;

    private boolean updateLater = false;//if data arrived before page loaded

    private OnMediaUnFavorite onMediaUnFavorite = new OnMediaUnFavorite() {
        @Override
        public void updateList(Media media) {
            user.changeMediaToOppositeFavorite(media.getId());//update favorite locally

            movieAdapter = new MovieAdapter(getUserListByLastClicked(), onMediaUnFavorite, onRemovedMedia);
            movie_LST_movieList.setAdapter(movieAdapter);
            movie_LST_movieList.getAdapter().notifyDataSetChanged();
            FirebaseDataManagement.getInstance().addMedia(new UserMediaTracker(media,user.getMediaIDFavorite(media.getId())),user);//will override
        }
    } ;

    private OnRemovedMedia onRemovedMedia = new OnRemovedMedia() {
        @Override
        public void removeMedia(Media media) {
            user.removeMediaByID(media.getId());//remove media locally

            movieAdapter = new MovieAdapter(getUserListByLastClicked(), onMediaUnFavorite, onRemovedMedia);
            movie_LST_movieList.setAdapter(movieAdapter);
            movie_LST_movieList.getAdapter().notifyDataSetChanged();
            FirebaseDataManagement.getInstance().deleteMedia(media.getId(),user);
        }
    };

    private ArrayList<UserMediaTracker> getUserListByLastClicked() {
        if (lastMediaClickedOn == LastMediaClickedOn.favorite) {
            return user.getFavoriteMovieMediaList();
        }
            return user.getMovieList();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie,container,false);
        initViews(view);

        if(updateLater){
            setAdapter();
            setViews();
        }

        return view;

    }

    public void initViews(View view){
        movie_BTN_watched = view.findViewById(R.id.movie_BTN_watched);
        movie_BTN_favorite = view.findViewById(R.id.movie_BTN_favorite);
        movie_LST_movieList = view.findViewById(R.id.movie_LST_movieList);

        movie_LST_movieList.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void setUser(User user) {
        this.user = user;
        setAdapter();// will change update later if not fit
        if(!updateLater) {
            setViews();
        }
    }

    private void setViews() {
        movie_BTN_watched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = LastMediaClickedOn.watched;
                showAllMovieList();
            }
        });

        movie_BTN_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = LastMediaClickedOn.favorite;
                showAllFavoriteMovieList();
            }
        });
    }

    private void showAllMovieList() {
        movie_LST_movieList.setAdapter(new MovieAdapter(user.getMovieList(), onMediaUnFavorite, onRemovedMedia));
        movie_LST_movieList.getAdapter().notifyDataSetChanged();
    }

    private void showAllFavoriteMovieList() {
        movie_LST_movieList.setAdapter(new MovieAdapter(user.getFavoriteMovieMediaList(), onMediaUnFavorite, onRemovedMedia));
        movie_LST_movieList.getAdapter().notifyDataSetChanged();
    }

    private void setAdapter() {
        if (movie_LST_movieList == null) {
            updateLater = true;
        } else {
            movieAdapter = new MovieAdapter(user.getMovieList(), onMediaUnFavorite, onRemovedMedia);
            movie_LST_movieList.setAdapter(movieAdapter);
            updateLater = false;
        }
    }
}
