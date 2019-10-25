package com.android.carrental.account;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.carrental.R;
import com.android.carrental.model.CreditCard;
import com.android.carrental.model.User;
import com.android.carrental.payment.PrePayment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DUPLICATE_USER_MESSAGE = "User with this email already exist.";
    private static final String USERS_KEY = "users";
    private static final String REQUIRED_FIELDS_MESSAGE = "All fields are required";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String PASSWORD_LENGTH_MESSAGE = "Password must be at least 6 chars long";
    private Button signup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFullName;
    private EditText editTextStreetAddress;
    private EditText editTextPhoneNumber;
    private EditText editTextAptNumber;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private Button payment;
    private CreditCard creditCard = null;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = (Button) findViewById(R.id.register_user);
        editTextEmail = (EditText) findViewById(R.id.user_email);
        editTextPassword = (EditText) findViewById(R.id.user_password);
        editTextFullName = (EditText) findViewById(R.id.name);
        editTextStreetAddress = (EditText) findViewById(R.id.address_street);
        editTextAptNumber = (EditText) findViewById(R.id.address_apt_number);
        editTextCity = (EditText) findViewById(R.id.address_city);
        editTextZipCode = (EditText) findViewById(R.id.address_zip_code);
        payment = (Button) findViewById(R.id.payment_selector);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        signup.setOnClickListener(this);
        payment.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Register");

    }

    private void registerUser() {
        final String name = editTextFullName.getText().toString().trim();
        final String street_address = editTextStreetAddress.getText().toString().trim();
        final String aptNumber = editTextAptNumber.getText().toString().trim();
        final String city = editTextCity.getText().toString().trim();
        final String zipCode = editTextZipCode.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (name.isEmpty()) {
            editTextFullName.setError(getString(R.string.input_error_name));
            editTextFullName.requestFocus();
            return;
        }
        if (street_address.isEmpty()) {
            editTextStreetAddress.setError(getString(R.string.input_error));
            editTextStreetAddress.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            editTextCity.setError(getString(R.string.input_error));
            editTextCity.requestFocus();
            return;
        }
        if (zipCode.isEmpty()) {
            editTextZipCode.setError(getString(R.string.input_error));
            editTextZipCode.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        if (creditCard == null) {
            Toast.makeText(getApplicationContext(), "Please add payment method first", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            User newUser = new User(uid, phoneNumber, name, email, phoneNumber, street_address, aptNumber, city, zipCode);
                            newUser.setCreditCard(creditCard);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(uid)
                                    .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        openLoginActivity();
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(getApplicationContext(), "Email Already Exists!!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_user) {
            registerUser();
        } else if (v.getId() == R.id.payment_selector) {
            Intent intentToPayment = new Intent(this, PrePayment.class);
            startActivityForResult(intentToPayment, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            creditCard = (CreditCard) data.getExtras().getSerializable("cardDetails");
            payment.setText("Added");
        }
    }

    private void openLoginActivity() {
        Intent returnToLogin = new Intent();
        returnToLogin.putExtra(EMAIL_KEY, editTextEmail.getText().toString());
        returnToLogin.putExtra(PASSWORD_KEY, editTextPassword.getText().toString());
        setResult(Activity.RESULT_OK, returnToLogin);
        finish();
    }
}
