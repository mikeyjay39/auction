package com.example.auction.repository;

import com.example.auction.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	@Cacheable("username")
	User findByUsername(String username);
}
