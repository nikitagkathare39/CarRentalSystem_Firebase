package com.android.carrental.payment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.CreditCard;
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


public class PaymentMethod extends AppCompatActivity implements View.OnClickListener {

    private Button save_card_details;
    private EditText card_number;
    private EditText editTextCvv;
    private EditText zip_code;
    private EditText exp_month;
    private EditText exp_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
        save_card_details = (Button) findViewById(R.id.save_card_details);
        card_number = (EditText) findViewById(R.id.card_number);
        editTextCvv = (EditText) findViewById(R.id.cvv);
        zip_code = (EditText) findViewById(R.id.zip_code);
        save_card_details.setOnClickListener(this);
        getSupportActionBar().setTitle("Edit Payment Method");
        exp_month = (EditText) findViewById(R.id.exp_month);
        exp_year = (EditText) findViewById(R.id.exp_year);
        fetchExistingCardDetails();
    }

    private void fetchExistingCardDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        card_number.setText(user.getCreditCard().getCardNumber());
                        zip_code.setText(user.getCreditCard().getZipCode());
                        editTextCvv.setText(user.getCreditCard().getCvv());
                        exp_month.setText(user.getCreditCard().getExpMonth());
                        exp_year.setText(user.getCreditCard().getExpYear());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_card_details:
                validateDetails();
                break;
        }
    }

    private void validateDetails() {
        String cardNumber = card_number.getText().toString().trim();
        String cvv = editTextCvv.getText().toString().trim();
        String zipCode = zip_code.getText().toString().trim();
        String monthString = exp_month.getText().toString().trim();
        String yearString = exp_year.getText().toString().trim();

        if (cardNumber.isEmpty() && cardNumber.length() < 16 && cardNumber.length() > 16) {
            card_number.setError(getString(R.string.input_error));
            card_number.requestFocus();
            return;
        }
        if (monthString.isEmpty()) {
            exp_month.setError(getString(R.string.input_error));
            exp_month.requestFocus();
            return;
        }
        if (yearString.isEmpty()) {
            exp_year.setError(getString(R.string.input_error));
            exp_year.requestFocus();
            return;
        }
        if (Integer.parseInt(monthString) < 1 && Integer.parseInt(monthString) < 13) {
            exp_month.setError(getString(R.string.input_error_date_month));
            exp_month.requestFocus();
            return;
        }
        if (yearString.length() != 4 && Integer.parseInt(yearString) < 2019) {
            exp_year.setError(getString(R.string.input_error_date_year));
            exp_year.requestFocus();
            return;
        }

        if (cvv.isEmpty()) {
            editTextCvv.setError(getString(R.string.input_error));
            editTextCvv.requestFocus();
            return;
        }
        if (zipCode.isEmpty()) {
            zip_code.setError(getString(R.string.input_error));
            zip_code.requestFocus();
            return;
        }

        final CreditCard card = new CreditCard(cardNumber, cvv, zipCode, monthString, yearString);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        User user1 = user;
                        user1.setCreditCard(card);
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Card Details Updated Successfully", Toast.LENGTH_LONG).show();
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
