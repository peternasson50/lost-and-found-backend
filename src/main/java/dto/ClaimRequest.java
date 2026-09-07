package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimRequest {
    private Long foundItemId;
    private Long lostItemId; // optional, if they're linking it to their own lost report
    private String proofAnswers; // e.g. "IMEI: 123456, Serial: SN-2938471"
}