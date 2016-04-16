package com.example.mastek.blue.deep.swasthtesting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String SERVER_ADDRESS = "http://swasth-india.esy.es/swasth/user_login.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public int selected;
    EditText etusername;
    EditText pass;
    Button button;
    private User user;
    private UserLocalStore userLocalStore;
    private Locale myLocale;
    private String[] languages = {"English", "हिन्दी"};

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("lang_info", Context.MODE_PRIVATE);
        selected = preferences.getInt("key_lang", 0);

        setTitle(R.string.title_activity_login);

        setContentView(R.layout.activity_login);


//        languageSpinner = (Spinner) findViewById(R.id.languageSpinner);
//        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
//        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        languageSpinner.setAdapter(languageAdapter);
//        languageSpinner.setOnItemSelectedListener(this);
////      load shared preferences
//        languageSpinner.setSelection(selected);
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etusername = (EditText) findViewById(R.id.etLUsername);
        pass = (EditText) findViewById(R.id.etLPassword);
        button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(this);
        final Controller aController = (Controller) getApplicationContext();

        // Check if Internet Connection present
        if (!aController.isConnectingToInternet()) {

            // Internet Connection is not present
            aController.showAlertDialog(Login.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);

            // stop executing code by return
            return;
        }

        // Check if GCM configuration is set
        if (Config.YOUR_SERVER_URL == null || Config.GOOGLE_SENDER_ID == null || Config.YOUR_SERVER_URL.length() == 0
                || Config.GOOGLE_SENDER_ID.length() == 0) {

            // GCM sender id / server url is missing
            aController.showAlertDialog(Login.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);

            // stop executing code by return
            return;
        }

        String cardNo = etusername.getText().toString();
        // Check if user filled the form
        if (cardNo.trim().length() > 0) {
            // Launch Main Activity
            Intent i = new Intent(getApplicationContext(), Dashboard.class);
            i.putExtra("cardno", cardNo);
            startActivity(i);
            finish();

        } else {

            // user doen't filled that data
            // aController.showAlertDialog(Login.this, "Registration Error!", "Please enter your details", false);
        }
    }

    @Override
    public void onClick(View v) {
        login();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editor = preferences.edit();
        //String languageName = languages[position];
        switch (position) {
            case 1:
                selected = 1;
                setLocale("hi");
                break;

            default:
                selected = 0;
                setLocale("en");
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        etusername.setHint(R.string.username_edittext);
        pass.setHint(R.string.password_edittext);
        button.setText(R.string.login_button);
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        //((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        this.onConfigurationChanged(config);

        editor.clear();
        editor.putInt("key_lang", selected);
        editor.apply();
    }

    private void login() {
        final String username = etusername.getText().toString();
        final String password = pass.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int card_number = 0;
                String credits = "";
                String name = "";

                Log.i("TEST", "Response..." + response);
                if (response.contains("Failure")) {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials......." + response, Toast.LENGTH_LONG).show();
                } else if (response != null) {
                    Log.i("TEST", "Inside success...");

                    try {
                        Log.i("TEST", "Inside try...");
                        JSONObject obj = new JSONObject(response);
                        name = obj.getString("fname");
                        card_number = obj.getInt("user_card_number");
                        credits = obj.getString("u_credits");
                        Log.i("TEST", "Name:" + name + " Card Number: " + card_number + " Credits:" + credits);

                        user = new User(getApplicationContext());
                        user.addUserDetails(credits, card_number, name);
                        userLocalStore = new UserLocalStore(getApplicationContext());
                        userLocalStore.setLoggedInUser(true);
                        userLocalStore.storeUserData(username, password);

                        Intent intent = new Intent(Login.this, Dashboard.class);
                        intent.putExtra("cardno", etusername.getText().toString());

                        Log.d("TEST", "Value of fname:" + name);
                        Log.d("TEST", "Value of card number:" + card_number);
                        Log.d("TEST", "Value of credits:" + credits);

                        startActivity(intent);
                    } catch (Exception ex) {

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error......." + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}