package com.example.EventHub.Event;

import com.example.EventHub.EventStatus.EventStatus;
import com.example.EventHub.EventType.EventTypeMapper;
import com.example.EventHub.EventType.EventTypeRepository;
import com.example.EventHub.Organisation.OrganisationMapper;
import com.example.EventHub.Organisation.OrganisationRepository;
import com.example.EventHub.User.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    OrganisationMapper organisationMapper;
    EventTypeMapper eventTypeMapper;
    UserMapper userMapper;
    OrganisationRepository organisationRepository;
    EventTypeRepository eventTypeRepository;
  
    @Autowired
    public EventMapper(OrganisationMapper organisationMapper, EventTypeMapper eventTypeMapper, UserMapper userMapper, OrganisationRepository organisationRepository, EventTypeRepository eventTypeRepository) {
        this.organisationMapper = organisationMapper;
        this.eventTypeMapper = eventTypeMapper;
        this.userMapper = userMapper;
        this.organisationRepository = organisationRepository;
        this.eventTypeRepository = eventTypeRepository;
    }
    public Event toEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setDuration(eventDTO.getDuration());
        event.setDescription(eventDTO.getDescription());
        event.setPlace(eventDTO.getPlace());
        event.setTime(eventDTO.getTime());
        event.setTicketPrice(eventDTO.getTicketPrice());
        event.setCapacity(eventDTO.getCapacity());
        event.setOrganisation(organisationMapper.toEntity(eventDTO.getOrganisation()));
        event.setEventType(eventTypeMapper.toEntity(eventDTO.getEventTypeDTO()));
        event.setEventStatus(eventDTO.getEventStatus());
        event.setUsers(eventDTO.getUsers().stream().map(userMapper::toEntity).collect(Collectors.toList()));
        event.setEventPermission(eventDTO.getEventPermission());

        // Handle MultipartFile to byte[] conversion if file is present
        if (eventDTO.getFile() != null && !eventDTO.getFile().isEmpty()) {
            try {
                event.setImage(eventDTO.getFile().getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert file to byte array", e);
            }
        }

        return event;
    }
    public EventDTO toDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getDate(),
                event.getDuration(),
                event.getDescription(),
                event.getBase64Image(),  // Convert image to Base64 string
                event.getPlace(),
                event.getTime(),
                event.getTicketPrice(),
                event.getCapacity(),
                null, // Assuming file is handled differently
                organisationMapper.toDTO(event.getOrganisation()),
                eventTypeMapper.toDTO(event.getEventType()),
                event.getEventStatus(),
                event.getUsers().stream().map(userMapper::toDTO).collect(Collectors.toList()),
                event.getEventPermission()
        );
    }
}
