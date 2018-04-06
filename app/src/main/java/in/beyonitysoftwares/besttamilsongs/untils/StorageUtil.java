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

	public void setArtistFilter(String artist){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
 		editor.putString("artistFilter", artist);
		editor.apply();
	}
	public String getArtistFilter(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String answer = preferences.getString("artistFilter", "All Artist");
		return answer;
	}
	public void setAlbumilter(String album){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
 		editor.putString("albumFilter", album);
		editor.apply();
	}
	public String getAlbumFilter(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String answer = preferences.getString("albumFilter", "All Albums");
		return answer;
	}

	public void setGenreFilter(String genre){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("genreFilter", genre);
		editor.apply();
	}
	public String getGenreFilter(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String answer = preferences.getString("genreFilter", "All Genre");
		return answer;
	}

	public void setHeroFilter(String hero){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("heroFilter", hero);
		editor.apply();
	}
	public String getHeroFilter(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String answer = preferences.getString("heroFilter", "All Heros");
		return answer;
	}

	public void setHeroinFilter(String heroin){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("heroinFilter", heroin);
		editor.apply();
	}
	public String getHeroinFilter(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String answer = preferences.getString("heroinFilter", "All Heroins");
		return answer;
	}

	public void setHYearFilter(String year){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("yearFilter", year);
		editor.apply();
	}
	public String getYearFilter(){
		preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
		String answer = preferences.getString("yearFilter", "All Years");
		return answer;
	}


}