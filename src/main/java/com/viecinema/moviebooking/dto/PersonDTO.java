package com.viecinema.moviebooking.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PersonDTO {
    private int personId;
    private String name;
    private String bio;
    private Date dateOfBirth;
    private String nationality;
}
