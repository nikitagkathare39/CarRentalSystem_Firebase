package com.android.carrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.carrental.carbooking.CarBookingDashboard;
import com.android.carrental.R;
import com.android.carrental.model.Car;
import com.android.carrental.model.Station;

import java.util.List;

public class AvailableCarsAdapter extends RecyclerView.Adapter<AvailableCarsAdapter.AvailableCarViewHolder> {

    private String startTime;
    private String endTime;
    private Station selectedStation;
    private String selectedDate;
    private int hoursBooked;
    private Context context;
    private List<Car> availableCars;
    private Activity activity;

    public AvailableCarsAdapter(Context context, List<Car> availableCars, Activity activity, Station selectedStation, String selectedDate,
                                String startTime, String endTime, int hoursBooked) {
        this.availableCars = availableCars;
        this.context = context;
        this.selectedStation = selectedStation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.selectedDate = selectedDate;
        this.activity = activity;
        this.hoursBooked = hoursBooked;
    }

    @Override
    public AvailableCarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item, viewGroup, false);
        return new AvailableCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailableCarViewHolder availableCarViewHolder, int i) {
        final Car availableCar = availableCars.get(i);
        availableCarViewHolder.car_name.setText(availableCar.getName());
        availableCarViewHolder.car_color.setText(availableCar.getColor());
        availableCarViewHolder.cost_per_hour.setText("$" + availableCar.getRate() + "/hr");
        availableCarViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDashboard(availableCar);
            }
        });
    }

    private void showBookingDashboard(Car availableCar) {
        Intent intentToCarBooking = new Intent(activity, CarBookingDashboard.class);
        intentToCarBooking.putExtra("selectedStation", selectedStation);
        intentToCarBooking.putExtra("selectedCar", availableCar);
        intentToCarBooking.putExtra("rate", availableCar.getRate() * hoursBooked);
        intentToCarBooking.putExtra("startTime", startTime);
        intentToCarBooking.putExtra("endTime", endTime);
        intentToCarBooking.putExtra("selectedDate", selectedDate);
        intentToCarBooking.putExtra("hoursBooked", hoursBooked);
        activity.startActivity(intentToCarBooking);
    }

    @Override
    public int getItemCount() {
        return availableCars.size();
    }

    public class AvailableCarViewHolder extends RecyclerView.ViewHolder {

        public TextView car_name;
        public TextView car_color;
        public TextView cost_per_hour;

        public AvailableCarViewHolder(View itemView) {
            super(itemView);
            car_name = itemView.findViewById(R.id.car_name);
            car_color = itemView.findViewById(R.id.car_color);
            cost_per_hour = itemView.findViewById(R.id.car_cost_per_hour);
        }
    }

}
