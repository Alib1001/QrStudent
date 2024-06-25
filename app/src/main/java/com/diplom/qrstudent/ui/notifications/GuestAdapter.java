package com.diplom.qrstudent.ui.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Models.UserDTO;
import com.diplom.qrstudent.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestViewHolder> {

    private List<UserDTO> guestList;

    public GuestAdapter(List<UserDTO> guestList) {
        this.guestList = guestList;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guest, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        UserDTO guest = guestList.get(position);
        holder.nameTextView.setText(guest.getFirstName() + " " + guest.getLastName());
        holder.descriptionTextView.setText(guest.getDescription());

        holder.verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyGuest(guest.getId(), v.getContext());
            }
        });

    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }

    public static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView verifiedTextView;
        Button verifyButton;
        Button declineButton;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.NameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            verifiedTextView = itemView.findViewById(R.id.textViewVerified);
            verifyButton = itemView.findViewById(R.id.VerifyBtn);
            declineButton = itemView.findViewById(R.id.DeclineBtn);
        }
    }

    private void verifyGuest(Long guestId, Context context) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService.class);        Call<Void> call = apiService.verifyGuest(guestId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Guest verified", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Failed to verify guest", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("GuestAdapter", "Failed to verify guest" + t.getMessage());
            }
        });
    }
}
