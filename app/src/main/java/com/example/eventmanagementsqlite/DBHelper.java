package com.example.eventmanagementsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "eventsdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Events (" +
                "title TEXT PRIMARY KEY, "+
                "description TEXT, " +
                "date TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Events");
        onCreate(db);
    }

    // Insert a new event
    public int insertEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("title", event.getTitle());
        content.put("description", event.getDescription());
        content.put("date", event.getDate());
        int result = (int)db.insert("Events",
                null, content);
        db.close();
        return result;
    }

    // Update a specific event
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("title", event.getTitle());
        content.put("description", event.getDescription());
        content.put("date", event.getDate());
        int result = db.update("Events", content, "title=?", new String[]{event.getTitle()});
        db.close();
        return result;
    }

    // Delete a specific event
    public int deleteEvent(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Events", "title=?", new String[]{title});
        db.close();
        return result;
    }

    // Search for a specific event
    public Event getEvent(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Events", new String[]{"title", "description", "date"},
                "title=?", new String[]{title}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Event event = new Event(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            return event;
        } else {
            return null;
        }
    }

    // Get all events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, description, date FROM Events", null);

        if(cursor != null)
            cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            eventList.add(new Event(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return eventList;
    }

    // Search for events matching title, description, and date
    public ArrayList<Event> searchEvent(String title, String description, String date) {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM events WHERE 1=1");
        ArrayList<String> args = new ArrayList<>();

        if (!title.isEmpty()) {
            query.append(" AND title LIKE ?");
            args.add("%" + title + "%");
        }
        if (!description.isEmpty()) {
            query.append(" AND description LIKE ?");
            args.add("%" + description + "%");
        }
        if (!date.isEmpty()) {
            query.append(" AND date LIKE ?");
            args.add("%" + date + "%");
        }

        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        if (cursor != null) {
            while (cursor.moveToNext()) {
                eventList.add(new Event(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2)));
            }
            cursor.close();
        }
        db.close();
        return eventList;
    }
}
