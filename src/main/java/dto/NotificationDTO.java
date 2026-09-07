package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}