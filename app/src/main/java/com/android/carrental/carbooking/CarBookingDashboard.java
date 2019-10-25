package com.android.carrental.carbooking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.Car;
import com.android.carrental.model.CarBooking;
import com.android.carrental.model.CarModel;
import com.android.carrental.model.Station;
import com.android.carrental.model.User;
import com.android.carrental.view.MyBookings;
import com.android.carrental.view.NearbyStations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarBookingDashboard extends AppCompatActivity implements View.OnClickListener {

    private Button confirm_booking;
    private TextView rate;
    private TextView car_name;
    private TextView pickup;
    private TextView date;
    private TextView start_time;
    private TextView end_time;
    private Station selectedStation;
    private Car selectedCar;
    private TextView card_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_booking);
        confirm_booking = (Button) findViewById(R.id.btn_booking_confirmation);
        rate = (TextView) findViewById(R.id.rate_on_booking_confirmation);
        car_name = (TextView) findViewById(R.id.car_name_for_booking_confirmation);
        date = (TextView) findViewById(R.id.date_on_booking_confirmation);
        start_time = (TextView) findViewById(R.id.start_time_on_booking_confirmation);
        end_time = (TextView) findViewById(R.id.end_time_on_booking_confirmation);
        pickup = (TextView) findViewById(R.id.pickup_location_on_booking_confirmation);
        card_number = (TextView) findViewById(R.id.card_number);
        confirm_booking.setOnClickListener(this);
        getSupportActionBar().setTitle("Confirm Reservation");
        fetchSelectedCarDetails();
        showPaymentDetails();
    }


    private void fetchSelectedCarDetails() {
        selectedCar = (Car) getIntent().getSerializableExtra("selectedCar");
        selectedStation = (Station) getIntent().getSerializableExtra("selectedStation");
        car_name.setText(selectedCar.getName() + " (" + selectedCar.getColor() + ")");
        pickup.setText(selectedStation.getAddress());
        date.setText(getIntent().getExtras().getString("selectedDate"));
        start_time.setText(getIntent().getExtras().getString("startTime"));
        end_time.setText(getIntent().getExtras().getString("endTime"));
        rate.setText("$" + getIntent().getExtras().getInt("rate") + "");
    }

    @Override
    public void onClick(View v) {

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String startTime = getIntent().getExtras().getString("startTime");
        String endTime = getIntent().getExtras().getString("endTime");
        String date = getIntent().getExtras().getString("selectedDate");
        int rate = getIntent().getExtras().getInt("rate");
        int hoursBooked = getIntent().getExtras().getInt("hoursBooked");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String id = databaseReference.push().getKey();
        CarBooking carBooking = new CarBooking(id, user, selectedCar, selectedStation, date, startTime, endTime, rate, hoursBooked, false);
        databaseReference.child("bookings").child(id).setValue(carBooking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Booking Confirmed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NearbyStations.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    private void showPaymentDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        card_number.setText("Card ends with " + user.getCreditCard().getCardNumber().substring(user.getCreditCard().getCardNumber().length() - 4));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
