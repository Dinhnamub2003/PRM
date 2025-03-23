package com.example.project_prm.Adapter.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartWithProduct> cartList = new ArrayList<>();
    private OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onDeleteClick(CartWithProduct cart);
    }

    public CartAdapter(OnCartItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartWithProduct cartWithProduct = cartList.get(position);
        holder.txtProductName.setText(cartWithProduct.getProductName());
        holder.txtProductQuantity.setText("Quantity: " + cartWithProduct.getCart().getQuantity());
        holder.txtProductPrice.setText(String.format("Price: %,.0f VND", cartWithProduct.getProductPrice()));
        holder.imgDelete.setOnClickListener(v -> listener.onDeleteClick(cartWithProduct));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setCartList(List<CartWithProduct> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductQuantity, txtProductPrice;
        ImageView imgDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
