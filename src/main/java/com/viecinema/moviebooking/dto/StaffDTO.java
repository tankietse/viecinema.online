package com.viecinema.moviebooking.dto;

import lombok.Data;

@Data
public class StaffDTO {
    private int staffId;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String password;
    private int theaterId;
}
