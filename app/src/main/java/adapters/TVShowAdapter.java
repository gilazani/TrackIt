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

import callbacks.OnMediaUnFavorite;
import callbacks.OnRemovedMedia;
import model.Media;
import model.UserMediaTracker;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {
    private ArrayList<UserMediaTracker> userMediaTrackers;
    private OnMediaUnFavorite onMediaUnFavorite;
    private OnRemovedMedia onRemovedMedia;

    public TVShowAdapter(ArrayList<UserMediaTracker> userMediaTrackers, OnMediaUnFavorite onMediaUnFavorite, OnRemovedMedia onRemovedMedia) {
        this.userMediaTrackers = userMediaTrackers;
        this.onMediaUnFavorite = onMediaUnFavorite;
        this.onRemovedMedia = onRemovedMedia;
    }


    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);
        TVShowViewHolder tVShowViewHolder = new TVShowViewHolder(view);
        return tVShowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.TVShowViewHolder holder, int position) {
        UserMediaTracker userMediaTracker = getItem(position);
        holder.media_LBL_mediaTitle.setText("" + userMediaTracker.getMedia().getL());

        if (userMediaTracker.getFavorite()) {
            holder.media_icon_isFavorite.setImageResource(R.drawable.heart);
        } else {
            holder.media_icon_isFavorite.setImageResource(R.drawable.empty_heart);
        }

        Glide
                .with(holder.media_IMG_mediaIMG)
                .load(userMediaTracker.getMedia().getImage().getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(holder.media_IMG_mediaIMG);

    }

    @Override
    public int getItemCount() {
        return userMediaTrackers.size();
    }

    private UserMediaTracker getItem(int position) {
        return userMediaTrackers.get(position);
    }


    public class TVShowViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView media_IMG_mediaIMG;
        private MaterialTextView media_LBL_mediaTitle;
        private ShapeableImageView media_icon_isFavorite;
        private ShapeableImageView media_IMG_remove;

        public TVShowViewHolder(@NonNull View itemView) {
            super(itemView);
            media_IMG_mediaIMG = itemView.findViewById(R.id.media_IMG_mediaIMG);
            media_LBL_mediaTitle = itemView.findViewById(R.id.media_LBL_mediaTitle);
            media_icon_isFavorite = itemView.findViewById(R.id.media_icon_isFavorite);
            media_IMG_remove = itemView.findViewById(R.id.media_IMG_remove);

            media_icon_isFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userMediaTrackers.get(getAdapterPosition()).getFavorite()) {
                        media_icon_isFavorite.setImageResource(R.drawable.empty_heart);
                    } else {
                        media_icon_isFavorite.setImageResource(R.drawable.heart);
                    }
                    Media media = userMediaTrackers.get(getAdapterPosition()).getMedia();
                    onMediaUnFavorite.updateList(media);
                }
            });

            media_IMG_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Media media = userMediaTrackers.get(getAdapterPosition()).getMedia();
                    onRemovedMedia.removeMedia(media);
                }
            });
        }
    }

}
