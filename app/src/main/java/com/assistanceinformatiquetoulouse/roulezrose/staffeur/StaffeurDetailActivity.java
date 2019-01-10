package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

// Class StaffeurDetailActivity
public class StaffeurDetailActivity extends AppCompatActivity {
    // Attributs privés
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
        pTextViewStaffeur = (TextView) findViewById(R.id.textViewStaffeur);
        pRecyclerView = findViewById(R.id.item_list);
        pFloatingActionButtonMaj = (FloatingActionButton) findViewById(R.id.fabMaJ);
        pFloatingActionButtonMaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        pLogin = getIntent().getStringExtra(getString(R.string.login));
        pUserId = getIntent().getIntExtra(getString(R.string.user_id), 0);
        pListePresenceRandonnee = (ArrayList<PresenceRandonnee>) getIntent().getSerializableExtra(getString(R.string.liste_presence));
        pTextViewStaffeur.setText(String.format("%s (%d)", pLogin, pUserId));
        pItemRecyclerViewAdapter = new ItemRecyclerViewAdapter(pListePresenceRandonnee);
        ((RecyclerView)pRecyclerView).setAdapter(pItemRecyclerViewAdapter);
    }

    // Class
    public class ItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

        // Classe ViewHolder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View aView;
            public TextView aRandoId;
            public TextView aDate;
            public TextView aPresence;

            public ViewHolder(View view) {
                super(view);
                aView = view;
                aRandoId = (TextView) view.findViewById(R.id.textViewRandoId);
                aDate = (TextView) view.findViewById(R.id.textViewDate);
                aPresence = (TextView) view.findViewById(R.id.textViewPresence);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + aPresence.getText() + "'";
            }
        }

        private final ArrayList<PresenceRandonnee> pListePresenceRandonnee;

        public ItemRecyclerViewAdapter(ArrayList<PresenceRandonnee> liste_presence_randonne) {
            pListePresenceRandonnee = liste_presence_randonne;
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
            holder.aRandoId.setText(String.format("%d", lPresenceRandonnee.lireRandonneeId()));
            holder.aDate.setText(lSimpleDateFormat.format(lPresenceRandonnee.lireDate()));
            holder.aPresence.setText(lPresenceRandonnee.lirePresence().toString());

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