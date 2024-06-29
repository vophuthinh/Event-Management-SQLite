package com.example.eventmanagementsqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private List<Event> eventList;
    private LayoutInflater inflater;


    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_list_event, parent, false);
        }
        TextView tvTitle = convertView.findViewById(R.id.tvEventTitle);

        Event event = eventList.get(position);
        tvTitle.setText(event.getTitle());

        return convertView;
    }


}
