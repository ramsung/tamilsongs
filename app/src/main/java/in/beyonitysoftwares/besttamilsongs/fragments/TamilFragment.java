package in.beyonitysoftwares.besttamilsongs.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;

import java.util.Map;

import in.beyonitysoftwares.besttamilsongs.R;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TamilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TamilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TamilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tamilone,tamiltwo;
    AdView adViewtop,adViewMiddle;
    private OnFragmentInteractionListener mListener;

    public TamilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TamilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TamilFragment newInstance(String param1, String param2) {
        TamilFragment fragment = new TamilFragment();
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
        View view = inflater.inflate(R.layout.activity_lyrics, container, false);

        tamilone = (TextView) view.findViewById(R.id.tamilone);
        tamiltwo = (TextView) view.findViewById(R.id.tamiltwo);
        //adViewtop = (AdView) view.findViewById(R.id.tamil_top);
        adViewMiddle= (AdView) view.findViewById(R.id.tamil_middle);

        InMobiBanner bannerAd = (InMobiBanner) view.findViewById(R.id.banner);
        bannerAd.load();
        bannerAd.setListener(new InMobiBanner.BannerAdListener() {
            @Override
            public void onAdLoadSucceeded(InMobiBanner inMobiBanner) {
                Log.d(TAG, "onAdLoadSucceeded: loaded successfully");
            }

            @Override
            public void onAdLoadFailed(InMobiBanner inMobiBanner, InMobiAdRequestStatus inMobiAdRequestStatus) {
                Log.d(TAG, "onAdLoadFailed: "+inMobiAdRequestStatus);
            }

            @Override
            public void onAdDisplayed(InMobiBanner inMobiBanner) {

            }

            @Override
            public void onAdDismissed(InMobiBanner inMobiBanner) {

            }

            @Override
            public void onAdInteraction(InMobiBanner inMobiBanner, Map<Object, Object> map) {

            }

            @Override
            public void onUserLeftApplication(InMobiBanner inMobiBanner) {

            }

            @Override
            public void onAdRewardActionCompleted(InMobiBanner inMobiBanner, Map<Object, Object> map) {

            }
        });
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice("45AEA33662E36BBB9B11FE55E4EFA874")
                .build();
        /*adViewtop.loadAd(adRequest1);
        adViewtop.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });*/
        AdRequest adRequest2 = new AdRequest.Builder()
                 .addTestDevice("45AEA33662E36BBB9B11FE55E4EFA874")
                 .build();
        adViewMiddle.loadAd(adRequest2);
        adViewMiddle.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });
        return view;
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

    public void setLyrics(String l1,String l2){
        tamilone.setText(l1);
        tamiltwo.setText(l2);

    }


}
