package in.beyonitysoftwares.besttamilsongs.models;

public class FilteredAlbum {

    String album_id;
    String album_name;
    String artist_id;
    String artist_name;
    String hero_id;
    String hero_name;
    String heroin_id;
    String heroin_name;
    String year;

    public FilteredAlbum(String album_id, String album_name, String artist_id, String artist_name, String hero_id, String hero_name, String heroin_id, String heroin_name, String year) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.artist_id = artist_id;
        this.artist_name = artist_name;
        this.hero_id = hero_id;
        this.hero_name = hero_name;
        this.heroin_id = heroin_id;
        this.heroin_name = heroin_name;
        this.year = year;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getHero_id() {
        return hero_id;
    }

    public void setHero_id(String hero_id) {
        this.hero_id = hero_id;
    }

    public String getHero_name() {
        return hero_name;
    }

    public void setHero_name(String hero_name) {
        this.hero_name = hero_name;
    }

    public String getHeroin_id() {
        return heroin_id;
    }

    public void setHeroin_id(String heroin_id) {
        this.heroin_id = heroin_id;
    }

    public String getHeroin_name() {
        return heroin_name;
    }

    public void setHeroin_name(String heroin_name) {
        this.heroin_name = heroin_name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
