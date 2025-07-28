package com.example.model;

public enum ReservationStatus {
    
    PENDING,     // 預約中（未付款）
    
    CONFIRMED,   // 成功預約 (已繳費)
    
    CANCELLED,   // 取消預約
    
    REFUNDED     // 已退款（取消後才能）

}
