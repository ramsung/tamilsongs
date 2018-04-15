package in.beyonitysoftwares.besttamilsongs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.models.Songs;
import in.beyonitysoftwares.besttamilsongs.music.MusicService;

/**
 * Created by mohan on 20/3/18.
 */

public class AllSongAdapter extends RecyclerView.Adapter<AllSongAdapter.viewHolder> {


    Context context;
    List<Songs> allSongsList;
    String link = "https://beyonitysoftwares.cf/tamillyrics/img/";
    AdapterCallback adapterCallback;

    public interface AdapterCallback {
        void MenuCall(int position);
        void songCallBack(int position);
    }
    public AllSongAdapter(Context context, List<Songs> allSongsList) {
        this.context = context;
        this.allSongsList = allSongsList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_view, parent, false);
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
        holder.songlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.songCallBack(position);
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.songscontext);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.playnow:
                                adapterCallback.songCallBack(position);
                                break;
                            case R.id.addtoqueue:
                                adapterCallback.MenuCall(position);
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }
    public void setMainCallbacks(AdapterCallback callbacks) {
        this.adapterCallback = callbacks;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView albumview;
        ImageButton menu;
        LinearLayout songlayout;
        TextView songTitle, albumTitle;
        public viewHolder(View itemView) {
            super(itemView);
            albumview = (ImageView) itemView.findViewById(R.id.albumView);
            songTitle = (TextView) itemView.findViewById(R.id.songTitle);
            albumTitle = (TextView) itemView.findViewById(R.id.albumTitle);
            menu = (ImageButton) itemView.findViewById(R.id.context);
            songlayout = (LinearLayout) itemView.findViewById(R.id.songlistlayout);


        }


    }
}
