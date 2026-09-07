package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClaimResponseDTO {
    private Long id;
    private String itemName;
    private String proofAnswers;
    private String status;
    private LocalDateTime createdAt;
}