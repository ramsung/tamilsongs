package in.beyonitysoftwares.besttamilsongs.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import in.beyonitysoftwares.besttamilsongs.Activities.MainActivity;
import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.customViews.CustomViewPager;
import in.beyonitysoftwares.besttamilsongs.pageAdapters.FragmentPageAdapter;
import info.hoang8f.android.segmented.SegmentedGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends Fragment {

    CustomViewPager viewPager;
    EnglishFragment englishFragment;
    TamilFragment tamilFragment;
    SegmentedGroup segmentedGroup;
    public LyricsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);
        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented);
        segmentedGroup.setTintColor(getResources().getColor(R.color.amber_900));
        segmentedGroup.check(R.id.tamil);
        englishFragment = new EnglishFragment();
        tamilFragment = new TamilFragment();

        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getFragmentManager());
        pageAdapter.addFragment(tamilFragment,"");
        pageAdapter.addFragment(englishFragment,"");

        viewPager = (CustomViewPager) view.findViewById(R.id.lyrcisVG);
        viewPager.setAdapter(pageAdapter);

        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.tamil:
                        setPageByNumber(0);
                        break;
                    case R.id.english:
                        setPageByNumber(1);
                        break;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                    setSelectedLyricPage(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    public void setLyrics(String l1,String l2, String l3,String l4){
        englishFragment.setLyrics(l1,l2);
        tamilFragment.setLyrics(l3,l4);
    }

    public void setPageByNumber(int num){
        viewPager.setCurrentItem(num);
    }
    public void setSelectedLyricPage(int position){

        if(position == 0){
            segmentedGroup.check(R.id.tamil);
        }else if(position ==1){
            segmentedGroup.check(R.id.english);
        }
    }
}
