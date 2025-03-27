package com.example.project_prm.Activity.User.ZaloPay;


//import com.example.zalo_pay_integration.Email.EmailSender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.Activity.User.ZaloPay.Api.CreateOrder;

import com.example.project_prm.Adapter.User.CheckOutAdapter;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.R;

import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;
import com.example.project_prm.ViewModel.User.CartViewModel;
import com.example.project_prm.ViewModel.User.ProductViewModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPayment extends AppCompatActivity {


    Button btnThanhToan;
    private ManageOrderViewModel orderViewModel;
    private RecyclerView recyclerView;
    private CheckOutAdapter adapter;
    private List<CartWithProduct> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);



        btnThanhToan = findViewById(R.id.buttonThanhToan);
        orderViewModel = new ViewModelProvider(this).get(ManageOrderViewModel.class);

        Intent intent = getIntent();
        String productNames = intent.getStringExtra("product_names");
        ArrayList<Integer> quantities = (ArrayList<Integer>) intent.getSerializableExtra("quantities");
        Double total = intent.getDoubleExtra("total", 0.0);


        btnThanhToan.setOnClickListener(v -> processPayment(total));
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        recyclerView = findViewById(R.id.recyclerViewCheckOut);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = (List<CartWithProduct>) getIntent().getSerializableExtra("cart_items");

        if (cartItems != null) {
            adapter = new CheckOutAdapter(cartItems);
            recyclerView.setAdapter(adapter);
        }


    }
    private void processPayment(double total) {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.format("%.0f", total));
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(OrderPayment.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        updateDatabaseAfterPayment();

//                                String emailSubject = "Payment Confirmation";
//                                String emailBody = "Your payment for the order has been successfully completed.\n" +
//                                        "Amount: " + txtTongTien.getText().toString() +
//                                        "\nOrder details: ...";  // You can add more order details here
//
//                                // Create the EmailSender object
//                                EmailSender emailSender = new EmailSender();
//                                emailSender.sendEmail(emailSubject, emailBody);
//
//                                // Continue with the rest of your flow
//                                Intent intent1 = new Intent(OrderPayment.this, PaymentNotification.class);
//                                intent1.putExtra("result", "Thanh toán thành công");
//                                startActivity(intent1);

                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        showPaymentResult("Hủy thanh toán");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        showPaymentResult("Lỗi thanh toán");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateDatabaseAfterPayment() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0;
        List<CartWithProduct> cartItems = (List<CartWithProduct>) getIntent().getSerializableExtra("cart_items");

        if (cartItems == null || cartItems.isEmpty()) return;

        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        CartViewModel cartViewModel = new CartViewModel(getApplication(), getUserId());

        for (CartWithProduct item : cartItems) {
            if (item.getCart().getQuantity() > item.getProductStock()) {
                Toast.makeText(this, "Product:  " + item.getProductName() + " invalid quantity!", Toast.LENGTH_SHORT).show();
                return;
            }
            totalPrice += item.getCart().getQuantity() * item.getProductPrice();
            orderDetails.add(new OrderDetail(0, 0, item.getCart().getProduct_id(), item.getCart().getQuantity(), item.getProductPrice()));


            productViewModel.updateProductStock(item.getCart().getProduct_id(), item.getCart().getQuantity());
        }

        Order order = new Order(0, getUserId(), totalPrice, "Payment",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()), "");

        orderViewModel.insertOrderWithDetails(order, orderDetails, () -> {
            for (CartWithProduct item : cartItems) {
                cartViewModel.delete(item.getCart());
            }
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });


        showPaymentResult("Thanh toán thành công");
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private void showPaymentResult(String message) {
        Intent intent = new Intent(OrderPayment.this, ProductListActivity.class);
        intent.putExtra("result", message);
        startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

}