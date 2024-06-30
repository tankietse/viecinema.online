package com.viecinema.moviebooking.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
}
