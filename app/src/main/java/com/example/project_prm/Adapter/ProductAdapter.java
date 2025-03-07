package com.example.project_prm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList = new ArrayList<>();

    public ProductAdapter(List<Product> productList) {
        if (productList != null) {
            this.productList = productList;
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.brand.setText(product.getBrand());
        holder.price.setText(String.format("%,.0f VND", product.getSale_price())); // Format tiền tệ
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> newProductList) {
        if (newProductList != null) {
            this.productList.clear();
            this.productList.addAll(newProductList);
            notifyDataSetChanged();
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, brand;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvProductName);
            price = itemView.findViewById(R.id.tvProductPrice);
            brand = itemView.findViewById(R.id.tvBrandName);
        }
    }
}
