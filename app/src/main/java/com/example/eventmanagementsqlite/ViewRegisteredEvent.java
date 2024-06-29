package com.example.eventmanagementsqlite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewRegisteredEvent extends AppCompatActivity {
    DBHelper dbHelper;
    EditText edtEventTitle, edtEventDes, edtEventDate;
    Button btnDelete, btnUpdate, btnSearch;
    ListView lvEvents;
    EventAdapter eventAdapter;
    List<Event> eventList;
    Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registered_event);

        dbHelper = new DBHelper(this);
        eventList = dbHelper.getAllEvents();

        edtEventTitle = findViewById(R.id.edtEventTitle);
        edtEventDes = findViewById(R.id.edtEventDes);
        edtEventDate = findViewById(R.id.edtEventDate);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnSearch = findViewById(R.id.btnSearch);
        lvEvents = findViewById(R.id.lvEvents);
        eventAdapter = new EventAdapter(this, eventList);
        lvEvents.setAdapter(eventAdapter);

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEvent = eventList.get(position);
                showEventDetailsPopup(selectedEvent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtEventTitle.getText().toString().trim();
                String description = edtEventDes.getText().toString().trim();
                String date = edtEventDate.getText().toString().trim();

                List<Event> results = dbHelper.searchEvent(title, description, date);

                if (results.isEmpty()) {
                    Toast.makeText(ViewRegisteredEvent.this, "No events found", Toast.LENGTH_SHORT).show();
                } else {
                    eventList.clear();
                    eventList.addAll(results);
                    eventAdapter.notifyDataSetChanged();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtEventTitle.getText().toString().trim();
                String description = edtEventDes.getText().toString().trim();
                String date = edtEventDate.getText().toString().trim();
                Event event = new Event(title, description, date);
                if(dbHelper.updateEvent(event) > 0) {
                    Toast.makeText(ViewRegisteredEvent.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                    eventList.clear();
                    eventList.addAll(dbHelper.getAllEvents());
                    eventAdapter.notifyDataSetChanged();
                    clearInputFields();
                } else {
                    Toast.makeText(ViewRegisteredEvent.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtEventTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(ViewRegisteredEvent.this, "Please enter event title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.deleteEvent(title) > 0) {
                    Toast.makeText(ViewRegisteredEvent.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    eventList.clear();
                    eventList.addAll(dbHelper.getAllEvents());
                    eventAdapter.notifyDataSetChanged();
                    clearInputFields();
                } else {
                    Toast.makeText(ViewRegisteredEvent.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void populateInputFields(Event event) {
        edtEventTitle.setText(event.getTitle());
        edtEventDes.setText(event.getDescription());
        edtEventDate.setText(event.getDate());
    }

    private void clearInputFields() {
        edtEventTitle.setText("");
        edtEventDes.setText("");
        edtEventDate.setText("");
    }

    // View Registered Event
    private void showEventDetailsPopup(final Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("View Event Details");

        View view = getLayoutInflater().inflate(R.layout.popup_event_details, null);

        TextView tvTitle = view.findViewById(R.id.tvPopupEventTitle);
        TextView tvDescription = view.findViewById(R.id.tvPopupEventDes);
        TextView tvDate = view.findViewById(R.id.tvPopupEventDate);

        tvTitle.setText("Event Title: " + event.getTitle());
        tvDescription.setText("Event Description: " + event.getDescription());
        tvDate.setText("Event Date: " + event.getDate());

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                populateInputFields(event);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
