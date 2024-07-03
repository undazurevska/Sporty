package com.example.sporty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sporty.data.database.AppDatabase;
import com.example.sporty.data.entities.Event;

public class CreateEventActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView eventImageView;
    private EditText eventNameInput;
    private EditText eventDescriptionInput;
    private EditText eventDateInput;
    private EditText eventResultsInput;
    private EditText eventPinInput;
    private Button saveEventButton;
    private Button uploadImageButton;
    private AppDatabase db;
    private Uri selectedImageUri;
    private int eventImageResourceId = R.drawable.event_def;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventImageView = findViewById(R.id.event_image);
        eventNameInput = findViewById(R.id.event_name);
        eventDescriptionInput = findViewById(R.id.event_description);
        eventDateInput = findViewById(R.id.event_date);
        eventResultsInput = findViewById(R.id.event_results);
        eventPinInput = findViewById(R.id.event_pin);
        saveEventButton = findViewById(R.id.save_event_button);
        uploadImageButton = findViewById(R.id.upload_image_button);

        db = AppDatabase.getDatabase(this);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            eventImageView.setImageURI(selectedImageUri);

            // Assuming event_def is the default image in case the user doesn't upload an image
            // Replace this logic with actual image processing if needed
            eventImageResourceId = R.drawable.event_def;  // Replace with logic to get actual resource ID if needed
        }
    }

    private void saveEvent() {
        String eventName = eventNameInput.getText().toString().trim();
        String eventDescription = eventDescriptionInput.getText().toString().trim();
        String eventDate = eventDateInput.getText().toString().trim();
        String eventResults = eventResultsInput.getText().toString().trim();
        String eventPin = eventPinInput.getText().toString().trim();

        if (eventName.isEmpty() || eventDescription.isEmpty() || eventDate.isEmpty() || eventResults.isEmpty() || eventPin.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event();
        event.eventName = eventName;
        event.eventDescription = eventDescription;
        event.eventDate = eventDate;
        event.eventResults = eventResults;
        event.eventPin = eventPin;
        event.eventImage = eventImageResourceId;

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.eventDao().insert(event);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateEventActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }).start();
    }
}
