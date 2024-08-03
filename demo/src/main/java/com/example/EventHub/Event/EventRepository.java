package com.example.EventHub.Event;

import com.example.EventHub.EventPermission.EventPermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {
    @Query("SELECT e FROM Event e " +
            "WHERE (:name IS NULL OR e.name LIKE %:name%) " +
            "AND (:place IS NULL OR e.place LIKE %:place%) " +
            "AND (:type IS NULL OR e.eventType.id = :type) " +
            "AND (:date IS NULL OR CAST(e.date AS string) LIKE %:date%) " +
            "AND (:minPrice IS NULL OR :maxPrice IS NULL OR e.ticketPrice BETWEEN :minPrice AND :maxPrice)" +
            "AND e.eventPermission = :permission")
    List<Event> findByPlaceTypeDateAndPrice(@Param("name") String name,
                                            @Param("place") String place,
                                            @Param("type") Integer type,
                                            @Param("date") String date,
                                            @Param("minPrice") Double minPrice,
                                            @Param("maxPrice") Double maxPrice,
                                            @Param("permission") EventPermission permission);

    Event findByName(String name);

    @Query("SELECT e FROM Event e WHERE e.eventPermission = :permission")
    List<Event> findByEventPermission(@Param("permission") EventPermission permission);
}
