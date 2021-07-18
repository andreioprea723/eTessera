package com.example.etessera20.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.etessera20.R;
import com.example.etessera20.adaptors.EvenimentAdaptor;
import com.example.etessera20.adaptors.EventItemClickListener;
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

public class FavoritesActivity extends DrawerActivity implements EventItemClickListener{
    RecyclerView Rv_teatru, Rv_standup, Rv_concerte;

    List<Eveniment> list_teatru;
    List<Eveniment> list_standup;
    List<Eveniment> list_concert;

    List<Favorite> mListaFavorite;

    EvenimentAdaptor teatruAdaptor, standupAdaptor, concertAdaptor;

    DatabaseReference eventReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_favorites, null, false);
        drawerLayout.addView(contentView, 0);

        eventReference = FirebaseDatabase.getInstance().getReference().child("Events");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        list_teatru = new ArrayList<Eveniment>();
        list_standup = new ArrayList<Eveniment>();
        list_concert = new ArrayList<Eveniment>();

        mListaFavorite = new ArrayList<Favorite>();

        Rv_teatru = findViewById(R.id.Rv_teatru_fav);
        Rv_teatru.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Rv_standup = findViewById(R.id.Rv_standup_fav);
        Rv_standup.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Rv_concerte = findViewById(R.id.Rv_concerte_fav);
        Rv_concerte.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Button refresh = findViewById(R.id.refresh_btn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        iniFavoritList();

        eventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_concert.clear();
                list_standup.clear();
                list_teatru.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Eveniment e = dataSnapshot.getValue(Eveniment.class);
                    for(Favorite favorite : mListaFavorite){
                        if((favorite.getId_eveniment().equals(e.getId())) && (favorite.getId_user().equals(userID))){
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

                teatruAdaptor = new EvenimentAdaptor(FavoritesActivity.this, list_teatru, FavoritesActivity.this::onEventClick);
                Rv_teatru.setAdapter(teatruAdaptor);

                standupAdaptor = new EvenimentAdaptor(FavoritesActivity.this, list_standup, FavoritesActivity.this::onEventClick);
                Rv_standup.setAdapter(standupAdaptor);

                concertAdaptor = new EvenimentAdaptor(FavoritesActivity.this, list_concert, FavoritesActivity.this::onEventClick);
                Rv_concerte.setAdapter(concertAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FavoritesActivity.this, eventImageView, "sharedName");

        startActivity(intent, options.toBundle());
    }

    public void iniFavoritList(){
        DatabaseReference favoriteReference;
        favoriteReference = FirebaseDatabase.getInstance().getReference().child("Favorite");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListaFavorite.clear();
                for(DataSnapshot favoriteSnapshot : snapshot.getChildren()){
                    Favorite favorite = favoriteSnapshot.getValue(Favorite.class);
                    mListaFavorite.add(favorite);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
