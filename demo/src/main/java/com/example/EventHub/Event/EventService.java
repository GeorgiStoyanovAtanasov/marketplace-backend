package com.example.EventHub.Event;

import com.example.EventHub.EventApplication;
import com.example.EventHub.EventPermission.EventPermission;
import com.example.EventHub.EventStatus.EventStatus;
import com.example.EventHub.EventType.EventType;
import com.example.EventHub.EventType.EventTypeDTO;
import com.example.EventHub.EventType.EventTypeMapper;
import com.example.EventHub.JWT.services.JwtService;
import com.example.EventHub.Organisation.Organisation;
import com.example.EventHub.Organisation.OrganisationRepository;
import com.example.EventHub.EventType.EventTypeRepository;
import com.example.EventHub.User.User;
import com.example.EventHub.User.UserRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventTypeRepository eventTypeRepository;
    @Autowired
    OrganisationRepository organisationRepository;
    @Autowired
    EventMapper eventMapper;
    @Autowired
    EventTypeMapper eventTypeMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate localDate = LocalDate.now();

    public String updateForm(Integer id, Model model) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event event = eventRepository.findById(id).get();
            model.addAttribute("eventTypes", eventTypeRepository.findAll());
            model.addAttribute("organisations", organisationRepository.findAll());
            model.addAttribute("updateEvent", event);
            return "event-update-form";
        } else {
            return "id could not be find";
        }
    }

    public String postUpdate(Integer id, Event updatedEvent, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeRepository.findAll());
            model.addAttribute("organisations", organisationRepository.findAll());
            return "event-update-form";
        } else {
            Event event = eventRepository.findById(id).get();
            getEvent(event, updatedEvent);
            eventRepository.save(event);
            model.addAttribute("event", event);
            return "event-update-result";
        }
    }

    private Event getEvent(Event event, Event updatedEvent) {
        event.setName(updatedEvent.getName());
        event.setDate(updatedEvent.getDate());
        event.setDuration(updatedEvent.getDuration());
        event.setDescription(updatedEvent.getDescription());
        event.setPlace(updatedEvent.getPlace());
        event.setTicketPrice(updatedEvent.getTicketPrice());
        event.setCapacity(updatedEvent.getCapacity());
        event.setOrganisation(updatedEvent.getOrganisation());
        event.setEventType(updatedEvent.getEventType());
        event.setEventStatus(updatedEvent.getEventStatus());
        return event;
    }

    public void delete(String name) {
        Event event = eventRepository.findByName(name);
        if (event == null) {
            throw new IllegalArgumentException("name cannot be null nigga");
        }
        eventRepository.delete(event);
    }

    public ResponseEntity<Map<String, List<?>>> searchEvents(String name,
                                                             String place,
                                                             Integer type,
                                                             String date,
                                                             Double minPrice,
                                                             Double maxPrice,
                                                             EventPermission eventPermission) {

        if (place == null) {
            place = "";
        }
        if (date == null) {
            date = "";
        }

        if (minPrice == null) {
            minPrice = (double) 0;
        }
        if (maxPrice == null) {
            maxPrice = (double) Integer.MAX_VALUE;
        }
        if (minPrice > maxPrice) {
            double maxPrice1 = maxPrice;
            maxPrice = minPrice;
            minPrice = maxPrice1;
        }

        List<Event> events = eventRepository.findByPlaceTypeDateAndPrice(name, place, type, date, minPrice, maxPrice, eventPermission);
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            eventDTOs.add(eventMapper.toDTO(events.get(i)));
        }
        List<EventType> allTypes = (List<EventType>) eventTypeRepository.findAll();
        List<EventTypeDTO> eventTypeDTOs = allTypes.stream().map(eventTypeMapper::toDTO).collect(Collectors.toList());

        Map<String, List<?>> response = new HashMap<>();
        response.put("events", eventDTOs);
        response.put("eventTypes", eventTypeDTOs);

        return ResponseEntity.ok(response);
    }


    public boolean errorEventStatus(EventDTO eventDTO) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(eventDTO.getDate());
            Date local = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (date.before(local)) {
                return true;
            }
        } catch (ParseException ex) {
            System.out.println("Parsing error!" + ex);
        }
        return false;
    }

    public void apply(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        Event event = eventRepository.findById(id).get();
        List<Event> events = user.getEvents();
        for (Event listEvent : events) {
            if (listEvent.equals(event)) {
                throw new IllegalStateException(); //a custom exception should be created
            }
        }
        event.getUsers().add(user);
        user.getEvents().add(event);
        userRepository.save(user);
        eventRepository.save(event);
    }
}