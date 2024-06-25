package com.diplom.qrstudent.Activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;

import com.diplom.qrstudent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.diplom.qrstudent.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "FCMTokenTag";
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isNotificationPermissionGranted()) {
            requestNotificationPermission();
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        String defaultLanguage = Locale.getDefault().getLanguage();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        String savedLanguage = preferences.getString("selectedLanguage", defaultLanguage);
        if (!TextUtils.isEmpty(savedLanguage)) {
            updateLanguage(savedLanguage);
            Menu menu = navView.getMenu();
            menu.findItem(R.id.navigation_home).setTitle(R.string.title_id);
            menu.findItem(R.id.navigation_dashboard).setTitle(R.string.title_schedule);
            menu.findItem(R.id.navigation_news).setTitle(R.string.menu_news);
            menu.findItem(R.id.navigation_rooms).setTitle(R.string.menu_rooms);
            menu.findItem(R.id.navigation_settings).setTitle(R.string.title_settings);

        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        SharedPreferences themePrefs = getApplicationContext().getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkModeEnabled = themePrefs.getBoolean("isDarkModeEnabled", false);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

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

    private boolean isNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            return notificationManager.areNotificationsEnabled();
        } else {
            return NotificationManagerCompat.from(this).areNotificationsEnabled();
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivityForResult(intent, REQUEST_CODE_NOTIFICATION_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_CODE_NOTIFICATION_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (isNotificationPermissionGranted()) {
                Log.d(TAG, "Notification permission granted");
            } else {
                Log.d(TAG, "Notification permission denied");
            }
        }
    }
}
