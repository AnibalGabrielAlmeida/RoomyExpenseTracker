package com.RoomyExpense.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    private String password;
     public enum Role {
        ADMIN, ROOMY
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate registrationDate;

    //profile picture (aws3 maybe)
    private String phoneNumber;


    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;
}
