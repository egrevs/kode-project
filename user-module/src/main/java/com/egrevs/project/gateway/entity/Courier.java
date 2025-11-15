package com.egrevs.project.gateway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "couriers")
public class Courier extends User{

    @Column(name = "status")
    private CourierStatus courierStatus;

}
