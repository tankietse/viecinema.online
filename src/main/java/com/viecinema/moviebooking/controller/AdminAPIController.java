package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.dto.MovieDTO;
import com.viecinema.moviebooking.dto.ScheduleDTO;
import com.viecinema.moviebooking.service.MovieService;
import com.viecinema.moviebooking.service.ScheduleService;
import com.viecinema.moviebooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminAPIController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;

    // Thêm phim mới
    @PostMapping("/add-movies")
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.createMovie(movieDTO));
    }

    // Hủy phim
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // Thêm lịch chiếu mới
    @PostMapping("/add-schedules")
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleDTO));
    }

    // Hủy lịch chiếu
    @DeleteMapping("/cancel-schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    // Khóa/Mở khóa tài khoản khách hàng
    // Ví dụ phương thức khóa tài khoản
    @PostMapping("/users/{id}/lock")
    public ResponseEntity<Void> lockUser(@PathVariable Integer id) {
        userService.lockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/unlock")
    public ResponseEntity<Void> unlockUser(@PathVariable Integer id) {
        userService.unlockUser(id);
        return ResponseEntity.noContent().build();
    }
}
