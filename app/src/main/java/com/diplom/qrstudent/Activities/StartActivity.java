package com.diplom.qrstudent.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.diplom.qrstudent.R;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    ImageButton goBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        String defaultLanguage = Locale.getDefault().getLanguage();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        String savedLanguage = preferences.getString("selectedLanguage", defaultLanguage);
        if (!TextUtils.isEmpty(savedLanguage)) {
            updateLanguage(savedLanguage);
        }

        SharedPreferences themePrefs = getApplicationContext().getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkModeEnabled = themePrefs.getBoolean("isDarkModeEnabled", false);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        goBtn = findViewById(R.id.goBtn);


        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserAuthenticated()) {

                    startHomeActivity();
                }
                else{
                    startLoginActivity();
                }
            }
        });
    }


    private boolean isUserAuthenticated() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.contains("jwtToken");
    }

    private void startHomeActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedLanguage", language);
        editor.apply();

    }

}