package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "screen_types")
public class ScreenType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_type_id")
    private Integer screenTypeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "screen_price")
    private BigDecimal screenPrice;
}
