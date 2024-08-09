package com.example.EventHub.EventType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Table(name = "event_types")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty(message = "Please enter the name of the type!")
    @Column(name = "type_name")
    private String typeName;

    public EventType(String typeName) {
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
