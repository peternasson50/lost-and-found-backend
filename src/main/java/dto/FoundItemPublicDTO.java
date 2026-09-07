package com.lostfound.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class FoundItemPublicDTO {
    private Long id;
    private String itemName;
    private String category;
    private String locationFound;
    private LocalDate dateFound;
    private String description;
    private String photoUrl;
    private String status;
    // deliberately no imei, serialNumber, or specialMarks
}