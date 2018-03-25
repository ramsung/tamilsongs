package in.beyonitysoftwares.besttamilsongs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.models.Songs;

/**
 * Created by mohan on 20/3/18.
 */

public class playListAdapter extends RecyclerView.Adapter<playListAdapter.viewHolder> {


    Context context;
    List<Songs> allSongsList;
    String link = "https://beyonitysoftwares.cf/tamillyrics/img/";

    public playListAdapter(Context context, List<Songs> allSongsList) {
        this.context = context;
        this.allSongsList = allSongsList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.play_list_view, parent, false);
        RecyclerView.ViewHolder holder = new viewHolder(itemView);

        return (viewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return allSongsList.size();
    }

    public Songs getItem(int position){
        return allSongsList.get(position);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Songs song = allSongsList.get(position);
        holder.albumTitle.setText(song.getAlbum_name());
        holder.songTitle.setText(song.getSong_title());
        Glide.with(context).load(link+""+song.getAlbum_id()+".png").into(holder.albumview);

    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView albumview;
        TextView songTitle, albumTitle;
        public viewHolder(View itemView) {
            super(itemView);
            albumview = (ImageView) itemView.findViewById(R.id.albumView);
            songTitle = (TextView) itemView.findViewById(R.id.songTitle);
            albumTitle = (TextView) itemView.findViewById(R.id.albumTitle);
        }

    }
}
