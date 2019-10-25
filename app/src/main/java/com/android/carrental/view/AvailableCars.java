package com.android.carrental.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.carrental.R;
import com.android.carrental.adapter.AvailableCarsAdapter;
import com.android.carrental.model.Car;
import com.android.carrental.model.CarBooking;
import com.android.carrental.model.CarModel;
import com.android.carrental.model.Station;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AvailableCars extends AppCompatActivity {

    private RecyclerView available_cars_recylerview;
    private AvailableCarsAdapter availableCarsAdapter;
    private List<Car> allCarsInSelectedStation;
    private Station selectedStation;
    private CarModel selectedCarModel;
    private String selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private int hoursBooked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_cars);
        available_cars_recylerview = findViewById(R.id.recycler_view_available_cars);
        allCarsInSelectedStation = new ArrayList<>();
        getUserSelectedData();
        getSupportActionBar().setTitle("Available Cars");
        initWidgets();
    }

    private void getUserSelectedData() {
        selectedStation = (Station) getIntent().getSerializableExtra("selectedStaion");
        selectedCarModel = (CarModel) getIntent().getSerializableExtra("selectedCarModel");
        selectedDate = getIntent().getStringExtra("selectedDate");
        selectedStartTime = getIntent().getStringExtra("selectedStartTime");
        selectedEndTime = getIntent().getStringExtra("selectedEndTime");
        hoursBooked = getIntent().getIntExtra("hoursBooked", 1);
    }

    private void initWidgets() {
        available_cars_recylerview.setHasFixedSize(true);
        available_cars_recylerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        allCarsInSelectedStation = getAllCarsInSelectedStation();
        availableCarsAdapter = new AvailableCarsAdapter(getApplicationContext(), allCarsInSelectedStation, this, selectedStation, selectedDate, selectedStartTime, selectedEndTime, hoursBooked);
        availableCarsAdapter.notifyDataSetChanged();
        available_cars_recylerview.setAdapter(availableCarsAdapter);
    }

    private List<Car> getAllCarsInSelectedStation() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Car car = snapshot.getValue(Car.class);
                    if (car.getStation().getId().equals(selectedStation.getId())) {
                        if (selectedCarModel.getName().equals(car.getModel().getName())) {
                            allCarsInSelectedStation.add(car);
                        } else if (selectedCarModel.getName().equals("All Types")) {
                            allCarsInSelectedStation.add(car);
                        }
                    }
                }
                Collections.sort(allCarsInSelectedStation, new Comparator<Car>() {
                    @Override
                    public int compare(Car car1, Car car2) {
                        return Integer.compare(car1.getRate(), car2.getRate());
                    }
                });
                availableCarsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return allCarsInSelectedStation;
    }


}
