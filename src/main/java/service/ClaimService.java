package com.lostfound.service;

import com.lostfound.dto.ClaimRequest;
import com.lostfound.dto.ClaimResponseDTO;
import com.lostfound.dto.ClaimReviewDTO;
import com.lostfound.model.Claim;
import com.lostfound.model.FoundItem;
import com.lostfound.model.LostItem;
import com.lostfound.model.User;
import com.lostfound.repository.ClaimRepository;
import com.lostfound.repository.FoundItemRepository;
import com.lostfound.repository.LostItemRepository;
import com.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final FoundItemRepository foundItemRepository;
    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ClaimResponseDTO submitClaim(ClaimRequest request, String claimantEmail) {
        User claimant = userRepository.findByEmail(claimantEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoundItem foundItem = foundItemRepository.findById(request.getFoundItemId())
                .orElseThrow(() -> new RuntimeException("Found item not found"));

        Claim claim = new Claim();
        claim.setClaimant(claimant);
        claim.setFoundItem(foundItem);
        claim.setProofAnswers(request.getProofAnswers());
        claim.setStatus("PENDING");

        if (request.getLostItemId() != null) {
            LostItem lostItem = lostItemRepository.findById(request.getLostItemId())
                    .orElseThrow(() -> new RuntimeException("Lost item not found"));
            claim.setLostItem(lostItem);
        }

        Claim saved = claimRepository.save(claim);
        return toDTO(saved);
    }

    public List<ClaimResponseDTO> getMyClaims(String claimantEmail) {
        User claimant = userRepository.findByEmail(claimantEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return claimRepository.findByClaimantId(claimant.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ClaimReviewDTO> getPendingClaims() {
        return claimRepository.findByStatus("PENDING")
                .stream()
                .map(this::toReviewDTO)
                .toList();
    }

    public ClaimResponseDTO approveClaim(Long claimId, String adminEmail) {
        return updateClaimStatus(claimId, "APPROVED", adminEmail);
    }

    public ClaimResponseDTO rejectClaim(Long claimId, String adminEmail) {
        return updateClaimStatus(claimId, "REJECTED", adminEmail);
    }

    private ClaimResponseDTO updateClaimStatus(Long claimId, String newStatus, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        claim.setStatus(newStatus);
        claim.setReviewedBy(admin);
        claim.setReviewedAt(LocalDateTime.now());

        if (newStatus.equals("APPROVED")) {
            FoundItem foundItem = claim.getFoundItem();
            foundItem.setStatus("RETURNED");
            foundItemRepository.save(foundItem);
        }

        Claim saved = claimRepository.save(claim);

        String message = newStatus.equals("APPROVED")
                ? "Your claim for \"" + claim.getFoundItem().getItemName() + "\" has been approved! Please arrange pickup with the admin office."
                : "Your claim for \"" + claim.getFoundItem().getItemName() + "\" was not approved.";
        notificationService.createNotification(claim.getClaimant(), message);

        return toDTO(saved);
    }

    private ClaimResponseDTO toDTO(Claim claim) {
        ClaimResponseDTO dto = new ClaimResponseDTO();
        dto.setId(claim.getId());
        dto.setItemName(claim.getFoundItem().getItemName());
        dto.setProofAnswers(claim.getProofAnswers());
        dto.setStatus(claim.getStatus());
        dto.setCreatedAt(claim.getCreatedAt());
        return dto;
    }

    private ClaimReviewDTO toReviewDTO(Claim claim) {
        ClaimReviewDTO dto = new ClaimReviewDTO();
        dto.setClaimId(claim.getId());
        dto.setClaimantName(claim.getClaimant().getFullName());
        dto.setProofAnswers(claim.getProofAnswers());
        dto.setFoundItemName(claim.getFoundItem().getItemName());
        dto.setActualImei(claim.getFoundItem().getImei());
        dto.setActualSerialNumber(claim.getFoundItem().getSerialNumber());
        dto.setActualSpecialMarks(claim.getFoundItem().getSpecialMarks());
        dto.setStatus(claim.getStatus());
        dto.setCreatedAt(claim.getCreatedAt());
        return dto;
    }
}