package com.diplom.qrstudent.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Models.UserDTO;
import com.diplom.qrstudent.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GuestAdapter guestAdapter;
    private List<UserDTO> guestList;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_notifications);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.menu_notifications);
        }

        SharedPreferences idPrefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = idPrefs.getInt("userId", -1);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        fetchGuests();
    }

    private void fetchGuests() {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        Call<List<UserDTO>> call = apiService.getGuests();

        call.enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserDTO>> call, @NonNull Response<List<UserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    guestList = response.body();
                    Log.d("NotificationsActivity", "Number of guests received: " + guestList.size());

                    List<UserDTO> filteredGuestList = filterGuests(guestList);

                    guestAdapter = new GuestAdapter(filteredGuestList);
                    recyclerView.setAdapter(guestAdapter);
                } else {
                    Log.e("NotificationsActivity", "Failed to fetch guests");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserDTO>> call, @NonNull Throwable t) {
                Log.e("NotificationsActivity", "Error: " + t.getMessage());
            }
        });
    }

    private List<UserDTO> filterGuests(List<UserDTO> guestList) {
        List<UserDTO> filteredList = new ArrayList<>();
        for (UserDTO guest : guestList) {
            if (guest.getUser() != null && guest.getUser().getId() == userId && !guest.getVerified()) {
                filteredList.add(guest);
            }
        }
        return filteredList;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
