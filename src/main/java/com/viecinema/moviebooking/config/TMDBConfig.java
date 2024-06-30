package com.viecinema.moviebooking.config;

import com.uwetrottmann.tmdb2.Tmdb;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Thay vì lặp lại việc đọc các giá trị cấu hình và tạo đối tượng Tmdb ở nhiều nơi trong ứng dụng,
// Ta chỉ cần làm điều đó một lần trong lớp này.
@Configuration
public class TMDBConfig {
//    Sửa API Key trong application.properties
    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    @Bean
    public Tmdb tmdb() {
        return new Tmdb(apiKey);
    }
}

