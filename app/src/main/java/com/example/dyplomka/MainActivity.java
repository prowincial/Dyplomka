package com.example.dyplomka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnNoteListener {

    RecyclerView recyclerView;
//    private DatabaseReference myRef;
    private ArrayList<Leagues> leaguesList;
    private RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //navigation bar
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.league_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        myRef = FirebaseDatabase.getInstance().getReference();


//        leaguesList = new ArrayList<>();
//        leaguesList.add(new Leagues("EPL", "https://img.lovepik.com/element/40018/1989.png_860.png"));
//        leaguesList.add(new Leagues("La Liga", "https://img.lovepik.com/element/40018/1989.png_860.png"));
//        leaguesList.add(new Leagues("Bundesliga", "https://img.lovepik.com/element/40018/1989.png_860.png"));
//        getDataFromFirebase();
        getLeaguesData();


    }


    private void getLeaguesData(){
        leaguesList = new ArrayList<>();
        FirebaseFirestore firFir = FirebaseFirestore.getInstance();
        firFir.collection("leagues")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            clearAll();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                leaguesList.add(new Leagues (document.getString("title"), document.getString("url")));
                                Log.w("Document",document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("Document", "Error getting documents.", task.getException());
                        }
                        init();
                    }
                });
    }

    public void init(){
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), leaguesList, this );
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }


    /*private void getDataFromFirebase(){

        Query query = myRef.child("leagues");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Leagues league = new Leagues();

                    league.setName((snapshot.child("title").getValue()).toString());
                    league.setImageUrl((snapshot.child("url").getValue()).toString());

                    leaguesList.add(league);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
    private void clearAll(){
        if (leaguesList != null){
            leaguesList.clear();
            if (recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        leaguesList = new ArrayList<>();
    }

    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra("some", leaguesList.get(position).getName());
        startActivity(intent);

    }
}