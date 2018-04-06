package in.beyonitysoftwares.besttamilsongs.databaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



import java.util.ArrayList;
import java.util.logging.Filter;

import in.beyonitysoftwares.besttamilsongs.models.FilteredAlbum;

import static in.beyonitysoftwares.besttamilsongs.appConfig.AppController.TAG;

/**
 * Created by mohan on 4/2/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "beyonity_tamillyrics";
    private static final String TABLE_ARTIST = "artist";
    private static final String TABLE_ALBUMS = "albums";
    private static final String TABLE_HERO = "hero";
    private static final String TABLE_HEROIN = "heroin";
    private static final String TABLE_LYRICIST = "lyricist";
    private static final String TABLE_GENRE = "genre";
    private static final String TABLE_FAVORITE = "favorite";
    private static final String TABLE_UPDATE = "updatedetails";


    //album
    private static final String KEY_ALBUM_ID = "album_id";
    private static final String KEY_ALBUM_NAME = "album_name";
    private static final String KEY_ALBUM_YEAR = "album_year";



    //artist
    private static final String KEY_ARTIST_ID = "artist_id";
    private static final String KEY_ARTIST_NAME = "artist_name";


    //hero
    private static final String KEY_HERO_ID = "hero_id";
    private static final String KEY_HERO_NAME = "hero_name";

    //heroin
    private static final String KEY_HEROIN_ID = "heroin_id";
    private static final String KEY_HEROIN_NAME = "heroin_name";

    //lyricist
    private static final String KEY_LYRICIST_ID = "lyricist_id";
    private static final String KEY_LYRICIST_NAME = "lyricist_name";

    //genre
    private static final String KEY_GENRE_ID = "genre_id";
    private static final String KEY_GENRE_NAME = "genre_name";


    //favorites
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FAVORITE_ID = "favorite_id";
    private static final String KEY_SONG_ID = "song_id";


    //update
    private static final String KEY_TABLE_NAME = "table_name";
    private static final String KEY_UPDATE_TIME = "timestamp";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALBUMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALBUMS + " (" + KEY_ALBUM_ID + " INTEGER UNIQUE, " + KEY_ALBUM_NAME + " varchar(255), " + KEY_ARTIST_ID +" INTEGER, "+KEY_HERO_ID+ " INTEGER, " +KEY_HEROIN_ID+ " INTEGER, " +  KEY_ALBUM_YEAR + " INTERGER)";
        String CREATE_ARTIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ARTIST + " (" + KEY_ARTIST_ID + " INTEGER UNIQUE, " + KEY_ARTIST_NAME + " varchar(255))";
        String CREATE_HERO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HERO + " (" + KEY_HERO_ID + " INTEGER UNIQUE, " + KEY_HERO_NAME + " varchar(255))";
        String CREATE_HEROIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HEROIN + " (" + KEY_HEROIN_ID + " INTEGER UNIQUE, " + KEY_HEROIN_NAME + " varchar(255))";
        String CREATE_LYRICIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LYRICIST + " (" + KEY_LYRICIST_ID + " INTEGER UNIQUE, " + KEY_LYRICIST_NAME + " varchar(255))";
        String CREATE_GENRE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GENRE + " (" + KEY_GENRE_ID + " INTEGER UNIQUE, " + KEY_GENRE_NAME + " varchar(255))";
        String CREATE_FAVORITE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_FAVORITE +" (" +KEY_FAVORITE_ID+ " INTEGER  UNIQUE, "+KEY_USER_ID+ " INTEGER, "+KEY_SONG_ID + " INTEGER)";
        String CREATE_UPDATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_UPDATE +" (" +KEY_TABLE_NAME+ " varchar(50) UNIQUE, "+KEY_UPDATE_TIME+ " DATETIME)";

        db.execSQL(CREATE_ALBUMS_TABLE);
        db.execSQL(CREATE_ARTIST_TABLE);
        db.execSQL(CREATE_HERO_TABLE);
        db.execSQL(CREATE_HEROIN_TABLE);
        db.execSQL(CREATE_LYRICIST_TABLE);
        db.execSQL(CREATE_GENRE_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_UPDATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insertAlbums(String album_id, String album_name, String artist_id,String hero_id,String heroin_id,String year) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ALBUM_ID, album_id);
        contentValues.put(KEY_ALBUM_NAME, album_name);
        contentValues.put(KEY_ARTIST_ID, artist_id);
        contentValues.put(KEY_HERO_ID, hero_id);
        contentValues.put(KEY_HEROIN_ID, heroin_id);
        contentValues.put(KEY_ALBUM_YEAR, year);


        db.insert(TABLE_ALBUMS, null, contentValues);
        return true;


    }

    public ArrayList<Integer> getAlbumIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        String st = "SELECT album_id FROM albums";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(st, null);
        while (c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndex("album_id")));
        }
        if(c != null){
            c.close();
        }
        return ids;
    }



    public String getAlbumName(int album_id) {
        String albumName = "";
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT album_name FROM " + TABLE_ALBUMS + " WHERE album_id = '" + album_id + "'";
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                albumName = c.getString(c.getColumnIndex(KEY_ALBUM_NAME));
            }
        }
        if(c!=null){
            c.close();
        }

        return albumName;
    }



    public int getYearByAlbumId(int album_id){
        int year = 0;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT "+KEY_ALBUM_YEAR+" FROM " + TABLE_ALBUMS +" WHERE album_id  = '"+album_id+"'";
        Cursor c = db.rawQuery(selectQuery,null);
        if(c != null){
            while (c.moveToNext()){
                year = c.getInt(c.getColumnIndex(KEY_ALBUM_YEAR));
            }
        }

        c.close();
        return year;

    }

    public boolean insertArtist(String artist_id, String artist_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ARTIST_ID, artist_id);
        contentValues.put(KEY_ARTIST_NAME, artist_name);

        db.insert(TABLE_ARTIST, null, contentValues);
        return true;


    }


    public boolean insertHero(String hero_id, String hero_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HERO_ID, hero_id);
        contentValues.put(KEY_HERO_NAME, hero_name);

        db.insert(TABLE_HERO, null, contentValues);
        return true;


    }

    public boolean insertHeroin(String heroin_id, String heroin_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HEROIN_ID, heroin_id);
        contentValues.put(KEY_HEROIN_NAME, heroin_name);

        db.insert(TABLE_HEROIN, null, contentValues);
        return true;


    }

    public boolean insertLyricist(String lyricist_id, String lyricist_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LYRICIST_ID, lyricist_id);
        contentValues.put(KEY_LYRICIST_NAME, lyricist_name);

        db.insert(TABLE_LYRICIST, null, contentValues);
        return true;


    }

    public boolean insertGenre(String genre_id, String genre_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GENRE_ID, genre_id);
        contentValues.put(KEY_GENRE_NAME, genre_name);

        db.insert(TABLE_GENRE, null, contentValues);
        return true;


    }

    public boolean insertUpdate(String table_name, String timestamp) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TABLE_NAME, table_name);
        contentValues.put(KEY_UPDATE_TIME, timestamp);

        db.insert(TABLE_UPDATE, null, contentValues);
        return true;


    }

    public String getUpdateDetails(String table_name) {

        String timestamp = "0";
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT "+KEY_UPDATE_TIME+" FROM " + TABLE_UPDATE + " WHERE "+KEY_TABLE_NAME+" = '" + table_name + "'";
        //Log.d(TAG, "getUpdateDetails: "+st);
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                timestamp = c.getString(c.getColumnIndex(KEY_UPDATE_TIME));
            }
        }
        if(c!=null){
            c.close();
        }

        return timestamp;


    }
    public boolean updateUpdateTable(String table_name, String timestamp) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UPDATE_TIME, timestamp);

        db.update(TABLE_UPDATE, contentValues,KEY_TABLE_NAME+" = ?",new String[]{table_name});
        return true;


    }

    public void deleteRecords(String table_name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ table_name);
        db.close();
    }

    public ArrayList<String> getAlbumNames(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT album_name FROM " + TABLE_ALBUMS;
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                String albumName = c.getString(c.getColumnIndex(KEY_ALBUM_NAME));
                list.add(albumName);
            }
        }
        if(c!=null){
            c.close();
        }

        return list;
    }

    public String getArtistName(int artist_id) {
        String artistName = "";
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT artist_name FROM " + TABLE_ARTIST+ " WHERE artist_id = '" + artist_id + "'";
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                artistName = c.getString(c.getColumnIndex(KEY_ARTIST_NAME));
            }
        }
        if(c!=null){
            c.close();
        }

        return artistName;
    }
    public String getHeroName(int hero_id) {
        String heroName = "";
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT hero_name FROM " + TABLE_HERO+ " WHERE hero_id = '" + hero_id + "'";
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                heroName = c.getString(c.getColumnIndex(KEY_HERO_NAME));
            }
        }
        if(c!=null){
            c.close();
        }

        return heroName;
    }

    public String getHeroinName(int heroin_id) {
        String heroinName = "";
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT heroin_name FROM " + TABLE_HEROIN+ " WHERE heroin_id = '" + heroin_id + "'";
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                heroinName = c.getString(c.getColumnIndex(KEY_HEROIN_NAME));
            }
        }
        if(c!=null){
            c.close();
        }

        return heroinName;
    }
    public String getGenreName(int genre_id) {
        String genreName = "";
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT genre_name FROM " + TABLE_GENRE+ " WHERE genre_id = '" + genre_id + "'";
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                genreName = c.getString(c.getColumnIndex(KEY_GENRE_NAME));
            }
        }
        if(c!=null){
            c.close();
        }

        return genreName;
    }
    public ArrayList<String> getArtistNames(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT artist_name FROM " + TABLE_ARTIST;
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                String artistname= c.getString(c.getColumnIndex(KEY_ARTIST_NAME));
                list.add(artistname);
            }
        }
        if(c!=null){
            c.close();
        }

        return list;
    }

    public ArrayList<String> getHeroNames(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT hero_name FROM " + TABLE_HERO;
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                String name= c.getString(c.getColumnIndex(KEY_HERO_NAME));
                list.add(name);
            }
        }
        if(c!=null){
            c.close();
        }

        return list;
    }

    public ArrayList<String> getHeroinNames(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT heroin_name FROM " + TABLE_HEROIN;
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                String name= c.getString(c.getColumnIndex(KEY_HEROIN_NAME));
                list.add(name);
            }
        }
        if(c!=null){
            c.close();
        }

        return list;
    }
    public ArrayList<String> getGnereNames(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT genre_name FROM " + TABLE_GENRE;
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                String name= c.getString(c.getColumnIndex(KEY_GENRE_NAME));
                list.add(name);
            }
        }
        if(c!=null){
            c.close();
        }

        return list;
    }

    public ArrayList<String> getAllYears(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT album_year FROM " + TABLE_ALBUMS;
        Cursor c = db.rawQuery(st, null);
        if (c != null) {
            while (c.moveToNext()) {
                String year= c.getString(c.getColumnIndex(KEY_ALBUM_YEAR));
                list.add(year);
            }
        }
        if(c!=null){
            c.close();
        }

        return list;
    }

    public ArrayList<FilteredAlbum> getAlbumsByFilter(String artist,String hero,String heroin,String year){
        ArrayList<FilteredAlbum> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if(artist.equals("All Artist")) {
            String st = "SELECT album_id,album_name,artist_id,hero_id,heroin_id,album_year from " + TABLE_ALBUMS;
            Cursor c = db.rawQuery(st, null);
            if (c != null) {
                while (c.moveToNext()) {
                    String album_id = c.getString(c.getColumnIndex(KEY_ALBUM_ID));
                    String album_name = c.getString(c.getColumnIndex(KEY_ALBUM_NAME));
                    String artist_id = c.getString(c.getColumnIndex(KEY_ARTIST_ID));
                    String artist_name = getArtistName(Integer.parseInt(artist_id));
                    String hero_id = c.getString(c.getColumnIndex(KEY_HERO_ID));
                    String hero_name = getHeroName(Integer.parseInt(hero_id));
                    String heroin_id = c.getString(c.getColumnIndex(KEY_HEROIN_ID));
                    String heroin_name = getHeroinName(Integer.parseInt(heroin_id));
                    String album_year = c.getString(c.getColumnIndex(KEY_ALBUM_YEAR));
                    FilteredAlbum album = new FilteredAlbum(
                            album_id,
                            album_name,
                            artist_id,
                            artist_name,
                            hero_id,
                            hero_name,
                            heroin_id,
                            heroin_name,
                            album_year
                    );
                    list.add(album);


                }
            }
            if(c!=null){
                c.close();
            }

            if(hero.equals("All Heros")){
                ArrayList<FilteredAlbum> listheros = list;
                if(heroin.equals("All Heroins")){
                    ArrayList<FilteredAlbum> listheroins = listheros;
                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }

                }else {
                    ArrayList<FilteredAlbum> listheroins = new ArrayList<>();
                    for(FilteredAlbum album:list){
                        if(album.getHeroin_name().equals(heroin)){
                            listheroins.add(album);
                        }
                    }

                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }


                }
            }else {
                ArrayList<FilteredAlbum> listheros = new ArrayList<>();
                for(FilteredAlbum album:list){
                    if(album.getHero_name().equals(hero)){
                        listheros.add(album);
                    }
                }

                if(heroin.equals("All Heroins")){
                    ArrayList<FilteredAlbum> listheroins = listheros;
                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }

                }else {
                    ArrayList<FilteredAlbum> listheroins = new ArrayList<>();
                    for(FilteredAlbum album:list){
                        if(album.getHeroin_name().equals(heroin)){
                            listheroins.add(album);
                        }
                    }

                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }

                }

            }

        }else{
            ArrayList<FilteredAlbum> listArtist = new ArrayList<>();
           String artist_id = getArtistId(artist);
           String st = "SELECT album_id,album_name,hero_id,heroin_id,album_year from "+TABLE_ALBUMS+" WHERE "+KEY_ARTIST_ID+" = '"+artist_id+"'";
           Cursor c = db.rawQuery(st,null);
            if (c != null) {
                while (c.moveToNext()) {
                    String album_id = c.getString(c.getColumnIndex(KEY_ALBUM_ID));
                    String album_name = c.getString(c.getColumnIndex(KEY_ALBUM_NAME));
                    String artist_name = artist;
                    String hero_id = c.getString(c.getColumnIndex(KEY_HERO_ID));
                    String hero_name = getHeroName(Integer.parseInt(hero_id));
                    String heroin_id = c.getString(c.getColumnIndex(KEY_HEROIN_ID));
                    String heroin_name = getHeroinName(Integer.parseInt(heroin_id));
                    String album_year = c.getString(c.getColumnIndex(KEY_ALBUM_YEAR));
                    FilteredAlbum album = new FilteredAlbum(
                            album_id,
                            album_name,
                            artist_id,
                            artist_name,
                            hero_id,
                            hero_name,
                            heroin_id,
                            heroin_name,
                            album_year
                    );
                    listArtist.add(album);
                }
            }
            if(c!=null){
                c.close();
            }

            if(hero.equals("All Heros")){
                ArrayList<FilteredAlbum> listheros = listArtist;
                if(heroin.equals("All Heroins")){
                    ArrayList<FilteredAlbum> listheroins = listheros;
                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }

                }else {
                    ArrayList<FilteredAlbum> listheroins = new ArrayList<>();
                    for(FilteredAlbum album:list){
                        if(album.getHeroin_name().equals(heroin)){
                            listheroins.add(album);
                        }
                    }

                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }


                }
            }else {
                ArrayList<FilteredAlbum> listheros = new ArrayList<>();
                for(FilteredAlbum album:list){
                    if(album.getHero_name().equals(hero)){
                        listheros.add(album);
                    }
                }

                if(heroin.equals("All Heroins")){
                    ArrayList<FilteredAlbum> listheroins = listheros;
                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }

                }else {
                    ArrayList<FilteredAlbum> listheroins = new ArrayList<>();
                    for(FilteredAlbum album:list){
                        if(album.getHeroin_name().equals(heroin)){
                            listheroins.add(album);
                        }
                    }

                    if(year.equals("All Years")){
                        list = listheroins;
                    }else {
                        ArrayList<FilteredAlbum> listyears = new ArrayList<>();
                        for(FilteredAlbum album:list){
                            if(album.getYear().equals(year)){
                                listyears.add(album);
                            }
                        }
                        list = listyears;
                    }

                }

            }

        }

        return list;
    }

    public String getArtistId(String artist_name){
        SQLiteDatabase db = getReadableDatabase();
        String id = "";
        String st = "SELECT artist_id from "+TABLE_ARTIST+" WHERE "+KEY_ARTIST_NAME+" = '"+artist_name+"'";
        Cursor c = db.rawQuery(st,null);
        if(c!=null){
            while (c.moveToNext()){
                id = c.getString(c.getColumnIndex(KEY_ARTIST_ID));
            }
        }
        return id;
    }
}