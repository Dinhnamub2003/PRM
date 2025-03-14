package com.example.project_prm.Activity.Admin.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Entities.Category;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.Entities.Role;
import com.example.project_prm.Entities.User;
import com.example.project_prm.R;
import com.example.project_prm.Repository.CategoryRepository;
import com.example.project_prm.Repository.ProductRepository;
import com.example.project_prm.Repository.RoleRepository;
import com.example.project_prm.Repository.UserRepository;
import com.example.project_prm.Utils.ImageUtils;
import com.example.project_prm.ViewModel.Admin.ManageAccountViewModel;
import com.example.project_prm.ViewModel.Admin.ManageProductViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditAccountActivity extends AppCompatActivity {

    private EditText etUserName, etPassword, etPhone, etMail, etAddress;
    private ImageView imgUserEdit;
    private User existingUser;
    private Spinner spinnerRole;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private Button btnSave, btnUpload, btnBack;
    private ActivityResultLauncher<Intent> imagePicker;
    private ManageAccountViewModel manageAccountViewModel;
    private List<Role> roleList = new ArrayList<>();
    private String imagePath ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        etUserName = findViewById(R.id.etUserNameEdit);

        etPassword = findViewById(R.id.etPasswordEdit);
        etPhone = findViewById(R.id.etPhoneNumberEdit);
        etMail   = findViewById(R.id.etMailEdit);
        etAddress = findViewById(R.id.etAddressEdit);
        imgUserEdit = findViewById(R.id.imgUserEdit);
        spinnerRole = findViewById(R.id.spinnerRoleEdit);
        btnSave = findViewById(R.id.btnSaveUserEdit);
        btnUpload = findViewById(R.id.btnUploadImageEdit);
        btnBack = findViewById(R.id.btnBackEdit);
        btnSave.setOnClickListener(v -> saveUser());
        btnUpload.setOnClickListener(v -> openFileChooser());
        btnBack.setOnClickListener(v -> finish());

        manageAccountViewModel = new ViewModelProvider(this).get(ManageAccountViewModel.class);

        // Khởi tạo repository
        userRepository = new UserRepository(getApplication());
        roleRepository = new RoleRepository(getApplication());

        int productId = getIntent().getIntExtra("USER_ID", -1);
        if (productId != -1) {
            loadUserData(productId);
        }else{
            Toast.makeText(this, "PLease choose product to edit!", Toast.LENGTH_SHORT).show();
        }

        setupImagePicker();
    }

    private void loadUserData(int userId) {
        ManageAccountViewModel manageAccountViewModel1 = new ViewModelProvider(this).get(ManageAccountViewModel.class);
        manageAccountViewModel1.getUserById(userId).observe(this, this::updateUI);
    }
    private void setupImagePicker() {
        imagePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                Bitmap bitmap;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImageUri);
                                    bitmap = ImageDecoder.decodeBitmap(source);
                                } else {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                }

                                // Tạo tên file ảnh duy nhất
                                String fileName = "user_" + System.currentTimeMillis() + ".png";

                                // Lưu ảnh vào bộ nhớ trong và lấy đường dẫn
                                imagePath = ImageUtils.saveImageToInternalStorage(this, bitmap, fileName);

                                // Hiển thị ảnh lên ImageView
                                imgUserEdit.setImageBitmap(bitmap);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Error when choose image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePicker.launch(intent);
    }
    private void updateUI(User user ) {
        if (user != null) {
            existingUser = user;
            etUserName.setText(user.getUsername());
            etPassword.setText(user.getPassword());
            etAddress.setText(user.getAddress());
            etPhone.setText(user.getPhone());
            etMail.setText(user.getGmail());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                File imageFile = new File(user.getImage());
                if (imageFile.exists()) {
                    imgUserEdit .setImageURI(Uri.fromFile(imageFile)); // Hiển thị ảnh từ file nội bộ
                    imagePath = user.getImage();
                }
            }


            if (spinnerRole != null) {
                loadSpinnerRole(user.getRole_id());
            }
        }
    }
    private void loadSpinnerRole(int selectedRole) {
        roleRepository.getAllRoles().observe(this, roles -> {
            if (roles != null) {
                roleList = roles;
                List<String> roleNames = new ArrayList<>();
                for (Role role : roles) {
                    roleNames.add(role.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, roleNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRole.setAdapter(adapter);

                for (int i = 0; i < roles.size(); i++) {
                    if (roles.get(i).getId() == selectedRole) {
                        spinnerRole.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void saveUser() {
        String username = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String mail = etMail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || mail.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Enter require information", Toast.LENGTH_SHORT).show();
            return;
        }



        int selectedPosition = spinnerRole.getSelectedItemPosition();
        int selectedRoleId = roleList.get(selectedPosition).getId();

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        User updatedUser = new User(
                existingUser.getId(),
                username, password, phone, mail, address,  imagePath,selectedRoleId,
                existingUser.getCreated_at(), currentTime,"", 0
        );



        manageAccountViewModel.update(updatedUser);

        Toast.makeText(this, "Update successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}