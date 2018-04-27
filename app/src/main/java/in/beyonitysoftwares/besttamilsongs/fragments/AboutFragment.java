package in.beyonitysoftwares.besttamilsongs.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import in.beyonitysoftwares.besttamilsongs.R;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppConfig;
import in.beyonitysoftwares.besttamilsongs.appConfig.AppController;
import in.beyonitysoftwares.besttamilsongs.models.Songs;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;
import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.getAppLink;
import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.getInstance;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText album,song;
    Button submit,rate,share,contact;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        View view =  inflater.inflate(R.layout.fragment_about, container, false);
        CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.userImageProfile);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView email = (TextView) view.findViewById(R.id.email);
        album = (EditText) view.findViewById(R.id.inputAlbum);
        song = (EditText) view.findViewById(R.id.inputSong);
        submit = (Button) view.findViewById(R.id.submit);
        rate = (Button) view.findViewById(R.id.app_rate);
        share = (Button) view.findViewById(R.id.app_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "http://play.google.com/store/apps/details?id=" + getContext().getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tamil Songs & Lyrics (Best Music App with Songs and lyrics)");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        contact = (Button) view.findViewById(R.id.app_telegram_channel);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://t.me/beyonity";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(album.getText())&&!TextUtils.isEmpty(song.getText())){
                        request(album.getText().toString().trim(),song.getText().toString().trim());
                }else if(!TextUtils.isEmpty(album.getText())){
                    requestAlbum(album.getText().toString().trim());
                }else if(!TextUtils.isEmpty(song.getText())){
                    requestSong(song.getText().toString().trim());
                }else {
                    Log.d(TAG, "onClick: on else");

                    Toast.makeText(getContext(),"Request Cannot be Empty, need atleast album or song",Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Glide.with(getContext()).load(user.getPhotoUrl()).into(profileImage);
           //profileImage.setImageURI(user.getPhotoUrl());
           name.setText(user.getDisplayName());
           email.setText(user.getEmail());
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void requestAlbum(String album) {
        Log.d(TAG, "requestAlbum: user id = "+AppController.getSignDb().getUserDetails().get("id")+" album = "+album);
        AndroidNetworking.post(AppConfig.PUT_REQUEST_ALBUM)
                .addBodyParameter("user_id", AppController.getSignDb().getUserDetails().get("id"))
                .addBodyParameter("album",album)
                .setTag("request")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);

                        try {
                            if(!response.getBoolean("error")){
                                Toast.makeText(getContext(),"Request Successfully Received",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getActivity().getApplicationContext(), "error occured sorry of inconvenience", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });


    }

    private void request(String album,String song) {
        Log.d(TAG, "requestAlbum: user id = "+AppController.getSignDb().getUserDetails().get("id")+" album = "+album+" song = "+song);
        AndroidNetworking.post(AppConfig.PUT_REQUEST)
                .addBodyParameter("user_id", AppController.getSignDb().getUserDetails().get("id"))
                .addBodyParameter("album",album)
                .addBodyParameter("song",song)
                .setTag("request")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);

                        try {
                            if(!response.getBoolean("error")){
                                Toast.makeText(getContext(),"Request Successfully Received",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getActivity().getApplicationContext(), "error occured sorry of inconvenience", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });


    }

    private void requestSong(String song) {
        Log.d(TAG, "requestAlbum: user id = "+AppController.getSignDb().getUserDetails().get("id")+" song = "+song);
        AndroidNetworking.post(AppConfig.PUT_REQUEST_SONG)
                .addBodyParameter("user_id", AppController.getSignDb().getUserDetails().get("id"))
                .addBodyParameter("song",song)
                .setTag("request")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            if(!response.getBoolean("error")){
                                Toast.makeText(getContext(),"Request Successfully Received",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getActivity().getApplicationContext(), "error occured sorry of inconvenience", Toast.LENGTH_SHORT).show();

                        //isLoading = false;
                    }
                });


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
