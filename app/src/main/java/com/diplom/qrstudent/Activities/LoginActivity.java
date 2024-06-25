package com.diplom.qrstudent.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = RetrofitClient.getRetrofitInstance(getApplicationContext())
                .create(ApiService.class);

        if (isUserAuthenticated()) {
            startHomeActivity();
        }

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int touchX = (int) event.getX();
                    int textEnd = etPassword.getWidth() - etPassword.getPaddingEnd();
                    int drawableEnd = textEnd - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();

                    if (touchX >= drawableEnd && touchX <= textEnd) {
                        if (etPassword.getTransformationMethod() == null) {
                            etPassword.setTransformationMethod(new PasswordTransformationMethod());
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.no_eye_et, 0);
                        } else {
                            etPassword.setTransformationMethod(null);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_et, 0);
                        }
                        etPassword.setSelection(etPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                Call<Map<String, String>> loginCall = apiService.loginUser(username, password);
                loginCall.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful()) {
                            Map<String, String> responseBody = response.body();
                            if (responseBody != null && responseBody.containsKey("message") &&
                                    responseBody.containsKey("token") &&
                                    responseBody.containsKey("userType") &&
                                    responseBody.get("userType").equals("Student")) {
                                String jwtToken = responseBody.get("token");
                                int userId = Integer.parseInt(responseBody.get("userId"));
                                saveJwtToken(jwtToken, userId);
                                sendFCMTokenToServer();
                                startHomeActivity();
                            } else if (responseBody != null && responseBody.containsKey("error")) {
                                Toast.makeText(LoginActivity.this, "Login failed: " + responseBody.get("error"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    }
                });
            }
        });
    }

    private void saveJwtToken(String jwtToken, int userId) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("jwtToken", jwtToken);
        editor.putInt("userId", userId);
        editor.apply();
    }

    private void sendFCMTokenToServer() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String fcmToken = task.getResult();
                    int userId = getUserIdFromPreferences();
                    if (userId != -1) {
                        apiService.sendFcmToken((long) userId, fcmToken).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("FCM Token", "Sent to server successfully");
                                } else {
                                    Log.e("FCM Token", "Failed to send to server: " + response.toString() + fcmToken);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("FCM Token", "Failed to send to server: " + t.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("FCM Token", "Failed to retrieve token: " + task.getException().getMessage());
                }
            }
        });
    }

    private int getUserIdFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }

    private boolean isUserAuthenticated() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.contains("jwtToken");
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
