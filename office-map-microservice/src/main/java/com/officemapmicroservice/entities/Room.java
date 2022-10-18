package com.officemapmicroservice.entities;

import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long roomId;
    @ManyToOne
    @JoinColumn(name = "floor_floor_id")
    private Floor floor;
    private String room;

    public Floor getFloor() {
        return floor;
    }
}
