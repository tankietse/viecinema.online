package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "theaters", indexes = {
        @Index(name = "city_index", columnList = "city"),
        @Index(name = "theater_name_index", columnList = "theater_name")
})
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Integer theaterId;

    @NotEmpty(message = "Không được để trống")
    @Column(name = "theater_name", nullable = false)
    private String theaterName;

    @NotEmpty(message = "Không được để trống")
    @Column(name = "address")
    private String address;

    @NotEmpty(message = "Không được để trống")
    @Column(name = "city")
    private String city;

//    @Column(name = "state")
//    private String state;

//    @Column(name = "postal_code")
//    private String postalCode;

//    @Column(name = "country")
//    private String country;

    @NotEmpty(message = "Không được để trống")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Screen> screens;

}
