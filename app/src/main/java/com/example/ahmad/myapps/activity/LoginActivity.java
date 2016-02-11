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

/**
 * Created by ahmad on 02/02/2016.
 */
public class LoginActivity extends AppCompatActivity{
    public static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Bind(R.id.inputEmail) EditText emailText;
    @Bind(R.id.inputPassword) EditText passwordText;
    @Bind(R.id.btnSignin) Button loginBtn;
    @Bind(R.id.linkSingup) TextView linkRegistrasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        /* Check user already logged in */
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        /* Link Reaction*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    checkLogin(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linkRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrasiActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkLogin(final String email, final String password) {
        /* tag used to cancel the request */
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response : " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    /* Check for error node in json */
                    if (status != false) {

                        /* user success login and create login session */
                        session.setLogin(true);

                        /* now store the user in SQLite (users_table)->> URL_LOGIN*/

                        JSONObject result = jsonObject.getJSONObject("result");
                        String name = result.getString("name");
                        String email = result.getString("email");
                        String image = result.getString("image");
                        String created_at = result.getString("created_at");
                        String updated_at = result.getString("updated_at");

                        db.addUser(name, email, image, created_at, updated_at);

                        /* Launch destiny activity */
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        /* Error login get the error message */
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Json Error" + errorMsg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: Internet", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        VolleySingleton.getInstance().addToRequestQueue(strReq, tag_string_req);
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
