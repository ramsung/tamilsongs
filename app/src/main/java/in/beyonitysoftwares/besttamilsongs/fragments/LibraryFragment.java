package in.beyonitysoftwares.besttamilsongs.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.beyonitysoftwares.besttamilsongs.Activities.MainActivity;
import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.adapters.AllSongAdapter;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppConfig;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.models.Songs;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;
import in.beyonitysoftwares.besttamilsongs.untils.RecyclerItemClickListener;
import in.beyonitysoftwares.besttamilsongs.untils.StorageUtil;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {


    CustomViewPager viewPager;
    public enum filterSongBy{
        song,year,movie;
    }
    public enum orderSongBy{
        ASC,DESC
    }

    int count = 0;
    List<Songs> allSongList;
    JSONObject Albumobject = new JSONObject();
    String presentOffset = "0";
    String limit = "20";
    RecyclerView allsongsrv;
    AllSongAdapter allSongAdapter;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount,totalItemcountInLayout;
    int setTotalNumberOfSongs = 0;
    Spinner artistSpinner,albumSpinner,herospinner,heroinSpinner,yearSpinner,genreSpinner;
    HashMap<String,String> songfiltermap;
    ProgressBar progressBar;
    final String filterSongKey = "songFilter";
    final String orderByKey = "order";
    boolean isLoading = false;
    List<String> artistList;
    List<String> albumList;
    List<String> heroList;
    List<String> heroinList;
    List<String> yearList;
    List<String> lyricistList;
    List<String> genreList;
    ArrayAdapter<String> dataAdapter1;
    ArrayAdapter<String> dataAdapter2;
    ArrayAdapter<String> dataAdapter3;
    ArrayAdapter<String> dataAdapter4;
    ArrayAdapter<String> dataAdapter5;
    ArrayAdapter<String> dataAdapter6;


    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

       /* allSongsFragment = new AllSongsFragment();
        artistFragment = new ArtistFragment();

        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getFragmentManager());
        pageAdapter.addFragment(allSongsFragment,"");
        pageAdapter.addFragment(artistFragment,"");;


        viewPager = (CustomViewPager) view.findViewById(R.id.librayVG);

        viewPager.setAdapter(pageAdapter);*/

        //TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager,true);
        songfiltermap = new HashMap<>();
        songfiltermap.put(filterSongKey, String.valueOf(LibraryFragment.filterSongBy.song));
        songfiltermap.put(orderByKey,String.valueOf(LibraryFragment.orderSongBy.ASC));
        songfiltermap.put("isFilter","no");
        artistSpinner = (Spinner) view.findViewById(R.id.ArtistSpinner);
        albumSpinner = (Spinner) view.findViewById(R.id.AlbumSpinner);
        herospinner = (Spinner) view.findViewById(R.id.HeroSpinner);
        heroinSpinner = (Spinner) view.findViewById(R.id.HeroinSpinner);
        yearSpinner = (Spinner) view.findViewById(R.id.YearSpinner);
        genreSpinner = (Spinner) view.findViewById(R.id.GenreSpinner);


        albumList = new ArrayList<>();
        artistList = new ArrayList<>();
        yearList = new ArrayList<>();
        heroinList = new ArrayList<>();
        heroList = new ArrayList<>();
        genreList = new ArrayList<>();
        lyricistList = new ArrayList<>();
        setAdapters();


        allSongList = new ArrayList<>();
        allsongsrv = (RecyclerView) view.findViewById(R.id.allSongsrv);
        allsongsrv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        allsongsrv.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        allsongsrv.setLayoutManager(layoutManager);
        allSongAdapter = new AllSongAdapter(getContext(),allSongList);
        allsongsrv.setAdapter(allSongAdapter);
        allsongsrv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Songs clickedItem = allSongAdapter.getItem(position);
                LoadingVisibleTrue();
                getLyrics(clickedItem);
                ((MainActivity)getActivity()).playSong(clickedItem);

            }
        }));
        initSongs();
        return view;
    }
    private void getLyrics(Songs clickedItem) {
        if(songfiltermap.get("isFilter").equals("no")){

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
                                ((MainActivity)getActivity()).setLyrics(lyrics_one,lyrics_two,lyrics_three,lyrics_four);

                                LoadingVisibleFalse();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                LoadingVisibleFalse();
                            }


                        }
                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "onError: "+error.getErrorDetail());
                            Toast.makeText(getContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();
                            LoadingVisibleFalse();

                            //isLoading = false;
                        }
                    });
        }

    }


    public void initSongs(){

        if(songfiltermap.get("isFilter").equals("no")){

            AndroidNetworking.post(AppConfig.GET_NO_OF_SONGS)
                    .addBodyParameter("filterSongs", "")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.d(TAG, "onResponse: "+response);
                            try {
                                totalItemCount = Integer.parseInt(String.valueOf(response.get("size")));
                                getSongs();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "onError: "+error.getErrorDetail());
                            Toast.makeText(getContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();
                            ((MainActivity)getActivity()).setVisibleFalse();
                            //isLoading = false;
                        }
                    });
        }


    }
    public void getSongs(){

        //Log.d(TAG, "getSongs: called get songs......");
        AndroidNetworking.post(AppConfig.GET_SONGS_with_limits)
                .addBodyParameter("limit", "20")
                .addBodyParameter("offset", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "onResponse: "+response);
                        try {
                            ((MainActivity)getActivity()).setVisibleFalse();

                            JSONArray array = response.getJSONArray("songs");
                            for(int a = 0;a< array.length();a++){
                                JSONObject songobject = array.getJSONObject(a);
                                Songs s = new Songs();
                                s.setAlbum_id(String.valueOf(songobject.get("album_id")));
                                s.setSong_id(String.valueOf(songobject.get("song_id")));
                                s.setSong_title(songobject.getString("song_title"));
                                if(!allSongList.contains(s)){
                                    allSongList.add(s);
                                }


                            }
                            //Log.d(TAG, "onResponse: size = "+allSongList.size());
                            setSongs();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).setVisibleFalse();
                        //isLoading = false;
                    }
                });
    }
    public void setSongs(){
        for(Songs s : allSongList){

            AndroidNetworking.post(AppConfig.GET_ALBUM_BY_DETAILS)
                    .addBodyParameter("album_id", s.getAlbum_id())
                    .setTag("Album Details")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {

                        @Override
                        public void onResponse(JSONObject response) {

                            isLoading = false;
                            ((MainActivity)getActivity()).setVisibleFalse();
                            //Log.d(TAG, "onResponse: "+response);
                            try {
                                JSONArray array = response.getJSONArray("album_details");
                                for(int a = 0;a< array.length();a++){
                                    JSONObject songobject = array.getJSONObject(a);

                                    if(Integer.parseInt(s.getAlbum_id())==Integer.parseInt(String.valueOf(songobject.get("album_id")))){
                                        //Log.d(TAG, "onResponse: song id = "+s.getAlbum_id()+" album id = "+songobject.get("album_id"));
                                        int index = allSongList.indexOf(s);
                                        allSongList.get(index).setAlbum_name(songobject.getString("album_name"));
                                        allSongAdapter.notifyDataSetChanged();
                                        isLoading = false;
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "onError: "+error.getErrorDetail());
                            Toast.makeText(getContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();
                            ((MainActivity)getActivity()).setVisibleFalse();
                            isLoading =false;
                        }
                    });

        }
    }

    public void LoadingVisibleTrue(){
        ((MainActivity) getActivity()).setVisibleTrue();
    }
    public void LoadingVisibleFalse(){
        ((MainActivity) getActivity()).setVisibleFalse();
    }

    public void setAlbums(ArrayList<String> values){
        if(albumList !=null){
            albumList.clear();
            albumList.add(new StorageUtil(getContext()).getAlbumFilter());
            albumList.addAll(values);
            albumSpinner.setSelection(0);

            dataAdapter2.notifyDataSetChanged();


        }

    }
    public void setHeros(ArrayList<String> values){
        if(heroList !=null){
            heroList.clear();
            heroList.add(new StorageUtil(getContext()).getHeroFilter());
            heroList.addAll(values);
            herospinner.setSelection(0);
            dataAdapter3.notifyDataSetChanged();
        }

    }
    public void setHeroins(ArrayList<String> values){
        if(heroinList !=null){
            heroinList.clear();
            heroinList.add(new StorageUtil(getContext()).getHeroinFilter());
            heroinList.addAll(values);
            heroinSpinner.setSelection(0);
            dataAdapter4.notifyDataSetChanged();
        }


    }public void setGenres(ArrayList<String> values){
        if(genreList!=null){
            genreList.clear();
            genreList.add(new StorageUtil(getContext()).getGenreFilter());
            genreList.addAll(values);
            genreSpinner.setSelection(0);
            dataAdapter6.notifyDataSetChanged();
        }

    }
    public void setYears(ArrayList<String> values){
        if(yearList !=null){
            yearList.clear();
            yearList.add(new StorageUtil(getContext()).getYearFilter());
            yearList.addAll(values);
            yearSpinner.setSelection(0);
            dataAdapter5.notifyDataSetChanged();
        }

    }
    public void setArtists(ArrayList<String> values){
        if(artistList !=null){
            artistList.clear();
            artistList.add(new StorageUtil(getContext()).getArtistFilter());
            artistList.addAll(values);
            artistSpinner.setSelection(0);
            dataAdapter1.notifyDataSetChanged();
        }
    }


    public void setAdapters(){
        dataAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, artistList);

        dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, heroList);
        dataAdapter4 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, heroinList);
        dataAdapter5 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, yearList);
        dataAdapter6 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, genreList);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        artistSpinner.setAdapter(dataAdapter1);

        herospinner.setAdapter(dataAdapter3);
        heroinSpinner.setAdapter(dataAdapter4);
        yearSpinner.setAdapter(dataAdapter5);
        genreSpinner.setAdapter(dataAdapter6);

        dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, albumList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        albumSpinner.setAdapter(dataAdapter2);
    }




}
