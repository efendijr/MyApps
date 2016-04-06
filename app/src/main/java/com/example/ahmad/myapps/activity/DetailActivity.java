package com.example.ahmad.myapps.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.api.Config;
import com.example.ahmad.myapps.utils.SQLiteHandler;
import com.example.ahmad.myapps.utils.SessionManager;
import com.example.ahmad.myapps.utils.VolleySingleton;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "menu_id";
    public static final String EXTRA_NAME = "menu_name";
    public static final String EXTRA_PRICE = "menu_price";
    public static final String EXTRA_DESC = "menu_desc";
    public static final String EXTRA_IMAGE = "menu_image";
    public static final String EXTRA_SKU = "menu_sku";
    public static final String EXTRA_RES_ID = "resto_id";
    public static final String EXTRA_RES_NAME = "resto_name";
    public static final String EXTRA_LOGO = "resto_logo";



    private static final String TAG = DetailActivity.class.getSimpleName();

    private SQLiteHandler db;
    private SessionManager session;

    // Progress dialog
    private ProgressDialog pDialog;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.txtTitle) TextView name;
    @Bind(R.id.txtSubTitle) TextView price;
    @Bind(R.id.circularImage) ImageView logo;
    @Bind(R.id.mainImage) ImageView mainImage;
    @Bind(R.id.txtContent) TextView description;
    @Bind(R.id.productId) TextView id;
    @Bind(R.id.userApi) TextView txtApi;
    @Bind(R.id.userName) TextView txtuserName;
    @Bind(R.id.userAddress) TextView txtuserAddress;
    @Bind(R.id.inputAddress) EditText inputAddress;
    @Bind(R.id.layout1) RelativeLayout layout1;
    @Bind(R.id.card1) CardView card1;


    //    Palette palette;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        String userId = user.get("id");
        String userName = user.get("name");
        String email = user.get("email");
        String image = user.get("image");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final Intent intent = getIntent();
        final String menuId = intent.getStringExtra(EXTRA_ID);
        final String menuName = intent.getStringExtra(EXTRA_NAME);
        final String menuPrice = intent.getStringExtra(EXTRA_PRICE);
        final String menudesc = intent.getStringExtra(EXTRA_DESC);
        final String menuImage = intent.getStringExtra(EXTRA_IMAGE);
        final String menuSku = intent.getStringExtra(EXTRA_SKU);
        final String restoId = intent.getStringExtra(EXTRA_RES_ID);
        final String restoName = intent.getStringExtra(EXTRA_RES_NAME);
        final String restoLogo = intent.getStringExtra(EXTRA_LOGO);




        collapsingToolbarLayout.setTitle(menuName);
        name.setText(menuName);
        price.setText(menuPrice);
        description.setText(menudesc);
        txtuserName.setText(userName);
        txtApi.setText(email);


        if ((menuImage != null) && menuImage.contains("http")){
            Picasso.with(this).load(menuImage).placeholder(R.drawable.broken).into(mainImage);
        } else {
            mainImage.setImageResource(R.drawable.broken);
        }

        if ((restoLogo != null) && restoLogo.contains("http")) {
            Picasso.with(this).load(restoLogo).placeholder(R.drawable.broken).into(logo);
        } else {
            logo.setImageResource(R.drawable.broken);
        }


        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!session.isLoggedIn()){
                    Snackbar.make(v, "Anda Belum Login", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                } else {

                    String jumlah = inputAddress.getText().toString();

                    Context context = v.getContext();
                    Intent intent2 = new Intent(context, BayarActivity.class);
                    intent2.putExtra(BayarActivity.EXTRA_PRICE, "" + menuPrice);
                    intent2.putExtra(BayarActivity.EXTRA_IMAGE, menuImage);
                    intent2.putExtra(BayarActivity.EXTRA_NAME, menuName);
                    intent2.putExtra(BayarActivity.EXTRA_JUMLAH, jumlah);
                    context.startActivity(intent2);

                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Menu Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
