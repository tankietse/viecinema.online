package com.viecinema.moviebooking.config;

import com.viecinema.moviebooking.util.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }
}

