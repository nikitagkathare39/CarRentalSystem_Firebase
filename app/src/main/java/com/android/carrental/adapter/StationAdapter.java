package com.android.carrental.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.carrental.carbooking.CarOptionsFilter;
import com.android.carrental.R;
import com.android.carrental.model.Station;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private static final String SEPARATOR = ", ";
    private Context context;
    private List<Station> stations;

    public StationAdapter(Context context, List<Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.station_item, viewGroup, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StationViewHolder stationViewHolder, int i) {
        final Station station = stations.get(i);
        stationViewHolder.station_address.setText(station.getAddress());
        stationViewHolder.station_city_and_state.setText(station.getCity().concat(SEPARATOR).concat(station.getState()));
        stationViewHolder.station_distance.setText(station.getDistance() + "");
        stationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectedStationDetails(station);
            }
        });
    }

    private void openSelectedStationDetails(Station station) {
        Intent intent = new Intent(context, CarOptionsFilter.class);
        intent.putExtra("selectedCarDetails", station);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public class StationViewHolder extends RecyclerView.ViewHolder {

        public TextView station_address;
        public TextView station_city_and_state;
        public TextView station_distance;

        public StationViewHolder(View itemView) {
            super(itemView);
            station_address = itemView.findViewById(R.id.station_address);
            station_city_and_state = itemView.findViewById(R.id.station_city_and_state);
            station_distance = itemView.findViewById(R.id.station_distance);
        }
    }

}