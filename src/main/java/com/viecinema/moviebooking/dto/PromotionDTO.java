package com.viecinema.moviebooking.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PromotionDTO {
    private int promotionId;
    private String code;
    private String name;
    private String description;
    private String discountType;
    private double discountValue;
    private Date startDate;
    private Date endDate;
    private Double minPurchaseAmount;
    private Double maxDiscountAmount;
    private int usageLimit;
    private int timesUsed;
    private int userId;
    private int bookingId;
}
