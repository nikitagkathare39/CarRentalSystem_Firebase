package com.android.carrental.payment;

import android.app.Activity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PrePayment extends AppCompatActivity implements View.OnClickListener {

    private Button save_card_details;
    private EditText card_number;
    private EditText editTextCvv;
    private EditText zip_code;
    private EditText exp_month;
    private EditText exp_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_payment);
        save_card_details = (Button) findViewById(R.id.save_card_details_pre_payment);
        card_number = (EditText) findViewById(R.id.card_number_pre_payment);
        editTextCvv = (EditText) findViewById(R.id.cvv_pre_payment);
        zip_code = (EditText) findViewById(R.id.zip_code_pre_payment);
        save_card_details.setOnClickListener(this);
        getSupportActionBar().setTitle("Payment Method");
        exp_month = (EditText) findViewById(R.id.exp_month_pre_payment);
        exp_year = (EditText) findViewById(R.id.exp_year_pre_payment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_card_details_pre_payment:
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
        if (cardNumber.isEmpty() || cardNumber.length() < 16 || cardNumber.length() > 16) {
            card_number.setError("Enter 16 digit card number");
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

        CreditCard card = new CreditCard(cardNumber, cvv, zipCode, monthString, yearString);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("cardDetails", card);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
