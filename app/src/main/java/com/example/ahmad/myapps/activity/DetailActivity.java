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

    private List<PayPalItem> productInCart = new ArrayList<PayPalItem>();

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT)
            .clientId(Config.PAYPAL_CLIENT_ID)
            .merchantName(Config.DEFAULT_MERCHANT);

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

        Intent intent1 = new Intent(this, PayPalService.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!session.isLoggedIn()){
                    Snackbar.make(v, "Anda Belum Login", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent2);
                                }
                            }).show();
                } else {
                    launchPaypal();
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

    public void launchPaypal(){
        final  Intent intent = getIntent();
        final String price = intent.getStringExtra(EXTRA_PRICE);
        final String name = intent.getStringExtra(EXTRA_NAME);
        String harga = price.replace("Rp. ", "");

        final BigDecimal sPrice = new BigDecimal(harga);

        PayPalPayment payPalPayment = new PayPalPayment(sPrice, Config.DEFAULT_CURRENCY, name, Config.PAYMENT_INTENT);
        Intent intent1 = new Intent(DetailActivity.this, PaymentActivity.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent1, REQUEST_CODE_PAYMENT);

    }


    /* verifiying mobile payment on server side */
    private void verifyPayment(final String paymentId, final String payment_client) {
        pDialog.setMessage("verifikasi payment..");
        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.URL_PAYMENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "verify pament: " + response.toString());

                try {
                    JSONObject res = new JSONObject(response);
                    boolean error = res.getBoolean("error");
                    String message = res.getString("message");

                    Toast.makeText(DetailActivity.this, "message: " + message, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hidepDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Verifikasi Error: " + error.getMessage());
                Toast.makeText(DetailActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                hidepDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                db = new SQLiteHandler(DetailActivity.this);
                session = new SessionManager(DetailActivity.this);
                HashMap<String, String> anggota = db.getUserDetails();
                String anggotaId = anggota.get("id");
                String anggotaName = anggota.get("name");
                String anggotaImage = anggota.get("image");

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

                Map<String, String> params = new HashMap<String, String>();
                params.put("paymentId", paymentId);
                params.put("paymentClientJson", payment_client);
                params.put("menuId", menuId);
                params.put("menuName", menuName);
                params.put("menuPrice", menuPrice);
                params.put("menuImage", menuImage);
                params.put("menuSku", menuSku);
                params.put("restoName", restoName);
                params.put("restoLogo", restoLogo);
                params.put("anggotaId", anggotaId);
                params.put("anggotaName", anggotaName);
                params.put("anggotaImage", anggotaImage);

                return params;
            }
        };

        int socketTimeOut = 6000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }


    /* receive paypal response */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirmation = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        Log.e(TAG, confirmation.toJSONObject().toString(7));
                        Log.e(TAG, confirmation.getPayment().toJSONObject().toString(7));

                        String paymentId = confirmation.toJSONObject().getJSONObject("response").getString("id");
                        String payment_client = confirmation.getPayment().toJSONObject().toString();

                        Log.e(TAG, "paymentId: " + paymentId + ", payment_json: " + payment_client);

                        verifyPayment(paymentId, payment_client);

                    } catch (JSONException e) {
                        Log.e(TAG, "failure : " + e.getMessage());
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "User Canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG, "Invalid payment or PayPalConfiguration was submitted..");
            }
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
