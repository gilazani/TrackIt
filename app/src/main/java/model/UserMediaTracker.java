package model;

import java.io.Serializable;

public class UserMediaTracker implements Serializable {
    private Media media;
    private boolean favorite;

    public UserMediaTracker() {

    }

    public UserMediaTracker(Media media, boolean favorite) {
        this.media = media;
        this.favorite = favorite;
    }

    public UserMediaTracker(UserMediaTracker userMediaTracker) {
        media = userMediaTracker.getMedia();
        favorite = userMediaTracker.getFavorite();
    }

    public Media getMedia() {
        return media;
    }

    public UserMediaTracker setMedia(Media media) {
        this.media = media;
        return this;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public UserMediaTracker setFavorite(boolean favorite) {
        this.favorite = favorite;
        return this;
    }
}
