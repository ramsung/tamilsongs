package in.beyonitysoftwares.besttamilsongs.models;

/**
 * Created by mohan on 16/3/18.
 */

public class Songs {
    String song_id;
    String album_id;
    String song_title;
    String album_name;
    String download_link;
    String genre_name;
    String lyricist_name;
    String track_no;

    public String getTrack_no() {
        return track_no;
    }

    public void setTrack_no(String track_no) {
        this.track_no = track_no;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public String getLyricist_name() {
        return lyricist_name;
    }

    public void setLyricist_name(String lyricist_name) {
        this.lyricist_name = lyricist_name;
    }

    public Songs(){

   }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }
}
