package com.example.firebasetugas;

public class Artist {
    String idArtist, namaArtist, genreArtist;

    public Artist(){

    }

    public Artist(String idArtist, String namaArtist, String genreArtist){
        this.idArtist = idArtist;
        this.namaArtist = namaArtist;
        this.genreArtist = genreArtist;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(String idArtist) {
        this.idArtist = idArtist;
    }

    public String getNamaArtist() {
        return namaArtist;
    }

    public void setNamaArtist(String namaArtist) {
        this.namaArtist = namaArtist;
    }

    public String getGenreArtist() {
        return genreArtist;
    }

    public void setGenreArtist(String genreArtist) {
        this.genreArtist = genreArtist;
    }
}
