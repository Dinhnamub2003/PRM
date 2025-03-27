package com.example.project_prm.Activity.User.LiveStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

public class LiveActivity extends AppCompatActivity {

    String userID, name, liveID;
    boolean isHost;

    TextView txtLiveId;
    ImageView btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live);


        txtLiveId = findViewById(R.id.txtLive);
        btnShare = findViewById(R.id.btnShare);

// Lấy dữ liệu từ Intent
        userID = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        liveID = getIntent().getStringExtra("live_id");
        isHost = getIntent().getBooleanExtra("host", false);  // Mặc định là false nếu không có giá trị

// Hiển thị Live ID trên TextView
        txtLiveId.setText(liveID);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addFragment();
                    }
                });
            }
        }).start();


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);  // Thiết lập hành động chia sẻ
                intent.setType("text/plain");  // Định dạng nội dung là văn bản

                // Thêm nội dung cần chia sẻ (Live ID)
                intent.putExtra(Intent.EXTRA_TEXT, "Join my Live, Live ID - " + liveID);

                // Chọn ứng dụng để chia sẻ thông qua một chooser
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });


    }


    void addFragment() {
        ZegoUIKitPrebuiltLiveStreamingConfig config;

        if (isHost) {
            config = ZegoUIKitPrebuiltLiveStreamingConfig.host();
        } else {
            config = ZegoUIKitPrebuiltLiveStreamingConfig.audience();
        }

        ZegoUIKitPrebuiltLiveStreamingFragment fragment =
                ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                        Constant.AppID,
                        Constant.AppSign,
                        userID,
                        name,
                        liveID,
                        config
                );

        // Thêm Fragment vào container
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.liveContainer, fragment)
                .commitNow();
    }
}
