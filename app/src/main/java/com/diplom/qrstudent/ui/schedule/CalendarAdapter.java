package com.diplom.qrstudent.ui.schedule;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.diplom.qrstudent.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<String> calendarDates;
    private List<String> calendarDays;
    private List<Integer> weekNumbers;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public CalendarAdapter(List<String> calendarDays, List<String> calendarDates) {
        this.calendarDays = calendarDays;
        this.calendarDates = calendarDates;
        this.weekNumbers = new ArrayList<>();
        calculateWeekNumbers();
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.bind(calendarDays.get(position), calendarDates.get(position), weekNumbers.get(position));

        holder.itemView.setSelected(selectedPosition == position);
        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.calendar_item_bg_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.calendar_item_bg_normal);
        }

    }

    @Override
    public int getItemCount() {
        return calendarDates.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView calendarDateTextView, calendarDayTextView;

        CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            calendarDateTextView = itemView.findViewById(R.id.calendarDateTextView);
            calendarDayTextView = itemView.findViewById(R.id.calendarDayTextView);
            itemView.setOnClickListener(this);
        }

        void bind(String calendarDay, String calendarDate, int weekNumber) {
            int index = calendarDate.indexOf('-');
            if (index != -1) {
                String textBeforeDash = calendarDate.substring(0, index);
                calendarDateTextView.setText(textBeforeDash);
            } else {
                calendarDateTextView.setText(calendarDate);
            }

            calendarDayTextView.setText(calendarDay);

        }

        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && currentPosition < calendarDays.size()) {
                setSelectedPosition(currentPosition);
                if (onDaySelectedListener != null) {
                    onDaySelectedListener.onDaySelected(calendarDays.get(currentPosition));
                }
            }
        }


    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public String getSelectedDayOfWeek() {
        if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < calendarDays.size()) {
            return calendarDays.get(selectedPosition);
        }
        return null;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public int getWeekNumberForPosition(int position) {
        if (position >= 0 && position < weekNumbers.size()) {
            return weekNumbers.get(position);
        }
        return -1;
    }


    public interface OnDaySelectedListener {
        void onDaySelected(String selectedDate);
    }


    private OnDaySelectedListener onDaySelectedListener;

    public void setOnDaySelectedListener(OnDaySelectedListener listener) {
        this.onDaySelectedListener = listener;
    }

    public interface OnCalendarScrollListener {
        void onCalendarScrolled(int position);
    }

    private OnCalendarScrollListener onCalendarScrollListener;

    public void setOnCalendarScrollListener(OnCalendarScrollListener listener) {
        this.onCalendarScrollListener = listener;
    }

    private void calculateWeekNumbers() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int currentWeek = 1;
        for (String day : calendarDays) {
            if (day.equalsIgnoreCase("Mon")
                    || day.equalsIgnoreCase("пн")
                    || day.equalsIgnoreCase("дс")){
                weekNumbers.add(currentWeek++);
            } else {
                weekNumbers.add(currentWeek);
            }
        }
    }

    public String getSelectedDate() {
        if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < calendarDates.size()) {
            return calendarDates.get(selectedPosition);
        }
        return null;
    }



}