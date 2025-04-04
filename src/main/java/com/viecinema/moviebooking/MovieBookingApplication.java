package com.viecinema.moviebooking;

import com.viecinema.moviebooking.service.TMDBSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class MovieBookingApplication implements CommandLineRunner {

	@Autowired
	private TMDBSyncService tmdbSyncService;

	public static void main(String[] args) {
		SpringApplication.run(MovieBookingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		tmdbSyncService.syncPopularMovies();
	}
}
