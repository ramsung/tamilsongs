package in.beyonitysoftwares.besttamilsongs.Activities;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.inmobi.sdk.InMobiSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.adapters.playListAdapter;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppConfig;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppController;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.customViews.SmoothProgressBar;
import in.beyonitysoftwares.besttamilsongs.databaseHandler.DatabaseHandler;
import in.beyonitysoftwares.besttamilsongs.databaseHandler.SQLiteSignInHandler;
import in.beyonitysoftwares.besttamilsongs.databaseHandler.SessionManager;
import in.beyonitysoftwares.besttamilsongs.fragments.AboutFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.FavouritesFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.LibraryFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.LyricsFragment;
import in.beyonitysoftwares.besttamilsongs.models.FilteredAlbum;
import in.beyonitysoftwares.besttamilsongs.models.Songs;
import in.beyonitysoftwares.besttamilsongs.music.MusicService;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;
import in.beyonitysoftwares.besttamilsongs.untils.Helper;
import in.beyonitysoftwares.besttamilsongs.untils.RecyclerItemClickListener;
import in.beyonitysoftwares.besttamilsongs.untils.StorageUtil;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.fabric.sdk.android.Fabric;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;


public class MainActivity extends AppCompatActivity implements MusicService.mainActivityCallback,View.OnClickListener, playListAdapter.AdapterCallback{

    private TextView mTextMessage;
    int updateAll = 0;

    TextView songsdetails;
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
    DrawerLayout drawer;
    private ProgressDialog pDialog;
    String des;
    String titleFromfirebase;
    //db
    //DatabaseHandler db;
    SegmentedGroup segmentedGroup;
    int SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SessionManager session;
    private SQLiteSignInHandler db;

    List<Songs>  originalPlayList;
    SearchView songSearch;
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
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-7987343674758455~6065686189");
        //db = new DatabaseHandler(getApplicationContext());
        InMobiSdk.init(MainActivity.this, "f8fcaf8d25c04b7586fb741b3dd266f8");
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Preparing database");
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        db = new SQLiteSignInHandler(getApplicationContext());

        // Session manager
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn() || user == null) {

            signIn();

        } else {
            HashMap<String, String> details = db.getUserDetails();
            Toast.makeText(this, "Welcome " + details.get("name"), Toast.LENGTH_SHORT).show();
            showDialog();
            getupdatetime();


            //logoutUser();
        }


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        Log.d(TAG, "onCreate: got app link");
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }
    public void signinTry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Error While Connecting");
        builder.setMessage("oops Looks like network issues make sure your internet connection is on and try again... ");
        builder.setNegativeButton("Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        System.exit(1);
                    }
                });
        builder.setPositiveButton("Try again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        signIn();
                    }
                });

        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                handleSignInResult(task);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                signinTry();
                // ...
            }
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            Toast.makeText(this, "Welcome " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
            String name = account.getDisplayName();
            String email = account.getEmail();


            user(email, name, FirebaseInstanceId.getInstance().getToken());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            signinTry();
            //updateUI(null);
        }
    }

    private void user(String email, String name, String token) {

        AndroidNetworking.post(AppConfig.GET_LOGIN_REGISTER)
                .addBodyParameter("email", email)
                .addBodyParameter("name",name)
                .addBodyParameter("fcmtoken",token)
                .setTag("lyrics")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {

                            boolean error = response.getBoolean("error");
                            if (!error) {
                                session.setLogin(true);
                                JSONObject user = response.getJSONObject("user");

                                db.addUser(response.getInt("id"),user.getString("email"),user.getString("name"));
                                showDialog();
                                getupdatetime();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();

                        signinTry();
                    }
                });
    }

    public void callBackAfterNetworking(){
        Log.d(TAG, "callBackAfterNetworking: "+updateAll);
        if(updateAll==6) {
            Log.d(TAG, "callBackAfterNetworking: called library");
            //libraryFragment.setSpinners();
            getFav();

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void init(){
        //init

        originalPlayList = new ArrayList<>();
        playpause = (FloatingActionButton) findViewById(R.id.PlayButton);
        skipnext = (FloatingActionButton) findViewById(R.id.SkipNext);
        skipprev = (FloatingActionButton) findViewById(R.id.SkipPrev);
        songsdetails = (TextView) findViewById(R.id.songdetails);
        songsdetails.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        songsdetails.setMarqueeRepeatLimit(-1);
        songsdetails.setSingleLine(true);
        songsdetails.setSelected(true);

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
        if(new StorageUtil(this).loadAudio()!=null){
            playlist.addAll(new StorageUtil(this).loadAudio());
            originalPlayList.addAll(playlist);
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


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
        songSearch = (SearchView) view.findViewById(R.id.songsearch);
        songSearch.setQueryHint("Search Songs in Playlist");
        songSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()<0 || newText.isEmpty()){
                    if(playlist!=null){
                        playlist.clear();
                        playlist.addAll(originalPlayList);
                            new StorageUtil(getApplicationContext()).storeAudio(playlist);
                            playlistadapter.notifyDataSetChanged();
                            Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
                            sendBroadcast(setplaylist);


                    }
                    return false;
                }else if(newText.length()>0){
                    List<Songs> filteredList = new ArrayList<>();
                    for (Songs s : originalPlayList){
                        Log.d(TAG, "onQueryTextChange: "+s.getSong_title()+" new text = "+newText);
                        if(s.getSong_title().toLowerCase().contains(newText.toLowerCase())){
                            filteredList.add(s);
                        }
                    }
                    Log.d(TAG, "onQueryTextChange: size "+filteredList.size());
                    if(filteredList.size()>0){
                        if(playlist!=null){
                            playlist.clear();
                            playlist.addAll(filteredList);
                                new StorageUtil(getApplicationContext()).storeAudio(playlist);
                                playlistadapter.notifyDataSetChanged();
                                Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
                                sendBroadcast(setplaylist);


                        }
                    }

                }
                return false;
            }
        });
        rvPlayList = (RecyclerView) findViewById(R.id.rvPlaylist);
        rvPlayList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        rvPlayList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvPlayList.setLayoutManager(layoutManager);
        rvPlayList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setSelected(true);
            }
        }));
        playlistadapter = new playListAdapter(getApplicationContext(),playlist);
        rvPlayList.setAdapter(playlistadapter);
        playlistadapter.notifyDataSetChanged();
        playlistadapter.setMainCallbacks(this);

        mHandler.post(runnable);
        update();

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
                                String local_time = AppController.getDb().getUpdateDetails(table_name);
                                long local_time_long = Long.parseLong(local_time);
                                long remote_time_long = Long.parseLong(remote_time);
                                Log.d(TAG, "onResponse: table name = "+table_name+" remote time = "+remote_time+" local time = "+local_time);
                                if(local_time.equals("0")){
                                    if(table_name.equals("albums")){

                                        getAlbums(table_name,remote_time,local_time);

                                    }else if (table_name.equals("artist")){

                                        getArtists(table_name,remote_time,local_time);

                                    }else if (table_name.equals("hero")){

                                        getHeros(table_name,remote_time,local_time);

                                    }else if (table_name.equals("heroin")){

                                        getHeroins(table_name,remote_time,local_time);

                                    }else if (table_name.equals("lyricist")){

                                        getLyricists(table_name,remote_time,local_time);

                                    }else if (table_name.equals("genre")){

                                        getGenres(table_name,remote_time,local_time);

                                    }

                                }else if(remote_time_long>local_time_long){

                                    if(table_name.equals("albums")){

                                        getAlbums(table_name, remote_time, local_time);

                                    }else if (table_name.equals("artist")){

                                        getArtists(table_name, remote_time, local_time);

                                    }else if (table_name.equals("hero")){

                                        getHeros(table_name, remote_time, local_time);

                                    }else if (table_name.equals("heroin")){

                                        getHeroins(table_name, remote_time, local_time);

                                    }else if (table_name.equals("lyricist")){

                                        getLyricists(table_name, remote_time, local_time);

                                    }else if (table_name.equals("genre")){

                                        getGenres(table_name, remote_time, local_time);

                                    }


                                }else{
                                    if(table_name.equals("albums")){
                                        updateAll++;
                                        callBackAfterNetworking();


                                    }else  if(table_name.equals("artist")){
                                        updateAll++;
                                        callBackAfterNetworking();

                                    }else  if(table_name.equals("hero")){
                                        updateAll++;
                                        callBackAfterNetworking();

                                    }else  if(table_name.equals("heroin")){
                                        updateAll++;
                                        callBackAfterNetworking();


                                    }else  if(table_name.equals("genre")){
                                        updateAll++;
                                        callBackAfterNetworking();

                                    }else  if(table_name.equals("lyricist")){
                                        updateAll++;
                                        callBackAfterNetworking();

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
            if(new StorageUtil(getApplicationContext()).loadAudio()!=null) {
                Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
                sendBroadcast(setplaylist);
            }
            //update();
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
                    songsdetails.setText(player.getActiveSong().getSong_title()+" | "+player.getActiveSong().getAlbum_name());
                }


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

            hideDialog();

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


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }
                        @Override
                        public void onError(ANError error) {
                            Log.e(TAG, "onError: "+error.getErrorDetail());
                            Toast.makeText(getApplicationContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();

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
    public void errorloading() {
        Toast.makeText(this, "Error loading song from the server try again later", Toast.LENGTH_LONG).show();
        hideDialog();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "calls: on start called");
        super.onStart();
        if (!serviceBound) {
            Intent playerIntent = new Intent(MainActivity.this, MusicService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            //update();
            Log.i("calls", "service bounded");


        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
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
               // update();

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
        session.setLogin(false);
        db.deleteUsers();

        super.onDestroy();

        if(originalPlayList!=null&&originalPlayList.size()>0){
            new StorageUtil(getApplicationContext()).storeAudio((ArrayList<Songs>) originalPlayList);
        }
    }

    public void playSong(Songs song){

        pDialog.setMessage("Loading "+song.getSong_title() +" from "+song.getAlbum_name());
        showDialog();
        StorageUtil storageUtil = new StorageUtil(getApplicationContext());

        int index = 0;

        for(Songs s : playlist){
            if(s.getSong_id().equals(song.getSong_id())){
                index = playlist.indexOf(s);
                storageUtil.storeAudioIndex(index);
                Log.d(TAG, "onItemClick: storage = " + storageUtil.loadAudio().size()+" index  = "+index);
                Intent broadcastIntent = new Intent(MainActivity.Broadcast_PLAY_NEW_AUDIO);
                sendBroadcast(broadcastIntent);
                return;
            }
        }

        Log.d(TAG, "playSong: am in if");
        List<Songs> dummy = new ArrayList<>();
        dummy.addAll(playlist);
        playlist.clear();
        playlist.add(0,song);
        playlist.addAll(dummy);
        playlistadapter.notifyDataSetChanged();
        storageUtil.storeAudio(playlist);
        storageUtil.storeAudioIndex(0);
        Log.d(TAG, "onItemClick: storage = " + storageUtil.loadAudio().size());
        Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
        sendBroadcast(setplaylist);
        Intent broadcastIntent = new Intent(MainActivity.Broadcast_PLAY_NEW_AUDIO);
        sendBroadcast(broadcastIntent);



    }

    public void addToQueue(Songs song){
        StorageUtil storageUtil = new StorageUtil(getApplicationContext());


        for(Songs s : playlist){
            if(s.getSong_id().equals(song.getSong_id())){
                return;
            }
        }

        ArrayList<Songs> dummylist = new ArrayList<>();
        dummylist.add(song);
        if(new StorageUtil(this).loadAudio()!=null){
            dummylist.addAll(new StorageUtil(this).loadAudio());
        }
        playlist.add(song);
        playlistadapter.notifyDataSetChanged();
        storageUtil.storeAudio(dummylist);
        player.getPlaylist().add(song);
    }
    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void closeDrawer() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }



    public void getAlbums(String table_name, String remote_time, String local_time){
        AndroidNetworking.post(AppConfig.GET_ALBUMS)
                .addBodyParameter("albums", "all")
                .setTag("albums")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);

                        try {

                            if(response.getString("error").equals("false")) {

                                if(!local_time.equals("0")){
                                    AppController.getDb().deleteRecords(table_name);
                                }
                                JSONArray array = response.getJSONArray("albums");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    String album_id = String.valueOf(object.get("album_id"));
                                    String album_name = String.valueOf(object.get("album_name"));
                                    String artist_id = String.valueOf(object.get("artist_id"));
                                    String hero_id = String.valueOf(object.get("hero_id"));
                                    String heroin_id = String.valueOf(object.get("heroin_id"));
                                    String year = String.valueOf(object.get("year"));
                                    if(AppController.getDb().insertAlbums(album_id,album_name,artist_id,hero_id,heroin_id,year)){

                                    }else {
                                        Log.d(TAG, "onResponse: error inserting albums in local database");
                                    }


                                }
                                if(local_time.equals("0")){
                                    AppController.getDb().insertUpdate(table_name,remote_time);
                                }else {
                                    AppController.getDb().updateUpdateTable(table_name,remote_time);
                                }




                                Log.d(TAG, "onResponse: length " + array.length());

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to get albums from database", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "oncallback: album called call back");
                        updateAll++;
                        callBackAfterNetworking();

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading albums from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }

    public void getArtists(String table_name, String remote_time, String local_time){
        AndroidNetworking.post(AppConfig.GET_ARTISTS)
                .addBodyParameter("artist", "all")
                .setTag("artists")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            if(response.getString("error").equals("false")) {
                                if(!local_time.equals("0")){
                                    AppController.getDb().deleteRecords(table_name);
                                }
                                JSONArray array = response.getJSONArray("artists");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    String artist_id = String.valueOf(object.get("artist_id"));
                                    String artist_name = String.valueOf(object.get("artist_name"));
                                    if(AppController.getDb().insertArtist(artist_id,artist_name)){

                                    }else {
                                        Log.d(TAG, "onResponse: error inserting artists in local database");
                                    }

                                }
                                if(local_time.equals("0")){
                                    AppController.getDb().insertUpdate(table_name,remote_time);
                                }else {
                                    AppController.getDb().updateUpdateTable(table_name,remote_time);
                                }


                                Log.d(TAG, "onResponse: length " + array.length());

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to get artists from database", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "oncallback: artist called call back");
                        updateAll++;
                        callBackAfterNetworking();



                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading artists from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }

    public void getHeros(String table_name, String remote_time, String local_time){
        AndroidNetworking.post(AppConfig.GET_HEROS)
                .addBodyParameter("hero", "all")
                .setTag("heros")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            if(response.getString("error").equals("false")) {
                                if(!local_time.equals("0")){
                                    AppController.getDb().deleteRecords(table_name);
                                }
                                JSONArray array = response.getJSONArray("heros");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    String hero_id = String.valueOf(object.get("hero_id"));
                                    String hero_name = String.valueOf(object.get("hero_name"));
                                    if(AppController.getDb().insertHero(hero_id,hero_name)){

                                    }else {
                                        Log.d(TAG, "onResponse: error inserting heros in local database");
                                    }


                                }
                                if(local_time.equals("0")){
                                    AppController.getDb().insertUpdate(table_name,remote_time);
                                }else {
                                    AppController.getDb().updateUpdateTable(table_name,remote_time);
                                }


                                Log.d(TAG, "onResponse: length " + array.length());

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to get heros from database", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "oncallback: heros called call back");
                        updateAll++;
                        callBackAfterNetworking();

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading heros from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }


    public void getHeroins(String table_name, String remote_time, String local_time){
        AndroidNetworking.post(AppConfig.GET_HEROINS)
                .addBodyParameter("heroin", "all")
                .setTag("heroins")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       Log.d(TAG, "onResponse: "+response);
                        try {
                            if(response.getString("error").equals("false")) {
                                JSONArray array = response.getJSONArray("heroins");
                                if(!local_time.equals("0")){
                                    AppController.getDb().deleteRecords(table_name);
                                }
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    String heroin_id = String.valueOf(object.get("heroin_id"));
                                    String heroin_name = String.valueOf(object.get("heroin_name"));
                                    if(AppController.getDb().insertHeroin(heroin_id,heroin_name)){

                                    }else {
                                        Log.d(TAG, "onResponse: error inserting heroins in local database");
                                    }


                                }
                                if(local_time.equals("0")){
                                    AppController.getDb().insertUpdate(table_name,remote_time);
                                }else {
                                    AppController.getDb().updateUpdateTable(table_name,remote_time);
                                }


                                Log.d(TAG, "onResponse: length " + array.length());

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to get heroins from database", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d(TAG, "oncallback: heroin called call back");
                        updateAll++;
                        callBackAfterNetworking();


                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading heroins from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }

    public void getLyricists(String table_name, String remote_time, String local_time){
        AndroidNetworking.post(AppConfig.GET_LYRICISTS)
                .addBodyParameter("lyricist", "all")
                .setTag("lyricists")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            if(response.getString("error").equals("false")) {
                                JSONArray array = response.getJSONArray("lyricists");
                                if(!local_time.equals("0")){
                                    AppController.getDb().deleteRecords(table_name);
                                }
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    String lyricist_id = String.valueOf(object.get("lyricist_id"));
                                    String lyricist_name = String.valueOf(object.get("lyricist_name"));
                                    if(AppController.getDb().insertLyricist(lyricist_id,lyricist_name)){

                                    }else {
                                        Log.d(TAG, "onResponse: error inserting lyricists in local database");
                                    }


                                }
                                if(local_time.equals("0")){
                                    AppController.getDb().insertUpdate(table_name,remote_time);
                                }else {
                                    AppController.getDb().updateUpdateTable(table_name,remote_time);
                                }

                                Log.d(TAG, "onResponse: length " + array.length());

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to get lyricists from database", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "oncallback: lyricist called call back");
                        updateAll++;
                        callBackAfterNetworking();

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading lyricists from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }

    public void getGenres(String table_name, String remote_time, String local_time){

        AndroidNetworking.post(AppConfig.GET_GENRES)
                .addBodyParameter("genre", "all")
                .setTag("genres")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean answer = false;
                       Log.d(TAG, "onResponse: "+response);
                        try {
                            if(response.getString("error").equals("false")) {
                                if(!local_time.equals("0")){
                                    AppController.getDb().deleteRecords(table_name);
                                }
                                JSONArray array = response.getJSONArray("genres");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    String genre_id = String.valueOf(object.get("genre_id"));
                                    String genre_name = String.valueOf(object.get("genre_name"));
                                    AppController.getDb().insertGenre(genre_id,genre_name);

                                }
                                    if(local_time.equals("0")){
                                        AppController.getDb().insertUpdate(table_name,remote_time);
                                    }else {
                                        AppController.getDb().updateUpdateTable(table_name,remote_time);
                                    }



                                Log.d(TAG, "onResponse: length " + array.length());

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to get genres from database", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "oncallback: genre called call back");
                        updateAll++;
                        callBackAfterNetworking();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading genre from the database", Toast.LENGTH_SHORT).show();


                        //isLoading = false;
                    }
                });


    }

    public void getFav(){

        AndroidNetworking.post(AppConfig.GET_FAV)
                .addBodyParameter("user_id", db.getUserDetails().get("id"))
                .setTag("fav")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean answer = false;
                        Log.d(TAG, "onResponse: fav "+response);
                        try {

                            boolean error = response.getBoolean("error");
                            if (!error) {
                                JSONArray array = response.getJSONArray("favs");
                                for (int a = 0; a < array.length(); a++) {
                                    JSONObject object = array.getJSONObject(a);
                                    int id = object.getInt("id");
                                    int user_id = object.getInt("user_id");
                                    int song_id = object.getInt("song_id");
                                    AppController.getDb().insertFavorites(id,user_id, song_id);
                                }
                                init();
                                handleNotificationsAndLinks();
                                hideDialog();
                            }



                            //hideDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: fav"+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error loading genre from the database", Toast.LENGTH_SHORT).show();


                        //isLoading = false;
                    }
                });


    }

    public void reloadFav(){
            favouritesFragment.reload();

       updatePlaylist();
    }

    public void removeFavImageFromAllSongs(){
        libraryFragment.updateAdapter();
    }


    @Override
    public void removeThis(int position,Songs s) {
            playlist.remove(position);
            for(Songs song : originalPlayList){
                if(song.getSong_id().equals(s.getSong_id())){
                    originalPlayList.remove(song);
                }
            }
            playlistadapter.notifyDataSetChanged();
            new StorageUtil(this).storeAudio(playlist);

    }



    @Override
    public void songCallBack(int position) {
            getLyrics(playlist.get(position));
            closeDrawer();
            Songs s = playlist.get(position);
            Log.d(TAG, "songCallBack: "+s.getAlbum_name()+" album id = "+s.getAlbum_id());
            playSong(playlist.get(position));

    }

    @Override
    public void notifyAdapter() {
            libraryFragment.updateAdapter();
            favouritesFragment.reload();
            updatePlaylist();


    }

    public void updatePlaylist(){
        if(playlist!=null){
            playlist.clear();
            if(new StorageUtil(this).loadAudio()!=null){
                playlist.addAll(new StorageUtil(this).loadAudio());
                playlistadapter.notifyDataSetChanged();
                Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
                sendBroadcast(setplaylist);
            }

        }

    }

    public void clearPlayList(View v){
        if(playlist!=null){
            playlist.clear();
            originalPlayList.clear();
            new StorageUtil(this).clearCachedAudioPlaylist();
            playlistadapter.notifyDataSetChanged();
        }
    }

    public void handleNotificationsAndLinks(){
        String songIdFromFirebase = "";

        Log.d(TAG, "handleIntent: called");
        if (getIntent().getStringExtra("id") != null&&getIntent().getStringExtra("des")!=null&&getIntent().getStringExtra("title")!=null) {
            Log.d(TAG, "onCreate: " + getIntent().getStringExtra("id"));
            songIdFromFirebase = getIntent().getStringExtra("id");
            des = getIntent().getStringExtra("des");
            titleFromfirebase = getIntent().getStringExtra("title");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle(titleFromfirebase);


            builder.setMessage(des);

            builder.setNegativeButton("Don't Play",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            return;
                        }
                    });
            String finalSongIdFromFirebase = songIdFromFirebase;
            builder.setPositiveButton("Play Now!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            handleSongRequest(finalSongIdFromFirebase);
                        }
                    });

            builder.show();
        }/*else if(getIntent().getStringExtra("album")!=null&&getIntent().getStringExtra("des")!=null){

            String albumName  = getIntent().getStringExtra("album");
            Log.d(TAG, "handleNotificationsAndLinks: "+albumName);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle(Helper.FirstLetterCaps(albumName));


            builder.setMessage(getIntent().getStringExtra("des")+" check out album list now");

            builder.setNegativeButton("Don't Play",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            return;
                        }
                    });
            String finalSongIdFromFirebase = songIdFromFirebase;
            builder.setPositiveButton("Play Songs!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            handleAlbumRequest(albumName);
                        }
                    });

            builder.show();



        }*/else {

            Log.d(TAG, "handleIntent: am inside else");
            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();
            if (appLinkData != null) {
                String songId = appLinkData.getQueryParameter("song");
                Log.d(TAG, "calls: song =  " + songId);
                if (songId == null) {
                    return;
                }
                handleSongRequest(songId);

            }else {
                //checkForUpdate();
            }
        }



    }

    public void handleSongRequest(String songId) {
        /*Log.d(TAG, "handleSongRequest: "+songId);
        songId = songId.replaceAll("%20", " ");
        songId = songId.toUpperCase();
        StorageUtil storageUtil = new StorageUtil(getApplicationContext());
        Songs song = AppController.getDb().getSongBySongTitle(songId);
        if (song == null) {
            Toast.makeText(player, songId + " is not found in our database", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storageUtil.loadAudio() == null || totalSongs > storageUtil.loadAudio().size()) {
            Log.d(TAG, "calls: its null");
            for (song songs : songList) {
                song s = new song(songs.getSong_id(), songs.getSong_title(), songs.getAlbum_id(), songs.getAlbum_name(), songs.getDownload_link(), songs.getLyricist(), songs.getTrack_no());
                playlist.add(s);
            }
            Log.d(TAG, "calls: playlist = " + playlist.size());
            int index = 0;
            for (song s : playlist) {
                if (s.getSong_title().equals(song.getSong_title()) && s.getAlbum_name().equals(song.getAlbum_name())) {
                    index = playlist.indexOf(s);
                    Log.d(TAG, "calls: " + s);
                }
            }
            storageUtil.storeAudio(playlist);
            storageUtil.storeAudioIndex(index);
            Log.d(TAG, "calls: storage = " + storageUtil.loadAudio().size() + " service bond = " + serviceBound);
            Intent setplaylist = new Intent(MainActivity.Broadcast_NEW_ALBUM);
            sendBroadcast(setplaylist);
            Intent broadcastIntent = new Intent(MainActivity.Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);


        } else {
            int index = 0;
            Log.i(TAG, "calls: " + song.getSong_title() + " " + song.getAlbum_name());
            ArrayList<song> array = new StorageUtil(getApplicationContext()).loadAudio();
            for (song s : array) {
                if (s.getSong_title().equals(song.getSong_title()) && s.getAlbum_name().equals(song.getAlbum_name())) {
                    Log.i(TAG, "calls: " + s.getSong_title() + " " + s.getAlbum_name());
                    index = array.indexOf(s);
                    Log.i(TAG, "ca;;s: " + s.getSong_title() + " " + s.getAlbum_name() + " " + index);
                }
            }
            storageUtil.storeAudioIndex(index);
            Intent broadcastIntent = new Intent(MainActivity.Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);

        }*/


        AndroidNetworking.post(AppConfig.GET_SONGS_BY_ID)
                .addBodyParameter("song_id", songId)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getSongs fav "+response);
                        Songs s = new Songs();
                        try {

                            JSONArray array = response.getJSONArray("songs");
                            for(int a = 0;a< array.length();a++){
                                JSONObject songobject = array.getJSONObject(a);

                                s.setAlbum_id(String.valueOf(songobject.get("album_id")));
                                s.setSong_id(String.valueOf(songobject.get("song_id")));
                                s.setSong_title(songobject.getString("song_title"));
                                s.setAlbum_name(AppController.getDb().getAlbumName(Integer.parseInt(songobject.getString("album_id"))));

                           }
                                                        //Log.d(TAG, "onResponse: size = "+allSongList.size());
                            playSong(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getMessage());
                        Toast.makeText(getApplicationContext(), "error loading songs from the database", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });
    }


    }





