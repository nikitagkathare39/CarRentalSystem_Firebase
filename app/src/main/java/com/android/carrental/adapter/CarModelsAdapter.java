package com.android.carrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.carrental.R;
import com.android.carrental.model.CarModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarModelsAdapter extends RecyclerView.Adapter<CarModelsAdapter.CarModelViewHolder> {

    private Context context;
    private List<CarModel> carmodels;
    private Activity activity;

    public CarModelsAdapter(Context context, List<CarModel> carmodels, Activity activity) {
        this.context = context;
        this.carmodels = carmodels;
        this.activity = activity;
    }

    @Override
    public CarModelsAdapter.CarModelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_model_item, viewGroup, false);
        return new CarModelsAdapter.CarModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CarModelsAdapter.CarModelViewHolder carModelViewHolder, int i) {
        final CarModel carModel = carmodels.get(i);
        carModelViewHolder.car_model_name.setText(carModel.getName());
        carModelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarFilterActivity(carModel);
            }
        });
    }

    private void openCarFilterActivity(CarModel carModel) {
        Intent returnToCarFilter = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("carmodel", carModel);
        returnToCarFilter.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, returnToCarFilter);
        activity.finish();
    }

    @Override
    public int getItemCount() {
        return carmodels.size();
    }

    public class CarModelViewHolder extends RecyclerView.ViewHolder {

        public TextView car_model_name;

        public CarModelViewHolder(View itemView) {
            super(itemView);
            car_model_name = itemView.findViewById(R.id.car_model_name);
        }
    }

}
