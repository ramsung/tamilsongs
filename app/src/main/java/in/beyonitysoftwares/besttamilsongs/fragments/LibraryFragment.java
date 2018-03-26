package in.beyonitysoftwares.besttamilsongs.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    AllSongsFragment allSongsFragment;
    ArtistFragment artistFragment;

    CustomViewPager viewPager;

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        allSongsFragment = new AllSongsFragment();
        artistFragment = new ArtistFragment();

        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getFragmentManager());
        pageAdapter.addFragment(allSongsFragment,"");
        pageAdapter.addFragment(artistFragment,"");;


        viewPager = (CustomViewPager) view.findViewById(R.id.librayVG);

        viewPager.setAdapter(pageAdapter);

        //TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager,true);


        return view;
    }

}
