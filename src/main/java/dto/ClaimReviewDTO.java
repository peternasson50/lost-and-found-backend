package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClaimReviewDTO {
    private Long claimId;
    private String claimantName;
    private String proofAnswers;

    private String foundItemName;
    private String actualImei;
    private String actualSerialNumber;
    private String actualSpecialMarks;

    private String status;
    private LocalDateTime createdAt;
}