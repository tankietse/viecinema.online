-- Bảng genres
CREATE TABLE genres (
    genre_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Bảng actors
CREATE TABLE actors (
    actor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    date_of_birth DATE,
    nationality VARCHAR(100)
);

-- Bảng directors
CREATE TABLE directors (
    director_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    date_of_birth DATE,
    nationality VARCHAR(100)
);

-- Bảng movies (cập nhật)
CREATE TABLE movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    tmdb_id INT UNIQUE,
    title VARCHAR(255) NOT NULL,
    overview TEXT,
    poster_path VARCHAR(255),
    backdrop_path VARCHAR(255),
    release_date DATE,
    runtime INT,
    original_language VARCHAR(10),
    INDEX idx_title (title),
    INDEX idx_tmdb_id (tmdb_id)
);

CREATE TABLE trailers (
    video_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    key_value VARCHAR(255),
    name VARCHAR(255),
    site VARCHAR(255),
    type VARCHAR(50),
    iso_639_1 VARCHAR(10),
    iso_3166_1 VARCHAR(10),
    size INT,
    official BOOLEAN,
    published_at TIMESTAMP,
    tmdb_trailer_id VARCHAR(50),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);

-- Bảng theaters (cập nhật)
CREATE TABLE theaters (
    theater_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    phone_number VARCHAR(20),
    capacity INT,
    INDEX idx_city_state (city, state)
);

-- Bảng screens
CREATE TABLE screens (
    screen_id INT PRIMARY KEY AUTO_INCREMENT,
    theater_id INT,
    name VARCHAR(50),
    capacity INT,
    FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)
);

-- Bảng seat_types (cập nhật)
CREATE TABLE seat_types (
    seat_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) UNIQUE NOT NULL
);

-- Bảng staffs
CREATE TABLE staffs (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Bảng users (cập nhật)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    role ENUM('admin', 'customer', 'theater_owner') NOT NULL DEFAULT 'customer',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username)
);


-- Bảng promotions (cập nhật)
CREATE TABLE promotions (
    promotion_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    discount_type ENUM('percentage', 'fixed_amount') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    start_date DATE,
    end_date DATE,
    min_purchase_amount DECIMAL(10,2),
    max_discount_amount DECIMAL(10,2),
    usage_limit INT,
    times_used INT DEFAULT 0
);

-- Bảng showtimes
CREATE TABLE showtimes (
    showtime_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    screen_id INT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (screen_id) REFERENCES screens(screen_id),
    INDEX idx_movie_id (movie_id),
    INDEX idx_start_time_end_time (start_time, end_time)
);

-- Bảng seats (cập nhật)
CREATE TABLE seats (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    screen_id INT,
    row_name VARCHAR(5),
    seat_number INT,
    seat_type_id INT,
    FOREIGN KEY (screen_id) REFERENCES screens(screen_id),
    FOREIGN KEY (seat_type_id) REFERENCES seat_types(seat_type_id),
    UNIQUE KEY (screen_id, row_name, seat_number)
);

-- Bảng bookings
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    showtime_id INT,
    booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2),
    status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id),
    INDEX idx_user_id (user_id),
    INDEX idx_booking_date (booking_date)
);

-- Bảng booking_seats (thay thế cho booking_details)
CREATE TABLE booking_seats (
    booking_seat_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    seat_id INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
    UNIQUE KEY (booking_id, seat_id)
);

-- Bảng payments (cập nhật)
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    amount DECIMAL(10, 2),
    payment_method ENUM('credit_card', 'debit_card', 'paypal', 'cash'),
    transaction_id VARCHAR(100),
    payment_status ENUM('pending', 'completed', 'failed', 'refunded'),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

-- Bảng reviews
CREATE TABLE reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    user_id INT,
    rating INT,
    comment TEXT,
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_movie_user (movie_id, user_id),
    CHECK (rating >= 1 AND rating <= 5)
);

-- Bảng movie_genres
CREATE TABLE movie_genres (
    movie_id INT,
    genre_id INT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);

-- Bảng movie_actors
CREATE TABLE movie_actors (
    movie_id INT,
    actor_id INT,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (actor_id) REFERENCES actors(actor_id)
);

-- Bảng movie_directors
CREATE TABLE movie_directors (
    movie_id INT,
    director_id INT,
    PRIMARY KEY (movie_id, director_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (director_id) REFERENCES directors(director_id)
);

-- Bảng booking_history
CREATE TABLE booking_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    user_id INT,
    status VARCHAR(20),
    change_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Bảng statistics
CREATE TABLE statistics (
    date DATE,
    movie_id INT,
    total_revenue DECIMAL(10, 2),
    tickets_sold INT,
    PRIMARY KEY (date, movie_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);

-- Bảng prices
CREATE TABLE prices (
    price_id INT PRIMARY KEY AUTO_INCREMENT,
    seat_type_id INT,
    showtime_id INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (seat_type_id) REFERENCES seat_types(seat_type_id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)
);
