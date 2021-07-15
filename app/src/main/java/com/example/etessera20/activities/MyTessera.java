package com.example.etessera20.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etessera20.R;
import com.example.etessera20.adaptors.EvenimentAdaptor;
import com.example.etessera20.adaptors.EventItemClickListener;
import com.example.etessera20.models.Achitat;
import com.example.etessera20.models.Eveniment;
import com.example.etessera20.models.Favorite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyTessera extends DrawerActivity implements EventItemClickListener {
    RecyclerView Rv_teatru, Rv_standup, Rv_concerte;

    List<Eveniment> list_teatru;
    List<Eveniment> list_standup;
    List<Eveniment> list_concert;

    List<Achitat> mListaBilete;

    EvenimentAdaptor teatruAdaptor, standupAdaptor, concertAdaptor;

    DatabaseReference eventReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_mytikets, null, false);
        drawerLayout.addView(contentView, 0);

        eventReference = FirebaseDatabase.getInstance().getReference().child("Events");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        list_teatru = new ArrayList<Eveniment>();
        list_standup = new ArrayList<Eveniment>();
        list_concert = new ArrayList<Eveniment>();

        mListaBilete = new ArrayList<Achitat>();

        Rv_teatru = (RecyclerView) findViewById(R.id.Rv_teatru_myTikets);
        Rv_teatru.setLayoutManager(new LinearLayoutManager(MyTessera.this, LinearLayoutManager.HORIZONTAL, false));

        Rv_standup = (RecyclerView) findViewById(R.id.Rv_standup_myTikets);
        Rv_standup.setLayoutManager(new LinearLayoutManager(MyTessera.this, LinearLayoutManager.HORIZONTAL, false));

        Rv_concerte = (RecyclerView) findViewById(R.id.Rv_concerte_myTikets);
        Rv_concerte.setLayoutManager(new LinearLayoutManager(MyTessera.this, LinearLayoutManager.HORIZONTAL, false));

        iniMyTiketsList();

        eventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_concert.clear();
                list_standup.clear();
                list_teatru.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Eveniment e = dataSnapshot.getValue(Eveniment.class);
                    for(Achitat achitat : mListaBilete){
                        if((achitat.getId_eveniment().equals(e.getId())) && (achitat.getId_user().equals(userID))){
                            if (e.getType().equals("teatru")) {
                                list_teatru.add(e);
                            }
                            if (e.getType().equals("stand-up")) {
                                list_standup.add(e);
                            }
                            if (e.getType().equals("concert")) {
                                list_concert.add(e);
                            }
                        }
                    }
                }

                teatruAdaptor = new EvenimentAdaptor(MyTessera.this, list_teatru, MyTessera.this::onEventClick);
                Rv_teatru.setAdapter(teatruAdaptor);

                standupAdaptor = new EvenimentAdaptor(MyTessera.this, list_standup, MyTessera.this::onEventClick);
                Rv_standup.setAdapter(standupAdaptor);

                concertAdaptor = new EvenimentAdaptor(MyTessera.this, list_concert, MyTessera.this::onEventClick);
                Rv_concerte.setAdapter(concertAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniMyTiketsList() {
        DatabaseReference favoriteReference;
        favoriteReference = FirebaseDatabase.getInstance().getReference().child("Achitat");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListaBilete.clear();
                for(DataSnapshot achitatSnapshot : snapshot.getChildren()){
                    Achitat achitat = achitatSnapshot.getValue(Achitat.class);
                    mListaBilete.add(achitat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onEventClick(Eveniment eveniment, ImageView eventImageView) {

        Intent intent = new Intent(this, EvenimentDetailsActivity.class);

        intent.putExtra("title", eveniment.getTitlu());
        intent.putExtra("imgCover", eveniment.getThumbnail());
        intent.putExtra("imgURL", eveniment.getCover_img());
        intent.putExtra("description", eveniment.getDescription());
        intent.putExtra("price", eveniment.getPrice());
        intent.putExtra("video", eveniment.getVideo());
        intent.putExtra("id", eveniment.getId());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MyTessera.this, eventImageView, "sharedName");

        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filer(newText, list_teatru, teatruAdaptor);
                filer(newText, list_standup, standupAdaptor);
                filer(newText, list_concert, concertAdaptor);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void filer(String s, List<Eveniment> cautaList, EvenimentAdaptor cautaAdaptor){
        List<Eveniment> listFilter = new ArrayList<Eveniment>();

        for(Eveniment eveniment : cautaList){
            if(eveniment.getTitlu().toLowerCase().contains(s.toLowerCase())){
                listFilter.add(eveniment);
            }

        }

        cautaAdaptor.filterList(listFilter);
    }
}
