package com.example.capstone_elibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {


    FirebaseFirestore fstore;
    EditText noteTitle,noteContent;
    ProgressBar progressBarsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fstore = FirebaseFirestore.getInstance();
        noteContent = findViewById(R.id.addnotecontent);
        noteTitle= findViewById(R.id.addNoteTitle);
        progressBarsave= findViewById(R.id.progressBarsave);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ntitle = noteTitle.getText().toString();
                String ncontent = noteContent.getText().toString();

                if (ntitle.isEmpty() || ncontent.isEmpty()){
                    Toast.makeText(AddNote.this, "Can not save note with empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBarsave.setVisibility(View.VISIBLE);
                //save note

                DocumentReference docref = fstore.collection("notes").document();
                Map<String,Object> note = new HashMap<>();
                note.put("title",ntitle);
                note.put("content",ncontent);

                docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNote.this, "Note added", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNote.this, "Error ----Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.close){
            Toast.makeText(AddNote.this, "Note Cancelled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
