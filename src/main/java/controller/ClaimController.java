package com.lostfound.controller;

import com.lostfound.dto.ClaimRequest;
import com.lostfound.dto.ClaimResponseDTO;
import com.lostfound.dto.ClaimReviewDTO;
import com.lostfound.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ClaimResponseDTO> submitClaim(@RequestBody ClaimRequest request, Authentication authentication) {
        ClaimResponseDTO result = claimService.submitClaim(request, authentication.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ClaimResponseDTO>> getMyClaims(Authentication authentication) {
        return ResponseEntity.ok(claimService.getMyClaims(authentication.getName()));
    }

    @GetMapping("/admin/pending")
    public ResponseEntity<List<ClaimReviewDTO>> getPendingClaims() {
        return ResponseEntity.ok(claimService.getPendingClaims());
    }

    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<ClaimResponseDTO> approveClaim(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(claimService.approveClaim(id, authentication.getName()));
    }

    @PutMapping("/admin/{id}/reject")
    public ResponseEntity<ClaimResponseDTO> rejectClaim(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(claimService.rejectClaim(id, authentication.getName()));
    }
}