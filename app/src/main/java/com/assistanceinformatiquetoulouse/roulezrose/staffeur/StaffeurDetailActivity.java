package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Thread.sleep;

// Class StaffeurDetailActivity
public class StaffeurDetailActivity extends AppCompatActivity {
    // Attributs privés
    private boolean pChargement;
    private ProgressBar pProgressBar;
    private TextView pTextViewStaffeur;
    private View pRecyclerView;
    private ItemRecyclerViewAdapter pItemRecyclerViewAdapter;
    private FloatingActionButton pFloatingActionButtonMaj;
    private String pLogin;
    private int pUserId;
    private ArrayList<PresenceRandonnee> pListePresenceRandonnee;
    private ArrayList<PresenceRandonnee> pListePresenceRandonneeCopie;

    // Méthode ecrirePresences
    private String ecrirePresences(URL url) throws IOException {
        String lStatus = "";
        InputStream lInputStream = null;
        try {
            if (url.getProtocol().contains("https")) {
                // Creer une communication https pour communiquer avec l'URL
                HttpsURLConnection lHttpsURLConnection = (HttpsURLConnection) url.openConnection();
                // Connexion à l'URL
                lHttpsURLConnection.connect();
                // Lire le flux depuis la connexion
                lInputStream = lHttpsURLConnection.getInputStream();
            }
            else
            {
                // Creer une communication http pour communiquer avec l'URL
                HttpURLConnection lHttpURLConnection = (HttpURLConnection) url.openConnection();
                // Connexion à l'URL
                lHttpURLConnection.connect();
                // Lire le flux depuis la connexion
                lInputStream = lHttpURLConnection.getInputStream();
            }
            BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(lInputStream));
            StringBuffer lStringBuffer  = new StringBuffer();
            String lLigne = "";
            while((lLigne = lBufferedReader.readLine()) != null) {
                lStringBuffer.append(lLigne);
                lStringBuffer.append("\n");
            }
            lStatus = lStringBuffer.toString();
            lBufferedReader.close();
        }
        catch (IOException e) {
        }
        finally {
            if (lInputStream != null)
            {
                lInputStream.close();
            }
            else
            {
            }
        }
        return(lStatus);
    }

    // Méthode onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        pChargement = true;
        pProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        pProgressBar.setVisibility(View.INVISIBLE);
        pTextViewStaffeur = (TextView) findViewById(R.id.textViewStaffeur);
        pRecyclerView = findViewById(R.id.item_list);
        pFloatingActionButtonMaj = (FloatingActionButton) findViewById(R.id.fabMaJ);
        pFloatingActionButtonMaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pProgressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<PresenceRandonnee> lListePresenceRandonnee;
                        lListePresenceRandonnee = pItemRecyclerViewAdapter.lireListePresenceRandonnee();
                        for (int i=0;i < lListePresenceRandonnee.size();i++) {
                            final int i_final = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pProgressBar.setProgress(100 * (i_final+1) / lListePresenceRandonnee.size());
                                }
                            });
                            PresenceRandonnee lPresenceRandonnee = lListePresenceRandonnee.get(i);
                            if (lPresenceRandonnee.equals(pListePresenceRandonneeCopie.get(i))) {
                            }
                            else {
                                try {
                                    URL lURL = new URL(String.format(getString(R.string.update_URL),
                                                                     lPresenceRandonnee.lireRandonneeId(),
                                                                     pUserId,
                                                                     lPresenceRandonnee.lirePresence()));
                                    if (ecrirePresences(lURL).equals("OK")) {
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch(Exception e) {
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
            }
        });
        pLogin = getIntent().getStringExtra(getString(R.string.login));
        pUserId = getIntent().getIntExtra(getString(R.string.user_id), 0);
        pListePresenceRandonnee = (ArrayList<PresenceRandonnee>) getIntent().getSerializableExtra(getString(R.string.liste_presence));
        pListePresenceRandonneeCopie = new ArrayList<PresenceRandonnee>();
        for (int i=0;i < pListePresenceRandonnee.size();i++) {
            pListePresenceRandonneeCopie.add(pListePresenceRandonnee.get(i).clone());
        }
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
            public TextView aRandoType;
            public TextView aRandoId;
            public TextView aDate;
            public Spinner aPresence;

            public ViewHolder(View view) {
                super(view);
                aView = view;
                aRandoType = (TextView) view.findViewById(R.id.textViewRandoType);
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

        // Attributs privées
        private final ArrayList<PresenceRandonnee> pListePresenceRandonnee;

        // Constructeur
        public ItemRecyclerViewAdapter(ArrayList<PresenceRandonnee> liste_presence_randonnee) {
            this.pListePresenceRandonnee = (ArrayList<PresenceRandonnee>) liste_presence_randonnee;
        }

        // Méthode lireListePresenceRandonnee
        public ArrayList<PresenceRandonnee> lireListePresenceRandonnee() {
            return(this.pListePresenceRandonnee);
        }

        // Méthode onCreateViewHolder
        @Override
        public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ItemRecyclerViewAdapter.ViewHolder(view);
        }

        // Méthode onBindViewHolder
        @Override
        public void onBindViewHolder(final ItemRecyclerViewAdapter.ViewHolder holder, final int position) {
            SimpleDateFormat lSimpleDateFormat;
            lSimpleDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
            PresenceRandonnee lPresenceRandonnee = this.pListePresenceRandonnee.get(position);
            holder.pPosition = position;
            holder.aRandoId.setText(String.format("%d", lPresenceRandonnee.lireRandonneeId()));
            holder.aDate.setText(lSimpleDateFormat.format(lPresenceRandonnee.lireDate()));
            holder.aPresence.setSelection(lPresenceRandonnee.lirePresence());
            int rando_type = lPresenceRandonnee.lireTypeRandonnee();
            switch(rando_type) {
                case 0 :
                    holder.aRandoType.setBackgroundColor(getColor(R.color.colorRandonneeVerte));
                    break;
                case 1 :
                case 3 :
                    holder.aRandoType.setBackgroundColor(getColor(R.color.colorRandonneeBleue));
                    break;
                case 2 :
                    holder.aRandoType.setBackgroundColor(getColor(R.color.colorRandonneeOrange));
                    break;
                case 4 :
                    holder.aRandoType.setBackgroundColor(getColor(R.color.colorRandonneeATheme));
                    break;
                case 5 :
                    holder.aRandoType.setBackgroundColor(getColor(R.color.colorBlack));
                    break;
                default :
                    holder.aRandoType.setBackgroundColor(getColor(R.color.colorLightGrey));
            }
            if ((position % 2) == 0) {
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

        // Méthode getItemCount
        @Override
        public int getItemCount() {
            return(this.pListePresenceRandonnee.size());
        }
    }
}