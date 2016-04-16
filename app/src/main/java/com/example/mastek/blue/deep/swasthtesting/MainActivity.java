package com.example.mastek.blue.deep.swasthtesting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public UserLocalStore userLocalStore;
    Button btnLogin;
    Intent intent;
    Spinner languageSpinner;
    private int selected;
    private String[] languages = {"English", "हिन्दी"};
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("lang_info", Context.MODE_PRIVATE);
        selected = preferences.getInt("key_lang", 0);

        setContentView(R.layout.activity_main);

        languageSpinner = (Spinner) findViewById(R.id.languageSpinner);
        languageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(selected);
        btnLogin = (Button) findViewById(R.id.btnStartLogin);
        btnLogin.setOnClickListener(this);
        // Button beginFeedbackButton = (Button) findViewById(R.id.beginFeedbackButton);
        // beginFeedbackButton.setOnClickListener(this);
        userLocalStore = new UserLocalStore(getApplicationContext());
        boolean status = userLocalStore.getLoginStatus();
//        Intent intent = new Intent(MainActivity.this, Dashboard.class);
//        Log.d("TEST", userLocalStore.getLoginStatus() + "");
//        if (status) {
//            Log.d("TEST", "SharedPref status" + status);
//            startActivity(intent);
//        }

    }

    @Override
    public void onClick(View v) {
        int x = v.getId();
        switch (x) {
            case R.id.btnStartLogin:
                intent = new Intent(this, Login.class);
                startActivity(intent);
//                finish();
                break;

//            case R.id.beginFeedbackButton:
//                intent = new Intent(this, FeedbackActivity.class);
//                startActivity(intent);
//                finish();
//                break;
        }

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
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        //((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        btnLogin.setText(R.string.login_button);
        this.onConfigurationChanged(config);

        editor.clear();
        editor.putInt("key_lang", selected);
        editor.apply();
    }
}
