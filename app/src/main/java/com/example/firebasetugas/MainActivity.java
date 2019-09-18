package com.example.firebasetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String NAMA_ARTIS = "namaartis";
    public static final String ID_ARTIS = "idartis";

    EditText nama;
    Button kirim;
    Spinner genre;

    DatabaseReference databaseReference;
    ListView listViewArtist;
    List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("artist");

        nama = findViewById(R.id.etxNama);
        kirim = findViewById(R.id.btnKirim);
        genre = findViewById(R.id.spnGenre);
        listViewArtist = findViewById(R.id.listView);

        artistList = new ArrayList<>();

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);

                Intent intent = new Intent(getApplicationContext(), AddTract.class);

                intent.putExtra(ID_ARTIS, artist.getIdArtist());
                intent.putExtra(NAMA_ARTIS, artist.getNamaArtist());

                startActivity(intent);
            }
        });

        listViewArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);

                showUpdateDialog(artist.getIdArtist(), artist.getNamaArtist());
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                artistList.clear();

                for (DataSnapshot artisSnap : dataSnapshot.getChildren()){
                    Artist artist = artisSnap.getValue(Artist.class);
                    artistList.add(artist);
                }

                ListArtist adapter = new ListArtist(MainActivity.this, artistList);
                listViewArtist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addArtist(){
        String name = nama.getText().toString().trim();
        String genre2 = genre.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)){
            String id = databaseReference.push().getKey();

            Artist artist = new Artist(id,name,genre2);
            
            databaseReference.child(id).setValue(artist);

            nama.setText("");

            Toast.makeText(this, "Artis Ditambahkan", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Nama harus diisi !", Toast.LENGTH_LONG).show();
        }
    }

    private void showUpdateDialog(final String idArtist, String namaArtist){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(dialogView);

        final EditText editNama = dialogView.findViewById(R.id.etxUpdate);
        final Button update = dialogView.findViewById(R.id.btnUpdate);
        final Spinner editGenre = dialogView.findViewById(R.id.spnUpdate);
        final Button delete = dialogView.findViewById(R.id.btnDelete);

        builder.setTitle("Ubah Artis " +namaArtist);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaUpdate = editNama.getText().toString().trim();
                String genreUpdate = editGenre.getSelectedItem().toString();

                if (TextUtils.isEmpty(namaUpdate)){
                    editNama.setError("Nama perlu diisi");
                    return;
                }
                updateArtist(idArtist, namaUpdate, genreUpdate);
                alertDialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(idArtist);
            }
        });
    }

    private boolean updateArtist(String id, String nama, String genre){
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("artist").child(id);
        Artist artist = new Artist(id, nama, genre);
        databaseReference1.setValue(artist);
        Toast.makeText(this, "Berhasil mengubah", Toast.LENGTH_LONG).show();
        return true;
    }


    private void deleteArtist(String artistId){
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("artist").child(artistId);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("tracks").child(artistId);

        databaseReference2.removeValue();
        databaseReference3.removeValue();

        Toast.makeText(this, "Artis ini telah dihapus", Toast.LENGTH_LONG).show();
    }
}
