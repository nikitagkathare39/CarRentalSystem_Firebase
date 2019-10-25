package com.android.carrental.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.carrental.R;
import com.android.carrental.model.Car;
import com.android.carrental.model.CarModel;
import com.android.carrental.model.Station;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddCar extends AppCompatActivity implements View.OnClickListener {

    private Button save_car;
    private EditText car_name;
    private EditText car_number;
    private EditText car_color;
    private EditText car_rate;
    private Spinner car_stations;
    private Spinner car_models;
    private List<Station> stations;
    private List<CarModel> carModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        save_car = (Button) findViewById(R.id.save_car);
        car_name = (EditText) findViewById(R.id.car_name);
        car_number = (EditText) findViewById(R.id.car_number);
        car_color = (EditText) findViewById(R.id.car_color);
        car_rate = (EditText) findViewById(R.id.car_rate);
        car_stations = (Spinner) findViewById(R.id.car_stations_spinner);
        car_models = (Spinner) findViewById(R.id.car_models_spinner);
        stations = new ArrayList<>();
        carModels = new ArrayList<>();
        fetchStations();
        fetchCarModels();
        save_car.setOnClickListener(this);
    }

    private void fetchStations() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stations");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Station station = snapshot.getValue(Station.class);
                    stations.add(station);
                }
                Collections.sort(stations, new Comparator<Station>() {
                    @Override
                    public int compare(Station station1, Station station2) {
                        return Double.compare(Double.parseDouble(station1.getDistance()), (Double.parseDouble(station2.getDistance())));
                    }
                });
                ArrayAdapter<Station> stationArrayAdapter = new ArrayAdapter<Station>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, stations);
                stationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                car_stations.setAdapter(stationArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchCarModels() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carmodels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CarModel carModel = snapshot.getValue(CarModel.class);
                    carModels.add(carModel);
                }
                ArrayAdapter<CarModel> carModelArrayAdapter = new ArrayAdapter<CarModel>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, carModels);
                carModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                car_models.setAdapter(carModelArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String id = databaseReference.push().getKey();
        Station station = (Station) car_stations.getSelectedItem();
        CarModel carModel = (CarModel) car_models.getSelectedItem();
        Car car = new Car(id, car_name.getText().toString(), car_color.getText().toString(), car_number.getText().toString(),
                Integer.parseInt(car_rate.getText().toString()), carModel, station);
        databaseReference.child("cars").child(id).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("saved", "saved");
            }
        });
    }
}
