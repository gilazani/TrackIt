package adapters;

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
import callbacks.OnMediaUnFavorite;
import callbacks.OnSearchResultReturn;
import enums.MediaType;
import model.Media;
import model.User;
import model.UserMediaTracker;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<UserMediaTracker> userMediaTrackers;
    private OnMediaUnFavorite onMediaUnFavorite;

    public FavoriteAdapter(ArrayList<UserMediaTracker> userMediaTrackers, OnMediaUnFavorite onMediaUnFavorite) {
        this.userMediaTrackers = userMediaTrackers;
        this.onMediaUnFavorite = onMediaUnFavorite;
    }


    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        FavoriteViewHolder favoriteViewHolder = new FavoriteViewHolder(view);
        return favoriteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        UserMediaTracker userMediaTracker = getItem(position);
        holder.favorite_LBL_mediaTitle.setText("" + userMediaTracker.getMedia().getL());

        if (userMediaTracker.getMedia().getQid() == MediaType.movie) {
            holder.favorite_icon_movieOrTV.setImageResource(R.drawable.movie_icon);
        } else {
            holder.favorite_icon_movieOrTV.setImageResource(R.drawable.tv_icon);
        }

        if (userMediaTracker.getFavorite()) {
            holder.favorite_icon_isFavorite.setImageResource(R.drawable.heart);
        } else {
            holder.favorite_icon_isFavorite.setImageResource(R.drawable.empty_heart);
        }

        Glide
                .with(holder.favorite_IMG_mediaIMG)
                .load(userMediaTracker.getMedia().getImage().getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(holder.favorite_IMG_mediaIMG);

    }

    @Override
    public int getItemCount() {
        return userMediaTrackers.size();
    }

    private UserMediaTracker getItem(int position) {
        return userMediaTrackers.get(position);
    }


    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView favorite_IMG_mediaIMG;
        private MaterialTextView favorite_LBL_mediaTitle;
        private ShapeableImageView favorite_icon_movieOrTV;
        private ShapeableImageView favorite_icon_isFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favorite_IMG_mediaIMG = itemView.findViewById(R.id.favorite_IMG_mediaIMG);
            favorite_LBL_mediaTitle = itemView.findViewById(R.id.favorite_LBL_mediaTitle);
            favorite_icon_movieOrTV = itemView.findViewById(R.id.favorite_icon_movieOrTV);
            favorite_icon_isFavorite = itemView.findViewById(R.id.favorite_icon_isFavorite);

            favorite_icon_isFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favorite_icon_isFavorite.setImageResource(R.drawable.empty_heart);
                    Media media = userMediaTrackers.get(getAdapterPosition()).getMedia();
                    onMediaUnFavorite.updateList(media);
                }
            });
            //itemView.setOnClickListener(v -> onClickCallback.focusOnPoint(getAdapterPosition()));
        }
    }

}
