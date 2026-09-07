package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class FoundItemAdminDTO {
    private Long id;
    private String itemName;
    private String category;
    private String locationFound;
    private LocalDate dateFound;
    private String description;
    private String photoUrl;
    private String imei;
    private String serialNumber;
    private String specialMarks;
    private String status;
}