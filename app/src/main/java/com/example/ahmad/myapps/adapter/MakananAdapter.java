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
import com.example.ahmad.myapps.model.Makanan;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmad on 02/02/2016.
 */
public class MakananAdapter extends RecyclerView.Adapter<MakananAdapter.ViewHolder>{

    private List<Makanan> makananList;

    public MakananAdapter(List<Makanan> makananList) {
        this.makananList = makananList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Makanan makanan = makananList.get(position);
        holder.bind(makanan);

    }

    @Override
    public int getItemCount() {
        return makananList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @Bind(R.id.circularImage) ImageView circularImage;
        @Bind(R.id.txtTitle) TextView textTitle;
        @Bind(R.id.txtSubTitle) TextView textSubTitle;
        @Bind(R.id.mainImage) ImageView mainImage;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Makanan makanan) {
            textTitle.setText(makanan.getMenu_name());
            textSubTitle.setText("Rp. " + makanan.getMenu_price());

            if ((makanan.getRestoran_logo() != null) && makanan.getRestoran_logo().contains("http")){
                Picasso.with(circularImage.getContext()).load(makanan.getRestoran_name()).placeholder(R.drawable.logo).into(circularImage);
            } else {
                circularImage.setImageResource(R.drawable.broken);
            }

            if ((makanan.getMenu_image() != null) && makanan.getMenu_image().contains("http")){
                Picasso.with(mainImage.getContext()).load(makanan.getMenu_image()).placeholder(R.drawable.logo).into(mainImage);
            } else {
                mainImage.setImageResource(R.drawable.broken);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_ID, makanan.getMenu_id());
                    intent.putExtra(DetailActivity.EXTRA_NAME, makanan.getMenu_name());
                    intent.putExtra(DetailActivity.EXTRA_PRICE, "Rp. " + makanan.getMenu_price());
                    intent.putExtra(DetailActivity.EXTRA_DESC, makanan.getMenu_description());
                    intent.putExtra(DetailActivity.EXTRA_IMAGE, makanan.getMenu_image());
                    intent.putExtra(DetailActivity.EXTRA_RES_ID, makanan.getRestoran_id());
                    intent.putExtra(DetailActivity.EXTRA_RES_NAME, makanan.getRestoran_name());
                    intent.putExtra(DetailActivity.EXTRA_LOGO, makanan.getRestoran_logo());
                    context.startActivity(intent);
                }
            });


        }
    }
}
