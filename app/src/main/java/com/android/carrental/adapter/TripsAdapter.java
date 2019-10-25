package com.android.carrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.carrental.R;
import com.android.carrental.help.EditTripRefund;
import com.android.carrental.model.CarBooking;
import com.android.carrental.view.MyBookingDetails;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.BookingsViewHolder> {
    private Context context;
    private List<CarBooking> bookings;
    private Activity activity;

    public TripsAdapter(Context context, List<CarBooking> bookings, Activity activity) {
        this.bookings = bookings;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public TripsAdapter.BookingsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, viewGroup, false);
        return new TripsAdapter.BookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripsAdapter.BookingsViewHolder bookingsViewHolder, int i) {
        final CarBooking booking = bookings.get(i);
        bookingsViewHolder.booked_car.setText(booking.getCar().getName());
        bookingsViewHolder.booking_date.setText(booking.getBookingDate());
        bookingsViewHolder.booking_status.setImageResource(getStatus(booking.isComplete()));
        bookingsViewHolder.booking_station.setText(booking.getStation().getAddress());
        bookingsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(booking);
            }
        });
    }

    private int getStatus(boolean isComplete) {
        if (isComplete) {
            return R.drawable.ic_check_black_24dp;
        }
        return R.drawable.icons8_hourglass_24;
    }

    private void showBookingDetails(CarBooking booking) {
        Intent intentToBookingDetails = new Intent(activity, EditTripRefund.class);
        intentToBookingDetails.putExtra("booking", booking);
        activity.startActivity(intentToBookingDetails);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class BookingsViewHolder extends RecyclerView.ViewHolder {

        public TextView booking_date;
        public TextView booked_car;
        public ImageView booking_status;
        public TextView booking_station;

        public BookingsViewHolder(View itemView) {
            super(itemView);
            booking_date = itemView.findViewById(R.id.booking_date);
            booked_car = itemView.findViewById(R.id.booking_car);
            booking_status = itemView.findViewById(R.id.booking_status);
            booking_station = itemView.findViewById(R.id.booking_station);
        }
    }

}
