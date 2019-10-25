package com.android.carrental.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.User;
import com.android.carrental.view.NearbyStations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMyAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText editTextStreetAddress;
    private EditText editTextAptNumber;
    private EditText editTextZipCode;
    private EditText editTextCity;
    private Button saveDetials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_account);
        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        editTextStreetAddress = (EditText) findViewById(R.id.street_address);
        editTextAptNumber = (EditText) findViewById(R.id.apt_number);
        editTextCity = (EditText) findViewById(R.id.city);
        editTextZipCode = (EditText) findViewById(R.id.zip_code);
        saveDetials = (Button) findViewById(R.id.save_details);
        saveDetials.setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("userDetails");
        if (bundle != null) {
            Log.i("NAME", bundle.getString("name"));
            editTextName.setText(bundle.getString("name"));
            editTextEmail.setText(bundle.getString("email"));
            editTextPhoneNumber.setText(bundle.getString("phoneNumber"));
            editTextStreetAddress.setText(bundle.getString("streetAddress"));
            editTextAptNumber.setText(bundle.getString("aptNumber"));
            editTextCity.setText(bundle.getString("city"));
            editTextZipCode.setText(bundle.getString("zipCode"));
            getSupportActionBar().setTitle("Edit Account");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_details) {
            saveUserDetials();
        }
    }

    private void saveUserDetials() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        final User newUser = new User(user.getId(),
                                editTextPhoneNumber.getText().toString().trim(),
                                editTextName.getText().toString().trim(),
                                editTextEmail.getText().toString().trim(),
                                editTextPhoneNumber.getText().toString().trim(),
                                editTextStreetAddress.getText().toString().trim(),
                                editTextAptNumber.getText().toString().trim(),
                                editTextCity.getText().toString().trim(),
                                editTextZipCode.getText().toString().trim());
                        User user1 = user;
                        newUser.setCreditCard(user.getCreditCard());
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), NearbyStations.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
