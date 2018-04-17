package in.beyonitysoftwares.besttamilsongs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.beyonitysoftwares.besttamilsongs.Activities.MainActivity;
import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.adapters.AllSongAdapter;
import in.beyonitysoftwares.besttamilsongs.adapters.favAdapter;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppConfig;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppController;
import in.beyonitysoftwares.besttamilsongs.models.Songs;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements favAdapter.AdapterCallback{

    List<Integer> songids = new ArrayList<>();
    ArrayList<Songs> favlist = new ArrayList<>();
    favAdapter adapter;
    RecyclerView favsongsrv;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites,container,false);
        favsongsrv = (RecyclerView) view.findViewById(R.id.favSongsrv);
        songids = AppController.getDb().getFavorites(Integer.parseInt(AppController.getSignDb().getUserDetails().get("id")));
        favsongsrv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        favsongsrv.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        favsongsrv.setLayoutManager(layoutManager);
        adapter = new favAdapter(getContext(),favlist);
        adapter.setMainCallbacks(this);
        favsongsrv.setAdapter(adapter);
        getSongs();
        return view;
    }

    @Override
    public void MenuCall(int position) {
        Songs clickedItem = adapter.getItem(position);
        ((MainActivity)getActivity()).addToQueue(clickedItem);
    }

    @Override
    public void songCallBack(int position) {
        Songs clickedItem = adapter.getItem(position);
        getLyrics(clickedItem);
        ((MainActivity)getActivity()).playSong(clickedItem);

    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }
    public void getSongs(){
        Log.d(TAG, "getSongs: "+songids.size());
    for(int id : songids){
        Log.d(TAG, "getSongs: fav getting song for id "+id);
        AndroidNetworking.post(AppConfig.GET_SONGS_BY_ID)
                .addBodyParameter("song_id", String.valueOf(id))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getSongs fav "+response);
                        try {

                            JSONArray array = response.getJSONArray("songs");
                            for(int a = 0;a< array.length();a++){
                                JSONObject songobject = array.getJSONObject(a);
                                Songs s = new Songs();
                                s.setAlbum_id(String.valueOf(songobject.get("album_id")));
                                s.setSong_id(String.valueOf(songobject.get("song_id")));
                                s.setSong_title(songobject.getString("song_title"));
                                if(!favlist.contains(s)){
                                    favlist.add(s);
                                }


                            }
                            adapter.notifyDataSetChanged();
                            //Log.d(TAG, "onResponse: size = "+allSongList.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getMessage());
                        Toast.makeText(getContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }
    }

    private void getLyrics(Songs clickedItem) {


        AndroidNetworking.post(AppConfig.GET_LYRICS)
                .addBodyParameter("song_id", clickedItem.getSong_id())
                .setTag("lyrics")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "onResponse: "+response);

                        try {
                            JSONArray lyrics = response.getJSONArray("lyrics");
                            JSONObject object = lyrics.getJSONObject(0);
                            String lyrics_one = object.getString("lyrics_one");
                            String lyrics_two = object.getString("lyrics_two");
                            String lyrics_three = object.getString("lyrics_three");
                            String lyrics_four = object.getString("lyrics_four");
                            ((MainActivity) getActivity()).setLyrics(lyrics_one, lyrics_two, lyrics_three, lyrics_four);


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: " + error.getErrorDetail());
                        Toast.makeText(getContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });

    }
    }
