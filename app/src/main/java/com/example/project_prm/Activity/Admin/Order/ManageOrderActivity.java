package com.example.project_prm.Activity.Admin.Order;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.Admin.ManageOrderAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.R;
import com.example.project_prm.Repository.OrderRepository;
import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;

import java.util.ArrayList;

public class ManageOrderActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ManageOrderViewModel manageOrderViewModel;
    private ImageButton btnAccept, btnReject;
    private ManageOrderAdapter manageOrderAdapter;
    private OrderRepository orderRepository;
    private ImageView ivFilter; // Thêm ImageView filter
    private String selectedStatus = "All"; // Trạng thái filter mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_order);
        getLayoutInflater().inflate(R.layout.activity_manage_order, findViewById(R.id.content_frame));
        recyclerView = findViewById(R.id.recyclerViewOrder);
        btnAccept = findViewById(R.id.btnAcceptOrder);
        btnReject = findViewById(R.id.btnRejectOrder);
        ivFilter = findViewById(R.id.ivFilterOrder); // Ánh xạ ImageView filter

        manageOrderAdapter = new ManageOrderAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manageOrderAdapter);

        orderRepository = new OrderRepository(this);
        manageOrderViewModel = new ViewModelProvider(this).get(ManageOrderViewModel.class);
        manageOrderViewModel.getAllOrder().observe(this, manageOrderAdapter::setOrderList);


        // Xử lý khi nhấn vào icon Filter
        ivFilter.setOnClickListener(v -> showFilterMenu(v));

        // Xử lý khi nhấn nút Chấp nhận
        btnAccept.setOnClickListener(v -> confirmUpdateOrderStatus("Completed"));

        // Xử lý khi nhấn nút Từ chối
        btnReject.setOnClickListener(v -> confirmUpdateOrderStatus("Cancelled"));
    }

    private void showFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_menu_order, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_by_pending) {
                selectedStatus = "Pending";
            } else if (itemId == R.id.filter_by_completed) {
                selectedStatus = "Completed";
            } else if (itemId == R.id.filter_by_cancelled) {
                selectedStatus = "Cancelled";
            } else {
                selectedStatus = "All";
            }

            manageOrderAdapter.filter( selectedStatus);
            return true;
        });
        popup.show();

    }

    private void confirmUpdateOrderStatus(String newStatus) {
        int selectedPosition = manageOrderAdapter.getSelectedPosition();
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select an order", Toast.LENGTH_SHORT).show();
            return;
        }

        Order selectedOrder = manageOrderAdapter.getOrderAt(selectedPosition);
        if (!"Pending".equals(selectedOrder.getStatus())) {
            Toast.makeText(this, "Only pending orders can be updated", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Order Update")
                .setMessage("Are you sure you want to mark this order as " + newStatus + "?")
                .setPositiveButton("Yes", (dialog, which) -> updateOrderStatus(selectedOrder, newStatus))
                .setNegativeButton("No", null)
                .show();
    }

    private void updateOrderStatus(Order order, String newStatus) {
        if ("Completed".equals(newStatus)) {
            orderRepository.acceptOrder(order.getId());
        } else if ("Cancelled".equals(newStatus)) {
            orderRepository.rejectOrder(order.getId());
        }

        order.setStatus(newStatus);
        manageOrderAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Order updated to " + newStatus, Toast.LENGTH_SHORT).show();
    }
}
