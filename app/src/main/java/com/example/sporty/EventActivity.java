package com.example.sporty;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        String eventName = getIntent().getStringExtra("event_name");
        String eventDescription = getIntent().getStringExtra("event_description");
        int eventImage = getIntent().getIntExtra("event_image", R.drawable.event_def);
        String eventDate = getIntent().getStringExtra("event_date");
        String eventResults = getIntent().getStringExtra("event_results");
        String eventPin = getIntent().getStringExtra("event_pin");

        ImageView eventImageView = findViewById(R.id.event_image);
        TextView eventNameTextView = findViewById(R.id.event_name);
        TextView eventDescriptionTextView = findViewById(R.id.event_description);
        TextView eventDateTextView = findViewById(R.id.event_date);
        TextView eventResultsTextView = findViewById(R.id.event_results);
        TextView eventPinTextView = findViewById(R.id.event_pin);

        // Set default image if no image is provided
        eventImageView.setImageResource(eventImage);
        eventNameTextView.setText(eventName);
        eventDescriptionTextView.setText(eventDescription);
        eventDateTextView.setText(eventDate);
        eventResultsTextView.setText(eventResults);

        if (eventPin != null && !eventPin.isEmpty()) {
            eventPinTextView.setText(eventPin);
            eventPinTextView.setVisibility(View.VISIBLE);
        }

        // Handle clicks to toggle visibility of additional information
        TextView generalInfoHeader = findViewById(R.id.general_info_header);
        LinearLayout generalInfoContent = findViewById(R.id.general_info_content);
        generalInfoHeader.setOnClickListener(v -> toggleVisibility(generalInfoContent));

        TextView entryListHeader = findViewById(R.id.entry_list_header);
        LinearLayout entryListContent = findViewById(R.id.entry_list_content);
        entryListHeader.setOnClickListener(v -> toggleVisibility(entryListContent));

        TextView contactInfoHeader = findViewById(R.id.contact_info_header);
        LinearLayout contactInfoContent = findViewById(R.id.contact_info_content);
        contactInfoHeader.setOnClickListener(v -> toggleVisibility(contactInfoContent));
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
