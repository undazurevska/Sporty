package com.example.sporty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporty.data.database.AppDatabase;
import com.example.sporty.data.entities.Event;
import com.example.sporty.data.entities.User;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private static final String TAG = "MainActivity";
    private RelativeLayout rootView;
    private int currentBackground = 0;
    private int[] backgrounds = {R.drawable.background1, R.drawable.background2, R.drawable.background3};
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.home_root_view);

        db = AppDatabase.getDatabase(this);
        if (db == null) {
            Log.e(TAG, "Database initialization failed");
            return;
        }

        Log.d(TAG, "Database initialized");

        // Populate database with initial data
        new Thread(new Runnable() {
            @Override
            public void run() {
                populateDatabase();
            }
        }).start();

        EditText pinInput = findViewById(R.id.pin_input);
        Button enterPinButton = findViewById(R.id.enter_pin_button);
        Button loginButton = findViewById(R.id.log_in_text);

        enterPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pinInput.getText().toString().trim();
                Log.d(TAG, "Entered PIN: " + pin);
                if (!pin.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Event event = db.eventDao().getEventByPin(pin);
                                if (event != null) {
                                    Log.d(TAG, "Event found: " + event.eventName);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(MainActivity.this, EventActivity.class);
                                            intent.putExtra("event_name", event.eventName);
                                            intent.putExtra("event_description", event.eventDescription);
                                            intent.putExtra("event_image", event.eventImage);
                                            intent.putExtra("event_date", event.eventDate);
                                            intent.putExtra("event_results", event.eventResults);

                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Invalid event PIN: " + pin);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Invalid event PIN", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error retrieving event: " + e.getMessage());
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter an event PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        startBackgroundChanger();
    }

    private void startBackgroundChanger() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentBackground = (currentBackground + 1) % backgrounds.length;
                rootView.setBackgroundResource(backgrounds[currentBackground]);
                startBackgroundChanger();
            }
        }, 5000); // Change background every 5 seconds
    }

    private void populateDatabase() {
        Event event1 = new Event();
        event1.eventPin = "1234";
        event1.eventName = "Event 1";
        event1.eventDescription = "Description for Event 1";
        event1.eventImage = R.drawable.event1;
        event1.eventDate = "31 May 2024";
        event1.eventResults = "Seville 1 - 1 Roma (Penalties: 4-1)";

        Event event2 = new Event();
        event2.eventPin = "5678";
        event2.eventName = "Event 2";
        event2.eventDescription = "Description for Event 2";
        event2.eventImage = R.drawable.event_def;
        event2.eventDate = "1 June 2024";
        event2.eventResults = "Team A 2 - 2 Team B (Penalties: 3-4)";

        User user1 = new User();
        user1.email = "user1@example.com";
        user1.password = "password1";

        db.eventDao().insert(event1);
        db.eventDao().insert(event2);
        db.userDao().insert(user1);
    }
}
