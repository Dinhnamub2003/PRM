package com.example.project_prm.Activity.User.LiveStream;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.UUID;

public class LiveStream extends AppCompatActivity {

    Button btnStartNewLive;
    TextInputEditText edtLiveId, edtName;

    String liveId, name, userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_stream);


        btnStartNewLive = findViewById(R.id.btnStartLive);
        edtLiveId = findViewById(R.id.edtLiveId);
        edtName = findViewById(R.id.edtName);

        edtLiveId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String liveId = edtLiveId.getText().toString();

                if (liveId.length() == 0) {
                    btnStartNewLive.setText("Start Live");
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnStartNewLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String liveId = edtLiveId.getText().toString();

                if (name.isEmpty()) {
                    edtName.setError("Name is Required");
                    edtName.requestFocus();
                    return;
                }

                if (liveId.length() > 0 && liveId.length() != 5) {
                    edtLiveId.setError("Invalid Live Id");  // Hiển thị lỗi nếu Live ID không hợp lệ
                    edtLiveId.requestFocus();  // Yêu cầu focus vào trường Live ID
                    return;  // Dừng việc tiếp tục nếu lỗi
                }

                // Nếu mọi thứ hợp lệ, gọi phương thức bắt đầu cuộc họp
                startMeeting();
            }
        });


    }


    void startMeeting() {
        boolean isHost = true;

        // Kiểm tra nếu liveId bị null
        if (liveId == null || liveId.isEmpty()) {
            liveId = generateLiveId();  // Tạo một Live ID mới nếu không có
        } else if (liveId.length() == 5) {
            isHost = false;
        }

        // Kiểm tra nếu name bị null
        if (name == null || name.isEmpty()) {
            name = "Unknow"; // Gán giá trị mặc định nếu name bị null
        }
        name = edtName.getText().toString();

        String userID = UUID.randomUUID().toString();  // Tạo một UUID cho người dùng

        Intent intent = new Intent(getApplicationContext(), LiveActivity.class);
        intent.putExtra("user_id", userID);
        intent.putExtra("name", name);
        intent.putExtra("live_id", liveId);
        intent.putExtra("host", isHost);

        startActivity(intent); // Thêm dòng này để thực sự chuyển sang LiveActivity
    }

    String generateLiveId() {
        StringBuilder id = new StringBuilder();
        while (id.length() < 5) {
            int random = new Random().nextInt(10);  // Tạo số ngẫu nhiên từ 0 đến 9
            id.append(random);
        }
        return id.toString();  // Trả về ID ngẫu nhiên dài 5 ký tự
    }
}