// EventDao.java
package com.example.sporty.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sporty.data.entities.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM events WHERE eventPin = :eventPin LIMIT 1")
    Event getEventByPin(String eventPin);

    @Query("SELECT * FROM events")
    List<Event> getAllEvents();
}
