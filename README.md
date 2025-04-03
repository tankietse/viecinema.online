# VIE Cinema - Hệ thống đặt vé xem phim trực tuyến

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

## Giới thiệu

VIE Cinema là một ứng dụng web Spring Boot cung cấp nền tảng đặt vé xem phim trực tuyến thân thiện với người dùng. Hệ thống cho phép người dùng duyệt phim, chọn suất chiếu, chọn ghế và thanh toán vé một cách an toàn. Hệ thống cũng bao gồm giao diện quản trị viên để quản lý danh sách phim, rạp chiếu, lịch chiếu và đặt vé.

## Giao diện người dùng

![Laptop Mockup](/UI/Celestial%20Laptop%20Mockup.png)

### Trang chủ
![Trang chủ](/UI/Home%20page.png)
![iPad Mockup Home](/UI/iPad%20Mockup%20Home.png)

### Chi tiết phim
![Chi tiết phim](/UI/Movie%20Detail.png)
![iPad Mockup Chi tiết phim](/UI/iPad%20Mockup%20Movie%20detail.png)

### Đặt vé và chọn ghế
![Đặt chỗ](/UI/đặt%20chổ.png)

### Popup đăng nhập và đăng ký
![Đăng nhập](/UI/login%20popupLogin.png)
![Đăng ký](/UI/Register%20%20popupLogin.png)

### Quên mật khẩu
![Quên mật khẩu](/UI/Fogot%20pass%20popupLogin.png)

### Thanh toán
![Thanh toán](/UI/Add%20payments%20%20popupLogin.png)
![Thanh toán lỗi](/UI/Add%20payment%20fail%20popupLogin.png)

## Giao diện quản trị

### Quản lý phim
![Quản lý phim](/UI/Admin/Admin-Quản%20lý%20phim.png)

### Quản lý rạp chiếu
![Quản lý rạp chiếu](/UI/Admin/Admin-%20Quản%20lý%20rạp%20chiếu.png)

### Quản lý phòng chiếu
![Quản lý phòng chiếu](/UI/Admin/Admin-%20Quản%20lý%20phòng%20chiếu.png)

## Kiến trúc hệ thống

### Mô hình MVC
Dự án được phát triển theo mô hình MVC (Model-View-Controller) sử dụng framework Spring Boot:

- **Model**: Các entity class định nghĩa cấu trúc dữ liệu của hệ thống
- **View**: Giao diện người dùng sử dụng Thymeleaf để render các template HTML
- **Controller**: Xử lý logic nghiệp vụ và điều hướng request

### Cấu trúc dự án

```
src/
  main/
    java/com/viecinema/moviebooking/
      config/           - Cấu hình hệ thống (Spring Security, TMDB API)
      controller/       - Xử lý request và điều hướng
      dto/              - Các đối tượng truyền dữ liệu
      exception/        - Xử lý ngoại lệ
      model/            - Các entity class
      repository/       - Spring Data JPA repository
      service/          - Lớp xử lý logic nghiệp vụ
    resources/
      application.properties - Cấu hình ứng dụng
      db/migration/          - Flyway migration scripts
      static/                - Tài nguyên tĩnh (CSS, JS, hình ảnh)
      templates/             - Thymeleaf templates
```

### Chi tiết các Model trong hệ thống

#### Các Entity chính

1. **User**
   - **Thuộc tính**: id, username, password, email, phone_number, role, locked, createdAt, updatedAt
   - **Quan hệ**: One-to-Many với Booking (một người dùng có thể đặt nhiều vé)
   - **Vai trò**: Lưu thông tin người dùng và phân quyền (CUSTOMER, ADMIN)

2. **Movie**
   - **Thuộc tính**: id, title, overview, posterPath, backdropPath, releaseDate, runtime, originalLanguage, status, tmdbID, viewCount
   - **Quan hệ**: One-to-Many với Showtime, Many-to-Many với Genre thông qua MovieGenre, One-to-Many với Trailer
   - **Vai trò**: Lưu trữ thông tin chi tiết về phim

3. **MovieDynamicData**
   - **Thuộc tính**: id, movieId, voteAverage, voteCount, popularity, lastUpdated
   - **Quan hệ**: Many-to-One với Movie
   - **Vai trò**: Lưu trữ dữ liệu động được cập nhật thường xuyên của phim

4. **Theater**
   - **Thuộc tính**: theaterId, theaterName, address, city, phoneNumber, email, status
   - **Quan hệ**: One-to-Many với Screen
   - **Vai trò**: Quản lý thông tin rạp chiếu phim

5. **Screen**
   - **Thuộc tính**: id, name, theater, screenType, capacity, seat
   - **Quan hệ**: Many-to-One với Theater, Many-to-One với ScreenType, One-to-One với Seat
   - **Vai trò**: Quản lý thông tin về phòng chiếu trong rạp

6. **ScreenType**
   - **Thuộc tính**: screenTypeId, typeName, description, screenPrice
   - **Quan hệ**: One-to-Many với Screen
   - **Vai trò**: Quản lý loại phòng chiếu (2D, 3D, IMAX, VIP...)

7. **Seat**
   - **Thuộc tính**: seatId, rowNumber, seatNumber, seatType
   - **Quan hệ**: Many-to-One với SeatType
   - **Vai trò**: Quản lý thông tin về ghế trong rạp

8. **SeatType**
   - **Thuộc tính**: seatTypeId, typeName, description, priceMultiplier
   - **Quan hệ**: One-to-Many với Seat
   - **Vai trò**: Phân loại ghế (Standard, VIP, Couple...)

9. **Showtime**
   - **Thuộc tính**: showtimeId, movie, startDate, endDate, status
   - **Quan hệ**: Many-to-One với Movie, One-to-Many với Screening
   - **Vai trò**: Quản lý thông tin suất chiếu phim

10. **Screening**
    - **Thuộc tính**: screeningId, showtime, screen, screeningDate, startTime, endTime, price
    - **Quan hệ**: Many-to-One với Showtime, Many-to-One với Screen
    - **Vai trò**: Lịch chiếu cụ thể cho từng phòng

11. **Booking**
    - **Thuộc tính**: bookingId, user, showtime, bookingDate, totalAmount, status, seats, createdAt, updatedAt
    - **Quan hệ**: Many-to-One với User, Many-to-One với Showtime, One-to-Many với BookingSeats
    - **Vai trò**: Quản lý thông tin đặt vé

12. **BookingSeats**
    - **Thuộc tính**: bookingSeatId, booking, seat, price
    - **Quan hệ**: Many-to-One với Booking, Many-to-One với Seat
    - **Vai trò**: Chi tiết ghế đã đặt trong một đơn đặt vé

13. **Payment**
    - **Thuộc tính**: paymentId, booking, amount, paymentMethod, transactionId, paymentStatus, paymentDate
    - **Quan hệ**: Many-to-One với Booking
    - **Vai trò**: Lưu trữ thông tin thanh toán

14. **Person**
    - **Thuộc tính**: personId, name, bio, dateOfBirth, nationality
    - **Quan hệ**: Many-to-Many với Movie thông qua MoviePerson
    - **Vai trò**: Lưu thông tin về diễn viên, đạo diễn

15. **Genre**
    - **Thuộc tính**: genreId, name
    - **Quan hệ**: Many-to-Many với Movie thông qua MovieGenre
    - **Vai trò**: Lưu thông tin thể loại phim

### Các Bảng Liên Kết

1. **MovieGenre**
   - **Thuộc tính**: movieId, genreId
   - **Vai trò**: Liên kết Movie với Genre (Many-to-Many)

2. **MoviePerson**
   - **Thuộc tính**: movieId, personId, role (ACTOR, DIRECTOR, PRODUCER...)
   - **Vai trò**: Liên kết Movie với Person (Many-to-Many)

3. **BookingHistory**
   - **Thuộc tính**: historyId, booking, user, status, changeDate
   - **Vai trò**: Lưu lịch sử thay đổi trạng thái đặt vé

### Các DTO (Data Transfer Objects)

1. **MovieDTO**
   - Chứa thông tin phim để hiển thị cho người dùng
   - Bao gồm: id, title, overview, posterPath, releaseDate, genres, directors, actors, voteAverage...

2. **ScreenInfoDTO**
   - Chứa thông tin chi tiết phòng chiếu
   - Bao gồm: screenId, screenName, theaterName, theaterAddress, capacity, screenType, seatType...

3. **PersonDTO**
   - Chứa thông tin người (diễn viên, đạo diễn)
   - Bao gồm: personId, name, bio, dateOfBirth, nationality...

4. **TrailerDTO**
   - Chứa thông tin trailer phim
   - Bao gồm: trailerId, movieId, key, name, site, type...

5. **BookingHistoryDTO**
   - Chứa thông tin lịch sử đặt vé
   - Bao gồm: historyId, bookingId, userId, status, changeDate...

### Các Repository

1. **UserRepository**: Truy vấn và quản lý dữ liệu người dùng
2. **MovieRepository**: Truy vấn và quản lý dữ liệu phim
3. **TheaterRepository**: Truy vấn và quản lý dữ liệu rạp chiếu
4. **ScreenRepository**: Truy vấn và quản lý dữ liệu phòng chiếu
5. **ShowtimeRepository**: Truy vấn và quản lý dữ liệu lịch chiếu
6. **ScreeningRepository**: Truy vấn và quản lý dữ liệu suất chiếu
7. **BookingRepository**: Truy vấn và quản lý dữ liệu đặt vé
8. **GenreRepository**: Truy vấn và quản lý dữ liệu thể loại phim
9. **PersonRepository**: Truy vấn và quản lý dữ liệu diễn viên, đạo diễn
10. **TrailerRepository**: Truy vấn và quản lý dữ liệu trailer
11. **MovieDynamicDataRepository**: Truy vấn và quản lý dữ liệu động của phim
12. **SeatTypeRepository**: Truy vấn và quản lý dữ liệu loại ghế
13. **PaymentRepository**: Truy vấn và quản lý dữ liệu thanh toán
14. **StatisticsRepository**: Truy vấn dữ liệu thống kê

### Chi tiết các Service

1. **MovieService**
   - **Phương thức chính**:
     - `getAllMovies()`: Lấy tất cả phim
     - `getCurrentMonthMovies()`: Lấy phim đang chiếu trong tháng hiện tại
     - `getComingSoonMovies()`: Lấy phim sắp chiếu
     - `getMovieById(Long id)`: Lấy chi tiết một phim theo ID
     - `createMovie(MovieDTO movieDTO)`: Tạo phim mới
     - `updateMovie(Long id, MovieDTO movieDTO)`: Cập nhật thông tin phim
     - `deleteMovie(Long id)`: Xóa phim
     - `getTrendingMovies(int limit)`: Lấy danh sách phim thịnh hành

2. **UserService**
   - **Phương thức chính**:
     - `findAllUsers()`: Lấy tất cả người dùng
     - `getUserById(Long id)`: Lấy người dùng theo ID
     - `getUserByUsername(String username)`: Tìm người dùng theo tên đăng nhập
     - `getUserByEmail(String email)`: Tìm người dùng theo email
     - `saveUser(User user)`: Lưu người dùng mới
     - `updateUserInfo(User user)`: Cập nhật thông tin người dùng
     - `lockUser(Long id)`: Khóa tài khoản người dùng
     - `unlockUser(Long id)`: Mở khóa tài khoản người dùng

3. **BookingService**
   - **Phương thức chính**:
     - `getAllBookings()`: Lấy tất cả đơn đặt vé
     - `getBookingById(Integer id)`: Lấy đơn đặt vé theo ID
     - `createBooking(Booking booking)`: Tạo đơn đặt vé mới
     - `updateBookingStatus(Integer id, BookingStatus status)`: Cập nhật trạng thái đặt vé
     - `getUserBookings(Integer userId)`: Lấy đơn đặt vé của người dùng
     - `getShowtimeBookings(Integer showtimeId)`: Lấy đơn đặt vé của suất chiếu
     - `getCurrentMonthRevenue()`: Lấy doanh thu tháng hiện tại
     - `getYearlyRevenue()`: Lấy doanh thu năm hiện tại
     - `getUserBookingHistory(Long userId)`: Lấy lịch sử đặt vé của người dùng
     - `getRecentActivities(int limit)`: Lấy hoạt động gần đây trong hệ thống

4. **TheaterService**
   - **Phương thức chính**:
     - `findAll()`: Lấy tất cả rạp chiếu
     - `findById(Integer id)`: Lấy rạp chiếu theo ID
     - `save(Theater theater)`: Lưu hoặc cập nhật rạp chiếu
     - `deleteById(Integer id)`: Xóa rạp chiếu

5. **ScreenService**
   - **Phương thức chính**:
     - `findAll()`: Lấy tất cả phòng chiếu
     - `findById(Long id)`: Lấy phòng chiếu theo ID
     - `save(Screen screen)`: Lưu hoặc cập nhật phòng chiếu
     - `deleteById(Long id)`: Xóa phòng chiếu
     - `getScreenInfoByScreenId(Integer screenId)`: Lấy thông tin chi tiết phòng chiếu
     - `findByTheater(Theater theater)`: Lấy phòng chiếu theo rạp

6. **ShowtimeService**
   - **Phương thức chính**:
     - `findShowtimes(...)`: Tìm kiếm lịch chiếu với bộ lọc
     - `findById(Long id)`: Lấy lịch chiếu theo ID
     - `save(Showtime showtime)`: Lưu hoặc cập nhật lịch chiếu
     - `deleteById(Long id)`: Xóa lịch chiếu
     - `calculateBookingRate()`: Tính tỷ lệ đặt vé
     - `findByMovieId(Long movieId)`: Lấy lịch chiếu của phim
     - `findByTheaterId(Long theaterId)`: Lấy lịch chiếu của rạp

7. **TMDBSyncService**
   - **Phương thức chính**:
     - `syncPopularMovies()`: Đồng bộ dữ liệu phim phổ biến từ TMDB API
     - `updateOrInsertMovie(Long tmdbId)`: Cập nhật hoặc thêm mới phim từ TMDB

8. **StatisticsService**
   - **Phương thức chính**:
     - `getStatisticsByMonth(int year, int month)`: Lấy thống kê theo tháng

### Chi tiết các Controller

1. **HomeController**
   - **Endpoint**: `/home` - Hiển thị trang chủ với danh sách phim
   - **Endpoint**: `/now-playing` - Hiển thị phim đang chiếu
   - **Endpoint**: `/coming-soon` - Hiển thị phim sắp chiếu

2. **MovieController**
   - **Endpoint**: `/movie-detail?id={id}` - Hiển thị chi tiết phim
   - **Endpoint**: `/get-screenings-by-date` - API lấy lịch chiếu theo ngày

3. **UserController**
   - **Endpoint**: `/login` - Đăng nhập
   - **Endpoint**: `/register` - Đăng ký tài khoản
   - **Endpoint**: `/forgot-pass` - Khôi phục mật khẩu
   - **Endpoint**: `/user-profile` - Quản lý thông tin cá nhân
   - **Endpoint**: `/change-password` - Đổi mật khẩu

4. **AdminController**
   - Quản lý các chức năng hệ thống và điều hướng tới các controller quản trị cụ thể

5. **TheaterController**
   - **Endpoint**: `/admin/theaters` - Quản lý rạp chiếu
   - **Endpoint**: `/admin/theaters/create` - Tạo rạp mới
   - **Endpoint**: `/admin/theaters/{id}/edit` - Chỉnh sửa rạp
   - **Endpoint**: `/admin/theaters/{id}/screens` - Xem phòng chiếu của rạp
   - **Endpoint**: `/admin/theaters/save` - Lưu thông tin rạp
   - **Endpoint**: `/admin/theaters/delete/{id}` - Xóa rạp

6. **CustomErrorController**
   - Xử lý các trang lỗi (404, 403, 500...)

### Công nghệ sử dụng

#### Backend:
- **Spring Boot**: Framework chính cho ứng dụng
- **Spring Security**: Xác thực và phân quyền
- **Spring Data JPA**: Truy cập dữ liệu
- **Hibernate**: ORM framework
- **Thymeleaf**: Template engine
- **Flyway**: Database migration
- **MySQL**: Cơ sở dữ liệu

#### Frontend:
- **HTML/CSS/JavaScript**: Giao diện người dùng cơ bản
- **Bootstrap**: Framework CSS cho responsive design
- **jQuery**: Thư viện JavaScript

## Chức năng hệ thống

### Phía người dùng:

* **Duyệt phim:**
    * Xem danh sách phim đang chiếu và sắp chiếu
    * Xem thông tin chi tiết phim (áp phích, trailer, tóm tắt, diễn viên)
    * Bộ lọc và tìm kiếm phim

* **Đặt vé:**
    * Chọn lịch chiếu và rạp
    * Bản đồ ghế ngồi tương tác với hiển thị ghế còn trống
    * Xử lý thanh toán an toàn

* **Tài khoản người dùng:**
    * Đăng ký và đăng nhập
    * Lịch sử đặt vé
    * Quản lý thông tin cá nhân
    * Đổi mật khẩu và khôi phục mật khẩu

### Phía quản trị:

* **Quản lý phim:**
    * Thêm, sửa, xóa thông tin phim
    * Quản lý chi tiết phim (tiêu đề, mô tả, thể loại, đạo diễn, diễn viên, áp phích, trailer)
    * Đồng bộ dữ liệu từ TMDB API

* **Quản lý rạp và phòng chiếu:**
    * Thêm, sửa, xóa rạp chiếu
    * Quản lý phòng chiếu cho từng rạp
    * Thiết lập sơ đồ ghế ngồi

* **Quản lý lịch chiếu:**
    * Lập lịch chiếu phim
    * Quản lý suất chiếu theo rạp và phòng

* **Quản lý đặt vé:**
    * Xem và quản lý tất cả đặt vé
    * Tạo báo cáo thống kê

* **Quản lý người dùng:**
    * Quản lý tài khoản người dùng
    * Phân quyền và khóa/mở khóa tài khoản

## Cơ sở dữ liệu

### Cấu trúc database (Migration V1)
```sql
-- Bảng users
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(100) UNIQUE,
    locked BOOLEAN DEFAULT FALSE,
    role ENUM('CUSTOMER', 'STAFF', 'ADMIN') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX email_index (email),
    INDEX username_index (username)
);

-- Bảng movies
CREATE TABLE movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    overview TEXT,
    poster_path VARCHAR(255),
    backdrop_path VARCHAR(255),
    release_date DATE,
    runtime INT,
    original_language VARCHAR(10),
    tmdb_id BIGINT,
    status ENUM('NOW_SHOWING', 'COMING_SOON', 'ENDED') DEFAULT 'COMING_SOON',
    view_count INT DEFAULT 0,
    INDEX title_index (title),
    INDEX status_index (status)
);

-- Bảng movie_dynamic_data
CREATE TABLE movie_dynamic_data (
    data_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT NOT NULL,
    vote_average DECIMAL(3,1) DEFAULT 0.0,
    vote_count INT DEFAULT 0,
    popularity DECIMAL(10,3) DEFAULT 0.0,
    last_updated DATETIME,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    INDEX movie_id_index (movie_id)
);

-- Các bảng khác (theaters, screens, screenings, bookings, etc.)
-- ...
```

## Chức năng đang phát triển

* **Tính năng đánh giá và bình luận phim**
* **Tích hợp thanh toán trực tuyến với VNPay và MoMo**
* **Hỗ trợ đa ngôn ngữ (Tiếng Việt và Tiếng Anh)**
* **Ứng dụng di động cho iOS và Android**
* **Hệ thống thông báo qua email khi đặt vé thành công**

## Cài đặt và chạy dự án

1. **Clone repository:**
   ```bash
   git clone https://github.com/tankietse/viecinema.online.git
   ```

2. **Cấu hình cơ sở dữ liệu:**
   - Tạo cơ sở dữ liệu MySQL
   - Cập nhật thông tin kết nối trong `application.properties`

3. **Biên dịch và chạy:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Truy cập ứng dụng:**
   - URL: http://localhost:8080

## Yêu cầu hệ thống

* Java 17 trở lên
* Maven 3.6 trở lên
* MySQL 8.0 trở lên

## Tác giả và đóng góp

* **Nguyễn Tấn Kiệt** - Developer chính - [GitHub](https://github.com/tankietse)

## Giấy phép

[GPL-3.0](https://www.gnu.org/licenses/gpl-3.0)

