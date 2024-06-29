package com.example.eventmanagementsqlite;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventRegistration extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);

        dbHelper = new DBHelper(this);
        Button btnRegister = findViewById(R.id.btnRegister);
        EditText edtEventTitle = findViewById(R.id.edtEventTitle);
        EditText edtEventDes = findViewById(R.id.edtEventDes);
        EditText edtEventDate = findViewById(R.id.edtEventDate);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtEventTitle.getText().toString().trim();
                String description = edtEventDes.getText().toString().trim();
                String date = edtEventDate.getText().toString().trim();
                Event event = new Event(title, description, date);
                if (validateInput(title, description, date)) {

                    if (dbHelper.insertEvent(event) > 0) {
                        Toast.makeText(getApplicationContext(), "Event registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to register event", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInput(String title, String description, String date) {
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter an event title", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter an event description", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please enter an event date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            Toast.makeText(this, "Please enter a valid date (DD-MM-YYYY)", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
