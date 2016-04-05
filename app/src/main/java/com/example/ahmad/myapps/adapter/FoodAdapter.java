package com.example.ahmad.myapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.activity.DetailActivity;
import com.example.ahmad.myapps.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmad on 3/31/16.
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.bind(food);

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @Bind(R.id.circularImage)
        ImageView circularImage;
        @Bind(R.id.txtTitle)
        TextView textTitle;
        @Bind(R.id.txtSubTitle) TextView textSubTitle;
        @Bind(R.id.mainImage) ImageView mainImage;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Food food) {
            textTitle.setText(food.getName());
            textSubTitle.setText("Rp. " + food.getPrice());

            if ((food.getPhoto() != null) && food.getPhoto().contains("http")){
                Picasso.with(circularImage.getContext()).load(food.getPhoto()).placeholder(R.drawable.logo).into(circularImage);
            } else {
                circularImage.setImageResource(R.drawable.broken);
            }

            if ((food.getPhoto() != null) && food.getPhoto().contains("http")){
                Picasso.with(mainImage.getContext()).load(food.getPhoto()).placeholder(R.drawable.logo).into(mainImage);
            } else {
                mainImage.setImageResource(R.drawable.broken);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_ID, food.getId());
                    intent.putExtra(DetailActivity.EXTRA_NAME, food.getName());
                    intent.putExtra(DetailActivity.EXTRA_PRICE, "Rp. " + food.getPrice());
                    intent.putExtra(DetailActivity.EXTRA_DESC, food.getDescription());
                    intent.putExtra(DetailActivity.EXTRA_IMAGE, food.getPhoto());
                    context.startActivity(intent);
                }
            });
        }
    }

}
