package com.android.carrental.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.CarModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCarModel extends AppCompatActivity implements View.OnClickListener {

    private Button save_model;
    private EditText model_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_model);
        save_model = (Button) findViewById(R.id.save_model);
        model_name = (EditText) findViewById(R.id.model_name);
        save_model.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String id = databaseReference.push().getKey();
        CarModel carModel = new CarModel(model_name.getText().toString(), model_name.getText().toString());
        databaseReference.child("carmodels").child(id).setValue(carModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("saved", "saved");
                Toast.makeText(getApplicationContext(), "Saved Succesfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
