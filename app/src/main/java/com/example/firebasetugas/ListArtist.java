package com.example.firebasetugas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListArtist extends ArrayAdapter<Artist> {

    private Activity activity;
    private List<Artist> artistList;

    public ListArtist(Activity activity, List<Artist> artistList){
        super(activity, R.layout.list_artist_layout, artistList);
        this.activity = activity;
        this.artistList = artistList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_artist_layout, null, true);
        TextView textViewName = listViewItem.findViewById(R.id.txtRVNama);
        TextView textViewGenre = listViewItem.findViewById(R.id.txtRVGenre);

        Artist artist = artistList.get(position);

        textViewName.setText(artist.getNamaArtist());
        textViewGenre.setText(artist.getGenreArtist());

        return listViewItem;
    }
}
