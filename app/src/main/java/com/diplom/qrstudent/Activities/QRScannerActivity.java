package com.diplom.qrstudent.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Models.TurnstileHistory;
import com.diplom.qrstudent.R;
import com.diplom.qrstudent.ui.schedule.ScheduleFragment;
import com.google.zxing.Result;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private ZXingScannerView scannerView;
    private ApiService apiService;
    String codeInUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        scannerView = findViewById(R.id.scannerView);
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }

        getQRCode();
    }

    private void startScanner() {
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {

        String url = result.getText();
        if (url.equals(codeInUrl)) {
            scanUserOnBackend();
        } else if (url.contains("timetableID")) {
            handleTimetableID(url);
        } else {
            Toast.makeText(this, "QR code not recognized", Toast.LENGTH_SHORT).show();
        }

        scannerView.resumeCameraPreview(this);
    }

    private void handleTimetableID(String url) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        String[] parts = url.split("timetableID:");
        if (parts.length > 1) {
            try {
                int timetableID = Integer.parseInt(parts[1]);
                checkScanableAndAddStudentToTimetable(userId, timetableID);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkScanableAndAddStudentToTimetable(int userId, int timetableID) {
        Call<Boolean> call = apiService.checkTimetableScanable((int) timetableID);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean isScanable = response.body();
                    if (isScanable) {
                        addStudentToTimetable(userId, timetableID);
                    } else {
                        Toast.makeText(getApplicationContext(), "This timetable is not currently scanable", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error checking timetable scanable status", Toast.LENGTH_SHORT).show();
                    Log.e("Error checking timetable scanable status", response.raw().toString());
                }
                scannerView.resumeCameraPreview(QRScannerActivity.this);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error checking timetable scanable status", Toast.LENGTH_SHORT).show();
                scannerView.resumeCameraPreview(QRScannerActivity.this);
            }
        });
    }


    private void addStudentToTimetable(int userId, int timetableID) {
        Call<Void> call = apiService.addStudentToTimetable((long) timetableID, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Student added to timetable successfully", Toast.LENGTH_SHORT).show();
                    startHomeActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Error adding student to timetable", Toast.LENGTH_SHORT).show();
                }
                scannerView.resumeCameraPreview(QRScannerActivity.this);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error adding student to timetable", Toast.LENGTH_SHORT).show();
                scannerView.resumeCameraPreview(QRScannerActivity.this);
            }
        });
    }



    private void scanUserOnBackend() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        Call<TurnstileHistory> call = apiService.scanUser(userId);
        call.enqueue(new Callback<TurnstileHistory>() {
            @Override
            public void onResponse(Call<TurnstileHistory> call, Response<TurnstileHistory> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Qr код отсканирован успешно",Toast.LENGTH_SHORT).show();
                    startHomeActivity();
                } else {
                    Toast.makeText(getApplicationContext(),"Ошибка запроса",Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<TurnstileHistory> call, Throwable t) {
                Log.e("QrError",t.toString());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        startScanner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                Toast.makeText(this, "Camera permission denied. Cannot scan QR code.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void startScheduleActivity(){
        Intent intent = new Intent(getApplicationContext(), ScheduleFragment.class);
        startActivity(intent);
    }

    private void getQRCode() {
        apiService.getDynamicQRCode().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String qrCode = response.body();
                    codeInUrl = qrCode;
                } else {
                    Log.e("QRCodeError", "Failed to fetch QR code");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("QRCodeError", "Error fetching QR code: " + t.getMessage());
            }
        });
    }

}
