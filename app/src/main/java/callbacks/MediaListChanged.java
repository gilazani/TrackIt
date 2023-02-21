package callbacks;

import java.util.ArrayList;

import model.UserMediaTracker;

public interface MediaListChanged {
    void setMediaList(ArrayList<UserMediaTracker> mediaList);
    void notifyMediaListChange();
}
