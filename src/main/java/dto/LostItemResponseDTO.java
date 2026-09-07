package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class LostItemResponseDTO {
    private Long id;
    private String itemName;
    private String category;
    private String locationLost;
    private LocalDate dateLost;
    private String description;
    private String photoUrl;
    private String status;
    private LocalDateTime createdAt;
    private String reportedByName; // just the name, never the full User object
}