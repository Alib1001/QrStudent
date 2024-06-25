package com.diplom.qrstudent.ui.rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Models.Room;
import com.diplom.qrstudent.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoomsAdapter adapter;
    private List<Room> roomList;

    public RoomsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rooms, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_rooms);

        roomList = new ArrayList<>();
        adapter = new RoomsAdapter(roomList);
        recyclerView.setAdapter(adapter);

        updateToolbarText();
        fetchRooms();

        return root;
    }

    private void fetchRooms() {
        ApiService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(ApiService.class);
        Call<List<Room>> call = apiService.getFreeRooms(); // Assuming you have implemented getFreeRooms() method

        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void updateToolbarText() {
        if (requireActivity() != null && requireActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.menu_rooms);
            }
        }
    }

}
