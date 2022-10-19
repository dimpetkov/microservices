package com.officemapmicroservice.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;
    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;
    private String roomName;
    @OneToMany(mappedBy = "room")
    private List<Desk> desks;

    public Floor getFloor() {
        return floor;
    }

    public String getRoomName() {
        return roomName;
    }
}
