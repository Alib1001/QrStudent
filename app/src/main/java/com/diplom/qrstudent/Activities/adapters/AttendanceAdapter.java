package com.diplom.qrstudent.Activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private Context context;
    private List<TimeTable> timetableList;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());


    public AttendanceAdapter(Context context, List<TimeTable> timetableList) {
        this.context = context;
        this.timetableList = timetableList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeTable currentItem = timetableList.get(position);

        holder.dateTextView.setText(currentItem.getDate());
        holder.subjectNameTextView.setText(currentItem.getSubjectName());
        Date currentTime = new Date();
        try {
            Date lessonStartTime = sdf.parse(currentItem.getDate() + " " + currentItem.getStartTime());
            if (currentTime.before(lessonStartTime)) {
                holder.attendanceStatusTextView.setText(R.string.attendance_soon);
                holder.attendanceStatusTextView.setTextColor(Color.parseColor("#ff669900"));
            } else {
                if (currentItem.getAttendance()){
                    holder.attendanceStatusTextView.setText(R.string.attendance_true);
                    holder.attendanceStatusTextView.setTextColor(Color.parseColor("#ff669900"));
                } else {
                    holder.attendanceStatusTextView.setText(R.string.attendance_false);
                    holder.attendanceStatusTextView.setTextColor(Color.parseColor("#ffcc0000"));
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return timetableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView subjectNameTextView;
        TextView attendanceStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            attendanceStatusTextView = itemView.findViewById(R.id.attendanceStatusTextView);
        }
    }
}
