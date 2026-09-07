package com.lostfound.repository;

import com.lostfound.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByUserId(Long userId);
    List<FoundItem> findByStatus(String status);
    List<FoundItem> findByCategory(String category);
}