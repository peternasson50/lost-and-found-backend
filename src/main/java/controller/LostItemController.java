package com.lostfound.controller;

import com.lostfound.dto.LostItemResponseDTO;
import com.lostfound.model.LostItem;
import com.lostfound.model.User;
import com.lostfound.repository.LostItemRepository;
import com.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;

    @GetMapping("/mine")
    public ResponseEntity<List<LostItemResponseDTO>> getMyLostItems(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<LostItemResponseDTO> items = lostItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<LostItemResponseDTO>> getAllLostItemsForAdmin() {
        List<LostItemResponseDTO> items = lostItemRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<LostItemResponseDTO> reportLostItem(@RequestBody LostItem lostItem, Authentication authentication) {
        User user = getCurrentUser(authentication);

        lostItem.setUser(user);
        lostItem.setStatus("PENDING");

        LostItem saved = lostItemRepository.save(lostItem);
        return ResponseEntity.ok(toDTO(saved));
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private LostItemResponseDTO toDTO(LostItem item) {
        LostItemResponseDTO dto = new LostItemResponseDTO();
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setCategory(item.getCategory());
        dto.setLocationLost(item.getLocationLost());
        dto.setDateLost(item.getDateLost());
        dto.setDescription(item.getDescription());
        dto.setPhotoUrl(item.getPhotoUrl());
        dto.setStatus(item.getStatus());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setReportedByName(item.getUser().getFullName());
        return dto;
    }
}