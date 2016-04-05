package com.example.ahmad.myapps.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmad.myapps.R;
import com.example.ahmad.myapps.adapter.ViewPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {

    private static final int RESULT_OK = 1;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private FragmentCallbacks mCallbacks;

    public static interface FragmentCallbacks {
        void menuClick();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_with_tabs, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Products");
        toolbar.setNavigationIcon(R.drawable.ic_reorder_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.menuClick();
            }
        });

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click Again", Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        pagerAdapter.addFragment(new FoodFragment(), "Foods");
        pagerAdapter.addFragment(new DrinkFragment(), "Drinks");
        pagerAdapter.addFragment(new FoodyFragment(), "Foodys");
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallbacks) {
            mCallbacks = (FragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
