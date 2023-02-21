package adapters;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trackit.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import callbacks.OnAddToListPressed;
import callbacks.OnSearchResultReturn;
import enums.MediaType;
import model.Media;
import model.User;
import model.UserMediaTracker;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Media[] mediaItems;
    private User user;
    private OnAddToListPressed onAddToListPressed;

    public SearchAdapter(Media[] mediaItems, User user, OnAddToListPressed onAddToListPressed) {
        this.mediaItems = mediaItems;
        this.user = user;
        this.onAddToListPressed = onAddToListPressed;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        Media media = getItem(position);
        holder.search_LBL_mediaTitle.setText("" + media.getL());

        if (media.getQid() == MediaType.movie) {
            holder.search_icon_movieOrTV.setImageResource(R.drawable.movie_icon);
        } else {
            holder.search_icon_movieOrTV.setImageResource(R.drawable.tv_icon);
        }

        holder.search_IMG_checkbox.setVisibility(View.VISIBLE);
        if (user.checkMediaInList(media.getId())) {
            holder.search_IMG_check.setVisibility(View.VISIBLE);
        } else {
            holder.search_IMG_check.setVisibility(View.INVISIBLE);
        }

        Glide
                .with(holder.search_IMG_mediaIMG)
                .load(media.getImage().getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(holder.search_IMG_mediaIMG);

    }

    @Override
    public int getItemCount() {
        return mediaItems.length;
    }

    private Media getItem(int position) {
        return mediaItems[position];
    }

    public void updateList(Media[] searchResult) {
        mediaItems = searchResult;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView search_IMG_mediaIMG;
        private MaterialTextView search_LBL_mediaTitle;
        private ShapeableImageView search_icon_movieOrTV;
        private ShapeableImageView search_IMG_check;
        private ShapeableImageView search_IMG_checkbox;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            search_IMG_mediaIMG = itemView.findViewById(R.id.search_IMG_mediaIMG);
            search_LBL_mediaTitle = itemView.findViewById(R.id.search_LBL_mediaTitle);
            search_icon_movieOrTV = itemView.findViewById(R.id.search_icon_movieOrTV);
            search_IMG_check = itemView.findViewById(R.id.search_IMG_check);
            search_IMG_checkbox = itemView.findViewById(R.id.search_IMG_checkbox);
            search_IMG_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_IMG_check.setVisibility(View.VISIBLE);
                    onAddToListPressed.updateList(mediaItems[getAdapterPosition()]);
                }
            });
        }

        //itemView.setOnClickListener(v -> onClickCallback.focusOnPoint(getAdapterPosition()));
    }
}


