package com.example.roenbeauty.user.repository;

import com.example.roenbeauty.user.entity.User;
import com.example.roenbeauty.user.enums.OAuthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndProvider(String email, OAuthProvider provider);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, OAuthProvider provider);

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);

    Optional<User> findByProviderAndProviderIdAndDeletedFalse(
            OAuthProvider provider,
            String providerId
    );
}