package com.example.EventHub.Event;

import com.example.EventHub.EventPermission.EventPermission;
import com.example.EventHub.EventStatus.EventStatus;
import com.example.EventHub.EventType.EventType;
import com.example.EventHub.EventType.EventTypeDTO;
import com.example.EventHub.Organisation.Organisation;
import com.example.EventHub.Organisation.OrganisationDTO;
import com.example.EventHub.User.User;
import com.example.EventHub.User.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class EventDTO {
    @NotEmpty
    private Integer id;

    @NotEmpty(message = "The name of the event cannot be empty!")
    private String name;

    private String date;

    @NotNull(message = "Please enter the duration of the event!")
    private int duration;

    @NotEmpty(message = "Please enter a description!")
    private String description;

    private String image;  // This will be the Base64 string

    @NotEmpty(message = "Please enter the place of the event!")
    private String place;

    @NotEmpty(message = "Please enter the start time of the event!")
    private String time;

    @NotNull(message = "Please enter the price of the ticket for the event!")
    private double ticketPrice;

    @NotNull(message = "Please enter the capacity of the event!")
    private int capacity;

    @JsonIgnore
    private MultipartFile file;

    private OrganisationDTO organisation;
    private EventTypeDTO eventTypeDTO;
    private EventStatus eventStatus;
    private List<UserDTO> users;
    private EventPermission eventPermission;

    public EventDTO() {
    }

//    public EventDTO(Integer id, String name, String date, int duration, String description, String place, String time, double ticketPrice, int capacity, String image, OrganisationDTO dto, EventTypeDTO dto1, EventStatus eventStatus, List<UserDTO> users) {
//        this.id = id;
//        this.name = name;
//        this.date = date;
//        this.duration = duration;
//        this.description = description;
//        this.place = place;
//        this.time = time;
//        this.ticketPrice = ticketPrice;
//        this.capacity = capacity;
//        this.image = image;
//        this.organisation = dto;
//        this.eventTypeDTO = dto1;
//        this.eventStatus = eventStatus;
//        this.users = users;
//    }

}

