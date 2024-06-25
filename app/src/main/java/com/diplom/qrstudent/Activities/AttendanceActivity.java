package com.diplom.qrstudent.Activities;

import static com.diplom.qrstudent.ui.home.HomeFragment.GROUPSTR;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Activities.adapters.AttendanceAdapter;
import com.diplom.qrstudent.Models.TimeTable;
import com.diplom.qrstudent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {
    int userId;
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<TimeTable> timetableList = new ArrayList<>();
    private Toolbar toolbar;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;

    private TextView percentageTextView;

    double attendancePercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.title_attendance);

        SharedPreferences idPrefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = idPrefs.getInt("userId", -1);

        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.recyclerView);
        percentageTextView = findViewById(R.id.percentageTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceAdapter(this, timetableList);
        recyclerView.setAdapter(adapter);

        setupSpinner();

    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = (String) parent.getItemAtPosition(position);
                if (selectedSubject != null) {
                    loadTimetableData(selectedSubject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getSubjectNamesForGroup();
    }

    private void getSubjectNamesForGroup() {
        ApiService apiService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<List<String>> call = apiService.getSubjectNamesForGroup(GROUPSTR);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> subjects = response.body();
                    if (subjects != null) {
                        spinnerAdapter.clear();
                        spinnerAdapter.addAll(subjects);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(AttendanceActivity.this, "Error while loading data about subjects", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Error while loading data subjects", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTimetableData(String selectedSubject) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<List<TimeTable>> call = apiService.getSchedule();

        call.enqueue(new Callback<List<TimeTable>>() {
            @Override
            public void onResponse(Call<List<TimeTable>> call, Response<List<TimeTable>> response) {
                if (response.isSuccessful()) {
                    List<TimeTable> timetableData = response.body();
                    if (timetableData != null) {
                        int totalClasses = 0;
                        int attendedClasses = 0;
                        timetableList.clear();
                        for (TimeTable timetable : timetableData) {
                            if (timetable.getSubjectName().equals(selectedSubject) && timetable.getGroupName().equals(GROUPSTR)) {
                                totalClasses++;
                                boolean containsUserId = timetable.getStudentIds().contains(userId);
                                if (containsUserId) {
                                    attendedClasses++;
                                }
                                timetable.setAttendance(containsUserId);
                                timetableList.add(timetable);
                            }
                        }
                        Collections.sort(timetableList, new Comparator<TimeTable>() {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                            @Override
                            public int compare(TimeTable o1, TimeTable o2) {
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date1 = sdf.parse(o1.getDate());
                                    date2 = sdf.parse(o2.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date1 != null && date2 != null) {
                                    return date1.compareTo(date2);
                                }
                                return 0;
                            }
                        });

                        adapter.notifyDataSetChanged();

                        attendancePercentage = ((double) attendedClasses / totalClasses) * 100;
                        percentageTextView.setText(String.format(Locale.getDefault(), "%.2f%%", attendancePercentage));

                    }
                } else {
                    Toast.makeText(AttendanceActivity.this, "Error while loading data about timetable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TimeTable>> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Error while loading data about timetable", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
