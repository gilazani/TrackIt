package model;

import java.io.Serializable;
import java.util.ArrayList;

import callbacks.OnSearchResultReturn;
import enums.MediaType;
import utils.FirebaseDataManagement;

public class User implements Serializable {
    private String username;
    private String userID;
    private String userIMGURL;
    private int numberOfTVShows;
    private int numberOfMovies;
    private ArrayList<UserMediaTracker> mediaList;

    public User(String username, String userID, String userIMGURL) {
        //for a new user
        this.username = username;
        this.userID = userID;
        this.userIMGURL = userIMGURL;
        mediaList = new ArrayList<>();
        numberOfMovies = 0;
        numberOfTVShows = 0;
    }

    public boolean checkMediaInList(String movieID) {
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMedia().getId().equals(movieID)) {
                return true;
            }
        }
        return false;
    }

    public boolean addNewMedia(Media media) {
        //add new media
        if (checkMediaInList(media.getId())) {
            return false;
        }
        if (media.getQid() == MediaType.movie) {
            numberOfMovies++;
        } else {
            numberOfTVShows++;
        }
        UserMediaTracker mediaAndPref = new UserMediaTracker(media, false);
        mediaList.add(mediaAndPref);//not favorite as default
        FirebaseDataManagement.getInstance().addMedia(mediaAndPref, this);
        return true;
    }

    public void setMediaListFromDB(ArrayList<UserMediaTracker> mediaList) {
        this.mediaList = mediaList;
        numberOfMovies = 0;
        numberOfTVShows = 0;
        for (int i = 0; i < mediaList.size(); i++) {
            //init movies and tv shows number
            if (mediaList.get(i).getMedia().getQid() == MediaType.movie) {
                numberOfMovies++;
            } else {
                numberOfTVShows++;
            }
        }
    }


    public String getUserID() {
        return userID;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getUserIMGURL() {
        return userIMGURL;
    }

    public User setUserIMGURL(String userIMGURL) {
        this.userIMGURL = userIMGURL;
        return this;
    }

    public int getNumberOfTVShows() {
        return numberOfTVShows;
    }

    public User setNumberOfTVShows(int numberOfTVShows) {
        this.numberOfTVShows = numberOfTVShows;
        return this;
    }

    public int getNumberOfMovies() {
        return numberOfMovies;
    }

    public User setNumberOfMovies(int numberOfMovies) {
        this.numberOfMovies = numberOfMovies;
        return this;
    }

    public ArrayList<UserMediaTracker> getMediaList() {
        return mediaList;
    }

    public User setMediaList(ArrayList<UserMediaTracker> mediaList) {
        this.mediaList = mediaList;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<UserMediaTracker> getFavoriteMovieMediaList() {
        return getFilteredList(MediaType.movie, MediaType.movie, true, true);
    }

    private ArrayList<UserMediaTracker> getFilteredList(MediaType type1, MediaType type2, boolean isFavorite1, boolean isFavorite2) {
        //2 of every one for all - option filtered list
        ArrayList<UserMediaTracker> filtered = new ArrayList<>();
        for (int i = 0; i < mediaList.size(); i++) {
            if ((mediaList.get(i).getMedia().getQid() == type1 || mediaList.get(i).getMedia().getQid() == type2)
                    && (mediaList.get(i).getFavorite() == isFavorite1 || mediaList.get(i).getFavorite() == isFavorite2)) {
                filtered.add(mediaList.get(i));
            }
        }
        return filtered;
    }

    public ArrayList<UserMediaTracker> getFavoriteTVMediaList() {
        return getFilteredList(MediaType.tvSeries, MediaType.tvSeries, true, true);
    }

    public ArrayList<UserMediaTracker> getFavoriteAllMediaList() {
        return getFilteredList(MediaType.tvSeries, MediaType.movie, true, true);
    }

    public void changeMediaUnFavorite(String id) {
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMedia().getId().equals(id)) {
                mediaList.get(i).setFavorite(false);
            }
        }
    }

    public ArrayList<UserMediaTracker> getMovieList() {
        return getFilteredList(MediaType.movie, MediaType.movie, true, false);
    }

    public void changeMediaToOppositeFavorite(String id) {
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMedia().getId().equals(id)) {
                mediaList.get(i).setFavorite(!mediaList.get(i).getFavorite());
                return;
            }
        }
    }

    public boolean getMediaIDFavorite(String id) {
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMedia().getId().equals(id)) {
                return mediaList.get(i).getFavorite();
            }
        }
        return false;//false as default
    }

    public void removeMediaByID(String id) {
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMedia().getId().equals(id)) {
                mediaList.remove(i);
                return;
            }
        }
    }

    public ArrayList<UserMediaTracker> getTVShowList() {
        return getFilteredList(MediaType.tvSeries, MediaType.tvSeries, false, true);
    }

    public ArrayList<UserMediaTracker> getFavoriteTVShowMediaList() {
        return getFilteredList(MediaType.tvSeries, MediaType.tvSeries, true, true);
    }
}
