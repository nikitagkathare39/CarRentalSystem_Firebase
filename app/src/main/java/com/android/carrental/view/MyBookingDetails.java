package com.android.carrental.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.CarBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyBookingDetails extends AppCompatActivity implements View.OnClickListener {

    private ImageView car_model_image;
    private TextView car_name;
    private TextView pickup_location;
    private TextView start_time;
    private TextView end_time;
    private TextView date;
    private TextView total_fare;
    private Button finish_booking;
    //    private Button extend_booking;
    private CarBooking carBooking;
    private static final String AM = "AM";
    private static final String PM = "PM";
    private static String TIME_AM_PM = "";
    private static final String TIME_SEPARATOR = ":00";
    private static final String TIME_DIVIDER = "12";
    public static final String DATE_FORMAT = "dd MMM yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking_details);
        car_name = (TextView) findViewById(R.id.car_name_booking_details);
        pickup_location = (TextView) findViewById(R.id.pickup_location_on_booking_details);
        start_time = (TextView) findViewById(R.id.start_time_on_booking_details);
        end_time = (TextView) findViewById(R.id.end_time_on_booking_details);
        date = (TextView) findViewById(R.id.date_on_booking_details);
        total_fare = (TextView) findViewById(R.id.rate_on_booking_details);
        finish_booking = (Button) findViewById(R.id.finish_trip);
//        extend_booking = (Button) findViewById(R.id.extend_trip);
        finish_booking.setOnClickListener(this);
//        extend_booking.setOnClickListener(this);
        getSupportActionBar().setTitle("Booking Details");
        carBooking = (CarBooking) getIntent().getSerializableExtra("booking");
        if (carBooking.getBookingDate().trim().equals(getCurrentDate(0).trim())) {
            finish_booking.setText("CANCEL");
            if (getTimeDifference() <= 0)
                finish_booking.setText("FINISH");
        } else if (isCancel()) {
            finish_booking.setText("CANCEL");
        }

        if (carBooking.isComplete()) {
            finish_booking.setVisibility(View.GONE);
//            extend_booking.setVisibility(View.GONE);
        }
        fetchDetailsForMyBooking();

    }

    private void fetchDetailsForMyBooking() {
        car_name.setText(carBooking.getCar().getName());
        pickup_location.setText(carBooking.getStation().getAddress());
        start_time.setText(carBooking.getStartTime());
        end_time.setText(carBooking.getEndTime());
        total_fare.setText("$" + carBooking.getRate());
        date.setText(carBooking.getBookingDate());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_trip:
                finishTrip();
                break;
            /*case R.id.extend_trip:
                Intent intent = new Intent(this, ExtendTrip.class);
                intent.putExtra("booking", carBooking);
                startActivity(intent);
                break;*/
        }
    }

    private void finishTrip() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CarBooking booking = snapshot.getValue(CarBooking.class);
                    if (carBooking.getId().equals(booking.getId())) {
                        carBooking.setComplete(true);
                        if (finish_booking.getText().toString().equals("CANCEL")) {
                            carBooking.setRate(2);
                            Toast.makeText(getApplicationContext(), "Cancellation Fee of $2 is applied", Toast.LENGTH_LONG).show();
                        }
                        databaseReference.child(booking.getId()).setValue(carBooking).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                Intent intent = new Intent(getApplicationContext(), NearbyStations.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getCurrentTime(int step) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, step);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return formatTime(hour);
    }

    private String formatTime(int hour) {
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hour);
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            TIME_AM_PM = AM;
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            TIME_AM_PM = PM;
        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? TIME_DIVIDER : datetime.get(Calendar.HOUR) + "";
        return strHrsToShow + TIME_SEPARATOR + " " + TIME_AM_PM;
    }

    private int getTimeDifference() {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT + " hh:mm a");
            Date d1 = df.parse(carBooking.getBookingDate() + " " + carBooking.getStartTime());
            Date d2 = df.parse(carBooking.getBookingDate() + " " + getCurrentTime(0));
            int hoursDifference = (int) ((d1.getTime() - d2.getTime()) / 3600000L);

            if (getCurrentTime(0).equals(modifiedEndTime())) {
                hoursDifference += 1;
            }
            return hoursDifference;
        } catch (Exception e) {
            Log.i("exception", e.getMessage());
        }
        return 0;
    }

    private String modifiedEndTime() {
        return "11:59 PM";
    }

    private String getCurrentDate(int step) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, step);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return formatDate(year, month, day);
    }

    private static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    private boolean isCancel() {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            if ((df.parse(carBooking.getBookingDate())).before(df.parse(getCurrentDate(0)))) {
                return false;
            }
        } catch (Exception e) {

        }
        return true;
    }
}
