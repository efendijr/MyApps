package com.example.ahmad.myapps.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmad.myapps.MainActivity;
import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.api.Config;
import com.example.ahmad.myapps.utils.SQLiteHandler;
import com.example.ahmad.myapps.utils.SessionManager;
import com.example.ahmad.myapps.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegistrasiActivity extends AppCompatActivity {
    public static final String TAG = RegistrasiActivity.class.getSimpleName();

    @Bind(R.id.inputName) EditText nameText;
    @Bind(R.id.inputEmail) EditText emailText;
    @Bind(R.id.inputPassword) EditText passwordText;
    @Bind(R.id.btnSingup) Button registasiBtn;
    @Bind(R.id.linkSignin) TextView loginLink;
//    @Bind(R.id.toolbar) Toolbar toolbar;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi_activity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(RegistrasiActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        registasiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registrasiAnggota(name, email, password);

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registrasiAnggota(final String name, final String email, final String password){

        String tag_string_req = "req_register";

        pDialog.setMessage("Registered...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.URL_REGISTRASI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status != false) {

                        JSONObject result = jsonObject.getJSONObject("result");
                        String name = result.getString("name");
                        String email = result.getString("email");
                        String image = result.getString("image");
                        String created_at = result.getString("created_at");
                        String updated_at = result.getString("updated_at");

                        db.addUser(name, email, image, created_at, updated_at);

                        Toast.makeText(getApplicationContext(), "Success registration new user", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), "Error 1" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error 2", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), "Error 3", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };
        VolleySingleton.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
