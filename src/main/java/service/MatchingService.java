package com.lostfound.service;

import com.lostfound.dto.MatchDTO;
import com.lostfound.model.FoundItem;
import com.lostfound.model.LostItem;
import com.lostfound.repository.FoundItemRepository;
import com.lostfound.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;

    public List<MatchDTO> findMatchesForLostItem(Long lostItemId) {
        LostItem lostItem = lostItemRepository.findById(lostItemId)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));

        List<FoundItem> availableFoundItems = foundItemRepository.findByStatus("AVAILABLE");

        return availableFoundItems.stream()
                .map(found -> toMatchDTO(found, score(lostItem, found)))
                .filter(dto -> dto.getMatchScore() > 0)
                .sorted(Comparator.comparingInt(MatchDTO::getMatchScore).reversed())
                .toList();
    }

    private int score(LostItem lost, FoundItem found) {
        int score = 0;

        // Category match — strongest signal
        if (lost.getCategory() != null && lost.getCategory().equalsIgnoreCase(found.getCategory())) {
            score += 3;
        }

        // Item name similarity — simple contains-check both ways
        if (namesSimilar(lost.getItemName(), found.getItemName())) {
            score += 3;
        }

        // Date proximity — found within 14 days of being lost
        if (lost.getDateLost() != null && found.getDateFound() != null) {
            long daysBetween = ChronoUnit.DAYS.between(lost.getDateLost(), found.getDateFound());
            if (daysBetween >= 0 && daysBetween <= 14) {
                score += 2;
            }
        }

        // Location similarity — simple contains-check both ways
        if (locationsSimilar(lost.getLocationLost(), found.getLocationFound())) {
            score += 2;
        }

        return score;
    }

    private boolean namesSimilar(String a, String b) {
        if (a == null || b == null) return false;
        String x = a.toLowerCase().trim();
        String y = b.toLowerCase().trim();
        return x.contains(y) || y.contains(x);
    }

    private boolean locationsSimilar(String a, String b) {
        if (a == null || b == null) return false;
        String x = a.toLowerCase().trim();
        String y = b.toLowerCase().trim();
        return x.contains(y) || y.contains(x);
    }

    private MatchDTO toMatchDTO(FoundItem found, int score) {
        MatchDTO dto = new MatchDTO();
        dto.setFoundItemId(found.getId());
        dto.setFoundItemName(found.getItemName());
        dto.setFoundCategory(found.getCategory());
        dto.setFoundLocation(found.getLocationFound());
        dto.setFoundDate(found.getDateFound() != null ? found.getDateFound().toString() : null);
        dto.setMatchScore(score);
        return dto;
    }
}