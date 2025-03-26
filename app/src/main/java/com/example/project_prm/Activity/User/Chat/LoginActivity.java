package com.example.project_prm.Activity.User.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_prm.R;
import com.zegocloud.zimkit.services.ZIMKit;

import im.zego.zim.enums.ZIMErrorCode;

public class LoginActivity extends AppCompatActivity {

    EditText userIdInput;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_chat);

        // Khởi tạo Zego SDK
        ZIMKit.initWith(this.getApplication(), ConstantKey.appID, ConstantKey.appSign);
        ZIMKit.initNotifications();



        // Khởi tạo các view
        userIdInput = findViewById(R.id.userid_input);
        loginbtn = findViewById(R.id.login_btn);

        // Xử lý sự kiện khi nhấn nút Login
        loginbtn.setOnClickListener(v -> {
            String userId = userIdInput.getText().toString();
            if (userId.isEmpty()) {
                Toast.makeText(this, "Please enter a valid UserID", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi phương thức connectUser để đăng nhập
            connectUser(userId, userId, "");




        });
    }

    // Phương thức đăng nhập người dùng
    public void connectUser(String userId, String userName, String userAvatar) {
        ZIMKit.connectUser(userId, userName, userAvatar, errorInfo -> {
            if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                // Nếu kết nối thành công, chuyển sang ConversationActivity
                toConversationActivity();
            } else {
                // Hiển thị thông báo lỗi bằng Toast
                String errorMessage = "Login failed with error code: " + errorInfo.code;
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Phương thức chuyển sang Activity Conversation
    private void toConversationActivity() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }
}
