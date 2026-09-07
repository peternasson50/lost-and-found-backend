package com.lostfound.repository;

import com.lostfound.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByUserId(Long userId);
    List<LostItem> findByStatus(String status);
}