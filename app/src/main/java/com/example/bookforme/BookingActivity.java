package com.example.bookforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bookforme.models.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity {
    // Variables for storing View references and local data
    int startHour, startMin, endHour, endMin;
    EditText startTime, endTime, noOfPeople, tableNumber;
    Chip chech, available;
    TextView sorry;

    // Initialising firebase variables
    private DatabaseReference bookRef;
    private FirebaseAuth fauth;

    // Boolean array for storing available tables from database
    private boolean[] totalTables = {false, false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        // Getting View References
        startTime = findViewById(R.id.booking_start);
        endTime = findViewById(R.id.booking_end);

        bookRef = FirebaseDatabase.getInstance().getReference();
        fauth = FirebaseAuth.getInstance();

        tableNumber = findViewById(R.id.booking_table);
        noOfPeople = findViewById(R.id.booking_people);

        sorry = findViewById(R.id.booking_sorry);
        available = findViewById(R.id.booking_available);
        chech = findViewById(R.id.booking_check);

        // One time database read on startup to check availability of tables.
        bookRef.child("bookings").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                    // Read operation failed! Alerting user with Toast
                    Toast.makeText(BookingActivity.this, "Error: "+task.getException(), Toast.LENGTH_LONG).show();
                }
                else {

                    // Read operation successfull! Getting data and checking available tabels logic
                    HashMap mp;
                    HashMap res = (HashMap) Objects.requireNonNull(task.getResult()).getValue();
                    for(Object i: Objects.requireNonNull(res).values()){
                        mp = (HashMap) i;
                        Toast.makeText(BookingActivity.this, ""+mp.get("tableNumber"), Toast.LENGTH_LONG).show();
                        totalTables[Integer.parseInt(Objects.requireNonNull(mp.get("tableNumber")).toString())] = true;
                    }
                }
            }
        });
        tableNumber.setOnClickListener(new View.OnClickListener() { //Click listerer on numberOfTables to hide error message
            @Override
            public void onClick(View v) {
                sorry.setText("Sorry! The table is not available. Please pick another one");
                sorry.setVisibility(View.GONE);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() { // Click Listener on Start time for showing TimePickerDialog and getting tie.
            @Override
            public void onClick(View v) {
                TimePickerDialog tDialog = new TimePickerDialog(
                        BookingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startHour = hourOfDay;
                                startMin = minute;
                                Calendar cal = Calendar.getInstance();
                                cal.set(0, 0, 0, startHour, startMin);
                                startTime.setText(DateFormat.format("hh:mm aa", cal));
                            }
                        }, 12, 0, false
                );
                tDialog.updateTime(startHour, startMin);
                tDialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() { // Click Listener on Start time for showing TimePickerDialog and getting tie.
            @Override
            public void onClick(View v) {
                TimePickerDialog tDialog = new TimePickerDialog(
                        BookingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endHour = hourOfDay;
                                endMin = minute;
                                Calendar cal = Calendar.getInstance();
                                cal.set(0, 0, 0, endHour, endMin);
                                endTime.setText(DateFormat.format("hh:mm aa", cal));
                            }
                        }, 12, 0, false
                );
                tDialog.updateTime(endHour, endMin);
                tDialog.show();
            }
        });
    }

    public void bookTable(View view){ // If seat is available, click handler for green button.
        Booking booking = new Booking(startTime.getText().toString(), endTime.getText().toString(), noOfPeople.getText().toString(), tableNumber.getText().toString());
        try{
            bookRef.child("bookings").child(Objects.requireNonNull(fauth.getCurrentUser()).getUid()).setValue(booking);
            Toast.makeText(BookingActivity.this, "Booking successfull", Toast.LENGTH_LONG).show();
            startActivity(new Intent(BookingActivity.this, MainActivity.class));
            finish();
        }
        catch (DatabaseException e){
            System.out.println(e);
            Toast.makeText(BookingActivity.this, "Error: "+e, Toast.LENGTH_LONG).show();
        }
    }

    public void checkAvailability(View v){ // Checking whether the table is available
        int number = Integer.parseInt(tableNumber.getText().toString());
        if(number>8 || number<1){
            sorry.setVisibility(View.VISIBLE);
            sorry.setText("Please enter a valid table number between 1 and 8");
            return;
        }
        if(!totalTables[number]){
            available.setVisibility(View.VISIBLE);
            chech.setVisibility(View.GONE);
        } else{
            sorry.setVisibility(View.VISIBLE);
        }
    }
}