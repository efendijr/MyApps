package com.example.ahmad.myapps.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.adapter.FoodAdapter;
import com.example.ahmad.myapps.api.Config;
import com.example.ahmad.myapps.model.Food;
import com.example.ahmad.myapps.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = FoodyFragment.class.getSimpleName();

    private List<Food> listItems = new ArrayList<>();
    private FoodAdapter adapter;

    @Bind(R.id.swipe) SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.rootLayout)
    RelativeLayout rootLayout;

    public FoodyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_style, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new FoodAdapter(listItems);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe));
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                getData();
            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        listItems.clear();
        getData();
    }

    private void getData() {
        refreshLayout.setRefreshing(true);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                Config.URL_FOOD, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray items = response.getJSONArray("result");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = (JSONObject) items.get(i);

                        String id = item.getString("id");
                        String name = item.getString("name");
                        String description = item.getString("description");
                        String photo = item.getString("image");
                        BigDecimal price = new BigDecimal(item.getString("price"));

                        Food f = new Food(id, name, description, photo, price);
                        listItems.add(f);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "error1", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                refreshLayout.setRefreshing(false);
                Snackbar.make(rootLayout, "No Connection", Snackbar.LENGTH_SHORT)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData();
                            }
                        }).show();
            }
        });

        int socketTimeout = 6000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        objectRequest.setRetryPolicy(policy);

        VolleySingleton.getInstance().addToRequestQueue(objectRequest);

    }
}
