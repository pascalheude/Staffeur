package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

// Class StaffeurDetailActivity
public class StaffeurDetailActivity extends AppCompatActivity {
    // Attributs privés
    private boolean pChargement;
    private TextView pTextViewStaffeur;
    private View pRecyclerView;
    private ItemRecyclerViewAdapter pItemRecyclerViewAdapter;
    private FloatingActionButton pFloatingActionButtonMaj;
    private String pLogin;
    private int pUserId;
    private ArrayList<PresenceRandonnee> pListePresenceRandonnee;

    // Méthode onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        pChargement = true;
        pTextViewStaffeur = (TextView) findViewById(R.id.textViewStaffeur);
        pRecyclerView = findViewById(R.id.item_list);
        pFloatingActionButtonMaj = (FloatingActionButton) findViewById(R.id.fabMaJ);
        pFloatingActionButtonMaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // TODO Mettre à jour la BdD
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });
        pLogin = getIntent().getStringExtra(getString(R.string.login));
        pUserId = getIntent().getIntExtra(getString(R.string.user_id), 0);
        pListePresenceRandonnee = (ArrayList<PresenceRandonnee>) getIntent().getSerializableExtra(getString(R.string.liste_presence));
        pTextViewStaffeur.setText(String.format("%s (%d)", pLogin, pUserId));
        pItemRecyclerViewAdapter = new ItemRecyclerViewAdapter(pListePresenceRandonnee);
        ((RecyclerView)pRecyclerView).setAdapter(pItemRecyclerViewAdapter);
    }

    // Class ItemRecyclerViewAdapter
    public class ItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

        // Classe ViewHolder
        public class ViewHolder extends RecyclerView.ViewHolder {
            private int pPosition;
            public View aView;
            public TextView aRandoId;
            public TextView aDate;
            public Spinner aPresence;

            public ViewHolder(View view) {
                super(view);
                aView = view;
                aRandoId = (TextView) view.findViewById(R.id.textViewRandoId);
                aDate = (TextView) view.findViewById(R.id.textViewDate);
                aPresence = (Spinner) view.findViewById(R.id.spinnerPresence);
                ArrayAdapter<CharSequence> lArrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.presence, R.layout.spinner_item);
                lArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                aPresence.setAdapter(lArrayAdapter);
                aPresence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        if (pChargement) {
                            if (pPosition == (pListePresenceRandonnee.size() - 1)) {
                                pChargement = false;
                            }
                            else {
                            }
                        }
                        else {
                            ((TextView) view).setTextColor(getColor(R.color.colorGreen));
                            ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD);
                            PresenceRandonnee lPresenceRandonnee = pListePresenceRandonnee.get(pPosition);
                            lPresenceRandonnee.ecrirePresence(pos);
                            pListePresenceRandonnee.set(pPosition, lPresenceRandonnee);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }

        private final ArrayList<PresenceRandonnee> pListePresenceRandonnee;

        public ItemRecyclerViewAdapter(ArrayList<PresenceRandonnee> liste_presence_randonnee) {
            pListePresenceRandonnee = (ArrayList<PresenceRandonnee>) liste_presence_randonnee.clone();
        }

        @Override
        public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemRecyclerViewAdapter.ViewHolder holder, final int position) {
            SimpleDateFormat lSimpleDateFormat;
            lSimpleDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
            PresenceRandonnee lPresenceRandonnee = pListePresenceRandonnee.get(position);
            holder.pPosition = position;
            holder.aRandoId.setText(String.format("%d", lPresenceRandonnee.lireRandonneeId()));
            holder.aDate.setText(lSimpleDateFormat.format(lPresenceRandonnee.lireDate()));
            holder.aPresence.setSelection(lPresenceRandonnee.lirePresence());
            if ((position % 2) == 0) {
                holder.aRandoId.setBackgroundColor(getColor(R.color.colorLightGrey));
                holder.aDate.setBackgroundColor(getColor(R.color.colorLightGrey));
                holder.aPresence.setBackgroundColor(getColor(R.color.colorLightGrey));
            }
            else {
            }
            holder.aView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return pListePresenceRandonnee.size();
        }
    }
}