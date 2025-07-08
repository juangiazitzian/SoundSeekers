package com.uade.soundseekers.repository;

import com.uade.soundseekers.entity.User;
import com.uade.soundseekers.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}