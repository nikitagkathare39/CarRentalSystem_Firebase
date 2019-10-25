package com.android.carrental.help;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.carrental.R;
import com.android.carrental.adapter.BookingsAdapter;
import com.android.carrental.adapter.TripsAdapter;
import com.android.carrental.model.CarBooking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripRefunds extends AppCompatActivity {
    private RecyclerView trips_recycler_view;
    private List<CarBooking> bookings;
    private TripsAdapter tripsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_refunds);
        trips_recycler_view = findViewById(R.id.trips_recycler_view);
        initWidgets();
        getSupportActionBar().setTitle("Trip History");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initWidgets();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CarBooking booking = snapshot.getValue(CarBooking.class);
                    if (booking.getUser().equals(user)) {
                        bookings.add(booking);
                    }
                }
                tripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initWidgets() {
        trips_recycler_view.setHasFixedSize(true);
        trips_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        bookings = new ArrayList<>();
        tripsAdapter = new TripsAdapter(getApplicationContext(), bookings, this);
        tripsAdapter.notifyDataSetChanged();
        trips_recycler_view.setAdapter(tripsAdapter);
    }
}
