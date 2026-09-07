package com.lostfound.controller;

import com.lostfound.dto.MatchDTO;
import com.lostfound.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchController {

    private final MatchingService matchingService;

    @GetMapping("/api/lost-items/{id}/matches")
    public ResponseEntity<List<MatchDTO>> getMatches(@PathVariable Long id) {
        return ResponseEntity.ok(matchingService.findMatchesForLostItem(id));
    }
}