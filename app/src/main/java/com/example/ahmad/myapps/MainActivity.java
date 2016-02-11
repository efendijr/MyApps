package com.example.ahmad.myapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmad.myapps.activity.LoginActivity;
import com.example.ahmad.myapps.fragment.ProductFragment;
import com.example.ahmad.myapps.utils.SQLiteHandler;
import com.example.ahmad.myapps.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ProductFragment.FragmentCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SQLiteHandler db;
    private SessionManager session;

    private ImageView userPicture;
    private TextView nameText;
    private TextView emailText;
    private TextView klikdisini;

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, new ProductFragment(), ProductFragment.class.getSimpleName()).commit();

        }

        if (navigationView != null) {

            setupDrawerContent(navigationView);
            navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);

            final View headerView = navigationView.getHeaderView(0);

            userPicture = (ImageView) headerView.findViewById(R.id.userPicture);
            nameText = (TextView) headerView.findViewById(R.id.userName);
            emailText = (TextView) headerView.findViewById(R.id.userEmail);
            klikdisini = (TextView) headerView.findViewById(R.id.klik_disini);

            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());

            HashMap<String, String> user = db.getUserDetails();
            String name = user.get("name");
            String email = user.get("email");
            String image = user.get("image");

            if (!session.isLoggedIn()){
                klikdisini.setVisibility(View.VISIBLE);
            } else {
                klikdisini.setVisibility(View.GONE);
            }

            nameText.setText(name);
            emailText.setText(email);
            if ((image != null) && image.contains("http")){
                Picasso.with(userPicture.getContext()).load(image).placeholder(R.drawable.logo).into(userPicture);
            } else {
                userPicture.setImageResource(R.drawable.broken);
            }

            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                    if (!session.isLoggedIn()) {
                        Snackbar.make(v, "Anda Belum login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        Snackbar.make(v, "Anda Sudah login", Snackbar.LENGTH_LONG)
                                .setAction("Logout", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        logout();

                                    }
                                }).show();
                    }
                }
            });

        }

    }


    private void logout(){
        session.setLogin(false);
        db.deleteUsers();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                menuItem.setChecked(true);
                                setFragment(0);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_messages:
                                menuItem.setChecked(true);
                                setFragment(1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_friends:
                                menuItem.setChecked(true);
                                setFragment(2);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_discussion:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                return true;

                            case R.id.nav_setting:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_help:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }

                        return true;
                    }
                });
    }


    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ProductFragment productsFragment = new ProductFragment();
                fragmentTransaction.replace(R.id.frame_container, productsFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void menuClick() { // From fragment handles opening of the navigation drawer
        if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.START);
    }
}
