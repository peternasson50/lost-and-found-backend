package com.lostfound.controller;

import com.lostfound.dto.FoundItemPublicDTO;
import com.lostfound.model.FoundItem;
import com.lostfound.model.User;
import com.lostfound.repository.FoundItemRepository;
import com.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
@RequiredArgsConstructor
public class FoundItemController {

    private final FoundItemRepository foundItemRepository;
    private final UserRepository userRepository;

    // Public browse list — sensitive fields deliberately excluded
    @GetMapping
    public ResponseEntity<List<FoundItemPublicDTO>> browseFoundItems() {
        List<FoundItemPublicDTO> items = foundItemRepository.findByStatus("AVAILABLE")
                .stream()
                .map(this::toPublicDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    // Report a found item — finder CAN submit the sensitive fields here
    @PostMapping
    public ResponseEntity<FoundItemPublicDTO> reportFoundItem(@RequestBody FoundItem foundItem, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        foundItem.setUser(user);
        foundItem.setStatus("AVAILABLE");

        FoundItem saved = foundItemRepository.save(foundItem);
        return ResponseEntity.ok(toPublicDTO(saved)); // even the finder gets the public view back
    }

    private FoundItemPublicDTO toPublicDTO(FoundItem item) {
        FoundItemPublicDTO dto = new FoundItemPublicDTO();
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setCategory(item.getCategory());
        dto.setLocationFound(item.getLocationFound());
        dto.setDateFound(item.getDateFound());
        dto.setDescription(item.getDescription());
        dto.setPhotoUrl(item.getPhotoUrl());
        dto.setStatus(item.getStatus());
        // imei, serialNumber, specialMarks intentionally never set here
        return dto;
    }
}