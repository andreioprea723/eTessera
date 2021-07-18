package com.example.etessera20.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etessera20.R;
import com.example.etessera20.adaptors.EvenimentAdaptor;
import com.example.etessera20.adaptors.TranzactieAdaptor;
import com.example.etessera20.models.Card;
import com.example.etessera20.models.Eveniment;
import com.example.etessera20.models.Tranzactie;
import com.example.etessera20.models.Utilizator;
import com.example.etessera20.models.UtilizatorTranzactii;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class WalletActivity extends DrawerActivity {
    private RecyclerView rvTranzactii;
    private List<Tranzactie> mTranzactii;
    private List<UtilizatorTranzactii> mUtilizatorTranzactii;
    private TextView buget;
    private Button addMoney;

    private Dialog popUpAddMoney;
    private Dialog popUpAddCard;

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference tranzactiiReference;
    private DatabaseReference tranzacatiiUserReference;

    private TranzactieAdaptor tranzactieAdaptor;

    private String userID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_wallet, null, false);
        drawerLayout.addView(contentView, 0);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        tranzactiiReference = FirebaseDatabase.getInstance().getReference("Tranzactii");
        tranzacatiiUserReference = FirebaseDatabase.getInstance().getReference("UsersTranzactii");


        popUpAddCard = new Dialog(this);
        popUpAddMoney = new Dialog(this);

        mTranzactii = new ArrayList<Tranzactie>();
        mUtilizatorTranzactii = new ArrayList<UtilizatorTranzactii>();

        addMoney = findViewById(R.id.btn_add_money);
        rvTranzactii = findViewById(R.id.rv_tranzactii);
        rvTranzactii.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        tranzactiiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mTranzactii.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("id_user").getValue().equals(userID)){
                            Tranzactie tranzactie = dataSnapshot.getValue(Tranzactie.class);
                            mTranzactii.add(tranzactie);
                        }
                    }
                Collections.reverse(mTranzactii);
                tranzactieAdaptor = new TranzactieAdaptor(WalletActivity.this, mTranzactii);
                rvTranzactii.setAdapter(tranzactieAdaptor);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userID = user.getUid();

        buget = findViewById(R.id.tv_bani);

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilizator userProfile = snapshot.getValue(Utilizator.class);

                    buget.setText(Long.toString(userProfile.getBuget()) + " lei");
                
                addMoney.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(userProfile.getCard().equals("no card")){
                            showPopUpAddCard(v);
                        }else {
                            showPopUpAddMoney(v, userProfile.getBuget());
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WalletActivity.this, "Ceva nu functioneaza corect!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showPopUpAddCard(View view){
        EditText et_cardHolder;
        EditText et_cardnumber;
        EditText et_expireDate;
        EditText et_CVV;
        Button finish;
        TextView close;

        DatabaseReference cardReference = FirebaseDatabase.getInstance().getReference().child("Cards");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        popUpAddCard.setContentView(R.layout.popup_addcard);

        et_cardHolder = popUpAddCard.findViewById(R.id.et_card_holder);
        et_cardnumber = popUpAddCard.findViewById(R.id.et_card_number);
        et_expireDate = popUpAddCard.findViewById(R.id.et_data_expirare);
        et_CVV = popUpAddCard.findViewById(R.id.et_cvv);
        finish = popUpAddCard.findViewById(R.id.bt_finish);
        close = popUpAddCard.findViewById(R.id.close_addcard);
        
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardHolder = et_cardHolder.getText().toString().trim();
                String cardNumber = et_cardnumber.getText().toString().trim();
                String dataExpirare = et_expireDate.getText().toString().trim();
                String CSV = et_CVV.getText().toString().trim();

                if(cardHolder.isEmpty()){
                    et_cardHolder.setError("Numele detinatorului cardului este necesar!");
                    et_cardHolder.requestFocus();
                    return;
                }

                if(cardNumber.isEmpty()){
                    et_cardnumber.setError("Numarul cardului este necesar!");
                    et_cardnumber.requestFocus();
                    return;
                }

                if(cardNumber.length() > 16 || cardNumber.length() < 16){
                    et_cardnumber.setError("Numarul cardului este invalid!");
                    et_cardnumber.requestFocus();
                    return;
                }

                if(!cardNumber.matches("[0-9]+")){
                    et_cardnumber.setError("Numarul cardului este invalid!");
                    et_cardnumber.requestFocus();
                    return;
                }

                if(dataExpirare.isEmpty()){
                    et_expireDate.setError("Data de expirare a cardului este necesara!");
                    et_expireDate.requestFocus();
                    return;
                }

                if(CSV.isEmpty()){
                    et_CVV.setError("CSV este obligatoriu!");
                    et_CVV.requestFocus();
                    return;
                }

                if(CSV.length() != 3){
                    et_CVV.setError("CSV invalid!");
                    et_CVV.requestFocus();
                    return;
                }

                if(!CSV.matches("[0-9]+")){
                    et_cardnumber.setError("Numarul cardului este invalid!");
                    et_cardnumber.requestFocus();
                    return;
                }

                Card card = new Card(cardHolder,cardNumber,dataExpirare,CSV, userID);
                cardReference.push().setValue(card);
                userReference.child(userID).child("card").setValue(cardNumber);

                popUpAddCard.dismiss();
                Toast.makeText(WalletActivity.this, "Cardul dumneavoastra a fost adaugat!", Toast.LENGTH_LONG).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAddCard.dismiss();
            }
        });

        popUpAddCard.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpAddCard.show();

    }

    public void showPopUpAddMoney(View view, long buget){
        EditText et_suma;
        Button adauga;
        TextView close;
        TextView schimba_metodata;
        TextView sterge_card;

        
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference tranzactiiReference = FirebaseDatabase.getInstance().getReference().child("Tranzactii");
        DatabaseReference cardReference = FirebaseDatabase.getInstance().getReference().child("Cards");

        popUpAddMoney.setContentView(R.layout.popup_addmoney);

        et_suma = popUpAddMoney.findViewById(R.id.et_add_money);
        adauga = popUpAddMoney.findViewById(R.id.bt_adauga);
        close = popUpAddMoney.findViewById(R.id.close_addmoney);
        schimba_metodata = popUpAddMoney.findViewById(R.id.tv_schimba_modalitatea);
        sterge_card = popUpAddMoney.findViewById(R.id.tv_elimina_card);

        adauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long suma = Long.parseLong(et_suma.getText().toString().trim());
                long buget_actual;
                buget_actual = suma + buget;

                userReference.child(FirebaseAuth.getInstance().getUid()).child("buget").setValue(buget_actual);
                String uniqueID = UUID.randomUUID().toString();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                Tranzactie tranzactie = new Tranzactie(uniqueID , suma , formattedDate ,"adaugare" , "Fonduri", userID);
                tranzactiiReference.push().setValue(tranzactie);

                Toast.makeText(WalletActivity.this, "Au fost adaugati in contul dumneavostra " + Long.toString(suma) + " lei", Toast.LENGTH_LONG).show();
                popUpAddMoney.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAddMoney.dismiss();
            }
        });

        schimba_metodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAddMoney.dismiss();
                showPopUpAddCard(v);
            }
        });

        sterge_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WalletActivity.this);
                builder.setTitle("Sunteti sigur ca doriti sa stergeti datele carduli?");
                builder.setMessage("Pentru a putea adauga bani in portofel trebuie sa inregistrati un alt card!");
                builder.setCancelable(true);
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cardReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if(dataSnapshot.child("id_user").getValue().equals(userID)){
                                        dataSnapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                            userReference.child(userID).child("card").setValue("no card");                    }
                });

                builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                popUpAddMoney.dismiss();
            }

        });

        popUpAddMoney.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpAddMoney.show();                                                                 
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
                filer(newText, mTranzactii, tranzactieAdaptor );
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void filer(String s, List<Tranzactie> cautaList, TranzactieAdaptor cautaAdaptor){
        List<Tranzactie> listFilter = new ArrayList<Tranzactie>();

        for(Tranzactie tranzactie : cautaList){
            if(tranzactie.getNume().toLowerCase().contains(s.toLowerCase())){
                listFilter.add(tranzactie);
            }
            else if(tranzactie.getData().toLowerCase().contains(s.toLowerCase())){
                listFilter.add(tranzactie);
            }
        }

        cautaAdaptor.filterList(listFilter);
    }
}
