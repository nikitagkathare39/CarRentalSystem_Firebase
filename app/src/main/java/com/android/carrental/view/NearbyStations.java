package com.android.carrental.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.carrental.account.EditMyAccount;
import com.android.carrental.account.MyAccount;
import com.android.carrental.payment.PaymentMethod;
import com.android.carrental.help.Help;
import com.android.carrental.R;
import com.android.carrental.adapter.StationAdapter;
import com.android.carrental.model.Station;
import com.android.carrental.account.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearbyStations extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView recyclerView;
    private List<Station> stations;
    private StationAdapter stationAdapter;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_stations);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setTitle("Choose Nearby Station");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.staions_recycler_view);
        initWidgets();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_help:
                intent = new Intent(NearbyStations.this, Help.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(NearbyStations.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_account:
                intent = new Intent(NearbyStations.this, MyAccount.class);
                startActivity(intent);
                break;
            case R.id.my_bookings:
                intent = new Intent(this, MyBookings.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initWidgets();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stations");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Station user = snapshot.getValue(Station.class);
                    stations.add(user);
                }
                Collections.sort(stations, new Comparator<Station>() {
                    @Override
                    public int compare(Station station1, Station station2) {
                        return Double.compare(Double.parseDouble(station1.getDistance()), (Double.parseDouble(station2.getDistance())));
                    }
                });
                stationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initWidgets() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stations = new ArrayList<>();
        stationAdapter = new StationAdapter(getApplicationContext(), stations);
        stationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(stationAdapter);
    }
}
