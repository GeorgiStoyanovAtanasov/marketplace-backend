package com.example.EventHub.Event;

import com.example.EventHub.EventStatus.EventStatus;
import com.example.EventHub.EventType.EventType;
import com.example.EventHub.Organisation.Organisation;
import com.example.EventHub.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String date;
    private int duration;
    private String description;
    private String place;
    private String time;
    private double ticketPrice;
    private int capacity;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;  // Store as byte array
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    @ManyToMany
    @JoinTable(
    name = "events_users",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User>users;

    public Event(String name, String date, int duration, String description, String place, String time, double ticketPrice, int capacity, byte[] image, Organisation organisation, EventType eventType, EventStatus eventStatus, List<User> users) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.description = description;
        this.place = place;
        this.time = time;
        this.ticketPrice = ticketPrice;
        this.capacity = capacity;
        this.image = image;
        this.organisation = organisation;
        this.eventType = eventType;
        this.eventStatus = eventStatus;
        this.users = users;
    }

    public String getBase64Image() {
        return Base64.getEncoder().encodeToString(this.image);
    }

    public EventStatus getEventStatus() {
        LocalDate localDate=LocalDate.now();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date eventDate = dateFormat.parse(date);
            Date local = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (eventDate.before(local)) {
                eventStatus=EventStatus.FINISHED;
            }
            else {
                eventStatus=EventStatus.AVAILABLE;
            }
        }catch (ParseException ex){
            System.out.println("Parsing error!" + ex);
        }
        if(users != null) {
            if (users.size() >= capacity) {
                eventStatus = EventStatus.FULL;
            }
        }
        return this.eventStatus;
    }
}
