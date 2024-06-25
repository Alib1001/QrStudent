package com.diplom.qrstudent.ui.schedule;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.qrstudent.Models.TimeTable;
import com.diplom.qrstudent.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<TimeTable> timeTableList;

    public ScheduleAdapter(List<TimeTable> timeTableList) {
        this.timeTableList = timeTableList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeTable timeTable = timeTableList.get(position);
        holder.bind(timeTable);
    }

    @Override
    public int getItemCount() {
        return timeTableList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStartTime;
        TextView textViewEndTime;
        TextView textViewSubjectName;
        TextView textViewRoom;
        TextView textViewAttendanceStatus;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTime);
            textViewSubjectName = itemView.findViewById(R.id.textViewSubjectName);
            textViewRoom = itemView.findViewById(R.id.textViewRoom);
            textViewAttendanceStatus = itemView.findViewById(R.id.textViewAttendanceStatus);

        }

        public void bind(TimeTable timeTable) {
            textViewStartTime.setText(timeTable.getStartTime());
            textViewEndTime.setText(timeTable.getEndTime());
            textViewSubjectName.setText(timeTable.getSubjectName());

            textViewRoom.setText("Room: " + timeTable.getClassroom().getRoomName()+", "
                    + timeTable.getClassroom().getBuilding());

            Date currentTime = new Date();
            try {
                Date lessonStartTime = sdf.parse(timeTable.getDate() + " " + timeTable.getStartTime());
                if (currentTime.before(lessonStartTime)) {
                    textViewAttendanceStatus.setText(R.string.attendance_soon);
                    textViewAttendanceStatus.setTextColor(Color.parseColor("#ff669900"));
                } else {
                    if (timeTable.getAttendance()){
                        textViewAttendanceStatus.setText(R.string.attendance_true);
                        textViewAttendanceStatus.setTextColor(Color.parseColor("#ff669900"));
                    } else {
                        textViewAttendanceStatus.setText(R.string.attendance_false);
                        textViewAttendanceStatus.setTextColor(Color.parseColor("#ffcc0000"));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
