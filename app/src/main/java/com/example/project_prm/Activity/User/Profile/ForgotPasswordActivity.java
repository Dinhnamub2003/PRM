package com.example.project_prm.Activity.User.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.UserViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnSend = findViewById(R.id.btnSend);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getResetPasswordResult().observe(this, result -> {
            if (result.isSuccess()) {
                Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show();
                navigateToResetScreen(result.getUserId());
            } else {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnSend.setOnClickListener(v -> onSendButtonClicked());
    }

    private void onSendButtonClicked() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }

        if (phone.isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            return;
        }
        userViewModel.resetPassword(username, email);
    }

    private void navigateToResetScreen(int userId) {
        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}