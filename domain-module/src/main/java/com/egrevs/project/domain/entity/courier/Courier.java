package com.egrevs.project.domain.entity.courier;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.enums.CourierStatus;
import com.egrevs.project.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "couriers")
@Getter
@Setter
public class Courier {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CourierStatus courierStatus;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "courier", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();
}
