package in.beyonitysoftwares.besttamilsongs.fragments;

import android.content.Context;
import android.net.Uri;
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
import android.widget.ProgressBar;
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
import in.beyonitysoftwares.besttamilsongs.models.Songs;
import in.beyonitysoftwares.besttamilsongs.untils.RecyclerItemClickListener;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllSongsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllSongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllSongsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    HashMap<String,String> songfiltermap;
    ProgressBar progressBar;
    final String filterSongKey = "songFilter";
    final String orderByKey = "order";
    boolean isLoading = false;

    private OnFragmentInteractionListener mListener;

    public AllSongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllSongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllSongsFragment newInstance(String param1, String param2) {
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        songfiltermap = new HashMap<>();
        songfiltermap.put(filterSongKey, String.valueOf(filterSongBy.song));
        songfiltermap.put(orderByKey,String.valueOf(orderSongBy.ASC));
        songfiltermap.put("isFilter","no");
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

            }
        }));
       /* allsongsrv.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(!isLoading) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemcountInLayout = layoutManager.getItemCount();
                        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                        int remainingItem = totalItemcountInLayout - (firstVisibleItem + visibleItemCount);
                        //Log.d(TAG, "onScrolled: remainging item " + remainingItem + " total items loaded " + totalItemcountInLayout);
                        //Log.d(TAG, "onScrolled: loading or not " + isLoading);
                        if (totalItemcountInLayout < totalItemCount) {


                            if (remainingItem < 6 && (!isLoading)) {
                                //Log.d(TAG, "onScrolled: calcualtion " + ((totalItemCount - totalItemcountInLayout) / 20));
                                if (((totalItemCount - totalItemcountInLayout) / 20) != 0) {
                                    //Log.d(TAG, "onScrolled: "+presentOffset);
                                    presentOffset = String.valueOf(totalItemcountInLayout);
                                    limit = "20";
                                    isLoading = true;

                                    ((MainActivity) getActivity()).setVisibleTrue();
                                    Log.d(TAG, "onScrolled: count "+count++);
                                    getSongs();

                                } else {
                                    presentOffset = String.valueOf(totalItemcountInLayout);
                                    limit = String.valueOf(totalItemCount - totalItemcountInLayout);
                                    isLoading = true;
                                    ((MainActivity) getActivity()).setVisibleTrue();
                                    getSongs();
                                }


                            }
                        }

                    }
                }
            }
        });*/

    initSongs();




        return view;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
