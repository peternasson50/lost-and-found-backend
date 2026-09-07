package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDTO {
    private Long foundItemId;
    private String foundItemName;
    private String foundCategory;
    private String foundLocation;
    private String foundDate;
    private int matchScore;
}