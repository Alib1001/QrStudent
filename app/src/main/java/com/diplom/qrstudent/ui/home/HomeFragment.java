package com.diplom.qrstudent.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Models.UserDTO;
import com.diplom.qrstudent.Activities.QRScannerActivity;
import com.diplom.qrstudent.R;
import com.diplom.qrstudent.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    UserDTO user;
    private ImageView profileImageView;
    private TextView textViewName, textViewUserType, idTextview, groupTextView;
    private ImageButton scanBtn;
    public static String GROUPSTR;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        String defaultLanguage = Locale.getDefault().getLanguage();
        SharedPreferences preferences = getContext().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        String savedLanguage = preferences.getString("selectedLanguage", defaultLanguage);
        if (!TextUtils.isEmpty(savedLanguage)) {
            updateLanguage(savedLanguage);
        }

        getUserById();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        profileImageView = root.findViewById(R.id.profileImageView);
        textViewName = root.findViewById(R.id.textViewName);
        textViewUserType = root.findViewById(R.id.textViewUserType);
        idTextview = root.findViewById(R.id.idTextview);
        groupTextView = root.findViewById(R.id.groupTextView);
        scanBtn = root.findViewById(R.id.scanBtn);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRScannerActivity.class);
                startActivity(intent);

            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getUserById() {

        SharedPreferences preferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        ApiService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(ApiService.class);
        Call<UserDTO> call = apiService.getUserById(Long.valueOf(userId));
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    user = userDTO;
                    if (user != null){
                        textViewName.setText(user.getFirstName() + " " + user.getLastName());
                        textViewUserType.setText(user.getUserType());
                        idTextview.setText("ID №:    " + user.getId());

                        groupTextView.setText(user.getGroupName());
                        Picasso.get()
                                .load(user.getImageUrl())
                                .fit().centerCrop()
                                .placeholder(R.drawable.ic_image_profile)
                                .error(R.drawable.ic_image_profile)
                                .into(profileImageView);
                        GROUPSTR = user.getGroupName();

                    }
                } else {
                    Toast.makeText(getContext(),String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getContext(),String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();

            }
        });
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

    }


}