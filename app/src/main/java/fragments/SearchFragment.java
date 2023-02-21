package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import adapters.SearchAdapter;
import callbacks.OnAddToListPressed;
import callbacks.OnSearchResultReturn;
import enums.MediaType;
import model.Image;
import model.Media;
import model.User;
import model.UserMediaTracker;
import utils.FirebaseDataManagement;
import utils.MediaDataCollector;

public class SearchFragment extends Fragment {
    private TextInputEditText search_input_search;
    private RecyclerView search_LST_result;

    private SearchAdapter searchAdapter;

    private Media[] media;

    private boolean searchArrived = false;

    private User user;
    private OnAddToListPressed onAddToListPressed = new OnAddToListPressed() {
        @Override
        public void updateList(Media media) {
            FirebaseDataManagement.getInstance().addMedia(new UserMediaTracker(media, false),user);//false as default
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initViews(view);

        setOnInput();

        setAdapter();

        return view;

    }

    public void setAdapter() {
        Media[] media = new Media[0];
        searchAdapter = new SearchAdapter(media, user, onAddToListPressed);
        search_LST_result.setAdapter(searchAdapter);
    }

    private void setOnInput() {
        search_input_search.setOnKeyListener((view1, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                MediaDataCollector.getInstance().getSearchResult(search_input_search.getText().toString());
                while(!searchArrived){
                    //delay until search result arrived
                }
                searchArrived =false;
                search_LST_result.setAdapter(new SearchAdapter(media, user,onAddToListPressed));
                search_LST_result.getAdapter().notifyDataSetChanged();
                return true;
            }
            return false;
        });
    }

    public void initViews(View view){
        search_input_search = view.findViewById(R.id.search_input_search);

        search_LST_result = view.findViewById(R.id.search_LST_result);

        search_LST_result.setLayoutManager(new LinearLayoutManager(view.getContext()));


    }

    public void updateAdapter(Media[] searchResult) {
        //searchAdapter = new SearchAdapter(searchResult);
        //searchAdapter.updateList(searchResult);
        media = searchResult;
        //search_LST_result.setAdapter(searchAdapter);
//        new Handler(Looper.getMainLooper()).post(() ->
//                search_LST_result.setAdapter(searchAdapter)
//        );
        //searchAdapter.notify();
    }

    public void updateFlag() {
        searchArrived = true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCallbacks(OnAddToListPressed onAddToListPressed) {
        this.onAddToListPressed = onAddToListPressed;
    }
}
