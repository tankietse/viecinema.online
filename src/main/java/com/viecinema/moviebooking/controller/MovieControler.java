package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.dto.*;
import com.viecinema.moviebooking.model.*;
import com.viecinema.moviebooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MovieControler {
    @Autowired
    private MovieService movieService;
    @Autowired
    private ShowtimeService showtimeService;
    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private ScreenService screenService;

    @GetMapping("/movie-detail")
    public String getMovieDetailPage(@RequestParam("id") Integer id, Model model) {
        MovieDTO movie = movieService.getMovieById(id);
        List<Showtime> showtimes = showtimeService.getShowtimesByMovieId(movie.getId());
        List<List<String>> formattedShowtimeDates = getFormattedShowtimeDates(showtimes);

        model.addAttribute("movie", movie);
        model.addAttribute("showtimes", showtimes);
        model.addAttribute("showtimeDates", formattedShowtimeDates);
        return "movie/movie-detail";
    }

    private List<List<String>> getFormattedShowtimeDates(List<Showtime> showtimes) {
        List<List<String>> formattedShowtimeDates = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(6);

        for (Showtime showtime : showtimes) {
            List<String> dates = new ArrayList<>();
            LocalDate startDate = showtime.getStartDate();
            LocalDate date = startDate.isBefore(currentDate) ? currentDate : startDate;

            while (!date.isAfter(endDate) && !date.isAfter(showtime.getEndDate())) {
                String formattedDate = date.format(dateFormatter);
                String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
                dates.add(formattedDate + "|" + dayOfWeek);
                date = date.plusDays(1);
            }

            if (!dates.isEmpty()) {
                formattedShowtimeDates.add(dates);
            }
        }

        return formattedShowtimeDates;
    }

    @PostMapping("/get-screenings-by-date")
    @ResponseBody
    public ResponseEntity<?> getScreeningsByDate(@RequestParam String selectedDate, @RequestParam Integer showtimeId) {
        System.out.println("Du lieu cho detail: " + selectedDate + " " + showtimeId);
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(selectedDate, dateFormatter);

            List<Screening> screenings = screeningService.getScreeningsByShowtimeIdAndDate(showtimeId, date);

            Map<String, List<ScreeningWithScreenInfoDTO>> result = screenings.stream()
                    .map(screening -> {
                        ScreenInfoDTO screenInfo = screenService.getScreenInfoByScreenId(screening.getScreen().getId());
                        return new ScreeningWithScreenInfoDTO(screening, screenInfo);
                    })
                    .collect(Collectors.groupingBy(ScreeningWithScreenInfoDTO::getTheaterName));

            System.out.println(result); // Log dữ liệu trả về
            return ResponseEntity.ok(result);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid date format: " + selectedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
