package in.beyonitysoftwares.besttamilsongs.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends Fragment {

    CustomViewPager viewPager;
    EnglishFragment englishFragment;
    TamilFragment tamilFragment;

    public LyricsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        englishFragment = new EnglishFragment();
        tamilFragment = new TamilFragment();

        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getFragmentManager());
        pageAdapter.addFragment(tamilFragment,"");
        pageAdapter.addFragment(englishFragment,"");

        viewPager = (CustomViewPager) view.findViewById(R.id.lyrcisVG);
        viewPager.setAdapter(pageAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

        return view;
    }

}
