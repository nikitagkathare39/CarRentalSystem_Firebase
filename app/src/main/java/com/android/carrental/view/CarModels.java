package com.android.carrental.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.carrental.R;
import com.android.carrental.adapter.CarModelsAdapter;
import com.android.carrental.model.CarModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarModels extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CarModel> carModels;
    private CarModelsAdapter carModelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_models);
        recyclerView = findViewById(R.id.car_models_recycler_view);
        initWidgets();
        getSupportActionBar().setTitle("Car Models");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carmodels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CarModel carModel = snapshot.getValue(CarModel.class);
                    carModels.add(carModel);
                }

                carModelsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initWidgets() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        carModels = new ArrayList<>();
        carModelsAdapter = new CarModelsAdapter(getApplicationContext(), carModels, this);
        carModelsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(carModelsAdapter);
    }
}
