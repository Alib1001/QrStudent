package com.diplom.qrstudent.ui.settings;

import static android.content.Context.MODE_PRIVATE;

import static com.diplom.qrstudent.API.RetrofitClient.getRetrofitInstance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Activities.ChangePwdActivity;
import com.diplom.qrstudent.Activities.InfoActivity;
import com.diplom.qrstudent.Activities.LoginActivity;
import com.diplom.qrstudent.Models.UserDTO;
import com.diplom.qrstudent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {
    UserDTO user;
    Spinner languageSpinner;
    ImageView arrowChangePwd;
    ImageView arrowLogOff;
    ImageView photoImgView;
    ImageView arrowEditPhoto;

    TextView accountSettingsTv;
    TextView nameTextView;
    TextView editPhotoTv;
    TextView TvChangePassword;
    TextView TvLanguage;
    TextView tvLogOff;
    TextView tvMore;
    TextView tvAbout;
    TextView tvPrivacy;
    TextView tvTerms;
    Switch switchDarkMode;

    ImageView arrowAbout,arrowPrivacy,arrowTerms;


    private static final int PICK_IMAGE_REQUEST = 1234;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences themePrefs = getContext().getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkModeEnabled = themePrefs.getBoolean("isDarkModeEnabled", false);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        RelativeLayout settingsLayout = rootView.findViewById(R.id.settingsLayout);

        if (isDarkModeEnabled) {
            settingsLayout.setBackgroundResource(R.drawable.rounded_corner_dark);

        } else {
            settingsLayout.setBackgroundResource(R.drawable.rounded_corner);
        }



        String defaultLanguage = Locale.getDefault().getLanguage();
        SharedPreferences preferences = getContext().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        String savedLanguage = preferences.getString("selectedLanguage", defaultLanguage);
        languageSpinner = rootView.findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);


        int defaultLanguagePosition = getPositionForLanguage(savedLanguage);
        languageSpinner.setSelection(defaultLanguagePosition);

        BroadcastReceiver languageChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isAdded()) {
                    updateBottomNavigation();
                }
            }
        };
        IntentFilter filter = new IntentFilter("language-changed");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(languageChangeReceiver, filter);



        accountSettingsTv = rootView.findViewById(R.id.accountSettingsTv);
        arrowChangePwd = rootView.findViewById(R.id.arrowChangePwd);
        arrowLogOff = rootView.findViewById(R.id.arrowLogOff);
        photoImgView = rootView.findViewById(R.id.photoImgView);
        arrowEditPhoto = rootView.findViewById(R.id.arrowEditPhoto);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        editPhotoTv = rootView.findViewById(R.id.editPhotoTv);
        TvChangePassword = rootView.findViewById(R.id.TvChangePassword);
        TvLanguage = rootView.findViewById(R.id.TvLanguage);
        tvLogOff = rootView.findViewById(R.id.tvLogOff);
        tvMore = rootView.findViewById(R.id.tvMore);
        tvAbout = rootView.findViewById(R.id.tvAbout);
        tvPrivacy = rootView.findViewById(R.id.tvPrivacy);
        tvTerms = rootView.findViewById(R.id.tvTerms);
        switchDarkMode = rootView.findViewById(R.id.switchDarkMode);
        arrowPrivacy = rootView.findViewById(R.id.arrowPrivacy);
        arrowTerms = rootView.findViewById(R.id.arrowTerms);
        arrowAbout = rootView.findViewById(R.id.arrowAbout);

        checkDarkModeStatus();

        getUserById();


        arrowPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoActivity(getString(R.string.privacy_policy_title),
                        getString(R.string.privacy_policy_content));

            }
        });

        arrowTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoActivity(getString(R.string.terms_and_conditions_title),
                        getString(R.string.terms_and_conditions));

            }
        });

        arrowAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoActivity(getString(R.string.about_us_title), getString(R.string.about_us));
            }
        });


        arrowChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePwdActivity.class);
                startActivity(intent);
            }
        });

        arrowLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOffUser();
            }
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                updateLanguage(selectedLanguage);
                Log.e("savedLanguage1", selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        arrowEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableDarkMode();
                    saveThemeMode(true);
                } else {
                    disableDarkMode();
                    saveThemeMode(false);
                }
            }
        });




        return rootView;
    }

    private void enableDarkMode() {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public void getUserById() {
        SharedPreferences preferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        ApiService apiService = getRetrofitInstance(getContext()).create(ApiService.class);
        Call<UserDTO> call = apiService.getUserById(Long.valueOf(userId));
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    user = userDTO;
                    nameTextView.setText(user.getFirstName() + " " + user.getLastName());
                    Picasso.get()
                            .load(user.getImageUrl())
                            .fit().centerCrop()
                            .placeholder(R.drawable.ic_image_profile)
                            .error(R.drawable.ic_image_profile)
                            .into(photoImgView);

                } else {
                    Toast.makeText(getContext(), String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getContext(), String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void logOffUser() {
        SharedPreferences preferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwtToken");
        editor.remove("userId");
        editor.apply();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
    private int getPositionForLanguage(String language) {
        String[] languages = getResources().getStringArray(R.array.languages_array);
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(language)) {
                return i;
            }
        }
        return 0;
    }

    private void updateLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());

        SharedPreferences preferences = getContext().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedLanguage", language);
        editor.apply();

        Intent intent = new Intent("language-changed");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        accountSettingsTv.setText(R.string.account_settings);
        editPhotoTv.setText(R.string.edit_photo);
        TvChangePassword.setText(R.string.change_password);
        TvLanguage.setText(R.string.str_language);
        tvLogOff.setText(R.string.log_off);
        tvMore.setText(R.string.more);
        tvAbout.setText(R.string.about_us_title);
        tvPrivacy.setText(R.string.privacy_policy_title);
        tvTerms.setText(R.string.terms_and_conditions_title);
        switchDarkMode.setText(R.string.dark_mode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToServer(imageUri);
        }
    }

    private void uploadImageToServer(Uri imageUri) {
        SharedPreferences preferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            File file = createTempFile(inputStream);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            ApiService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(ApiService.class);
            Call<Void> call = apiService.uploadImage(userId, body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                        Log.e("Error", response.raw().toString());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error opening image file", Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempFile(InputStream inputStream) throws IOException {
        File file = File.createTempFile("file", ".jpg", getContext().getCacheDir());

        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    private void updateBottomNavigation() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.navigation_home).setTitle(R.string.title_id);
        menu.findItem(R.id.navigation_dashboard).setTitle(R.string.title_schedule);
        menu.findItem(R.id.navigation_news).setTitle(R.string.menu_news);
        menu.findItem(R.id.navigation_rooms).setTitle(R.string.menu_rooms);
        menu.findItem(R.id.navigation_settings).setTitle(R.string.title_settings);
    }

    private void saveThemeMode(boolean isDarkModeEnabled) {
        SharedPreferences preferences = getContext().getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDarkModeEnabled", isDarkModeEnabled);
        editor.apply();
    }

    private void checkDarkModeStatus() {
        int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switchDarkMode.setChecked(nightMode == Configuration.UI_MODE_NIGHT_YES);
    }

    private void openInfoActivity(String title, String content){
        Intent intent = new Intent(getContext(), InfoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivity(intent);
    }
}
