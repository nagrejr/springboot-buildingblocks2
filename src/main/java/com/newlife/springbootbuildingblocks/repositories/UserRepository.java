package com.newlife.springbootbuildingblocks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newlife.springbootbuildingblocks.entities.User;

//Repository
@Repository
public interface UserRepository  extends JpaRepository<User, Long>{

	User findByUsername(String username);
}
