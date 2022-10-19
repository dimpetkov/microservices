package com.officemapmicroservice.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employeeAllocation")
public class EmployeeAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long allocationId;
    @CreationTimestamp
    private Date createdAt;
    private long employeeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "desk_Id", referencedColumnName = "desk_id")
    private Desk desk;

    public Desk getDesk() {
        return desk;
    }
}
