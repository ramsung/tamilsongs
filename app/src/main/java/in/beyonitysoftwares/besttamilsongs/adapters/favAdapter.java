package in.beyonitysoftwares.besttamilsongs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppConfig;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppController;
import in.beyonitysoftwares.besttamilsongs.databaseHandler.SessionManager;
import in.beyonitysoftwares.besttamilsongs.models.Songs;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;

/**
 * Created by mohan on 20/3/18.
 */

public class favAdapter extends RecyclerView.Adapter<favAdapter.viewHolder> {


    Context context;
    List<Songs> allSongsList;
    String link = "https://beyonitysoftwares.cf/tamillyrics/img/";
    AdapterCallback adapterCallback;

    public interface AdapterCallback {
        void MenuCall(int position);
        void songCallBack(int position);
        void notifyAdapter();
    }
    public favAdapter(Context context, List<Songs> allSongsList) {
        this.context = context;
        this.allSongsList = allSongsList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_view, parent, false);
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

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Songs song = allSongsList.get(position);
                SessionManager session = new SessionManager(context);
                if (session.isLoggedIn()) {
                    boolean exists = AppController.getDb().isFavExists(Integer.parseInt(AppController.getSignDb().getUserDetails().get("id")), Integer.parseInt(song
                    .getSong_id()));


                        if(exists){
                            deleteFav(song.getSong_id(),holder);
                        }

                } else {
                    Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    public void setMainCallbacks(AdapterCallback callbacks) {
        this.adapterCallback = callbacks;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView albumview;
        ImageButton menu, fav;
        LinearLayout songlayout;
        TextView songTitle, albumTitle;
        public viewHolder(View itemView) {
            super(itemView);
            albumview = (ImageView) itemView.findViewById(R.id.albumView);
            songTitle = (TextView) itemView.findViewById(R.id.songTitle);
            albumTitle = (TextView) itemView.findViewById(R.id.albumTitle);
            menu = (ImageButton) itemView.findViewById(R.id.context);
            fav = (ImageButton) itemView.findViewById(R.id.fav);

            songlayout = (LinearLayout) itemView.findViewById(R.id.songlistlayout);


        }


    }



    private void deleteFav(String song_id, viewHolder holder) {
        AndroidNetworking.post(AppConfig.DELETE_FAV)
                .addBodyParameter("user_id", AppController.getSignDb().getUserDetails().get("id"))
                .addBodyParameter("song_id", song_id)
                .setTag("add fav")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            boolean error = response.getBoolean("error");
                            if (!error) {
                                //JSONArray array = response.getJSONArray("fav");
                                AppController.getDb().deleteFavorites(Integer.parseInt(AppController.getSignDb().getUserDetails().get("id")), Integer.parseInt(song_id));
                                holder.fav.setImageResource(R.drawable.heart_outline);
                                for(Songs s : allSongsList){
                                    if(s.getSong_id().equals(song_id)){
                                        allSongsList.remove(s);
                                    }
                                }
                                adapterCallback.notifyAdapter();
                            }

                            //hideDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(context, "error loading songs from the database", Toast.LENGTH_SHORT).show();



                    }
                });



    }
}
