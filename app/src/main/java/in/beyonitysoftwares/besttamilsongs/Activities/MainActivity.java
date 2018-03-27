package in.beyonitysoftwares.besttamilsongs.Activities;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.adapters.playListAdapter;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppConfig;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.customViews.SmoothProgressBar;
import in.beyonitysoftwares.besttamilsongs.databaseHandler.DatabaseHandler;
import in.beyonitysoftwares.besttamilsongs.fragments.AboutFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.FavouritesFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.LibraryFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.LyricsFragment;
import in.beyonitysoftwares.besttamilsongs.models.Songs;
import in.beyonitysoftwares.besttamilsongs.music.MusicService;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;
import in.beyonitysoftwares.besttamilsongs.untils.StorageUtil;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;

public class MainActivity extends AppCompatActivity implements MusicService.mainActivityCallback,View.OnClickListener{

    private TextView mTextMessage;

    //fragments
    AboutFragment aboutFragment;
    FavouritesFragment favouritesFragment;
    LibraryFragment libraryFragment;
    LyricsFragment lyricsFragment;
    CustomViewPager viewPager;
    public SmoothProgressBar loading;
    BottomNavigationView navigation;
    public static final String Broadcast_PLAY_NEW_AUDIO = "in.beyonitysoftwares.besttamilsongs.Activites.PlayNewAudio";
    public static final String Broadcast_NEW_ALBUM = "in.beyonitysoftwares.besttamilsongs.Activites.PlayNewAlbum";
    NavigationView navigationView;
    boolean serviceBound = false;
    MusicService player;
    String l1,l2,l3,l4;
    FloatingActionButton playpause,skipnext,skipprev;
    ImageButton playlistButton;
    SeekBar seekBar;
    private Handler mHandler = new Handler();
    ArrayList<Songs> playlist;
    playListAdapter playlistadapter;
    RecyclerView rvPlayList;
    private static final String TAG = "MainActivity";

    //db
    DatabaseHandler db;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_library:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_about:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-7987343674758455~6065686189");
        db = new DatabaseHandler(getApplicationContext());

        //init
        playpause = (FloatingActionButton) findViewById(R.id.PlayButton);
        skipnext = (FloatingActionButton) findViewById(R.id.SkipNext);
        skipprev = (FloatingActionButton) findViewById(R.id.SkipPrev);

        playpause.setOnClickListener(this);
        skipprev.setOnClickListener(this);
        skipnext.setOnClickListener(this);

        l1 = "";
        l2 = "";
        l3 = "";
        l4 = "";

        playlistButton = (ImageButton) findViewById(R.id.ButtonPlaylist);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        aboutFragment = new AboutFragment();
        lyricsFragment = new LyricsFragment();
        favouritesFragment = new FavouritesFragment();
        libraryFragment = new LibraryFragment();
        loading = (SmoothProgressBar) findViewById(R.id.google_now);
        loading.setVisibility(View.INVISIBLE);
        viewPager = (CustomViewPager) findViewById(R.id.mainVG);
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(lyricsFragment,"Lyrics");
        pageAdapter.addFragment(libraryFragment,"Song Library");
        pageAdapter.addFragment(favouritesFragment,"Favorites");
        pageAdapter.addFragment(aboutFragment,"About");

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(pageAdapter);
        viewPager.setPagingEnabled(false);


        playlist = new ArrayList<>();
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (player != null) {
                        if (player.mediaPlayer != null) {
                            player.seekTo(progress);
                        }
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        rvPlayList = (RecyclerView) findViewById(R.id.rvPlaylist);
        rvPlayList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        rvPlayList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvPlayList.setLayoutManager(layoutManager);
        playlistadapter = new playListAdapter(getApplicationContext(),playlist);
        rvPlayList.setAdapter(playlistadapter);
        playlistadapter.notifyDataSetChanged();
        mHandler.post(runnable);
        
        
        getupdatetime();

    }

    private void getupdatetime() {

        AndroidNetworking.post(AppConfig.GET_UPDATE_TIME)
                .addBodyParameter("updated", "checking update")
                .setTag("update time")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray update = response.getJSONArray("update");
                            //Log.d(TAG, "onResponse: length = "+update.length());
                            for(int a =0;a<update.length();a++){
                                JSONObject object = update.getJSONObject(a);

                                String table_name = String.valueOf(object.get("table_name"));
                                String remote_time = String.valueOf(object.get("update_time"));
                                String local_time = db.getUpdateDetails(table_name);
                                long local_time_long = Long.parseLong(local_time);
                                long remote_time_long = Long.parseLong(remote_time);

                                if(local_time.equals("")){
                                    db.insertUpdate(table_name,remote_time);
                                }else if(remote_time_long>local_time_long){

                                    if(db.updateUpdateTable(table_name,remote_time)){
                                        Log.d(TAG, "onResponse: table name = "+table_name+" local time = "+local_time+" remote time = "+remote_time);
                                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
                                    }

                                }




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();
                        setVisibleFalse();
                        //isLoading = false;
                    }
                });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (player != null) {
                if (player.isPlaying()) {
                    int position = player.getCurrentPosition();
                    seekBar.setProgress(position);
                    //currentTime.setText(Helper.durationCalculator(position));

                }
            }
            mHandler.postDelayed(runnable, 1000);
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.PlayButton: {
                if (player != null) {
                    if (player.isPlaying()) {
                        playpause.setImageResource(R.drawable.play);
                        player.pauseMedia();
                    } else {
                        if (player.mediaPlayer != null) {
                            if (player.getCurrentPosition() > 0) player.resumeMedia();
                            playpause.setImageResource(R.drawable.pause);
                        }
                    }
                }
                break;
            }
            case R.id.SkipPrev: {
                if (player != null) {
                    if (player.isPlaying()) {
                        if (new StorageUtil(getApplicationContext()).loadAudio().size() > 0) {
                            player.skipToPrevious();
                        }
                    }
                }
                break;
            }
            case R.id.SkipNext: {
                if (player != null) {
                    if (player.isPlaying()) {
                        if (new StorageUtil(getApplicationContext()).loadAudio().size() > 0) {
                            player.skipToNext();
                        }
                    }
                }
                break;
            }

        }
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            player = binder.getService();
            player.setMainCallbacks(MainActivity.this);
            update();
            serviceBound = true;
            Log.d(TAG, "calls: now service bound true");


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onBindingDied(ComponentName name) {

        }

    };
        public void setVisibleTrue(){
        loading.setVisibility(View.VISIBLE);
    }
    public void setVisibleFalse(){
        loading.setVisibility(View.INVISIBLE);
    }

    public void setLyrics(String l1,String l2,String l3, String l4){
        lyricsFragment.setLyrics(l1,l2,l3,l4);
        viewPager.setCurrentItem(0);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void update() {
        if (player != null && player.mediaPlayer != null) {
            if (player.isPlaying()) {


                seekBar.setMax(player.getDuration());
                //totalTime.setText(Helper.durationCalculator(player.getDuration()));
                playpause.setImageResource(R.drawable.pause);
                if(l1.equals("")||l2.equals("")||l3.equals("")||l4.equals("")){
                    getLyrics(player.getActiveSong());
                }
                setVisibleFalse();

                /*if (checkFavoriteItem()) {
                    fav.setImageResource(R.drawable.favon);
                } else {
                    fav.setImageResource(R.drawable.heart);
                }*/
                Log.i("CalledSet", "called set details");

            } else {

                if (player.mediaPlayer != null) {
                    seekBar.setMax(player.getDuration());
                    playpause.setImageResource(R.drawable.play);
                }


                //playpause.setImageResource(android.R.drawable.ic_media_play);
            }


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
                                setLyrics(lyrics_one,lyrics_two,lyrics_three,lyrics_four);

                                setVisibleFalse();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                setVisibleFalse();
                            }


                        }
                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "onError: "+error.getErrorDetail());
                            Toast.makeText(getApplicationContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();
                           setVisibleFalse();
                            //isLoading = false;
                        }
                    });


    }


    @Override
    public void updateSongDownload(MediaPlayer mediaPlayer,int Progress) {
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setSecondaryProgress(Progress);
    }

    @Override
    public void showDialog(Songs s) {

    }
    @Override
    protected void onStart() {
        Log.i(TAG, "calls: on start called");
        super.onStart();
        if (!serviceBound) {
            Intent playerIntent = new Intent(MainActivity.this, MusicService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            update();
            Log.i("calls", "service bounded");


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "calls: on resume called");
        if (serviceBound) {
            if (player != null) {
                player.setMainCallbacks(MainActivity.this);
                if (player.isPlaying()) {
                    Log.d(TAG, "calls: playing");
                    //downloadLyrics(player.getActiveSong());
                }
                update();

            }
        }


        Log.i(TAG, "calls: on resume over");
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: on destroy called");
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
            player.setMainCallbacks(null);
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Log.i(TAG, "onDestroy: am in destory");

        super.onDestroy();


    }

    public void playSong(Songs song){

        StorageUtil storageUtil = new StorageUtil(getApplicationContext());
        setVisibleTrue();
        playlist.clear();
        playlist.add(song);
        playlistadapter.notifyDataSetChanged();
        storageUtil.storeAudio(playlist);
        storageUtil.storeAudioIndex(0);
        Log.d(TAG, "onItemClick: storage = " + storageUtil.loadAudio().size());
        Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
        sendBroadcast(setplaylist);
        Intent broadcastIntent = new Intent(MainActivity.Broadcast_PLAY_NEW_AUDIO);
        sendBroadcast(broadcastIntent);




    }
    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {

            super.onBackPressed();
        }
    }

    public void closeDrawer() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

}
