package com.example.piotrskorupa.raspiweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText cityEdit, countryEdit;
    private Button saveButton;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cityEdit = (EditText) findViewById(R.id.city_edit_text);
        countryEdit = (EditText) findViewById(R.id.country_edit_text);
        saveButton = (Button) findViewById(R.id.save_button);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cityEdit.setText(sharedPref.getString("city", ""));
        countryEdit.setText(sharedPref.getString("country", ""));

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("city", cityEdit.getText().toString());
                editor.putString("country", countryEdit.getText().toString());
                editor.commit();

                Snackbar.make(view, "Data has been saved.",
                        Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }
}
