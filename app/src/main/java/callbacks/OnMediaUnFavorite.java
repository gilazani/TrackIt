package callbacks;

import java.util.ArrayList;

import model.Media;
import model.UserMediaTracker;

public interface OnMediaUnFavorite {
    void updateList(Media media);
}
