package com.example.etessera20.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.etessera20.R;
import com.example.etessera20.adaptors.DistributieAdaptor;
import com.example.etessera20.models.Achitat;
import com.example.etessera20.models.Artist;
import com.example.etessera20.models.Distributie;
import com.example.etessera20.models.Eveniment;
import com.example.etessera20.models.Favorite;
import com.example.etessera20.models.Tranzactie;
import com.example.etessera20.models.Utilizator;
import com.example.etessera20.models.UtilizatorTranzactii;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EvenimentDetailsActivity extends AppCompatActivity {

    private ImageView EventThumbnailImg, EventCoverImg;
    private TextView tv_title, tv_description;
    private Button butonFav;
    private FloatingActionButton play;
    private RecyclerView recyclerView;
    private DistributieAdaptor distributieAdaptor;
    private Dialog popUpPlay;

    private DatabaseReference eventsReference;
    private DatabaseReference achitatReference;
    private DatabaseReference favoriteReference;
    private FirebaseUser user;

    private String userID;
    private List<Achitat> mListaAchitat;
    private List<Favorite> mListaFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eveniment_details);

        eventsReference = FirebaseDatabase.getInstance().getReference().child("Events");
        achitatReference = FirebaseDatabase.getInstance().getReference().child("Achitat");
        favoriteReference = FirebaseDatabase.getInstance().getReference().child("Favorite");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        mListaFavorite = new ArrayList<Favorite>();
        mListaAchitat = new ArrayList<Achitat>();

        popUpPlay = new Dialog(this);


        iniViews();

        setupRvDistributie();
    }

    void iniViews(){

        recyclerView = findViewById(R.id.rv_distributie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        String evenimentTitlu = getIntent().getExtras().getString("title");
        String evenimentDescriere = getIntent().getExtras().getString("description");
        String imagResourceId = getIntent().getExtras().getString("imgURL");
        String imgThumbnail = getIntent().getExtras().getString("imgCover");
        String video = getIntent().getExtras().getString("video");
        String key = getIntent().getExtras().getString("id");

        play = findViewById(R.id.details_actionButton);

        EventThumbnailImg = findViewById(R.id.details_event_cover);
        Picasso.get().load(imgThumbnail).into(EventThumbnailImg);

        EventCoverImg = findViewById(R.id.details_event_img);
        Picasso.get().load(imagResourceId).into(EventCoverImg);

        tv_title = findViewById(R.id.details_event_title);
        tv_title.setText(evenimentTitlu);

        tv_description = findViewById(R.id.details_event_description);
        tv_description.setText(evenimentDescriere);

        butonFav = findViewById(R.id.buton_favorite);

        eventsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot eventSnapshot: snapshot.getChildren()){
                    if(eventSnapshot.getKey().equals(key)){
                        iniFavorite();

                        butonFav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("######### A FOST APASAT BUTONUL!!");
                                if(mListaFavorite.isEmpty()){
                                    System.out.println("$$$$$$$$$$$$$$ AM INTRAT IN IF EMPTY");
                                    Favorite favorite = new Favorite(UUID.randomUUID().toString(), key, userID);
                                    favoriteReference.push().setValue(favorite);
                                    butonFav.setBackgroundResource(R.drawable.ic_star_black_24dp);
                                }
                                else {
                                    System.out.println("%%%%%%%%%%%% AM INTRAT IN IF FULL");
                                    remove(favoriteReference);
                                    butonFav.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                                }
                            }
                        });

                        iniAchitat();

                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mListaAchitat.isEmpty()){
                                    showPopUp(v);
                                }else{
                                    Intent intent = new Intent(EvenimentDetailsActivity.this, PlayerActivity.class);
                                    intent.putExtra("URL", video);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // animation
        EventCoverImg.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        EventCoverImg.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));

    }

    void setupRvDistributie(){
        List<Artist> mArtisti = new ArrayList<Artist>();
        List<Distributie> mDistributie = new ArrayList<Distributie>();

        DatabaseReference databaseArtisti = FirebaseDatabase.getInstance().getReference().child("Artists");
        DatabaseReference databaseArtistiEveniment = FirebaseDatabase.getInstance().getReference().child("ArtistsEvents");


        String key = getIntent().getExtras().getString("id");

        databaseArtistiEveniment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDistributie.clear();
                for(DataSnapshot distributieSnapshot : snapshot.getChildren()){
                    if(key.equals(distributieSnapshot.child("id_eveniment").getValue())){
                        Distributie distributie = distributieSnapshot.getValue(Distributie.class);
                        mDistributie.add(distributie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseArtisti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mArtisti.clear();
                for(DataSnapshot artistiSnapshot : snapshot.getChildren()){
                    for(Distributie distributie:mDistributie){
                       if(distributie.getId_artist().equals(artistiSnapshot.child("id").getValue())){
                            Artist artist = artistiSnapshot.getValue(Artist.class);
                            mArtisti.add(artist);
                        }
                    }

                    distributieAdaptor = new DistributieAdaptor(EvenimentDetailsActivity.this, mArtisti);
                    recyclerView.setAdapter(distributieAdaptor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

    public void showPopUp(View view){
        TextView tv_valoare;
        ImageView iv_cover;
        TextView tv_titlu_popup;
        Button buy;
        Button cancel;

        DatabaseReference tranzactiiReference = FirebaseDatabase.getInstance().getReference().child("Tranzactii");;
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference eventsReference = FirebaseDatabase.getInstance().getReference().child("Events");

        String key = getIntent().getExtras().getString("id");
        String evenimentTitluPopUp = getIntent().getExtras().getString("title");
        String imagResourceIdPopUP = getIntent().getExtras().getString("imgURL");;
        long pricePopUp = getIntent().getExtras().getLong("price");
        String video = getIntent().getExtras().getString("video");

        popUpPlay.setContentView(R.layout.popup_play);

        tv_valoare = popUpPlay.findViewById(R.id.tv_popup_valoare);
        tv_valoare.setText(Double.toString(pricePopUp) + " lei");

        iv_cover = popUpPlay.findViewById(R.id.iv_popup_play);
        Picasso.get().load(imagResourceIdPopUP).into(iv_cover);


        tv_titlu_popup = popUpPlay.findViewById(R.id.tv_popup_titlu);
        tv_titlu_popup.setText(evenimentTitluPopUp);

        buy = popUpPlay.findViewById(R.id.bt_buy);
        cancel = popUpPlay.findViewById(R.id.bt_cancel);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EvenimentDetailsActivity.this);
                builder.setTitle("Sunteti sigur ca doriti sa faceti tranzactia?");
                builder.setMessage("vor fi extrasi din portofelul dumneavoastra "+ Double.toString(pricePopUp) + " lei");
                builder.setCancelable(true);
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usersReference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Utilizator utilizator = snapshot.getValue(Utilizator.class);
                                if(utilizator.getBuget()>=pricePopUp){

                                    String uniqueID = UUID.randomUUID().toString();
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String formattedDate = df.format(c);
                                    Tranzactie tranzactie = new Tranzactie(uniqueID , pricePopUp , formattedDate ,"extragere" , evenimentTitluPopUp, userID);
                                    tranzactiiReference.push().setValue(tranzactie);

                                    long buget_actual = utilizator.getBuget() - pricePopUp;
                                    usersReference.child(FirebaseAuth.getInstance().getUid()).child("buget").setValue(buget_actual);

                                    iniAchitat();
                                    if(mListaAchitat.isEmpty()){
                                        Achitat achitat = new Achitat(UUID.randomUUID().toString(), key, userID);
                                        achitatReference.push().setValue(achitat);
                                    }

                                    Toast.makeText(EvenimentDetailsActivity.this, "Au fost extrasi din cont " + pricePopUp + " lei", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(EvenimentDetailsActivity.this, "FONDURI INSUFICIENTE!!!", Toast.LENGTH_LONG).show();
                                    popUpPlay.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        popUpPlay.dismiss();
                    }
                });

                builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpPlay.dismiss();
            }
        });

        popUpPlay.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpPlay.show();
    }

    public void iniFavorite(){
        String key = getIntent().getExtras().getString("id");


        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListaFavorite.clear();
                for(DataSnapshot favoriteSnapshot : snapshot.getChildren()){
                    if((favoriteSnapshot.child("id_eveniment").getValue().equals(key)) && (favoriteSnapshot.child("id_user").getValue().equals(userID))){
                            Favorite favorite = favoriteSnapshot.getValue(Favorite.class);
                            mListaFavorite.add(favorite);
                        }

                }
                if(mListaFavorite.isEmpty()){
                    butonFav.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                }
                else{
                    butonFav.setBackgroundResource(R.drawable.ic_star_black_24dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void iniAchitat(){
        String key = getIntent().getExtras().getString("id");
        String video = getIntent().getExtras().getString("video");

        achitatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListaAchitat.clear();
                for(DataSnapshot achitatSnapshot : snapshot.getChildren()){
                    if((achitatSnapshot.child("id_eveniment").getValue().equals(key)) && (achitatSnapshot.child("id_user").getValue().equals(userID))){
                        Achitat achitat = achitatSnapshot.getValue(Achitat.class);
                        mListaAchitat.add(achitat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void remove(DatabaseReference removeReference){
        String key = getIntent().getExtras().getString("id");

        removeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot removeSnapshot : snapshot.getChildren()){
                    if(removeSnapshot.child("id_eveniment").getValue().equals(key)){
                        removeSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
