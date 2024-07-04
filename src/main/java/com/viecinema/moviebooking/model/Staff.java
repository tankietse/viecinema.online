package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "staffs", indexes = {
        @Index(name = "email_index", columnList = "email"),
        @Index(name = "theater_id_index", columnList = "theater_id")
})
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int staffId;

    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String password;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
}
