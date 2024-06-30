CREATE TABLE movie_dynamic_data (
    id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    vote_average DECIMAL(3,1),
    vote_count INT,
    popularity DECIMAL(10,3),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);
