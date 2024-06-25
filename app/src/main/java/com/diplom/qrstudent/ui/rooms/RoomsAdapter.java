package com.diplom.qrstudent.ui.rooms;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.diplom.qrstudent.Models.Room;
import com.diplom.qrstudent.R;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {

    private List<Room> rooms;

    public RoomsAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.textRoomName.setText(String.format("%s %s", holder.itemView.getContext().getString(R.string.room_label), room.getRoomName()));
        holder.textBuilding.setText(String.format("%s %s", holder.itemView.getContext().getString(R.string.frame_label), room.getBuilding()));
        if (room.getBusy()){
            holder.textBusy.setTextColor(Color.parseColor("#ffcc0000"));
            holder.textBusy.setText(String.format(holder.itemView.getContext().getString(R.string.busy_true)));
        }
        else if (!room.getBusy()){
            holder.textBusy.setTextColor(Color.parseColor("#ff669900"));
            holder.textBusy.setText(String.format(holder.itemView.getContext().getString(R.string.busy_false)));
        }

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView textRoomName, textBuilding, textBusy;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            textRoomName = itemView.findViewById(R.id.text_room_name);
            textBuilding = itemView.findViewById(R.id.text_building);
            textBusy = itemView.findViewById(R.id.text_busy);
        }
    }
}
