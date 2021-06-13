package com.example.bookforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookforme.databinding.ActivityBookingBinding;
import com.example.bookforme.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    FirebaseAuth auth;
    DatabaseReference database;
    TextView name, email;
    Chip pic;
    Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.name);
        email = findViewById(R.id.profileEmail);
        pic = findViewById(R.id.profilePic);
        signout = findViewById(R.id.profileSignout);
        pic.setText("U");
        email.setText("Loading");
        name.setText("Loading");

        database.child("User").child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, ""+task.getException(), Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    HashMap res = (HashMap) task.getResult().getValue();
                    String nameText = res.get("username").toString();
                    String emailText = res.get("email").toString();
                    char ch = nameText.charAt(0);
                    String s = ""+ch;
                    pic.setText(s);
                    email.setText(emailText);
                    name.setText(nameText);
                }
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    auth.signOut();
                    Toast.makeText(ProfileActivity.this, "Signing Out.....", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                    finish();
                }
                catch (DatabaseException e){
                    Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}