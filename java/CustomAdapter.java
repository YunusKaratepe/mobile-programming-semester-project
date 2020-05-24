package com.yunuskaratepe.semester_project;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    ArrayList<Event> events;
    LayoutInflater inflater;
    Context context;


    public CustomAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.eventName.setText("Name: " +  events.get(position).getName());
        holder.eventStartingDate.setText("Starting: " + events.get(position).getStartingDate().toString());
        holder.eventEndingDate.setText("Ending: " + events.get(position).getEndingDate().toString());
        holder.linearLayout.setTag(holder);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListEventsActivity.selectedEvent = events.get(position);
                System.out.println("(CustomAdapter) selectedIndex: " + position);
                notifyDataSetChanged();
            }
        });

        if (ListEventsActivity.selectedEvent == events.get(position)) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FF969696"));
        }else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#F3F3F3F3"));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventStartingDate, eventEndingDate;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventStartingDate = itemView.findViewById(R.id.eventStartingDate);
            eventEndingDate = itemView.findViewById(R.id.eventEndingDate);
            linearLayout = itemView.findViewById(R.id.cardLinearLayout);
        }

    }
}
