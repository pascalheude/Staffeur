package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Classe StaffeurActivity
public class StaffeurActivity extends AppCompatActivity {
    // Attributs privées
    SharedPreferences pSharedPreferences;
    private Toolbar pToolbar;
    private EditText pEditTextLogin;
    private static EditText pEditTextPassword;
    private CheckBox pCheckBoxMemoriser;
    private Button pButtonConnexion;
    private View pProgressView;
    private View mLoginFormView;
    private ConnexionAsyncTask pConnexionAsyncTask;
    private int pUserId;
    private ArrayList<PresenceRandonnee> pListePresenceRandonnee;

    // Méthode onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffeur);
        pToolbar = (Toolbar) findViewById(R.id.toolbar);
        pToolbar.setTitle(R.string.app_name);
        setSupportActionBar(pToolbar);
        pEditTextLogin = (EditText) findViewById(R.id.editTextNomUtilisateur);
        pEditTextPassword = (EditText) findViewById(R.id.editTextMotDePasse);
        pCheckBoxMemoriser = (CheckBox) findViewById(R.id.checkBoxMemoriser);
        pSharedPreferences = getBaseContext().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        if (pSharedPreferences.contains(getString(R.string.memorized_data))) {
            pEditTextLogin.setText(pSharedPreferences.getString(getString(R.string.login), ""));
            pEditTextPassword.setText(pSharedPreferences.getString(getString(R.string.password), ""));
            pCheckBoxMemoriser.setChecked(true);
        }
        else {
            pEditTextLogin.setText("");
            pEditTextPassword.setText("");
            pCheckBoxMemoriser.setChecked(false);
        }
        pButtonConnexion = (Button) findViewById(R.id.buttonConnexion);
        pProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.connexionScrollView);
        pConnexionAsyncTask = null;
        pListePresenceRandonnee = new ArrayList<>();
        // TODO Voir si on conserve la fonction suivante
        pEditTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        pButtonConnexion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        pCheckBoxMemoriser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor lEditor = pSharedPreferences.edit();
                if (pCheckBoxMemoriser.isChecked()) {
                    lEditor.putBoolean(getString(R.string.memorized_data), true);
                    lEditor.putString(getString(R.string.login), pEditTextLogin.getText().toString());
                    lEditor.putString(getString(R.string.password), pEditTextPassword.getText().toString());
                    lEditor.commit();
                }
                else {
                    lEditor.clear();
                    lEditor.commit();
                }
            }
        });
    }

    // Méthode onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return(super.onCreateOptionsMenu(menu));
    }

    // Méthode onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(this);
                lAlertDialog.setTitle("Staff\nVersion " + this.getString(R.string.version));
                lAlertDialog.setMessage("Compatible login version " + this.getString(R.string.login_version) +
                        " update version " + this.getString(R.string.update_version) +
                        "\nPrésence des staffeurs\n© AIT 2019 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
                lAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }});
                lAlertDialog.setIcon(R.mipmap.ic_staffeur);
                lAlertDialog.create().show();
                break;
            default:
                break;
        }
        return(true);

    }
    // Méthode attemptLogin
    private void attemptLogin() {
        String login;
        String password;

        if (pConnexionAsyncTask == null) {
            // Efface les erreurs
            pEditTextLogin.setError(null);
            pEditTextPassword.setError(null);
            login = pEditTextLogin.getText().toString();
            password = pEditTextPassword.getText().toString();
            // Si le login est vide alors affiche une erreur
            if (TextUtils.isEmpty(login) == true) {
                pEditTextLogin.setError(getString(R.string.error_field_required));
                pEditTextLogin.requestFocus();
            }
            else {
                // Si le mot de passe est vide alors affiche une erreur sinon démarre la connexion
                if (TextUtils.isEmpty(password) == true) {
                    pEditTextPassword.setError(getString(R.string.error_invalid_password));
                    pEditTextPassword.requestFocus();
                }
                else {
                    afficheProgression(true);
                    pConnexionAsyncTask = new ConnexionAsyncTask(login, password);
                    pConnexionAsyncTask.execute((Void) null);
                }
            }
        }
        else {
        }
    }

    // Méthode afficheProgression
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void afficheProgression(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            pProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    // Classe ConnexionAsyncTask
    private class ConnexionAsyncTask extends AsyncTask<Void, Void, Boolean> {
        // Attributs privés
        private String pLogin;
        private String pPassword;
        private String pErreur;

        // Constructeur
        ConnexionAsyncTask(String login, String password) {
            byte[] bytes;
            try {
                bytes = login.getBytes("UTF-8");
                pLogin = new String(bytes, Charset.forName("UTF-8"));
            }
            catch(UnsupportedEncodingException e) {
                pLogin = null;
            }
            try {
                bytes = password.getBytes("UTF-8");
                pPassword = new String(bytes, Charset.forName("UTF-8"));
            }
            catch(UnsupportedEncodingException e) {
                pPassword = null;
            }
            pErreur = "";
        }

        // Méthode doInBackground
        @Override
        protected Boolean doInBackground(Void... params) {
            HttpPost lHttpPost = new HttpPost(getString(R.string.login_URL));
            HttpClient lHttpClient = new DefaultHttpClient();
            HttpResponse lHttpResponse;
            InputStream lInputStream;
            String lJSONString = "";
            JSONObject lGlobalJSONObject;
            JSONObject lJSONObject;
            JSONArray lListePresences;
            SimpleDateFormat lSimpleDateFormat;
            Date lDate;
            PresenceRandonnee lPresenceRandonnee;
            Presence lPresence;

            try {
                ArrayList<NameValuePair> lNameValuePairList = new ArrayList<NameValuePair>();
                lNameValuePairList.add(new BasicNameValuePair(getString(R.string.login), pLogin));
                lNameValuePairList.add(new BasicNameValuePair(getString(R.string.password), pPassword));
                lNameValuePairList.add(new BasicNameValuePair("nb", getString(R.string.nb_randonnee)));
                lHttpPost.setEntity(new UrlEncodedFormEntity(lNameValuePairList, "UTF-8"));
                lHttpResponse = lHttpClient.execute(lHttpPost);
                if (lHttpResponse.getStatusLine().getStatusCode() != 200) {
                    lInputStream = lHttpResponse.getEntity().getContent();
                    BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(lInputStream));
                    StringBuffer lStringBuffer  = new StringBuffer();
                    String lLigne = "";
                    while((lLigne = lBufferedReader.readLine()) != null) {
                        lStringBuffer.append(lLigne);
                        lStringBuffer.append("\n");
                    }
                    pErreur = lStringBuffer.toString();
                    lBufferedReader.close();
                    return(false);
                }
                else {
                    lInputStream = lHttpResponse.getEntity().getContent();
                    BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(lInputStream));
                    StringBuffer lStringBuffer  = new StringBuffer();
                    String lLigne = "";
                    while((lLigne = lBufferedReader.readLine()) != null) {
                        lStringBuffer.append(lLigne);
                        lStringBuffer.append("\n");
                    }
                    lJSONString = lStringBuffer.toString();
                    lBufferedReader.close();
                }
            }
            catch (IOException e) {
                return(false);
            }
            try {
                lGlobalJSONObject = new JSONObject(lJSONString);
                pUserId = Integer.parseInt(lGlobalJSONObject.getString(getString(R.string.user_id)));
                lListePresences = lGlobalJSONObject.getJSONArray("Présences");
                for (int i = 0; i < lListePresences.length(); i++) {
                    lJSONObject = lListePresences.getJSONObject(i);
                    lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    lDate = lSimpleDateFormat.parse(lJSONObject.getString("rando_date"));
                    if (lJSONObject.getString("valeur").equals("absent")) {
                        lPresence = Presence.ABSENT;
                    }
                    else if (lJSONObject.getString("valeur").equals("présent")) {
                        lPresence = Presence.PRESENT;
                    }
                    else if (lJSONObject.getString("valeur").equals("indécis")) {
                        lPresence = Presence.INDECIS;
                    }
                    else {
                        lPresence = Presence.AUCUNE;
                    }
                    lPresenceRandonnee = new PresenceRandonnee(lJSONObject.getInt("rando_id"),
                                                               lDate,
                                                               lJSONObject.getInt("rando_type"),
                                                               lPresence);
                    pListePresenceRandonnee.add(lPresenceRandonnee);
                }
            }
            catch(JSONException e) {
                return(false);
            }
            catch(ParseException e) {
                return(false);
            }
            Intent lIntent = new Intent(StaffeurActivity.this, StaffeurDetailActivity.class);
            lIntent.putExtra(getString(R.string.login), pLogin);
            lIntent.putExtra(getString(R.string.password), pPassword);
            lIntent.putExtra(getString(R.string.user_id), pUserId);
            lIntent.putExtra(getString(R.string.liste_presence), pListePresenceRandonnee);
            startActivity(lIntent);
            return(true);
        }

        // Méthode onPostExecute
        @Override
        protected void onPostExecute(final Boolean success) {
            pConnexionAsyncTask = null;
            afficheProgression(false);
            if (success) {
                finish();
            } else {
                StaffeurActivity.pEditTextPassword.setError(pErreur);
                StaffeurActivity.pEditTextPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            pConnexionAsyncTask = null;
            afficheProgression(false);
        }
    }
}