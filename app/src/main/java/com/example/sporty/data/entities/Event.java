// Event.java
package com.example.sporty.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String eventPin;
    public String eventName;
    public String eventDescription;
    public int eventImage;
    public String eventDate;
    public String eventResults;
}
