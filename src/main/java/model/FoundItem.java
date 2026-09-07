package com.lostfound.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "found_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // the finder

    @Column(name = "item_name", nullable = false)
    private String itemName;

    private String category;

    @Column(name = "location_found")
    private String locationFound;

    @Column(name = "date_found")
    private LocalDate dateFound;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    // sensitive fields — used only for ownership verification, never sent to public endpoints
    private String imei;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "special_marks", columnDefinition = "TEXT")
    private String specialMarks;

    @Column(nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}