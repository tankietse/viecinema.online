package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Statistics;
import com.viecinema.moviebooking.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    public List<Statistics> getStatisticsByMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Tháng trong Java bắt đầu từ 0
        Date startDate = calendar.getTime();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1); // Lấy ngày cuối cùng của tháng
        Date endDate = calendar.getTime();

        return statisticsRepository.findByDateBetween(startDate, endDate);
    }
}
