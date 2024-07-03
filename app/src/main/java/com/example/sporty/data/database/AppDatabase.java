package com.example.sporty.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.sporty.R;
import com.example.sporty.data.dao.EventDao;
import com.example.sporty.data.dao.UserDao;
import com.example.sporty.data.entities.Event;
import com.example.sporty.data.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Event.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final String TAG = "AppDatabase";
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract UserDao userDao();
    public abstract EventDao eventDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(TAG, "Database created, triggering data insertion");
            databaseWriteExecutor.execute(() -> {
                populateDatabase(INSTANCE.userDao(), INSTANCE.eventDao());
            });
        }
    };

    private static void populateDatabase(UserDao userDao, EventDao eventDao) {
        Log.d(TAG, "populateDatabase started");
        try {
            // Add sample users
            User user1 = new User();
            user1.email = "user1@example.com";
            user1.password = "password1";
            userDao.insert(user1);
            Log.d(TAG, "Inserted user: " + user1.email);

            User user2 = new User();
            user2.email = "user2@example.com";
            user2.password = "password2";
            userDao.insert(user2);
            Log.d(TAG, "Inserted user: " + user2.email);

            // Add sample events
            Event event1 = new Event();
            event1.eventPin = "1234";
            event1.eventName = "Event 1";
            event1.eventDescription = "Description for Event 1";
            event1.eventImage = R.drawable.event1;
            event1.eventDate = "31 May 2024";
            event1.eventResults = "Seville 1 - 1 Roma (Penalties: 4-1)";
            eventDao.insert(event1);
            Log.d(TAG, "Inserted event: " + event1.eventPin + ", " + event1.eventName);

            Event event2 = new Event();
            event2.eventPin = "5678";
            event2.eventName = "Event 2";
            event2.eventDescription = "Description for Event 2";
            event2.eventImage = R.drawable.event_def;
            event2.eventDate = "1 June 2024";
            event2.eventResults = "Team A 2 - 2 Team B (Penalties: 3-4)";
            eventDao.insert(event2);
            Log.d(TAG, "Inserted event: " + event2.eventPin + ", " + event2.eventName);
        } catch (Exception e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
        }
    }
}
