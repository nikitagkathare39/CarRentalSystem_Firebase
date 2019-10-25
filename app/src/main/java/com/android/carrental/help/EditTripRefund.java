package com.android.carrental.help;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.CarBooking;
import com.android.carrental.model.TripIssue;
import com.android.carrental.model.User;
import com.android.carrental.view.MyBookings;
import com.android.carrental.view.NearbyStations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditTripRefund extends AppCompatActivity implements View.OnClickListener {
    private ImageView car_model_image;
    private TextView car_name;
    private TextView pickup_location;
    private TextView start_time;
    private TextView end_time;
    private TextView date;
    private TextView total_fare;
    private EditText issue_on_booking_details;
    private Button submit;
    CarBooking carBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip_refund);
//        car_model_image = (ImageView) findViewById(R.id.car_model_image);
        car_name = (TextView) findViewById(R.id.car_name_booking_details);
        pickup_location = (TextView) findViewById(R.id.pickup_location_on_booking_details);
        start_time = (TextView) findViewById(R.id.start_time_on_booking_details);
        end_time = (TextView) findViewById(R.id.end_time_on_booking_details);
        date = (TextView) findViewById(R.id.date_on_booking_details);
        total_fare = (TextView) findViewById(R.id.rate_on_booking_details);
        issue_on_booking_details = (EditText) findViewById(R.id.issue_on_booking_details);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        carBooking = (CarBooking) getIntent().getSerializableExtra("booking");
        fetchDetailsForMyBooking();
    }

    private void fetchDetailsForMyBooking() {

        car_name.setText(carBooking.getCar().getName());
        pickup_location.setText(carBooking.getStation().getAddress());
        start_time.setText(carBooking.getStartTime());
        end_time.setText(carBooking.getEndTime());
        total_fare.setText(carBooking.getRate() + "");
        date.setText(carBooking.getBookingDate());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String id = FirebaseDatabase.getInstance().getReference().child(uid).child("issues").push().getKey();
            FirebaseDatabase.getInstance().getReference().child("issues").child(id).setValue(new TripIssue(uid, carBooking.getStation().getAddress(),
                    carBooking.getBookingDate(),
                    carBooking.getStartTime(),
                    carBooking.getEndTime(),
                    carBooking.getCar(),
                    carBooking.getRate(),
                    issue_on_booking_details.getText().toString().trim())).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Issue Submited", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NearbyStations.class);
                    startActivity(intent);
                }
            });
            finish();
        }
    }
}
