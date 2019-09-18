package com.example.firebasetugas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListTrack extends ArrayAdapter<Track> {

    private Activity activity;
    private List<Track> trackList;

    public ListTrack(Activity activity, List<Track> trackList){
        super(activity, R.layout.list_track_layout, trackList);
        this.activity = activity;
        this.trackList = trackList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_artist_layout, null, true);
        TextView textViewName = listViewItem.findViewById(R.id.txtRVNama);
        TextView textViewRating = listViewItem.findViewById(R.id.txtRVGenre);

        Track track = trackList.get(position);

        textViewName.setText(track.getTrackName());
        textViewRating.setText(String.valueOf(track.getTrackRating()));

        return listViewItem;
    }
}
