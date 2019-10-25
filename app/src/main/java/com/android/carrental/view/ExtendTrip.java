package com.android.carrental.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.CarBooking;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ExtendTrip extends AppCompatActivity implements View.OnClickListener {
    private Button station_display;
    private Button date_display;
    private Button start_time;
    private Button car_type_display;
    private TimePickerDialog timePickerDialog;
    private Button end_time;
    private Button extend_car;
    private Calendar calendar;
    CarBooking carSelected;
    private static final String AM = "AM";
    private static final String PM = "PM";
    private static String TIME_AM_PM = "";
    private static final String TIME_SEPARATOR = ":00";
    private static final String TIME_DIVIDER = "12";
    public static final String DATE_FORMAT = "dd MMM yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_trip);
        carSelected = (CarBooking) getIntent().getSerializableExtra("booking");
        initWidgets();
    }

    private void initWidgets() {
        station_display = (Button) findViewById(R.id.station_display);
        date_display = (Button) findViewById(R.id.date_display);
        start_time = (Button) findViewById(R.id.start_time);
        car_type_display = (Button) findViewById(R.id.car_type_display);
        end_time = (Button) findViewById(R.id.end_time);
        extend_car = (Button) findViewById(R.id.extend_car);
        calendar = Calendar.getInstance();
        station_display.setText(carSelected.getStation().getAddress());
        date_display.setText(carSelected.getBookingDate());
        start_time.setText(carSelected.getStartTime());
        car_type_display.setText(carSelected.getCar().getName());
        end_time.setOnClickListener(this);
        extend_car.setOnClickListener(this);
    }

    private void selectEndTime() {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String endTime = formatTime(hourOfDay);
                if (endTime.equals("12:00 AM")) {
                    endTime = modifiedEndTime();
                }
                end_time.setText(endTime);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
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

    private void extendCar() {
        if (getTimeDifference() > 0) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CarBooking carBooking = snapshot.getValue(CarBooking.class);
                        if (carSelected.getId().equals(carBooking.getId())) {
                            CarBooking newBooking = carBooking;
                            newBooking.setRate(newBooking.getRate() + getRate());
                            newBooking.setEndTime(end_time.getText().toString().trim());
                            databaseReference.child(newBooking.getId()).setValue(carBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Trip Extended Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), NearbyStations.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "End time must be greater than start time", Toast.LENGTH_SHORT).show();
        }


    }

    private int getTimeDifference() {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT + " hh:mm a");
            Date d1 = df.parse(carSelected.getBookingDate() + " " + carSelected.getStartTime());
            Date d2 = df.parse(carSelected.getBookingDate() + " " + end_time.getText().toString());
            int hoursDifference = (int) ((d2.getTime() - d1.getTime()) / 3600000L);
            if (end_time.getText().toString().equals(modifiedEndTime())) {
                hoursDifference += 1;
            }
            return hoursDifference;
        } catch (Exception e) {
            Log.i("exception", e.getMessage());
        }
        return 0;
    }

    private int getRate() {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT + " hh:mm a");
            Date d1 = df.parse(carSelected.getBookingDate() + " " + carSelected.getEndTime());
            Date d2 = df.parse(carSelected.getBookingDate() + " " + end_time.getText().toString());
            int hoursDifference = (int) ((d2.getTime() - d1.getTime()) / 3600000L);
            if (end_time.getText().toString().equals(modifiedEndTime())) {
                hoursDifference += 1;
            }
            return hoursDifference * carSelected.getRate();
        } catch (Exception e) {
            Log.i("exception", e.getMessage());
        }
        return 0;
    }

    private String modifiedEndTime() {
        return "11:59 PM";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_time:
                selectEndTime();
                break;
            case R.id.extend_car:
                extendCar();
                break;
        }
    }
}
