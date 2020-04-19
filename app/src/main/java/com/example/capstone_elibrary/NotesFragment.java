package com.example.capstone_elibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesFragment extends Fragment {

    DrawerLayout drawerLayout;
    RecyclerView noteLists;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<NoteClass, NoteViewHolder> noteAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notes_frag, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        noteLists = (RecyclerView) getView().findViewById(R.id.notelist);
        fStore = FirebaseFirestore.getInstance();

        Query query = fStore.collection("notes").orderBy("title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteClass> allNotes = new FirestoreRecyclerOptions.Builder<NoteClass>()
                .setQuery( query,NoteClass.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<NoteClass, NoteViewHolder>(allNotes) {


            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, final int i, @NonNull final NoteClass note) {
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContent.setText(note.getContent());
                final int code = getRandomColor();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                noteViewHolder.mCardView.setCardBackgroundColor(noteViewHolder.view.getResources().getColor(code,null));
                final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
//                }
//                Toast.makeText(MainActivity.this, "..."  + new Note().getTitle(), Toast.LENGTH_SHORT).show();
                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), NoteDetails.class);
                        i.putExtra("title",note.getTitle());
                        i.putExtra("content",note.getContent());
                        i.putExtra("code",code);
                        i.putExtra("noteId",docId);
                        v.getContext().startActivity(i);
//                        Toast.makeText(v.getContext(), "The item is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                ImageView menuIcon = noteViewHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                        PopupMenu menu = new PopupMenu(v.getContext(),v);
                        menu.setGravity(Gravity.END);
                        menu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(getActivity(), "Edit clicked", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(v.getContext(), EditNote.class);
                                i.putExtra("title",note.getTitle());
                                i.putExtra("content",note.getContent());
                                i.putExtra("noteId",docId);
                                startActivity(i);
                                return false;
                            }
                        });

                        menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference docRef = fStore.collection("notes").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // note deleted
                                        Toast.makeText(getActivity(), "Note deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Error in Deleting Note.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return false;
                            }
                        });

                        menu.show();

                    }
                });


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
                return new NoteViewHolder(view );
            }
        };


        drawerLayout = (DrawerLayout)getView().findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.addNoteFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),AddNote.class));

            }
        });
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_options,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.addNote){
            startActivity(new Intent(getActivity().getApplicationContext(),AddNote.class));
            Toast.makeText(getActivity().getApplicationContext(), "AddNote Menu is Clicked.", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.sync){
            Toast.makeText(getActivity().getApplicationContext(), "sync Menu is Clicked.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public  class NoteViewHolder extends  RecyclerView.ViewHolder{

        TextView noteTitle,noteContent;
        View view;
        CardView mCardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView;
        }
    }

    private int getRandomColor() {

        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.red);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);

    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
}
