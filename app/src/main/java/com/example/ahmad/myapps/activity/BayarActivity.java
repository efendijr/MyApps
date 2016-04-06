package com.example.ahmad.myapps.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.example.ahmad.myapps.MainActivity;
import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.api.Config;
import com.example.ahmad.myapps.utils.VolleySingleton;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BayarActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "menu_id";
    public static final String EXTRA_NAME = "menu_name";
    public static final String EXTRA_PRICE = "menu_price";
    public static final String EXTRA_JUMLAH = "menu_jumlah";

    private static final String TAG = BayarActivity.class.getSimpleName();

    private List<PayPalItem> productIncart = new ArrayList<PayPalItem>();

    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT)
            .clientId(Config.PAYPAL_CLIENT_ID)
            .merchantName("Gatskin Mart");


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

        final Intent intent = getIntent();
        final String menuImage = intent.getStringExtra(EXTRA_IMAGE);
        final String menuPrice = intent.getStringExtra(EXTRA_PRICE);
        final String menuName = intent.getStringExtra(EXTRA_NAME);
        final String menuJumlah = intent.getStringExtra(EXTRA_JUMLAH);

        collapsingToolbarLayout.setTitle(menuName);
        price.setText(menuPrice);
        name.setText(menuName);
        description.setText(menuJumlah);

        String price2 = menuPrice.replaceAll("Rp. ", "");
        BigDecimal pricedec = new BigDecimal(price2);
        BigDecimal jumlahdec = new BigDecimal(menuJumlah);

        BigDecimal total = pricedec.multiply(jumlahdec);
        id.setText("Rp. " + total);

        Intent intent1 = new Intent(this, PayPalService.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent1);

        if ((menuImage != null) && menuImage.contains("http")){
            Picasso.with(this).load(menuImage).placeholder(R.drawable.broken).into(mainImage);
        } else {
            mainImage.setImageResource(R.drawable.broken);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                postPayment();
                launchPaypal();
                postPayment();
//                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent1);
            }
        });

    }

    public void launchPaypal(){

        final Intent intent = getIntent();
        final String menuPrice = intent.getStringExtra(EXTRA_PRICE);
        final String menuName = intent.getStringExtra(EXTRA_NAME);
        final String menuJumlah = intent.getStringExtra(EXTRA_JUMLAH);

        String price2 = menuPrice.replaceAll("Rp. ", "");
        BigDecimal pricedec = new BigDecimal(price2);
        BigDecimal jumlahdec = new BigDecimal(menuJumlah);

        BigDecimal total = pricedec.multiply(jumlahdec);

        PayPalPayment payPalPayment = new PayPalPayment(total, Config.DEFAULT_CURRENCY, menuName, Config.PAYMENT_INTENT);
        Intent intent1 = new Intent(BayarActivity.this, PaymentActivity.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
    }

    private void postPayment(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.URL_BAYAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errorvolley) {
                Log.d("Error.Response", errorvolley.getMessage());
            }
        }){
            @Override
        protected Map<String, String> getParams() {

                final Intent intent = getIntent();
                final String menuPrice = intent.getStringExtra(EXTRA_PRICE);
                final String menuName = intent.getStringExtra(EXTRA_NAME);
                final String menuJumlah = intent.getStringExtra(EXTRA_JUMLAH);

                String price2 = menuPrice.replaceAll("Rp. ", "");
                BigDecimal pricedec = new BigDecimal(price2);
                BigDecimal jumlahdec = new BigDecimal(menuJumlah);

                BigDecimal total = pricedec.multiply(jumlahdec);



                Map<String, String> params = new HashMap<String, String>();
                params.put("menuName", menuName);
                params.put("menuPrice","" + pricedec);
                params.put("jumlah", menuJumlah);
                params.put("total", "" + total);

                return params;
            }

        };

        int socketTimeOut = 6000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString());



                    } catch () {
//                        Log.e(TAG, "a faliure: " , e);
                    }

                }
            }


            Toast.makeText(BayarActivity.this, "Successfully payment", Toast.LENGTH_SHORT).show();

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(BayarActivity.this, "Canceled Payment", Toast.LENGTH_SHORT).show();

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(BayarActivity.this, "Failed Payment", Toast.LENGTH_SHORT).show();
        }
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
