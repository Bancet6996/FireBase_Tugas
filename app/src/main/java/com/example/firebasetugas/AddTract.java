package com.example.firebasetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTract extends AppCompatActivity {

    TextView txtNamaTrack;
    EditText etxNamaTrack;
    SeekBar seekBar;
    Button btnTambahTrack;

    ListView listViewTrack;
    DatabaseReference databaseReference;
    List<Track> trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tract);

        txtNamaTrack = findViewById(R.id.txtViewNameArtistTrack);
        etxNamaTrack = findViewById(R.id.etxNamaTrack);
        seekBar = findViewById(R.id.seekbarRating);
        listViewTrack = findViewById(R.id.listViewTrack);
        btnTambahTrack = findViewById(R.id.btnTambah);

        Intent intent = getIntent();
        trackList = new ArrayList<>();

        String id = intent.getStringExtra(MainActivity.ID_ARTIS);
        String nama = intent.getStringExtra(MainActivity.NAMA_ARTIS);

        txtNamaTrack.setText(nama);

        databaseReference = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        btnTambahTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackList.clear();

                for (DataSnapshot trackSnap : dataSnapshot.getChildren()){
                    Track track = trackSnap.getValue(Track.class);
                    trackList.add(track);
                }

                ListTrack listTrack = new ListTrack(AddTract.this, trackList);
                listViewTrack.setAdapter(listTrack);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveTrack(){
        String trackName = etxNamaTrack.getText().toString().trim();
        int rating = seekBar.getProgress();
        if (!TextUtils.isEmpty(trackName)){
            String id = databaseReference.push().getKey();

            Track track = new Track(id, trackName, rating);
            databaseReference.child(id).setValue(track);

            Toast.makeText(this, "Trek berhasil ditambahkan", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Trek tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
    }
}
