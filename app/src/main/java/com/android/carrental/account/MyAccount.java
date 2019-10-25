package com.android.carrental.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.carrental.R;
import com.android.carrental.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccount extends AppCompatActivity implements View.OnClickListener {
    private TextView name_on_my_account;
    private TextView email_on_my_account;
    private TextView phone_on_my_account;
    private TextView street_on_my_account;
    private TextView apt_on_my_account;
    private TextView city_on_my_account;
    private TextView zip_on_my_account;
    private Button edit_my_account;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        name_on_my_account = (TextView) findViewById(R.id.name_on_account);
        email_on_my_account = (TextView) findViewById(R.id.email_on_account);
        phone_on_my_account = (TextView) findViewById(R.id.phone_on_account);
        street_on_my_account = (TextView) findViewById(R.id.street_on_account);
        apt_on_my_account = (TextView) findViewById(R.id.apt_on_account);
        city_on_my_account = (TextView) findViewById(R.id.city_on_account);
        zip_on_my_account = (TextView) findViewById(R.id.zip_on_account);
        edit_my_account = (Button) findViewById(R.id.edit_details);
        edit_my_account.setOnClickListener(this);
        getSupportActionBar().setTitle("My Account");
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        final String uid = FirebaseAuth.getInstance().getUid();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    user = users.getValue(User.class);
                    if (user.getId().equals(uid)) {
                        name_on_my_account.setText(user.getName());
                        email_on_my_account.setText(user.getEmail());
                        phone_on_my_account.setText(user.getPhoneNumber());
                        street_on_my_account.setText(user.getStreetAddress());
                        apt_on_my_account.setText(user.getAptNumber());
                        city_on_my_account.setText(user.getCity());
                        zip_on_my_account.setText(user.getZipCode());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_details) {
            Intent intent = new Intent(MyAccount.this, EditMyAccount.class);
            Bundle userDetails = new Bundle();
            userDetails.putString("name", user.getName());
            userDetails.putString("email", user.getEmail());
            userDetails.putString("phoneNumber", user.getPhoneNumber());
            userDetails.putString("streetAddress", user.getStreetAddress());
            userDetails.putString("aptNumber", user.getAptNumber());
            userDetails.putString("city", user.getCity());
            userDetails.putString("zipCode", user.getZipCode());
            intent.putExtra("userDetails", userDetails);
            startActivity(intent);
        }
    }
}
