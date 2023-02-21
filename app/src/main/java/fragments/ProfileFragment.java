package fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trackit.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import adapters.FavoriteAdapter;
import callbacks.OnMediaUnFavorite;
import enums.FavoriteLastMediaClickedOn;
import model.Media;
import model.User;
import model.UserMediaTracker;
import utils.FirebaseDataManagement;

public class ProfileFragment extends Fragment {
    private User user;

    private FavoriteAdapter favoriteAdapter;

    private View splashScreen;

    private MaterialTextView profile_LBL_username;
    private ShapeableImageView profile_IMG_profilePic;
    private MaterialTextView profile_LBL_tvCount;
    private MaterialTextView profile_LBL_tvShow;
    private MaterialTextView profile_LBL_movieCount;
    private MaterialTextView profile_LBL_movie;
    private MaterialButton profile_BTN_all;
    private MaterialButton profile_BTN_TVShows;
    private MaterialButton profile_BTN_movies;
    private RecyclerView profile_LST_favorites;
    private FavoriteLastMediaClickedOn lastMediaClickedOn = FavoriteLastMediaClickedOn.all; //all pick as default
    private OnMediaUnFavorite onMediaUnFavorite = new OnMediaUnFavorite() {
        @Override
        public void updateList(Media media) {
            user.changeMediaUnFavorite(media.getId());//update favorite locally

            favoriteAdapter = new FavoriteAdapter(getUserFavoriteListByLastClicked(), onMediaUnFavorite);
            profile_LST_favorites.setAdapter(favoriteAdapter);
            profile_LST_favorites.getAdapter().notifyDataSetChanged();
            FirebaseDataManagement.getInstance().addMedia(new UserMediaTracker(media, false), user);//will override
        }
    };

    private ArrayList<UserMediaTracker> getUserFavoriteListByLastClicked() {
        if (lastMediaClickedOn == FavoriteLastMediaClickedOn.all) {
            return user.getFavoriteAllMediaList();
        }
        if (lastMediaClickedOn == FavoriteLastMediaClickedOn.movie) {
            return user.getFavoriteMovieMediaList();
        }
        return user.getFavoriteTVMediaList();
    }


    private void showSplashScreen() {
        splashScreen.setVisibility(View.VISIBLE);
    }

    private void hideSplashScreen() {
        splashScreen.setVisibility(View.GONE);
    }


    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);

        //splashScreen = view.getRootView().findViewById(R.layout.splash_screen);

        //showSplashScreen();

        return view;

    }

    private void setAdapter() {
        favoriteAdapter = new FavoriteAdapter(user.getFavoriteAllMediaList(), onMediaUnFavorite);
        profile_LST_favorites.setAdapter(favoriteAdapter);
    }

    public void initViews(View view) {
        profile_LBL_username = view.findViewById(R.id.profile_LBL_username);
        profile_IMG_profilePic = view.findViewById(R.id.profile_IMG_profilePic);
        profile_LBL_tvCount = view.findViewById(R.id.profile_LBL_tvCount);
        profile_LBL_tvShow = view.findViewById(R.id.profile_LBL_tvShow);
        profile_LBL_movieCount = view.findViewById(R.id.profile_LBL_movieCount);
        profile_LBL_movie = view.findViewById(R.id.profile_LBL_movie);
        profile_BTN_all = view.findViewById(R.id.profile_BTN_all);
        profile_BTN_TVShows = view.findViewById(R.id.profile_BTN_TVShows);
        profile_BTN_movies = view.findViewById(R.id.profile_BTN_movies);
        profile_LST_favorites = view.findViewById(R.id.profile_LST_favorites);

        profile_LST_favorites.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void setUser(User user) {
        this.user = user;
        setAdapter();
        setProfile();
        //hideSplashScreen();
    }

    private void setProfile() {
        profile_LBL_username.setText(user.getUsername());

        Glide
                .with(profile_IMG_profilePic)
                .load(user.getUserIMGURL())
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(profile_IMG_profilePic);

        setWatchedLabel();
        profile_BTN_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = FavoriteLastMediaClickedOn.all;
                showAllFavoriteMediaList();
            }
        });
        profile_BTN_TVShows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = FavoriteLastMediaClickedOn.TVShow;
                showTVMediaFavorite();
            }
        });
        profile_BTN_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastMediaClickedOn = FavoriteLastMediaClickedOn.movie;
                showMovieMediaFavorite();
            }
        });
    }

    private void showMovieMediaFavorite() {
        profile_LST_favorites.setAdapter(new FavoriteAdapter(user.getFavoriteMovieMediaList(), onMediaUnFavorite));
        profile_LST_favorites.getAdapter().notifyDataSetChanged();
    }

    private void showTVMediaFavorite() {
        profile_LST_favorites.setAdapter(new FavoriteAdapter(user.getFavoriteTVMediaList(), onMediaUnFavorite));
        profile_LST_favorites.getAdapter().notifyDataSetChanged();
    }

    private void showAllFavoriteMediaList() {
        profile_LST_favorites.setAdapter(new FavoriteAdapter(user.getFavoriteAllMediaList(), onMediaUnFavorite));
        profile_LST_favorites.getAdapter().notifyDataSetChanged();
    }

    private void setWatchedLabel() {
        setLabelDefaultVisibility();
        String movie = "movie";//more than 1 movie -> movie = movies
        String TVShow = "TV show";
        int numberOfTVShows = user.getNumberOfTVShows();
        int numberOfMovies = user.getNumberOfMovies();
        if (numberOfTVShows == 0 && numberOfMovies == 0) {
            profile_LBL_tvCount.setVisibility(View.VISIBLE);
            profile_LBL_tvCount.setText("you have an empty list!");
        } else if (numberOfMovies >= 1) {
            //for correct grammar
            profile_LBL_movieCount.setVisibility(View.VISIBLE);
            profile_LBL_movie.setVisibility(View.VISIBLE);
            profile_LBL_movieCount.setText("" + numberOfMovies);
            if (numberOfMovies > 1) {
                movie += "s";
            }
            profile_LBL_movie.setText(movie);
        }
        if (numberOfTVShows >= 1) {
            profile_LBL_tvCount.setVisibility(View.VISIBLE);
            profile_LBL_tvShow.setVisibility(View.VISIBLE);
            profile_LBL_tvCount.setText("" + numberOfTVShows);
            if (numberOfTVShows > 1) {
                TVShow += "s";
            }
            profile_LBL_tvShow.setText(TVShow);
        }
    }

    private void setLabelDefaultVisibility() {
        //gone as default
        profile_LBL_tvCount.setVisibility(View.GONE);
        profile_LBL_tvShow.setVisibility(View.GONE);
        profile_LBL_movieCount.setVisibility(View.GONE);
        profile_LBL_movie.setVisibility(View.GONE);
    }
}
