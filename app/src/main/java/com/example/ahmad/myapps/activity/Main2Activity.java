package com.example.ahmad.myapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ahmad.myapps.R;

import java.math.BigDecimal;

import butterknife.Bind;

public class Main2Activity extends AppCompatActivity {
    public static final String EXTRA_PRICE = "menu_price";
//    public static final String EXTRA_JUMLAH = "menu_jumlah";

    @Bind(R.id.totalharga) TextView total_harga;
    @Bind(R.id.harga) TextView harga;
    @Bind(R.id.jumlah) TextView jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final Intent intent = getIntent();
//        final String price = intent.getStringExtra(EXTRA_PRICE);
//        final String count = intent.getStringExtra(EXTRA_JUMLAH);

        total_harga.setText("100");
        harga.setText("1000");
        jumlah.setText("1000");

//        String price2 = price.replaceAll("Rp ", "");
//
//        BigDecimal pricedec = new BigDecimal(price2);
//        BigDecimal jumlahdec = new BigDecimal(jumlah);

//        BigDecimal total = pricedec.multiply(jumlahdec);

//        if (count != null && !count.isEmpty()) {
////            final String count1 = "1";
//            total_harga.setText("perkalian antara harga dan jumlah");
//            harga.setText(price);
//            jumlah.setText(count);
//        } else {
//            total_harga.setText("perkalian antara harga dan jumlah");
//            harga.setText(price);
//            jumlah.setText("1");
//        }





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
