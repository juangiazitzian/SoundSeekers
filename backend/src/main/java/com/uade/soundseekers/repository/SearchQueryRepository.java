package com.uade.soundseekers.repository;

import com.uade.soundseekers.entity.SearchQuery;
import com.uade.soundseekers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {
    List<SearchQuery> findByUser(User user);
    List<SearchQuery> findByUserId(Long userId);

}