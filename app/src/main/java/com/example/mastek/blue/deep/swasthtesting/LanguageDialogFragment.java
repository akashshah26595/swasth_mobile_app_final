package com.example.mastek.blue.deep.swasthtesting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

//edit shared preferences

public class LanguageDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner languageSpinner;
    private String[] languages = {"English", "हिन्दी"};
    private int selected;
    private Locale myLocale;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        preferences = this.getActivity().getSharedPreferences("lang_info", Context.MODE_PRIVATE);
        selected = preferences.getInt("key_lang", 0);

        String title = getResources().getString(R.string.choose_language_button);
        builder.setTitle(title);

        //Toast.makeText(getContext(), " " + selected, Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        languageSpinner = (Spinner) view.findViewById(R.id.languageSpinner);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setOnItemSelectedListener(this);
//      load shared preferences
        languageSpinner.setSelection(selected);

        Button okButton = (Button) view.findViewById(R.id.okButton);
        okButton.setOnClickListener(this);
        return builder.create();
    }

    private void selectLanguage(int position) {
        switch (position) {
            case 1:
          //      Toast.makeText(getContext(), " " + selected, Toast.LENGTH_SHORT).show();
                setLocale("hi");
                break;
            default:
            //    Toast.makeText(getContext(), " " + selected, Toast.LENGTH_SHORT).show();
                setLocale("en");
                break;
        }
    }

    public void setLocale(String lang) {
        Configuration config = getActivity().getResources().getConfiguration();

        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        config.locale = myLocale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
        //onConfigurationChanged(config);  //not getting overridden
        this.onConfigurationChanged(config);


    }

    @Override
    public void onClick(View v) {
        selectLanguage(selected);
        editor = preferences.edit();

        editor.clear();
        editor.putInt("key_lang", selected);
        editor.apply();

        getActivity().recreate();
        dismiss();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                this.selected = 1;
                break;
            default:
                this.selected = 0;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
