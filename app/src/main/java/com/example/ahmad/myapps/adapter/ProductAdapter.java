package com.example.ahmad.myapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmad on 31/01/2016.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        final Product product = products.get(position);
        if ((product.getImage() != null) && product.getImage().contains("http")){
            Picasso.with(holder.imageView.getContext()).load(product.getImage()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.broken);
        }
        holder.title.setText(product.getName());
        holder.date.setText(product.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @Bind(R.id.image1)
        ImageView imageView;
        @Bind(R.id.txt_title)
        TextView title;
        @Bind(R.id.txt_date) TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
