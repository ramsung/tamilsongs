package in.beyonitysoftwares.besttamilsongs.untils;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.beyonitysoftwares.besttamilsongs.models.Songs;

/**
 * Created by mohan on 7/16/17.
 */
public class StorageUtil {

	private final String STORAGE = "in.beyonitysoftwares.besttamilsong";
	private SharedPreferences preferences;
	private Context context;

	public StorageUtil(Context context) {
		this.context = context;
	}

	public void storeAudio(ArrayList<Songs> arrayList) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(arrayList);
		editor.putString("audioArrayList", json);
		editor.apply();
	}

	public ArrayList<Songs> loadAudio() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = preferences.getString("audioArrayList", null);
		Type type = new TypeToken<ArrayList<Songs>>() {
		}.getType();
		return gson.fromJson(json, type);
	}

	public void storeAudioIndex(int index) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("audioIndex", index);
		editor.apply();
	}

	public int loadAudioIndex() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getInt("audioIndex", -1);//return -1 if no data found
	}

	public void storeResumePosition(int position) {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("resume", position);
		editor.apply();
	}
	public int getResumePosition() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		return preferences.getInt("resume", -1);//return -1 if no data found
	}

	public void clearCachedAudioPlaylist() {
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}


}