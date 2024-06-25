package com.diplom.qrstudent.Activities;

import  android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.diplom.qrstudent.API.ApiService;
import com.diplom.qrstudent.API.RetrofitClient;
import com.diplom.qrstudent.Models.UserDTO;
import com.diplom.qrstudent.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePwdActivity extends AppCompatActivity {

    private EditText oldPasswordEditText;
    private EditText newLoginEditText;
    private EditText newPasswordEditText;
    private Button confirmButton;

    private UserDTO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        getUserById();

        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newLoginEditText = findViewById(R.id.newLoginEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmButton = findViewById(R.id.confirmButton);

        oldPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int touchX = (int) event.getX();
                    int textEnd = oldPasswordEditText.getWidth() - oldPasswordEditText.getPaddingEnd();
                    int drawableEnd = textEnd - oldPasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();

                    if (touchX >= drawableEnd && touchX <= textEnd) {
                        if (oldPasswordEditText.getTransformationMethod() == null) {
                            oldPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
                            oldPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.no_eye_et, 0);
                        } else {
                            oldPasswordEditText.setTransformationMethod(null);
                            oldPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_et, 0);
                        }
                        oldPasswordEditText.setSelection(oldPasswordEditText.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });



        newPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int touchX = (int) event.getX();
                    int textEnd = newPasswordEditText.getWidth() - newPasswordEditText.getPaddingEnd();
                    int drawableEnd = textEnd - newPasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();

                    if (touchX >= drawableEnd && touchX <= textEnd) {
                        if (newPasswordEditText.getTransformationMethod() == null) {
                            newPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
                            newPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.no_eye_et, 0);
                        } else {
                            newPasswordEditText.setTransformationMethod(null);
                            newPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_et, 0);
                        }
                        newPasswordEditText.setSelection(newPasswordEditText.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });


        confirmButton.setOnClickListener(view -> updateSettings());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Эмулирует нажатие системной кнопки "назад"
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSettings() {
        String oldPassword = oldPasswordEditText.getText().toString();
        String newLogin = newLoginEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(this, "Please enter the old password", Toast.LENGTH_SHORT).show();
        } else if (newLogin.isEmpty() && !newPassword.isEmpty()) {
            updatePassword(oldPassword, newPassword);
        } else if (!newLogin.isEmpty() && newPassword.isEmpty()) {
            updateLogin(oldPassword, newLogin);
        } else if (!newLogin.isEmpty() && !newPassword.isEmpty()) {
            updateLoginAndPassword(oldPassword, newPassword, newLogin);
        } else {
            Toast.makeText(this, "Enter the fields correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePassword(String oldPassword, String newPassword) {
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
            Call<UserDTO> call = apiService.updateUser(user.getId(), user);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePwdActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });
        }
    }

    private void updateLogin(String oldPassword, String newLogin) {
        if (user.getPassword().equals(oldPassword)) {
            user.setUsername(newLogin);
            ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
            Call<UserDTO> call = apiService.updateUser(user.getId(), user);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePwdActivity.this, "Login updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLoginAndPassword(String oldPassword, String newPassword, String newLogin) {
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            user.setUsername(newLogin);
            ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
            Call<UserDTO> call = apiService.updateUser(user.getId(), user);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePwdActivity.this, "Settings updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserById() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        Call<UserDTO> call = apiService.getUserById(Long.valueOf(userId));
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                } else {
                    Toast.makeText(ChangePwdActivity.this, String.valueOf(response.errorBody()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(ChangePwdActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logOffUser() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwtToken");
        editor.remove("userId");
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
