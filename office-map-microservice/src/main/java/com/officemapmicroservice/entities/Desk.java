package com.officemapmicroservice.entities;

import javax.persistence.*;

import static com.officemapmicroservice.common.DeskStatusMessages.AVAILABLE;

@Entity
@Table(name = "desks")
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "desk_id")
    private Long deskId;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private int deskNumber;
    private String status = AVAILABLE;

    @OneToOne(mappedBy = "desk")
    private EmployeeAllocation allocation;


    public Long getDeskId() {
        return deskId;
    }
}
