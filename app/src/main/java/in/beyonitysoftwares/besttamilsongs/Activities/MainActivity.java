package in.beyonitysoftwares.besttamilsongs.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.customViews.SmoothProgressBar;
import in.beyonitysoftwares.besttamilsongs.fragments.AboutFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.FavouritesFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.LibraryFragment;
import in.beyonitysoftwares.besttamilsongs.fragments.LyricsFragment;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    //fragments
    AboutFragment aboutFragment;
    FavouritesFragment favouritesFragment;
    LibraryFragment libraryFragment;
    LyricsFragment lyricsFragment;
    CustomViewPager viewPager;
    public SmoothProgressBar loading;
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

        //init
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


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    public void setVisibleTrue(){
        loading.setVisibility(View.VISIBLE);
    }
    public void setVisibleFalse(){
        loading.setVisibility(View.INVISIBLE);
    }

}
